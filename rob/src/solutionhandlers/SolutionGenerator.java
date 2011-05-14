package solutionhandlers;


import rob.Problem;
import rob.Solution;

public abstract class SolutionGenerator {
	Problem problem;
	
	public SolutionGenerator(Problem problem){
		this.problem=problem;
	}
	
	public SolutionGenerator(){
	}

	public abstract Solution generate();	
	
	public void setProblem(Problem problem) {
		this.problem=problem;
	}
	
}
