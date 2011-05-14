package solutionhandlers;

import rob.Problem;
import rob.Solution;

public abstract class NeighbourGenerator {
	public abstract Solution generate(Solution solution, int distance);
	public abstract void setProblem(Problem problem);
}
