package main;

public class ValidExample {

	public static void main(String[] args) {

		Account a1 = new Account();
		Account a2 = new Account();
		
		a1.verify();
		
		a1.deposit(1000);
		a1.deposit(4000);
		a1.withdraw(3000);
		
		a1.close();
		
		a2.verify();
		
		a2.deposit(1000);
		a2.deposit(2000);
		a2.deposit(1000);
		a2.deposit(1000);
		
		a2.withdraw(5000);
		
		a2.close();

	}

}
