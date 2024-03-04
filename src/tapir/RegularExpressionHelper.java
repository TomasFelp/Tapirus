package tapir;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegularExpressionHelper {
	
	private static final String DEFAULT_STATE_CONTENT = "[^:;>-]*";
	private static final Character STATE_START = ':';
	private static final Character STATE_END = ';';
	private static final String PRECONDITION = ";>-";
	private static final String POSTCONDITION = "-<:";
	private static final String EMPTY_PRECONDITION = ":;>-";
	private static final String EMPTY_POSTCONDITION = "-<:;";

	
	/*
	 * For every element of the sequence that does not have a precondition or postcondition, add a generic by default.
	 */
	/*
	 * For every element of the sequence that does not have a precondition or postcondition, add a generic by default.
	 */
	public static String addDefaultStates(String regularExpression) {
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
                character != ':' && character != ';' && character != '<' && character != '>' ;
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
     * Lexicographically orders the tuples found in each condition.
     */
	public static String sortTuplesInState(String input) {
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
    public static String completeDefaultConditions(String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == STATE_END) {
                result.append(DEFAULT_STATE_CONTENT);
            }
            result.append(c);
        }

        return result.toString();
    }
    
    public static boolean is_regular_expression_with_states(String input) {
    	return input.contains(POSTCONDITION) || input.contains(POSTCONDITION);
    }

    /*
     * Some characters used to indicate the states are reserved by regex, 
     * this function identifies them and indicates that they should be taken literally
     */
    public static String indicateLiteralCharacters(String input) {
        StringBuilder result = new StringBuilder();

        boolean insideState = false;

        for (char c : input.toCharArray()) {
            if (c == STATE_START) {
                insideState = true;
            } else if (c == STATE_END) {
                insideState = false;
            }

            if (insideState && (c == '(' || c == ')')) {
                result.append("\\");
            }

            result.append(c);
        }

        return result.toString();
    }
    
    /*
     * receives a state in the form of a list of tuples and concatenates them with the precondition syntax
     */
    public static String makePrecondition(String input) {
    	return STATE_START + input + PRECONDITION;
    }
    
    /*
     * receives a state in the form of a list of tuples and concatenates them with the precondition syntax
     */
    public static String makePostScondition(String input) {
    	return POSTCONDITION + input + STATE_END;
    }
    
    /*
     * It precompiles a text with a certain format to bring it to a valid regex regular expression.
     * 
     */
    public static String preCompile(String intput) {
    	
    	String result = sortTuplesInState(intput);
    	result = addDefaultStates(result);
    	result = completeDefaultConditions(result);
    	result = indicateLiteralCharacters(result);
    	
    	return result;
    }
    
    public static void main(String[] args) {
        String input = "a-<:(x3!=true),(x2=7),(x1=5);bb:(x=true);>-c-<:(x=false);>eb-<:(b>=3),(c<=3),(a=true);";
        String resultado = preCompile(input);
        System.out.println(resultado);
    }
        
}


