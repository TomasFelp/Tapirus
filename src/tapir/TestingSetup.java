package tapir;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Account;
import main.CheckAccount;
import main.DummyClass;
import main.DummyClass2;
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
		mapAttributesToSymbols.put("amount", "a");
		mapAttributesToSymbols.put("isOverdrawn", "o");
		//Definition of the regular expression
		pattern = "cv(b|w-:'a>=0';|d-:'a<=10000';|w-:'a<0','o=true';(b|d-:'a<0';)*d-:'a>=0';|(d-:'a>10000';|f)b*u)*:'a=0';-x";
		//pattern = "cv(b|w-:(amount>=0);|d-:(amount<=10000);|w-:(amount<0),(isOverdrawn=true);(b|d-:(amount<0);)*d-:(amount>=0);|(d-:(amount>10000);|f)b*u)*:(amount=0);-x";
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(ModalAccount.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(ModalAccount.class.toString(), ti);
		
		
		/*
		 * --------------------------------------------------------------------------------------------------------------------------------------------------------
		 * 				DUMMY CLASS
		 * --------------------------------------------------------------------------------------------------------------------------------------------------------
		 */
		
		mapObjectsToCallSequence = null; 
		mapMethodsToSymbols = null; 
		regularExpression = null; 
		matcher = null;
		
		// Testing setup for Modal Account class
		//Definition of the methods and their corresponding symbols
		mapObjectsToCallSequence = new HashMap<>(); 
		mapMethodsToSymbols = new HashMap<String, String>();
		mapMethodsToSymbols.put("main.DummyClass.<init>", "i");
		mapMethodsToSymbols.put("main.DummyClass.a", "a");
		mapMethodsToSymbols.put("main.DummyClass.b", "b");
		mapMethodsToSymbols.put("main.DummyClass.c", "c");
		mapMethodsToSymbols.put("main.DummyClass.d", "d");
		mapMethodsToSymbols.put("main.DummyClass.setNumber", "N");
		mapMethodsToSymbols.put("main.DummyClass.setCharacter", "C");
		mapMethodsToSymbols.put("main.DummyClass.setBoolean", "B");
		mapMethodsToSymbols.put("main.DummyClass.setString", "S");
		//Definition of the attributes and their corresponding symbols
		mapAttributesToSymbols = new HashMap<String, String>();
		mapAttributesToSymbols.put("number", "n");
		mapAttributesToSymbols.put("bool", "b");
		mapAttributesToSymbols.put("string", "s");
		mapAttributesToSymbols.put("character", "c");
		//Definition of the regular expression
		
		 int test = 21;
		
		 switch (test) {
		 	case 1:
				pattern = "i-:'s=Max Power';abcd"; //test 1
		 		break;
		 	case 2:
		 		pattern = "iabcB:'b=true';-N-:'n=3';"; //test 2 
		 		break;
		 	case 3:
		 		pattern = "iaC:'c=e';-S-:'s=bye bye';bcd"; //test 3
		 		break;
		 	case 4:
		 		pattern = "iaC:'c=e';-S-:'s=bye bye';*bcd"; //test 4
		 		break;
		 	case 5:
		 		pattern = "iaC(:'c=e';-S-:'s=bye bye';|b)cd"; //test 5
		 		break;
		 	case 6:
		 		pattern = "iabN-:'b=true','n=17','s=Max Power';cd"; //test 6
		 		break;
		 	case 7:
		 		pattern = "iabN-:'s=Max Power','n=17','b=true';cd"; //test 7
		 		break;
		 	case 8:
		 		pattern = "iaN:'n>=237';-bc"; //test 8
		 		break;
		 	case 9:
		 		pattern = "iaN:'n>=0';-bc"; //test 9
		 		break;
		 	case 10:
		 		pattern = "iaN:'n>237';-bc"; //test 10
		 		break;
		 	case 11:
		 		pattern = "iaN:'n>0';-bc"; //test 11
		 		break;
		 	case 12:
		 		pattern = "iaN:'n<=237';-bc"; //test 12
		 		break;
		 	case 13:
		 		pattern = "iaN:'n<=0';-bc"; //test 13
		 		break;
		 	case 14:
		 		pattern = "iaN:'n<237';-bc"; //test 14
		 		break;
		 	case 15:
		 		pattern = "iaN:'n<0';-bc"; //test 15
		 		break;
		 	case 16:
		 		pattern = "iaS:'s!=Max Power';-bc"; //test 16
		 		break;
		 	case 17:
		 		pattern = "iaC:'c!=a';-bc"; //test 17
		 		break;
		 	case 18:
		 		pattern = "iaB:'b!=true';-bc"; //test 18
		 		break;
		 	case 19:
		 		pattern = "iaN:'n!=5';-bc"; //test 19
		 		break;
		 	case 20:
		 		pattern = "iaCN-:'n=5','c=u';N:'n!=5';-bc"; //test 20
		 		break;
		 	case 21:
		 		pattern = "iaCS-:'s=hello';Na-:'n=5','c=u';BCS:'s=mr. Thompson','b=true';-N-:'c=a','n=10';C:'n!=5';-N-:'n=11','c=r';bc"; //test 21
		 		break;
		 	default:
		 		System.out.println("test number not defined");
		 		pattern = ".*";
			 
		 }
				
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(DummyClass.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(DummyClass.class.toString(), ti);
		
		mapObjectsToCallSequence = null; 
		mapMethodsToSymbols = null; 
		regularExpression = null; 
		matcher = null;
		
		// Testing setup for Modal Account class
		//Definition of the methods and their corresponding symbols
		mapObjectsToCallSequence = new HashMap<>(); 
		mapMethodsToSymbols = new HashMap<String, String>();
		mapMethodsToSymbols.put("main.DummyClass2.<init>", "i");
		mapMethodsToSymbols.put("main.DummyClass2.a", "a");
		mapMethodsToSymbols.put("main.DummyClass2.b", "b");
		mapMethodsToSymbols.put("main.DummyClass2.c", "c");
		mapMethodsToSymbols.put("main.DummyClass2.setNumber", "N");
		mapMethodsToSymbols.put("main.DummyClass2.setString", "S");
		//Definition of the attributes and their corresponding symbols
		mapAttributesToSymbols = new HashMap<String, String>();
		mapAttributesToSymbols.put("number", "n");
		mapAttributesToSymbols.put("bool", "b");
		mapAttributesToSymbols.put("string", "s");
		mapAttributesToSymbols.put("character", "c");
		//Definition of the regular expression
		
		pattern = "iaSN-:'n=50','s!=null';ba"; //test 20
		
		
		//pattern = "cv(b|w-:(amount>=0);|d-:(amount<=10000);|w-:(amount<0),(isOverdrawn=true);(b|d-:(amount<0);)*d-:(amount>=0);|(d-:(amount>10000);|f)b*u)*:(amount=0);-x";
		regularExpression = ModalPattern.compile(pattern);
		//Initializing the regular expressions controller
		matcher = regularExpression.matcher("");	
		// All information related to how the Check Account class is testing is store in a TestingInformation instance
		ti = new TestingInformation(DummyClass2.class.toString(), mapObjectsToCallSequence, mapMethodsToSymbols, mapAttributesToSymbols, pattern, regularExpression, matcher, false);
		TestingCore.mapClassToTestingInformation.put(DummyClass2.class.toString(), ti);
		
}
	
}
