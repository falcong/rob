package testingjunit;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import solvingalgorithm.temporizedalgorithm.Timer;

public class TimerTest implements Observer {
	private boolean stop = false;
	
	/*
	 * test di run()
	 */
	@Test
	public void testRun() {
		//creo un timer con scadenza pari a 2 secondi
		final int TIME = 2;
		Timer timer = new Timer(TIME);
		timer.addObserver(this);
		
		/*
		 * controllo che la variabile stop sia messa a true dal timer (che chiamerà il metodo this.update) dopo
		 * 2 secondi
		 */
		long startTime = System.currentTimeMillis();
		timer.run();
		while(!stop){
			;
		}
		long finalTime = System.currentTimeMillis();
		long totalTime = finalTime-startTime;
		double totalTimeInSeconds = (double)totalTime/1000;
		
		assertEquals( TIME, totalTimeInSeconds, 0);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		stop = true;
	}

}
