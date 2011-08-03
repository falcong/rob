package solvingalgorithm;

import data.Problem;
import data.Solution;

public abstract class Algorithm{
	Problem problem;
	
	public abstract Solution execute(Solution startSolution);
}
