package tapir;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModalPattern {

    private static Pattern pattern = null;
    
    public static Pattern compile(String input) {
    	    	
    	if(RegularExpressionHelper.is_regular_expression_with_states(input)) {
    		input=RegularExpressionHelper.preCompile(input);
    	}
    	
		pattern = Pattern.compile(input);
		
		return pattern;
    }

    public Matcher matcher(String input) {
        return pattern.matcher(input);
    }
    
    public Pattern getPattern() {
    	return pattern;
    }

}