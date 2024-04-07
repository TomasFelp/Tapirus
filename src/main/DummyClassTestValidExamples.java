package main;

public class DummyClassTestValidExamples {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int test = 1;//Make sure the test number corresponds to the pattern selected in TestingSetup
		
		DummyClass d1 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d2 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d3 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d4 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d5 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d6 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d7 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass d8 = new DummyClass(1, 'a', true, "Max Power");
		DummyClass2 f1 = new DummyClass2(1, 'a', true, "Max Power");
		DummyClass2 f2 = new DummyClass2(1, 'a', true, null);
		
		switch (test) {
			case 1: // test 1 - RE: "i-:'s=Max Power';abcd" (test simple post condition in the first method of the sequence)
				d1.a();
				d1.b();
				d1.c();
				d1.d();
				break;
			case 2: // test 2 - RE: "iabc:'b=true';-N-:'n=3';" (test pre and post simple condition in the last method of the sequence)"
				d1.a();
				d1.b();
				d1.c();
				d1.setBoolean(true);
				d1.setNumber(3);
				break;
			case 3:	// test 3 - RE: "iaC:'c=e';-S-:'s=bye bye';bcd" (test pre and post simple condition in an intermediate method of the sequence)"
				d1.a();
				d1.setCharacter('e');
				d1.setString("bye bye");
				d1.b();
				d1.c();
				d1.d();
				break;
			case 4:	// test 4 - RE: "iaC:'c=e';-S-:'s=bye bye';*bcd" (test scope of quantifier, the quantifier must cover pre and post conditions. :'c=e';-S-:'s=bye bye';* eq to (:'c=e';-S-:'s=bye bye';)*)"
				d1.a();
				d1.setCharacter('e');
				d1.b();
				d1.c();
				d1.d();
				
				d2.a();
				d2.setCharacter('e');
				d2.setString("bye bye");
				d2.b();
				d2.c();
				d2.d();
				
				d3.a();
				d3.setCharacter('e');
				d3.setString("bye bye");
				d3.setString("bye bye");
				d3.setString("bye bye");
				d3.setString("bye bye");
				d3.b();
				d3.c();
				d3.d();
				break;
			case 5: // test 5 - RE: "iaC(:'c=e';-S-:'s=bye bye';|b)cd" (test scope of operator '|', the operator must cover pre and post conditions. :'c=e';-S-:'s=bye bye';|b eq to (:'c=e';-S-:'s=bye bye';)|b)"
				d1.a();
				d1.setCharacter('e');
				d1.setString("bye bye");
				d1.c();
				d1.d();
				
				d2.a();
				d2.setCharacter('e');
				d2.b();
				d2.c();
				d2.d();
				break;
			case 6:	// test 6 - RE: "iabN-:'b=true','n=17','s=Max Power';cd" (test method with multiple simple conditions arranged lexicographically ascending)"
				d1.a();
				d1.b();
				d1.setNumber(17);
				d1.c();
				d1.d();
				break;
			case 7: // test 7 - RE: "iabN-:'s=Max Power','n=17','b=true';cd" (test method with multiple simple conditions arranged lexicographically in descending order)"
				d1.a();
				d1.b();
				d1.setNumber(17);
				d1.c();
				d1.d();
				break;
			case 8: // test 8 - RE: "iaN:'n>=237';-bc" (test comparator >=)"
				d1.a();
				d1.setNumber(237);
				d1.b();
				
				d2.a();
				d2.setNumber(238);
				d2.b();
				
				d3.a();
				d3.setNumber(240);
				d3.b();
				
				d4.a();
				d4.setNumber(300);
				d4.b();
				
				d5.a();
				d5.setNumber(10000);
				d5.b();
				break;
			case 9:	// test 9 - RE: "iaN:'n>=0';-bc" (test comparator >=)"
				d1.a();
				d1.setNumber(0);
				d1.b();
				
				d2.a();
				d2.setNumber(7);
				d2.b();
				break;
			case 10: // test 10 - RE: "iaN:'n>237';-bc" (test comparator >)"
				d2.a();
				d2.setNumber(238);
				d2.b();
				
				d3.a();
				d3.setNumber(240);
				d3.b();
				
				d4.a();
				d4.setNumber(300);
				d4.b();
				
				d5.a();
				d5.setNumber(10000);
				d5.b();
				break;
			case 11: // test 11 - RE: "iaN:'n>0';-bc" (test comparator >)"
				d2.a();
				d2.setNumber(7);
				d2.b();
				break;
			case 12: // test 12 - RE: "iaN:'n<=237';-bc" (test comparator <=)"
				d1.a();
				d1.setNumber(237);
				d1.b();
				
				d2.a();
				d2.setNumber(236);
				d2.b();
				
				d3.a();
				d3.setNumber(220);
				d3.b();
				
				d4.a();
				d4.setNumber(100);
				d4.b();
				
				d5.a();
				d5.setNumber(20);
				d5.b();
				
				d6.a();
				d6.setNumber(5);
				d6.b();
				
				d7.a();
				d7.setNumber(0);
				d7.b();
				
				d8.a();
				d8.setNumber(-1000);
				d8.b();
				break;
			case 13: // test 13 - RE: "iaN:'n<=0';-bc" (test comparator <=)"
				d1.a();
				d1.setNumber(0);
				d1.b();
				
				d2.a();
				d2.setNumber(-5);
				d2.b();

				d3.a();
				d3.setNumber(-220);
				d3.b();
				break;
			case 14: // test 14 - RE: "iaN:'n<237';-bc" (test comparator <)"		
				d2.a();
				d2.setNumber(236);
				d2.b();
				
				d3.a();
				d3.setNumber(220);
				d3.b();
				
				d4.a();
				d4.setNumber(100);
				d4.b();
				
				d5.a();
				d5.setNumber(20);
				d5.b();
				
				d6.a();
				d6.setNumber(5);
				d6.b();
				
				d7.a();
				d7.setNumber(0);
				d7.b();
				
				d8.a();
				d8.setNumber(-1000);
				d8.b();
				break;
			case 15: // test 15 - RE: "iaN:'n<0';-bc" (test comparator <)"
				d2.a();
				d2.setNumber(-5);
				d2.b();
				
				d3.a();
				d3.setNumber(-220);
				d3.b();
				break;
			case 16: // test 16 - RE: "iaS:'s!=Max Power';-bc" (test the comparator != on a string)"
				d1.a();
				d1.setString("Max Potmer");
				d1.b();
				
				d2.a();
				d2.setString("Max Powerr");
				d2.b();
				
				d3.a();
				d3.setString("DMax Power");
				d3.b();
				break;
			case 17: // test 17 - RE: "iaC:'c!=a';-bc" (test the comparator != on a character)"
				d1.a();
				d1.setCharacter('e');
				d1.b();
				
				d2.a();
				d2.setCharacter(' ');
				d2.b();
				break;
			case 18: // test 18 - RE: "iaB:'b!=true';-bc" (test the comparator != on a boolean)"
				d1.a();
				d1.setBoolean(false);
				d1.b();
				break;
			case 19: // test 19 - RE: "iaN:'n!=5';-bc" (test the comparator != on a integer)"
				d1.a();
				d1.setNumber(6);
				d1.b();
				
				d2.a();
				d2.setNumber(-5);
				d2.b();
				
				d3.a();
				d3.setNumber(15);
				d3.b();
				break;
			case 20: // test 20 - RE DC1: "iaCN-:'n=5','c=u';N:'n!=5';-bc" (Test simple case with multiple objects of different classes)"
					//            RE DC2: "iaSN-:'n=50','s!=null';ba"
				d1.a();
				f2.a();
				f1.a();
				f1.setString("Hmmm...");
				d4.a();
				d4.setCharacter('u');
				f2.setString("dental plan");
				d1.setCharacter('u');
				f1.setNumber(50);
				f2.setNumber(50);
				d4.setNumber(5);
				f2.b();
				f1.b();
				d1.setNumber(5);
				d4.setNumber(4);
				f1.a();
				d4.b();
				d1.setNumber(6);
				f2.a();
				d1.b();
				break;
			case 21: // test 21 - RE: "iaCS-:'s=hello';Na-:'n=5','c=u';BCS:'s=mr. Thompson','b=true';-N-:'c=a','n=10';C:'n!=5';-N-:'n=11','c=r';bc" (test different calls with different pre and post conditions)"
				d1.a();
				d1.setCharacter('u');
				d1.setString("hello");
				d1.setNumber(5);
				d1.a();
				d1.setBoolean(true);
				d1.setCharacter('a');
				d1.setString("mr. Thompson");
				d1.setNumber(10);
				d1.setCharacter('r');
				d1.setNumber(11);
				d1.b();
				break;
			default:
				System.out.println("test number not defined");
			
		}
				
	}

}
