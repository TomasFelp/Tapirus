package tapir;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegularExpressionHelper {
	
	private static final String DEFAULT_STATE_CONTENT = "[^:;]*";
	private static final String DEFAULT_PRECONDITION = ":[^:;]*;-";
	private static final String DEFAULT_POSTCONDITION = "-:[^:;]*;";
	private static final Character STATE_START = ':';
	private static final Character STATE_END = ';';
	private static final String EMPTY_STATE = ":;";
	private static final String PRECONDITION = ";-";
	private static final String POSTCONDITION = "-:";
	private static final String EMPTY_PRECONDITION = ":;-";
	private static final String EMPTY_POSTCONDITION = "-:;";
	public static final String CONDITION_SEPARATOR = ",";
	public static final String CONDITION_BOUNDARIES = "'";

	
	/*
	 * For every element of the sequence that does not have a precondition or postcondition, add a generic by default.
	 */
	private static String addDefaultStates(String regularExpression) {
        StringBuilder result = new StringBuilder();
        boolean insideState = false;

        for (int i = 0; i < regularExpression.length(); i++) {
            char character = regularExpression.charAt(i);

            if (character == STATE_START) {
                insideState = true;
            }

            if (!insideState && is_not_a_reserved_character(character) && (i == 0 || regularExpression.charAt(i - 1) != '-')) 
            {
                result.append(EMPTY_PRECONDITION).append(character);
            } else {
                result.append(character);
            }
            
            if (!insideState && is_not_a_reserved_character(character) && (i == regularExpression.length() - 1 || regularExpression.charAt(i + 1) != '-')) 
            	{
                    result.append(EMPTY_POSTCONDITION);
                } 

            if (character == STATE_END) {
                insideState = false;
            }
        }
        
        return result.toString();
    }

	private static boolean is_not_a_reserved_character(char character) {
    	return character != '(' && character != ')' && character != '|' &&
                character != '*' && character != '-' && character != ']' && character != '[' &&
                character != '+' && character != '?' && character != '<' && character != '>' ;
    }


    /**
     * Sorts a list of tuples according to the lexicographic order of the first element of the tuple. 
     * It supports tuple formats (Xi w Yi), where w belongs to ["=","[=","]=","]","[","!"] among others.
     * @param input string representing a list of tuples
     * @return list of tuples, in string, ordered lexicographically by the first element of the tuple
     */
	public static String sortTuples(String input) {
        String[] tuplas = input.split(",");
        List<String> tuplasList = new ArrayList<>();
        Collections.addAll(tuplasList, tuplas);
        Collections.sort(tuplasList, Comparator.comparing(t -> t.split("[!=<>]")[0]));
        StringBuilder result = new StringBuilder();
        
        for (String tupla : tuplasList) {
            result.append(tupla).append(",");
        }
        
        
        if (result.length() > 0) {
            result.delete(result.length() - 1, result.length());
        }
        
        return result.toString();
    }

    /*
     * Lexicographically orders the tuples found in each state.
     */
	private static String sortTuplesInState(String input) {
        String patternState = "\\"+STATE_START+"(.*?)\\"+STATE_END;
        Pattern pattern = Pattern.compile(patternState);
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String textState = matcher.group(1);
            String resultSortTuples = sortTuples(textState);
            matcher.appendReplacement(result, STATE_START+resultSortTuples+STATE_END);
        }
        matcher.appendTail(result);

        return result.toString();
    }
    
	/*
     * Complete states with default conditions 
     */
	private static String completeDefaultConditions(String input) {
		String result = input.replace(EMPTY_STATE, ""+STATE_START+DEFAULT_STATE_CONTENT+STATE_END);
		result = result.replace(""+STATE_START+CONDITION_BOUNDARIES, ""+STATE_START+DEFAULT_STATE_CONTENT+CONDITION_BOUNDARIES);
		result = result.replace(""+CONDITION_BOUNDARIES+STATE_END, ""+CONDITION_BOUNDARIES+DEFAULT_STATE_CONTENT+STATE_END);
		result = result.replace(""+CONDITION_BOUNDARIES+CONDITION_SEPARATOR, ""+CONDITION_BOUNDARIES+CONDITION_SEPARATOR+"("+DEFAULT_STATE_CONTENT+",)*");

        return result;
    }
    
    public static boolean is_regular_expression_with_states(String input) {
    	return input.contains(POSTCONDITION) || input.contains(PRECONDITION);
    }
    
    /*
     * receives a state in the form of a list of tuples and concatenates them with the precondition syntax
     */
    public static String makePrecondition(String input) {
    	return STATE_START + input + PRECONDITION;
    }
    
    /*
     * receives a state in the form of a list of tuples and concatenates them with the postcondition syntax
     */
    public static String makePostScondition(String input) {
    	return POSTCONDITION + input + STATE_END;
    }
    
    /*
	 * Removes state from all methods except the last one
	 */
	public static String simplifySequence(String input) {	
		int penultimatePosition = findPenultimatePosition(input, STATE_START);
        String substringStart = input.substring(0, penultimatePosition);
        substringStart = substringStart.replaceAll(DEFAULT_PRECONDITION, "");
        substringStart = substringStart.replaceAll(DEFAULT_POSTCONDITION, "");
        String substringEnd = input.substring(penultimatePosition, input.length());
        String result = substringStart + substringEnd;

        return result;
	}
 
	/*
	 * Find the penultimate position of the indicated character
	 */
    private static int findPenultimatePosition(String string, char character) {
        int lastPosition = string.lastIndexOf(character);

        if (lastPosition != -1) {
            int penultimatePosition = string.lastIndexOf(character, lastPosition - 1);
            return penultimatePosition;
        }

        return -1;
    }
    
    /*
	 *  Replace the comparators and their values with regular expressions that match them
	 */
	public static String normalizeComparators(String input) {
        String result = input;
        String[] operators = {">=", "<", "<=", "!=", ">"};
        String operator;
        
        for (int i = 0; i < operators.length; i++) {
        	operator = operators[i];
	        String patron = operator + "[^\\"+STATE_END+"\\"+CONDITION_BOUNDARIES+"=-]*\\"+CONDITION_BOUNDARIES;
	        Pattern pattern = Pattern.compile(patron);
	        Matcher matcher = pattern.matcher(input);
	
	        while (matcher.find()) {
	            String value = matcher.group();
	            String regularExpressionForValue = value.replaceFirst(operator, "");
	            regularExpressionForValue = regularExpressionForValue.replace("'", "");
	            
	            switch (operator) {
                case ">=":
                	regularExpressionForValue = getRegularExpressionforGreaterOrEqualInteger(regularExpressionForValue);
                    break;
                case "<":
                	regularExpressionForValue = getRegularExpressionforLessInteger(regularExpressionForValue);
                    break;
                case "<=":
                	regularExpressionForValue = getRegularExpressionforLessOrEqualInteger(regularExpressionForValue);
                    break;
                case "!=":
                    regularExpressionForValue = getRegularExpressionforDifferentFrom(regularExpressionForValue);
                    break;
                case ">":
                	regularExpressionForValue = getRegularExpressionforGreaterInteger(regularExpressionForValue);
                    break;
                default:
                    break;
            }
	            result = result.replace(value,""+"="+regularExpressionForValue+CONDITION_BOUNDARIES);
	        }
        }
        return result;
    }
    
    /*
     * Returns a regular expression that accepts only values different from the one received.
     */
    private static String getRegularExpressionforDifferentFrom(String input) {
    	return "("+input+"[^\\"+CONDITION_BOUNDARIES+"]+|(?!"+input+")[^\\"+CONDITION_BOUNDARIES+"]*)";
    }
    
	/*
     * Returns a regular expression that accepts only integer numbers greater than the one contained in the received string
     */
	private static String getRegularExpressionforGreaterInteger(String input) {
		String result = "";
		if(isANegativeNumber(input)) {
			result = "(-" + getRegularExpressionforLess(input.substring(1, input.length())) + "|\\d{1,})";
		}else {
			result = getRegularExpressionforGreater(input);
		}
		
		return result;
	}
	
	/*
     * Returns a regular expression that accepts only integer numbers greater or equal than the one contained in the received string
     */
	private static String getRegularExpressionforGreaterOrEqualInteger(String input) {
		String result = "";
		if(isANegativeNumber(input)) {
			result = "(-" + getRegularExpressionforLessOrEqual(input.substring(1, input.length())) + "|\\d{1,})";
		}else {
			result = getRegularExpressionforGreaterOrEqual(input);
		}
		
		return result;
	}
	
	/*
	 * Determines if the number contained in the received string is positive or negative
	 */
	private static boolean isANegativeNumber(String input) {
    	return input.charAt(0) == '-';
    }
	
	/*
     * Returns a regular expression that accepts only natural numbers greater than the one contained in the received string
     */
    private static String getRegularExpressionforGreater(String input) {
        StringBuilder result = getRegularExpressionforGreater_aux(input);
        result.append("[1-9]\\d{"+input.length()+",}");
        
        return "(" + result.toString() +")";
    }
	
	/*
     * Returns a regular expression that accepts only natural numbers greater or equal than the one contained in the received string
     */
    private static String getRegularExpressionforGreaterOrEqual(String input) {
        StringBuilder result = getRegularExpressionforGreater_aux(input);
        int length = input.length();
        
        String subcadena = input.substring(0, length-1 );
        
        result.append(subcadena+"["+ ( Character.getNumericValue(input.charAt(length - 1))) +"-9]|");
        result.append("[1-9]\\d{"+length+",}");
        
        return "(" + result.toString() + ")";
    }
    
    private static StringBuilder getRegularExpressionforGreater_aux(String input) {
    	StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            result.append(subcadena).append("["+ ( Character.getNumericValue(input.charAt(i)) + 1 ) +"-9]").append("\\d{"+posicion+",}|");
        }
        
        return result;
    }
    
    /*
     * Returns a regular expression that accepts only integer numbers less than the content of the received string.
     */
    private static String getRegularExpressionforLessInteger(String input) {
    	String result = "";
		if(isANegativeNumber(input)) {
			result = "(-" + getRegularExpressionforGreater(input.substring(1, input.length())) + ")";
		}else {
			result = "(" + getRegularExpressionforLess(input) + "|-\\d{1,})";
		}
		
		return result;
    }
    
    /*
     * Returns a regular expression that accepts only integer numbers less or equal than the content of the received string.
     */
    private static String getRegularExpressionforLessOrEqualInteger(String input) {
    	String result = "";
		if(isANegativeNumber(input)) {
			result = "(-" + getRegularExpressionforGreaterOrEqual(input.substring(1, input.length())) + ")";
		}else {
			result = "(" + getRegularExpressionforLessOrEqual(input) + "|-\\d{1,})";
		}
		
		return result;
    }
    
    /*
     * Returns a regular expression that accepts only natural numbers less than the content of the received string.
     */
    private static String getRegularExpressionforLess(String input) {
    	
    	if((input.replaceAll("0", "")).isBlank())
    		return "";
    	
        StringBuilder result = getRegularExpressionforLess_aux(input);
        result.append("\\d{0,"+(input.length() - 1)+"}");
        
        return "(" + result.toString() +")";
    }
    
    /*
     * Returns a regular expression that accepts only natural numbers less or equal than the content of the received string.
     */
    private static String getRegularExpressionforLessOrEqual(String input) {
        StringBuilder result = getRegularExpressionforLess_aux(input);

        result.append(input+"|");
        result.append("\\d{0,"+(input.length() - 1)+"}");
        
        return "(" + result.toString() +")";
    }

    private static StringBuilder getRegularExpressionforLess_aux(String input) {
    	StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            if(input.charAt(i)!='0')
            	result.append(subcadena).append("[0-"+ ( Character.getNumericValue(input.charAt(i)) - 1 ) +"]").append("\\d{"+posicion+"}|");

        }
        
        return result;
    }

    /*
     * Adds "(" and ")" to the beginning and end of each set method states, so that the quantifiers *, + and ? have the correct scope
     */
    private static String adjustScopeOfQuantifiers(String input) {
    	
    	String result = input.replaceAll("(?<!(\\^|-))"+STATE_START, "("+STATE_START);
    	result = result.replaceAll("(?<!(\\^"+STATE_START+"))"+STATE_END+"(?!-)", STATE_END+")");
    	
        return result;
    }
    
    /*
     * It precompiles a text with a certain format to bring it to a valid regex regular expression.
     */
    public static String preCompile(String intput) {
    	
    	String result = sortTuplesInState(intput);
    	result = normalizeComparators(result);
    	result = addDefaultStates(result);
    	result = completeDefaultConditions(result);
    	result = adjustScopeOfQuantifiers(result);
    	
    	return result;
    }   
}


