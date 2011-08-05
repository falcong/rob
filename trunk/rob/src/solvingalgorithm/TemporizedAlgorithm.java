package solvingalgorithm;

import java.util.Observable;
import java.util.Observer;

import data.Solution;

public abstract class TemporizedAlgorithm extends Algorithm implements Observer {
	/*
	 * indica quando l'esecuzione dell'algoritmo deve terminare perché è stato esaurito il tempo massimo di esecuzione
	 */
	protected boolean stop = false;
	//di default non c'è un tempo massimo
	Timer timer = null;
	
	/*
	 * Chiamando questo metodo si ha l'interruzione dell'algoritmo
	 */
	@Override
	public void update(Observable o, Object arg) {
		stop = true;
	}
}
