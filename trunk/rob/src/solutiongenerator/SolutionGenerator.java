package solutiongenerator;


import data.Problem;
import data.Solution;

public abstract class SolutionGenerator {
	Problem problem;
	
	
	public SolutionGenerator(Problem problem){
		this.problem=problem;
	}

	
	public abstract Solution generate() throws Exception;
}
