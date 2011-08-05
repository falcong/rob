package solvingalgorithm;

import java.util.Observable;

public class Timer extends Observable {
	int expiryTime;
	
	//expiryTime espresso in secondi
	public Timer(int expiryTime){
		this.expiryTime = expiryTime;
	}
	
	public void start(){
		//controllo che non venga chiamato 2 volte
		setChanged();
		notifyObservers();
	}
}
