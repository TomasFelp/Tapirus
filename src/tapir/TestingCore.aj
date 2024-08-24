package tapir;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import org.aspectj.lang.JoinPoint;


/**
 * AOP class to manage...
 * @author Martin Larrea
 *
 */
public aspect TestingCore {
	
//	public static String targetClass;
//	public static HashMap<Integer, String> mapObjectsToCallSequence = null; 
//	public static HashMap<String, String> mapMethodsToSymbols = null; 
//	public static Pattern regularExpression = null; 
//	public static Matcher matcher = null;
	
	/**
	 * String is the target class
	 */
	protected static HashMap<String, TestingInformation> mapClassToTestingInformation = null; 
	
	protected HashMap<String, ObjectState> mapMethodsToPreviousObjectState = null;
	
	/**
	 * Initializes the test data before the main method is called. 
	 */
    pointcut mainMethod() : execution(public static void main(String[]));
    before() : mainMethod()
    {
    	TestingSetup.setup();
    	mapMethodsToPreviousObjectState = new HashMap<String, ObjectState>();
    }
    
    before() : (execution(* *.*.*(..) ) || execution(*.new(..))) && !within(TestingCore)  && !within(TestingSetup) {
    	
    	if (theInterceptedCallisOfModalInterest(thisJoinPoint)) {
    		mapMethodsToPreviousObjectState.put(getObjectStateIdentifier(thisJoinPoint), retrieveObjectFields(thisJoinPoint));
    	}
    }
    
    private  boolean theInterceptedCallisOfModalInterest(JoinPoint thisJoinPoint) {
    	return theInterceptedCallisOfInterest(thisJoinPoint) && getTestingInformation(thisJoinPoint).isModalTestType();
    }
    
    private  boolean theInterceptedCallisOfInterest(JoinPoint thisJoinPoint) {
    	return the_class_must_be_tested(thisJoinPoint) && the_method_must_be_tested(thisJoinPoint);
    }
 
    after() : (execution(* *.*.*(..) ) || execution(*.new(..))) && !within(TestingCore) {

    	if (the_class_must_be_tested(thisJoinPoint)) { 		
    		initSequence(thisJoinPoint);
    		
    		if(the_method_must_be_tested(thisJoinPoint)) {
    			TestingInformation ti = getTestingInformation(thisJoinPoint);
	    		updateSequence(thisJoinPoint);
	    		resetMatcher(ti, getSequence(thisJoinPoint));
	    		if(!isMatching(ti)) {
	    			handleNonMatchingSequence(thisJoinPoint);
	    		}
    		}
    	}
    }
    
    private void initSequence(JoinPoint thisJoinPoint) {
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	
    	if(!ti.getMapObjectsToCallSequence().containsKey(objectHashCode)) {
			ti.getMapObjectsToCallSequence().put(objectHashCode, "");
		}
    }
    
    private String getClassName(JoinPoint thisJoinPoint) {
    	return "class " + thisJoinPoint.getStaticPart().getSignature().getDeclaringTypeName();
    }
    
    private String getMethodName(JoinPoint thisJoinPoint) {
    	return thisJoinPoint.getSignature().getDeclaringTypeName() +"."+ thisJoinPoint.getSignature().getName();
    }
    
    private boolean the_class_must_be_tested(JoinPoint thisJoinPoint) {
    	return mapClassToTestingInformation.containsKey(getClassName(thisJoinPoint));
    }
     
    private boolean the_method_must_be_tested(JoinPoint thisJoinPoint) {
    	return getTestingInformation(thisJoinPoint).getMapMethodsToSymbols().containsKey(getMethodName(thisJoinPoint));
    }
    
    private TestingInformation getTestingInformation(JoinPoint thisJoinPoint) {
    	return mapClassToTestingInformation.get(getClassName(thisJoinPoint));
    }
    
    private int getObjectHashCode(JoinPoint thisJoinPoint) {
    	return thisJoinPoint.getThis().hashCode();
    }
    
    private ObjectState retrieveObjectFields(JoinPoint thisJoinPoint) {
    	Object object = thisJoinPoint.getThis();
        Class<?> clase = object.getClass();
        Field[] attributes = clase.getDeclaredFields();
        ObjectState interceptedObject = new ObjectState();
        
        for (Field attribute : attributes) {
        	attribute.setAccessible(true); 
            String attributeName = attribute.getName();
            String attributeSymbol = getAttributeSymbol(thisJoinPoint, clase.getName() + "." + attributeName);

            if (!attributeName.startsWith("ajc$") && attributeSymbol!=null) {
	            try {
	                Object attributeValue = attribute.get(object);
	                interceptedObject.attribute.add(attributeSymbol);
	                interceptedObject.value.add(attributeValue);
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
            }
        }
        
        return interceptedObject;
    }
    
    private String getAttributeSymbol(JoinPoint thisJoinPoint, String attributeName) {
    	return getTestingInformation(thisJoinPoint).getMapAttributesToSymbols().get(attributeName);
    }
    
    /*
     * Return a string made up of tuples 'attribute=value' according to the state of the object
     */
    private String normalizeObjectState(ObjectState objectState, JoinPoint thisJoinPoint) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < objectState.attribute.size(); i++) {
            result.append(RegularExpressionHelper.CONDITION_BOUNDARIES);
            result.append(objectState.attribute.get(i));
            result.append("=");
            result.append(objectState.value.get(i));
            result.append(RegularExpressionHelper.CONDITION_BOUNDARIES);
            result.append(RegularExpressionHelper.CONDITION_SEPARATOR);
        }
        
        if (result.length() > 0) {
            result.delete(result.length() - 1, result.length());
        }

        return RegularExpressionHelper.sortTuples(result.toString());
    }
    
    private String getObjectStateIdentifier(JoinPoint thisJoinPoint) {
    	return getMethodName(thisJoinPoint)+"."+getObjectHashCode(thisJoinPoint);
    }
    
    private void updateSequence(JoinPoint thisJoinPoint) {
    	
    	if(getTestingInformation(thisJoinPoint).isModalTestType()) {
    		updateModalSequence(thisJoinPoint);
    	}else {
    		updateUnimodalSequence(thisJoinPoint);
    	}
    }
    
    /*
     * Updates sequence formed only by method symbols
     */
    private void updateUnimodalSequence(JoinPoint thisJoinPoint) {
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	String methodSymbol = ti.getMapMethodsToSymbols().get(getMethodName(thisJoinPoint));
    	String newSequence = getSequence(thisJoinPoint).concat(methodSymbol);
    	
    	ti.getMapObjectsToCallSequence().put(objectHashCode, newSequence);
    }
    
    /*
     * Updates sequence made up of method symbols and object states
     */
    private void updateModalSequence(JoinPoint thisJoinPoint) {
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	String methodSymbol = ti.getMapMethodsToSymbols().get(getMethodName(thisJoinPoint));
    	String newSequence = getSequence(thisJoinPoint) + getPreCondition(thisJoinPoint) 
    									+ methodSymbol + getPostCondition(thisJoinPoint);
    	
    	ti.getMapObjectsToCallSequence().put(objectHashCode, newSequence);
    }
    
    /*
     * Gets the pre-execution state and returns it ready to add to the sequence
     */
    private String getPreCondition(JoinPoint thisJoinPoint) {
    	String result = normalizeObjectState(mapMethodsToPreviousObjectState.get(getObjectStateIdentifier(thisJoinPoint)), thisJoinPoint);
    
    	return RegularExpressionHelper.makePrecondition(result);
    }
    
    /*
     * Gets the post-execution state and returns it ready to add to the sequence
     */
    private String getPostCondition(JoinPoint thisJoinPoint) {
    	String result = normalizeObjectState(retrieveObjectFields(thisJoinPoint), thisJoinPoint);
    	
    	return RegularExpressionHelper.makePostScondition(result);
    }
    
    private String getSequence(JoinPoint thisJoinPoint) {
    	
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	    	
    	return ti.getMapObjectsToCallSequence().get(objectHashCode);
    }

    private void resetMatcher(TestingInformation ti, String newSequence) {
        ti.getMatcher().reset(newSequence);
    }

    private boolean isMatching(TestingInformation ti) {
        return ti.getMatcher().matches() || ti.getMatcher().hitEnd();
    }

    private void handleNonMatchingSequence(JoinPoint thisJoinPoint) {
        showError(thisJoinPoint);
        if (getTestingInformation(thisJoinPoint).getAbort()) {
            abort();
        } else {
            continueExecution();
        }
    }
    
    private void showError(JoinPoint thisJoinPoint){
    	showErrorFoundMessage();
    	showErrorInformation(thisJoinPoint);
    }
    
    private void showErrorFoundMessage() {
    	System.out.println("-------------------------------");
		System.out.println("---       ERROR FOUND       ---");
		System.out.println("-------------------------------");
    }
    
    private void showErrorInformation(JoinPoint thisJoinPoint){
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	
		System.out.println("Class: "+ getClassName(thisJoinPoint));
		System.out.println("Object Code: "+ getObjectHashCode(thisJoinPoint));
		System.out.println("Method Executed: "+ getMethodName(thisJoinPoint));
		System.out.println("Regular Expression: "+ ti.getSimplifiedRegularExpression());
		System.out.println("Execution Sequence: "+ getSimplifiedSequence(thisJoinPoint));
    }
    
    private String getSimplifiedSequence(JoinPoint thisJoinPoint) {
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	String result = ti.getMapObjectsToCallSequence().get(objectHashCode);
    	
    	if(ti.isModalTestType()) {
    		result = RegularExpressionHelper.simplifySequence(result);
    	}
    	
    	return result;
    }
    
    private void abort(){
    	showAbortMessage();
		System.exit(0);
    }
    
    private void showAbortMessage() {
    	System.out.println("-------------------------------");
		System.out.println("-----  SYSTEM ABORTING... -----");
		System.out.println("-------------------------------");
    }
    
    private void continueExecution(){
    	showContinueExecutionMessage();
		System.out.println();
    }
    
    private void showContinueExecutionMessage() {
    	System.out.println("-------------------------------");
		System.out.println("--  CONTINUING EXECUTION... ---");
		System.out.println("-------------------------------");
    }
    
    /**
     * This class is intended to store the attributes and their values of the intercepted objects.
     */
    protected class ObjectState {
    	
    	protected ArrayList<String> attribute;
    	protected ArrayList<Object> value;

        public ObjectState() {
            this.attribute =  new ArrayList<String>();
            this.value =  new ArrayList<Object>();
        }
    }
    
}

