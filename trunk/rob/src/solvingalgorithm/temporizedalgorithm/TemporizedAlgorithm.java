package solvingalgorithm.temporizedalgorithm;

import java.util.Observable;
import java.util.Observer;

import solvingalgorithm.Algorithm;

public abstract class TemporizedAlgorithm extends Algorithm implements Observer {
	/*
	 * indica quando l'esecuzione dell'algoritmo deve terminare perché è stato esaurito il tempo massimo di esecuzione
	 */
	protected boolean stop = false;
	//di default non c'è un tempo massimo
	protected Timer timer = null;
	
	/*
	 * Chiamando questo metodo si ha l'interruzione dell'algoritmo
	 */
	@Override
	public void update(Observable o, Object arg) {
		stop = true;
	}
	
	public void startTimer(){
		if(!timer.getStatus()){
			timer.run();
		}
	}
}
