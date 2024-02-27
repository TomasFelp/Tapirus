package main;


public class InvalidExample {

	public static void main(String[] args) {
		
		Account a9 = new Account();
		CheckAccount c8 = new CheckAccount();
		c8.verify();
		a9.verify();
		c8.verify();
		
		a9.deposit(1000);
		a9.deposit(4000);
		a9.withdraw(3000);
		
		a9.verify();
		
		a9.close();
	}

}
