package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Account;
import main.CheckAccount;
import main.ModalAccount;

public class TestingSetup {
	public static void setup() {
		HashMap<Integer, String> mapObjectsToCallSequence = null; 
		HashMap<String, String> mapMethodsToSymbols = null; 
		HashMap<String, String> mapAttributesToSymbols = null;
		Pattern regularExpression = null; 
		Matcher matcher = null;
		String pattern = null;
		TestingInformation ti = null;	
		
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
		pattern = "cvd(d|w)*x";
		//String pattern = "cvd(d|w)*x";
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(Account.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, true);
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
		pattern = "cvd(d|w)*x";
		//String pattern = "cvd(d|w)*x";
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(CheckAccount.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(CheckAccount.class.toString(), ti);
		
		mapObjectsToCallSequence = null; 
		mapMethodsToSymbols = null; 
		regularExpression = null; 
		matcher = null;
		
		// Testing setup for Modal Account class
		//Definition of the methods and their corresponding symbols
		mapObjectsToCallSequence = new HashMap<>(); 
		mapMethodsToSymbols = new HashMap<String, String>();
		mapMethodsToSymbols.put("main.ModalAccount.<init>", "c");
		mapMethodsToSymbols.put("main.ModalAccount.verify", "v");
		mapMethodsToSymbols.put("main.ModalAccount.deposit", "d");
		mapMethodsToSymbols.put("main.ModalAccount.withdraw", "w");
		mapMethodsToSymbols.put("main.ModalAccount.close", "x");
		mapMethodsToSymbols.put("main.ModalAccount.freeze", "f");
		mapMethodsToSymbols.put("main.ModalAccount.unfreeze", "u");
		mapMethodsToSymbols.put("main.ModalAccount.balance", "b");
		//Definition of the attributes and their corresponding symbols
		mapAttributesToSymbols = new HashMap<String, String>();
		mapAttributesToSymbols.put("main.ModalAccount.amount", "a");
		mapAttributesToSymbols.put("main.ModalAccount.isOverdrawn", "o");
		//Definition of the regular expression
		pattern = "cv(b|w-:'a>=0';|d-:'a<=10000';|w-:'a<0','o=true';(b|d-:'a<0';)*d-:'a>=0';|(d-:'a>10000';|f)b*u)*:'a=0';-x";
		//pattern = "cv(b|w-:(amount>=0);|d-:(amount<=10000);|w-:(amount<0),(isOverdrawn=true);(b|d-:(amount<0);)*d-:(amount>=0);|(d-:(amount>10000);|f)b*u)*:(amount=0);-x";
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(ModalAccount.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(ModalAccount.class.toString(), ti);

	}
	
}
