package solvingalgorithm.temporizedalgorithm;

import java.util.Observable;

public class Timer extends Observable implements Runnable {
	/**
	 * Tempo di scadenza in millisecondi.
	 */
	private long expiryTime;
	private boolean started = false;
	
	/**
	 * Costruisce un timer.
	 * @param expiryTimeSeconds - tempo di scadenza del timer espresso in secondi.
	 */
	public Timer(int expiryTimeSeconds){
		this.expiryTime = expiryTimeSeconds*1000;
	}

	/**
	 * Fa partire il timer. Quando passano {@link #expiryTime} millisecondi il metodo invoca
	 * {@link #notifyObservers()} e notifica tutti gli algoritimi iscritti al timer.
	 */
	@Override
	public void run() {
		started = true;
		try {
			Thread.sleep(expiryTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
		//resetto lo stato del timer
		started = false;
	}
	
	/**
	 * Ritorna lo stato del timer.
	 * @return {@code true} se il timer è già partito, {@code false} se non è
	 * stato ancora avviato.
	 */
	public boolean getStatus(){
		return started;
	}
}
