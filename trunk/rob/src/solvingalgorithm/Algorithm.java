package solvingalgorithm;

import data.Problem;
import data.Solution;

public abstract class Algorithm{
	protected Problem problem;
	
	public abstract Solution execute(Solution startSolution);
}
