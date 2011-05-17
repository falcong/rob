package solvingalgorithms;

import rob.Problem;
import rob.Solution;

public abstract class Algorithm{
	Problem problem;
	
	public abstract Solution execute(Solution startSolution);
	
	public abstract void setProblem(Problem problem);
}
