package tapir;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegularExpressionHelper {
	
	/*
	 * For every element of the sequence that does not have a precondition or postcondition, add a generic by default.
	 */
	public static String addDefaultConditions(String regularExpression) {
        StringBuilder result = new StringBuilder();
        boolean insideSquareBrackets = false;

        for (int i = 0; i < regularExpression.length(); i++) {
            char character = regularExpression.charAt(i);

            if (character == '[') {
                insideSquareBrackets = true;
            }

            if (!insideSquareBrackets && is_not_a_reserved_character(character) && (i == 0 || regularExpression.charAt(i - 1) != '-')) 
            {
                result.append("[.*]>-").append(character);
            } else {
                result.append(character);
            }
            
            if (!insideSquareBrackets && is_not_a_reserved_character(character) && (i == regularExpression.length() - 1 || regularExpression.charAt(i + 1) != '-')) 
            	{
                    result.append("-<[.*]");
                } 

            if (character == ']') {
                insideSquareBrackets = false;
            }
        }

        return result.toString();
    }
	
	private static boolean is_not_a_reserved_character(char character) {
    	return character != '(' && character != ')' && character != '|' &&
                character != '*' && character != '-' && character != '>' && character != '<' &&
                character != ']' && character != '[';
    }

    
    /**
     * Sorts a list of tuples according to the lexicographic order of the first element of the tuple. 
     * It supports tuple formats (Xi w Yi), where w belongs to ["=","<=",">=",">","<","!"] among others.
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
        public static String sortTuplesWithinSquareBrackets(String input) {
            String patternSquareBrackets = "\\[(.*?)\\]";
            Pattern pattern = Pattern.compile(patternSquareBrackets);
            Matcher matcher = pattern.matcher(input);
            StringBuffer result = new StringBuffer();
            
            while (matcher.find()) {
                String textInSquareBrackets = matcher.group(1);
                String resultSortTuples = sortTuples(textInSquareBrackets);
                matcher.appendReplacement(result, "["+resultSortTuples+"]");
            }
            matcher.appendTail(result);

            return result.toString();
        }
        

        /*
         * complete preconditions with default conditions ".*"
         */
        public static String completeDefaultConditions(String input) {
            StringBuilder result = new StringBuilder();

            for (char c : input.toCharArray()) {
                if (c == ']') {
                    result.append(".*");
                }
                result.append(c);
            }

            return result.toString();
        }
        /*
         * It precompiles a text with a certain format to bring it to a valid regex regular expression.
         * 
         * !!!!!!!!!!!!!!!!!IN PROGRESS...
         */
        public static String preCompile(String intput) {
        	
        	String result = sortTuplesWithinSquareBrackets(intput);
        	result = completeDefaultConditions(result);
        	result = addDefaultConditions(result);
        	
        	return result;
        }
        
        public static void main(String[] args) {
            String input = "a-<[(x3!=true),(x2=7),(x1<=5)]bb[(x=true)]>-c-<[(x=false)]eb-<[(b>=3),(c<=3),(a=true)].";
            String resultado = preCompile(input);
            System.out.println(resultado);
        }
}
