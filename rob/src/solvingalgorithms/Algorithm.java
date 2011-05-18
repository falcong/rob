package solvingalgorithms;

import rob.Problem;
import rob.Solution;

public abstract class Algorithm{
	Problem problem;
	//istante unix time in cui l'esecuzione deve terminare
	long finalTime = -1;
	
	public abstract Solution execute(Solution startSolution);
	
	public abstract void setProblem(Problem problem);
	
	public abstract void setFinalTime(long finalTime);
}
