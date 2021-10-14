/*
	은행클래스 - 계좌번호,잔액,입금기능,출금기능
	은행클래스 객체를 사용하는 입금기능의 스레드와 출금기능의 스레드를 만들고 
	동기화 처리 기능을 사용해 보세요.
*/
// 1초마다 계좌에 1만원씩을 입금하고 10만원 이상이 모여야 출금하는 식으로 동기화 과정을 구현해봄
class Bank {
	private String accountNum;
	private long balance = 0;
	
	public Bank(String accountNum) {
		this.accountNum = accountNum;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getBalance() {
		return balance;
	}
}

class Deposit implements Runnable {
	private Bank account;
	
	public Deposit(Bank account) {
		this.account = account;
	}
	public void deposit(long money) {
		long balance = account.getBalance();
		balance += money;
		account.setBalance(balance);
	}
	@Override
	public void run() {
		while(true) {
			synchronized(account) {
				deposit(10000);
				long balance = account.getBalance();
				System.out.println("1만원 저축하였습니다. 잔고: " + balance);
				if(balance >= 100000) account.notify();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
		}
	}
}

class Withdraw implements Runnable {
	private Bank account;
	
	public Withdraw(Bank account) {
		this.account = account;
	}
	public void withdraw(long money) {
		long balance = account.getBalance();
		balance -= money;
		account.setBalance(balance);
	}
	@Override
	public void run() {
		while(true) {
			synchronized(account) {
				if(account.getBalance() < 100000) { // 10만원 미만이면 저축할때까지 wait()로 대기
					try {
						account.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				withdraw(100000);
				System.out.println("10만원 출금하였습니다. 잔고: " + account.getBalance());
			} 
		}
	}
}

public class Assignment01 {
	public static void main(String[] args) {
		Bank account = new Bank("333-053617-01-017");
		Thread th1 = new Thread(new Withdraw(account));
		Thread th2 = new Thread(new Deposit(account));
		
		th1.start();
		th2.start();
	}
}
