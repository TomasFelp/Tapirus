package main;

public class ModalAccount {
	protected int amount;
	private boolean isFrozen;
	private boolean isOverdrawn;
	protected boolean verify;
	
	public ModalAccount() {
		this.amount=0;
		this.verify=false;
		this.isOverdrawn=false;
	}
	
	public void verify() {
		this.verify=true;
	}
	
	public boolean isVerify() {
		return this.verify;
	}
	
	public void deposit(int amount) {
		if(this.isVerify() && !isFrozen) {
			this.amount+=amount;
		}
	}
	
	public void withdraw(int amount) {
		if(this.isVerify() && !isFrozen) {
			this.amount-=amount;
		}
	}
	
	public void close() {
		this.amount=0;
		this.verify=false;
	}
	
	public int balance() {
        // Report current balance
        return amount;
    }

    public void freeze() {
        // Suspend customer transactions
        isFrozen = true;
    }

    public void unfreeze() {
        // Allow customer transactions
        isFrozen = false;
    }
}
