package main;

public class InvalidModalExample {

	public static void main(String[] args) {
			
		System.out.println("Start testing of the first ModalAccount object: It must be identified that the account cannot be overdrawn.");
		ModalAccount m1 = new ModalAccount();
		m1.verify();
		m1.deposit(5000);
		m1.withdraw(3000);
		m1.balance();
		m1.freeze();
		m1.balance();
		m1.balance();
		m1.unfreeze();
		m1.withdraw(5000);
			
		System.out.println("Test of the second ModalAccount object begins: It must be identified that the student account is not frozen when acquiring an amount greater than 10,000.");
		ModalAccount m2 = new ModalAccount();
		m2.verify();
		m2.deposit(1000);
		m2.deposit(20000);
		m2.deposit(10);

		System.out.println("Test of the third ModalAccount object begins: It must be identified that is trying to close the account but it is still have a balance");
		ModalAccount m3 = new ModalAccount();
		m3.verify();
		m3.deposit(2000);
		m3.withdraw(100);
		m3.balance();
		m3.deposit(2000);
		m3.close();
		
	}

}
