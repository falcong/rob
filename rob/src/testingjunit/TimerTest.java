package testingjunit;

import static org.junit.Assert.*;
import java.util.Observable;
import java.util.Observer;
import org.junit.Test;
import solvingalgorithm.temporizedalgorithm.Timer;

public class TimerTest implements Observer {
	private boolean stop = false;
	
	//test di run()
	/*
	 * Caso generale.
	 */
	@Test
	public void testRun() {
		//creo un timer con scadenza pari a 2 secondi
		//TIME = tempo di scadenza in millisecondi
		final int TIME = 2000;
		final int TIME_IN_SECONDS = TIME/1000;
		Timer timer = new Timer(TIME_IN_SECONDS);
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
		//totalTime è in millisecondi
		long totalTime = finalTime-startTime;
		
		final int TOLERANCE = 10;
		assertEquals( TIME, totalTime, TOLERANCE);
		
	}

	
	@Override
	public void update(Observable o, Object arg) {
		stop = true;
	}

}
