package main;

public class CheckAccount {
	
	protected int amount;
	protected boolean verify;
	
	public CheckAccount() {
		this.amount=0;
		this.verify=false;
	}
	
	public void verify() {
		this.verify=true;
	}
	
	public boolean isVerify() {
		return this.verify;
	}
	
	public void deposit(int amount) {
		if(this.isVerify()) {
			this.amount+=amount;
		}
	}
	
	public void withdraw(int amount) {
		if(this.isVerify()) {
			this.amount-=amount;
		}
	}
	
	public void close() {
		this.amount=0;
		this.verify=false;
	}

}
