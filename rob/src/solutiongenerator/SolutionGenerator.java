package solutiongenerator;


import data.Problem;
import data.Solution;

public abstract class SolutionGenerator {
	Problem problem;
	
	/**
	 * Crea un generatore di soluzione di tipo generico per il problema problem.
	 */
	public SolutionGenerator(Problem problem){
		this.problem=problem;
	}

	/**
	 * Genera la soluzione.
	 */
	public abstract Solution generate() throws Exception;
}
