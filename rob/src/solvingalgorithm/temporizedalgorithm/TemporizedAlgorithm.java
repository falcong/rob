package solvingalgorithm.temporizedalgorithm;

import java.util.Observable;
import java.util.Observer;

import solvingalgorithm.Algorithm;

public abstract class TemporizedAlgorithm extends Algorithm implements Observer {
	/**
	 * Indica quando l'esecuzione dell'algoritmo deve terminare perché è stato esaurito il tempo massimo di esecuzione.
	 */
	protected boolean stop = false;
	
	/**
	 * L'oggetto timer associato all'algoritmo. Di default non è impostato nessun timer.
	 */
	protected Timer timer = null;
	
	/**
	 * Chiamando questo metodo l'esecuzione dell'algoritmo si interromperà e ritornerà
	 * al chiamante l'ultimo risultato ottenuto. 
	 */
	@Override
	public void update(Observable o, Object arg) {
		stop = true;
	}
	
	/**
	 * Avvia il thread del timer se il timer non è già partito. Se il timer è già in
	 * esecuzione, questo metodo non fa nulla.
	 */
	public void startTimer(){
		if(!timer.getStatus()){
			timer.run();
		}
	}
}
