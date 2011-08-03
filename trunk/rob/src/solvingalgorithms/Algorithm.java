package solvingalgorithms;

import data.Problem;
import data.Solution;

public abstract class Algorithm{
	Problem problem;
	//istante unix time in cui l'esecuzione deve terminare
	long finalTime = -1;
	
	/*
	 * istante unix time in cui l'algorimto (se isolato)ha avuto inizio
	 * oppure istante in cui ha vauto inizio l'algoritmo padre
	 */
	long startTime;
	
	public abstract Solution execute(Solution startSolution);
	
	public abstract void setProblem(Problem problem);
	
	public abstract void setFinalTime(long finalTime);
	
	public abstract void setStartTime(long finalTime);
}
