package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    	String className = "class " + thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
    	if (mapClassToTestingInformation.containsKey(className)) {
    		TestingInformation ti = mapClassToTestingInformation.get(className);   		
    		int objectHashCode=thisJoinPoint.getThis().hashCode();
    		
    		if(!ti.getMapObjectsToCallSequence().containsKey(objectHashCode)) {
    			ti.getMapObjectsToCallSequence().put(objectHashCode, "");
    		}
    		
    		String methodName = thisJoinPoint.getSignature().getDeclaringTypeName() +"."+ thisJoinPoint.getSignature().getName(); 
    		String methodSymbol = ti.getMapMethodsToSymbols().get(methodName);
    		
    		if(ti.getMapMethodsToSymbols().containsKey(methodName)) {
    		
	    		String newSequence = ti.getMapObjectsToCallSequence().get(objectHashCode).concat(methodSymbol);
	    		ti.getMapObjectsToCallSequence().put(objectHashCode, newSequence);
	    		
	    		ti.getMatcher().reset(newSequence);
	    		boolean isMatching=ti.getMatcher().matches() || ti.getMatcher().hitEnd();
	    		
	    		if(!isMatching) {
	    		
	    			System.out.println("-------------------------------");
	    			System.out.println("---       ERROR FOUND       ---");
		    		System.out.println("-------------------------------");
		    		System.out.println("Class: "+ className);
		    		System.out.println("Object Code: "+ objectHashCode);
		    		System.out.println("Method Executed: "+ methodName);
		    		System.out.println("Regular Expression: "+ ti.getRegularExpression());
		    		System.out.println("Execution Sequence: "+ newSequence);
		    		if (ti.getAbort()) {
		    			System.out.println("-------------------------------");
			    		System.out.println("-----  SYSTEM ABORTING... -----");
			    		System.out.println("-------------------------------");
		    			System.exit(0);
		    		} else {
		    			System.out.println("-------------------------------");
			    		System.out.println("--  CONTINUING EXECUTION... ---");
			    		System.out.println("-------------------------------");
			    		System.out.println();
		    		}
		    		
		    		
		    		
	    		
	    		}
    		
    		}
	    		
	    	
    	}
    }
}
