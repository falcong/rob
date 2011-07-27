package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solvingalgorithms.Cplex;

public class LinesSolutionGeneratorTest {

	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public void testGenerate() {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.50.40.3.1.10.1.ctqd";
		Problem problem=pp.parse(PROBLEM_NAME);
		
		Cplex cplexSolver = new Cplex(problem);
		Solution sol = cplexSolver.execute(null);
		double objFunction = sol.getObjectiveFunction();
		
		final double expectedObjFunction = 9575.00;	
		final double tolerance = 0.10;
		
		assertEquals(expectedObjFunction, objFunction, tolerance);
	}

}
