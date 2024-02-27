package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * Initializes the test data before the main method is called. 
	 */
    pointcut mainMethod() : execution(public static void main(String[]));
    before() : mainMethod()
    {
    	TestingSetup.setup();
    	
    }
    
	
    /**
     * 
     */
    after() : (execution(* *.*.*(..) ) || execution(*.new(..))) && !within(TestingCore) {
    	
    	InterceptedMethodInformation intercepted = setInterceptedInformation(thisJoinPointStaticPart,thisJoinPoint);
    	
    	if (the_class_must_be_tested(intercepted) && the_method_must_be_tested(intercepted)) {
	    	checkSequence(intercepted);
    	}
    }
    
    
    private InterceptedMethodInformation setInterceptedInformation(JoinPoint.StaticPart thisJoinPointStaticPart, JoinPoint thisJoinPoint) {
    	
    	InterceptedMethodInformation intercepted=new InterceptedMethodInformation();
    	
    	setClassName(intercepted,thisJoinPointStaticPart);
    	
    	if (the_class_must_be_tested(intercepted)) {
    		setObjectInformtion(intercepted,thisJoinPoint);
    		initMapObjectsToCallSequence(intercepted);
    		setMethodName(intercepted, thisJoinPoint);
    		
    		if(the_method_must_be_tested(intercepted)) {
    			updateSequence(intercepted);
    		}	
    	}
    	
    	return intercepted;
    }
    
    private void setClassName(InterceptedMethodInformation intercepted,JoinPoint.StaticPart thisJoinPointStaticPart) {
    	intercepted.className="class " + thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
    }
    
    private void setMethodName(InterceptedMethodInformation intercepted,JoinPoint thisJoinPoint) {
    	intercepted.methodName=thisJoinPoint.getSignature().getDeclaringTypeName() +"."+ thisJoinPoint.getSignature().getName();
    }
    
    private boolean the_class_must_be_tested(InterceptedMethodInformation intercepted) {
    	return mapClassToTestingInformation.containsKey(intercepted.className);
    }
    	
    private boolean the_method_must_be_tested(InterceptedMethodInformation intercepted) {
    	return intercepted.ti.getMapMethodsToSymbols().containsKey(intercepted.methodName);
    }
    
    private void setObjectInformtion(InterceptedMethodInformation intercepted,JoinPoint thisJoinPoint) {
    	intercepted.ti=mapClassToTestingInformation.get(intercepted.className);
		intercepted.objectHashCode=thisJoinPoint.getThis().hashCode();
    }
    
    private void updateSequence(InterceptedMethodInformation intercepted){
    	String methodSymbol = intercepted.ti.getMapMethodsToSymbols().get(intercepted.methodName);
    	intercepted.newSequence=intercepted.ti.getMapObjectsToCallSequence().get(intercepted.objectHashCode).concat(methodSymbol);
    	intercepted.ti.getMapObjectsToCallSequence().put(intercepted.objectHashCode, intercepted.newSequence);
    }
    
    private void initMapObjectsToCallSequence(InterceptedMethodInformation intercepted) {
    	TestingInformation ti = intercepted.ti;
    	if(!ti.getMapObjectsToCallSequence().containsKey(intercepted.objectHashCode)) {
			ti.getMapObjectsToCallSequence().put(intercepted.objectHashCode, "");
		}
    	intercepted.ti=ti;
    }
    
    private void checkSequence(InterceptedMethodInformation intercepted) {
        TestingInformation ti = intercepted.ti;

        resetMatcher(ti, intercepted.newSequence);

        if (!isMatching(ti)) {
            handleNonMatchingSequence(intercepted);
        }
    }

    private void resetMatcher(TestingInformation ti, String newSequence) {
        ti.getMatcher().reset(newSequence);
    }

    private boolean isMatching(TestingInformation ti) {
        return ti.getMatcher().matches() || ti.getMatcher().hitEnd();
    }

    private void handleNonMatchingSequence(InterceptedMethodInformation intercepted) {
        showError(intercepted);
        if (intercepted.ti.getAbort()) {
            abort();
        } else {
            continueExecution();
        }
    }
    
    private void showError(InterceptedMethodInformation intercepted){
    	showErrorFoundMessage();
    	showErrorInformation(intercepted);
    }
    
    private void showErrorFoundMessage() {
    	System.out.println("-------------------------------");
		System.out.println("---       ERROR FOUND       ---");
		System.out.println("-------------------------------");
    }
    
    private void showErrorInformation(InterceptedMethodInformation intercepted){
		System.out.println("Class: "+ intercepted.className);
		System.out.println("Object Code: "+ intercepted.objectHashCode);
		System.out.println("Method Executed: "+ intercepted.methodName);
		System.out.println("Regular Expression: "+ intercepted.ti.getRegularExpression());
		System.out.println("Execution Sequence: "+ intercepted.newSequence);
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
     * This class is intended to encapsulate all the relevant information that is obtained from the intercepted method.
     */
    protected class InterceptedMethodInformation {
    	
    	protected String className="";
    	protected int objectHashCode=-1;
    	protected String methodName="";
    	protected TestingInformation ti=null;
    	protected String newSequence="";
    	
    	protected boolean the_class_must_be_tested=false;
    	
    	
    }
}
