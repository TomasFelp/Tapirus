package tapir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	public static HashMap<String, TestingInformation> mapClassToTestingInformation = null; 
	
	protected static HashMap<String, TestingInformation> mapMethodsToPreviousObjectState = null;
	
	/**
	 * Initializes the test data before the main method is called. 
	 */
    pointcut mainMethod() : execution(public static void main(String[]));
    before() : mainMethod()
    {
    	TestingSetup.setup();
    	
    }
    
	/**
	 * Completar... analizar el estado previo del objeo y guardarlo con el hash del objeto,metodo y clase para 
	 * posteriormente usarlo en el after al armar la secuencia, guarda las precondiciones de estado. 
	 * 
	 * Buena suerte Tomás del futuro.
	 */
    before() : (execution(* *.*.*(..) ) || execution(*.new(..))) && !within(TestingCore)  && !within(TestingSetup) {
    	
    	
    }
    
 
    after() : (execution(* *.*.*(..) ) || execution(*.new(..))) && !within(TestingCore) {

    	if (the_class_must_be_tested(thisJoinPoint)) {
    		TestingInformation ti = getTestingInformation(thisJoinPoint); 		
    		initSequence(thisJoinPoint);
    		
    		if(the_method_must_be_tested(thisJoinPoint)) {
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
    
    private void retrieveObjectFields(JoinPoint thisJoinPoint) {
    	
    	// Create an object to inspect
    	Object object = thisJoinPoint.getThis();

        // Get the class of the object
        Class<?> clase = object.getClass();
        //System.out.println("intercepted: "+thisJoinPoint.getSignature().getDeclaringTypeName() +"."+ thisJoinPoint.getSignature().getName()+" ------------");

        // Get all the fields of the class, including the private ones
        Field[] attributes = clase.getDeclaredFields();

        ObjectState interceptedObject = new ObjectState();
        
        // Iterate over fields and print their names and values
        for (Field attribute : attributes) {
        	attribute.setAccessible(true); // Make accessible even if private
            String attributeName = attribute.getName();

            if (!attributeName.startsWith("ajc$")) {
	            try {
	                // Get field value for given object
	                Object attributeValue = attribute.get(object);
	                interceptedObject.attribute.add(attributeName);
	                interceptedObject.value.add(attributeValue);
	                //System.out.println(attributeName + ": " + attributeValue);
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
            }
        }
        
        /*
        for (int i=0; i < interceptedObject.attribute.size(); i++) {
        	System.out.println(interceptedObject.attribute.get(i) + ": " + interceptedObject.value.get(i));
        }
        */
        
        //System.out.println("----------------------------------------------------");
    	
    }
    
    private void updateSequence(JoinPoint thisJoinPoint) {
    	
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	String methodSymbol = ti.getMapMethodsToSymbols().get(getMethodName(thisJoinPoint));
    	String newSequence = getSequence(thisJoinPoint).concat(methodSymbol);
    	
    	ti.getMapObjectsToCallSequence().put(objectHashCode, newSequence);
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
    	
    	int objectHashCode = getObjectHashCode(thisJoinPoint);
    	TestingInformation ti = getTestingInformation(thisJoinPoint);
    	
		System.out.println("Class: "+ getClassName(thisJoinPoint));
		System.out.println("Object Code: "+ getObjectHashCode(thisJoinPoint));
		System.out.println("Method Executed: "+ getMethodName(thisJoinPoint));
		System.out.println("Regular Expression: "+ ti.getRegularExpression());
		System.out.println("Execution Sequence: "+ ti.getMapObjectsToCallSequence().get(objectHashCode));
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
