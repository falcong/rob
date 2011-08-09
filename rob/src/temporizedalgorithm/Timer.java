package temporizedalgorithm;

import java.util.Observable;

public class Timer extends Observable implements Runnable {
	private long expiryTime;
	private boolean started = false;
	
	//expiryTime espresso in secondi
	public Timer(int expiryTime){
		this.expiryTime = expiryTime*1000;
	}
	
	/*public void start(){
		//controllo che non venga chiamato 2 volte
		
	}*/

	@Override
	public void run() {
		started = true;
		try {
			Thread.sleep(expiryTime);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
		started = false;
	}
	
	public boolean getStatus(){
		return started;
	}
}
