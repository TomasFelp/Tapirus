package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestingInformation {
	protected String targetClass;
	protected HashMap<Integer, String> mapObjectsToCallSequence = null; 
	protected HashMap<String, String> mapMethodsToSymbols = null; 
	protected  HashMap<String, String> mapAttributesToSymbols= null;
	protected Pattern regularExpression = null; 
	protected Matcher matcher = null;
	protected boolean abort = true;
	protected boolean modalTestType = false;
	protected String simplifiedRegularExpression;
	
	/**
	 * @param targetClass
	 * @param mapObjectsToCallSequence
	 * @param mapMethodsToSymbols
	 * @param regularExpression
	 * @param matcher
	 */
	public TestingInformation(String targetClass, HashMap<Integer, String> mapObjectsToCallSequence,
			HashMap<String, String> mapMethodsToSymbols, HashMap<String, String> mapAttributesToSymbols, String pattern, Pattern regularExpression, Matcher matcher, boolean abort) {
		this.targetClass = targetClass;
		this.mapObjectsToCallSequence = mapObjectsToCallSequence;
		this.mapMethodsToSymbols = mapMethodsToSymbols;
		this.mapAttributesToSymbols = mapAttributesToSymbols;
		this.regularExpression = regularExpression;
		this.matcher = matcher;
		this.abort = abort;
		setModalTypeTest(RegularExpressionHelper.is_regular_expression_with_states(regularExpression.toString()));
		simplifiedRegularExpression = pattern;
	}
	
	/**
	 * @return the targetClass
	 */
	public String getTargetClass() {
		return targetClass;
	}
	/**
	 * @param targetClass the targetClass to set
	 */
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	/**
	 * @return the mapObjectsToCallSequence
	 */
	public HashMap<Integer, String> getMapObjectsToCallSequence() {
		return mapObjectsToCallSequence;
	}
	/**
	 * @param mapObjectsToCallSequence the mapObjectsToCallSequence to set
	 */
	public void setMapObjectsToCallSequence(HashMap<Integer, String> mapObjectsToCallSequence) {
		this.mapObjectsToCallSequence = mapObjectsToCallSequence;
	}
	/**
	 * @return the mapMethodsToSymbols
	 */
	public HashMap<String, String> getMapMethodsToSymbols() {
		return mapMethodsToSymbols;
	}
	/**
	 * @param mapMethodsToSymbols the mapMethodsToSymbols to set
	 */
	public void setMapMethodsToSymbols(HashMap<String, String> mapMethodsToSymbols) {
		this.mapMethodsToSymbols = mapMethodsToSymbols;
	}
	/**
	 * @return the mapAttributesToSymbols
	 */
	public HashMap<String, String> getMapAttributesToSymbols() {
		return mapAttributesToSymbols;
	}
	/**
	 * @param mapMethodsToSymbols the mapMethodsToSymbols to set
	 */
	public void setMapAttributesToSymbols(HashMap<String, String> mapAttributesToSymbols) {
		this.mapAttributesToSymbols = mapAttributesToSymbols;
	}
	/**
	 * @return the regularExpression
	 */
	public Pattern getRegularExpression() {
		return regularExpression;
	}
	
	/**
	 * @return the simplifiedRegularExpression
	 */
	public String getSimplifiedRegularExpression() {
		return simplifiedRegularExpression;
	}
	
	/**
	 * @param regularExpression the regularExpression to set
	 */
	public void setRegularExpression(Pattern regularExpression) {
		this.regularExpression = regularExpression;
		setModalTypeTest(RegularExpressionHelper.is_regular_expression_with_states(regularExpression.toString()));
	}
	/**
	 * @return the matcher
	 */
	public Matcher getMatcher() {
		return matcher;
	}
	/**
	 * @param matcher the matcher to set
	 */
	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

	/**
	 * @return the abort
	 */
	public boolean isAbort() {
		return abort;
	}

	/**
	 * @param abort the abort to set
	 */
	public void setAbort(boolean abort) {
		this.abort = abort;
	}	
	
	/**
	 * @param abort the abort to get
	 */
	public boolean getAbort() {
		return this.abort;
	}	
	
	/**
	 * @param modalTestType the modalTestType to set
	 */
	private void setModalTypeTest(boolean modalTestType) {
		this.modalTestType = modalTestType;
	}	
	
	/**
	 * @param modalTestType the modalTestType to get
	 */
	public boolean isModalTestType() {
		return this.modalTestType;
	}	
}
