package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Account;
import main.CheckAccount;

public class TestingSetup {
	public static void setup() {
		HashMap<Integer, String> mapObjectsToCallSequence = null; 
		HashMap<String, String> mapMethodsToSymbols = null; 
		Pattern regularExpression = null; 
		Matcher matcher = null;
		
		//Specification of the test class\
		TestingCore.mapClassToTestingInformation = new HashMap<>();
			
		
		// Testing setup for Account class
		//Definition of the methods and their corresponding symbols
		mapObjectsToCallSequence = new HashMap<>(); 
		mapMethodsToSymbols = new HashMap<String, String>();
		mapMethodsToSymbols.put("main.Account.<init>", "c");
		mapMethodsToSymbols.put("main.Account.verify", "v");
		mapMethodsToSymbols.put("main.Account.deposit", "d");
		mapMethodsToSymbols.put("main.Account.withdraw", "w");
		mapMethodsToSymbols.put("main.Account.close", "x");
		//Definition of the regular expression
		regularExpression = Pattern.compile("cvd(d|w)*x");
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Account class is testing is store in a TestingInformation instance
		TestingInformation ti = new TestingInformation(Account.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, regularExpression, matcher, true);
		TestingCore.mapClassToTestingInformation.put(Account.class.toString(), ti);
		
		mapObjectsToCallSequence = null; 
		mapMethodsToSymbols = null; 
		regularExpression = null; 
		matcher = null;
		
		// Testing setup for Check Account class
		//Definition of the methods and their corresponding symbols
		mapObjectsToCallSequence = new HashMap<>(); 
		mapMethodsToSymbols = new HashMap<String, String>();
		mapMethodsToSymbols.put("main.CheckAccount.<init>", "c");
		mapMethodsToSymbols.put("main.CheckAccount.verify", "v");
		mapMethodsToSymbols.put("main.CheckAccount.deposit", "d");
		mapMethodsToSymbols.put("main.CheckAccount.withdraw", "w");
		mapMethodsToSymbols.put("main.CheckAccount.close", "x");
		//Definition of the regular expression
		regularExpression = Pattern.compile("cvd(d|w)*x");
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(CheckAccount.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(CheckAccount.class.toString(), ti);
}
	
}
