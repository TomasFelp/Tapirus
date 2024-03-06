package tapir;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegularExpressionHelper {
	
	private static final String DEFAULT_STATE_CONTENT = "[^:;>-]*";
	private static final String DEFAULT_PRECONDITION = ":[^:;>-]*;>-";
	private static final String DEFAULT_POSTCONDITION = "-<:[^:;>-]*;";
	private static final Character STATE_START = ':';
	private static final Character STATE_END = ';';
	private static final String PRECONDITION = ";>-";
	private static final String POSTCONDITION = "-<:";
	private static final String EMPTY_PRECONDITION = ":;>-";
	private static final String EMPTY_POSTCONDITION = "-<:;";

	

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
    private static String indicateLiteralCharacters(String input) {
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
     * Returns a string without the changes added in the precompilation
     */
    public static String simplifyRegularExpressionForView(String input) {
    	
    	String result = input.replaceAll("\\Q"+DEFAULT_STATE_CONTENT+"\\E","");
    	result = result.replaceAll("\\Q"+EMPTY_PRECONDITION+"\\E","");
    	result = result.replaceAll("\\Q"+EMPTY_POSTCONDITION+"\\E","");
    	result = result.replaceAll("\\\\","");
    	
    	return result;
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
        
        // Acceso e impresión de los elementos del arreglo
        for (int i = 0; i < operators.length; i++) {
        	operator = operators[i];
	        // Buscar todas las ocurrencias de la subcadena del operador precedida por ":" y seguida por ";"
	        String patron = operator + "[^\\;\\,\\)=-]*\\)";
	        Pattern pattern = Pattern.compile(patron);
	        Matcher matcher = pattern.matcher(input);
	
	        while (matcher.find()) {
	            // Obtener la subcadena encontrada y convertirla a mayúsculas
	            String value = matcher.group();

	            String regularExpressionForValue = value.replaceFirst(operator, "");
	            regularExpressionForValue = regularExpressionForValue.replace(")", "");
	            
	            switch (operator) {
                case ">=":
                	regularExpressionForValue = "=" + getRegularExpressionforGreaterOrEqual(regularExpressionForValue) + ")";
                    break;
                case "<":
                	regularExpressionForValue = "=" + getRegularExpressionforLess(regularExpressionForValue) + ")";
                    break;
                case "<=":
                	regularExpressionForValue = "=" + getRegularExpressionforLessOrEqual(regularExpressionForValue) + ")";
                    break;
                case "!=":
                    regularExpressionForValue = "=" + getRegularExpressionforDifferentFrom(regularExpressionForValue) + ")";
                    break;
                case ">":
                	regularExpressionForValue = "=" + getRegularExpressionforGreater(regularExpressionForValue) + ")";
                    break;
                default:
                    break;
            }
	            
	            // Reemplazar la subcadena original con la versión en mayúsculas
	            result = result.replace(value, regularExpressionForValue);
	        }
        }

        return result;
    }
    
    /*
     * Returns a regular expression that accepts only values different from the one received.
     */
    private static String getRegularExpressionforDifferentFrom(String input) {
    	return "("+input+".+|(?!"+input+").*)";
    }
    
    /*
     * Returns a regular expression that accepts only natural numbers greater or equal than the one contained in the received string
     */
    private static String getRegularExpressionforGreaterOrEqual(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length -1 ; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            result.append(subcadena).append("["+ ( Character.getNumericValue(input.charAt(i)) + 1 ) +"-9]").append("\\d{"+posicion+",}|");
        }
        
        String subcadena = input.substring(0, length-1 );
        
        result.append(subcadena+"["+ ( Character.getNumericValue(input.charAt(length - 1))) +"-9]|");
        result.append("[1-9]\\d{"+length+",}");
        
        return "(" + result.toString() + ")";
    }
    
    /*
     * Returns a regular expression that accepts only natural numbers greater than the one contained in the received string
     */
    private static String getRegularExpressionforGreater(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            result.append(subcadena).append("["+ ( Character.getNumericValue(input.charAt(i)) + 1 ) +"-9]").append("\\d{"+posicion+",}|");
        }

        result.append("[1-9]\\d{"+length+",}");
        
        return "(" + result.toString() +")";
    }
    
    /*
     * Returns a regular expression that accepts only natural numbers less than the content of the received string.
     */
    private static String getRegularExpressionforLess(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            result.append(subcadena).append("[0-"+ ( Character.getNumericValue(input.charAt(i)) - 1  ) +"]").append("\\d{"+posicion+"}|");
        }

        result.append("\\d{0,"+(input.length() - 1)+"}");
        
        return "(" + result.toString() +")";
    }

    /*
     * Returns a regular expression that accepts only natural numbers less or equal than the content of the received string.
     */
    private static String getRegularExpressionforLessOrEqual(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i++) {
            String subcadena = input.substring(0, i );
            int posicion = length - 1 - i;
            result.append(subcadena).append("[0-"+ ( Character.getNumericValue(input.charAt(i)) - 1  ) +"]").append("\\d{"+posicion+"}|");
        }

        result.append(input+"|");
        result.append("\\d{0,"+(input.length() - 1)+"}");
        
        return "(" + result.toString() +")";
    }
    
/*
    public static void main(String[] args) {
        String input = "3954";

        String resultado = getRegularExpressionforLess(input);
        System.out.println(resultado);
    }
    */
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
    
    /*
    public static void main(String[] args) {
        String input = "a-<:(x3!=true),(x2=7),(x1=5);bb:(x!=true);>-c-<:(x=false);>eb-<:(b>=3),(c<=3),(a!=true);";
        String resultado = preCompile(input);
        System.out.println(resultado);
    }
    */
    
    public static void main(String[] args) {
        String input = "a-<:(x3!=true),(x2<7),(x1=5);bb:(x<=123);>-c-<:(x=false);>eb-<:(b>=3),(c<=3),(a!=true);";
        String resultado = normalizeComparators(input);
        System.out.println(resultado);
    }
        
}


