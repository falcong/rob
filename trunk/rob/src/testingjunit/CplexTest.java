package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import org.junit.Test;
import data.Problem;
import data.Solution;
import solvingalgorithm.Cplex;

public class CplexTest {
	

	//test di execute()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testExecute1() throws Exception {
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.40.3.1.99.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		Cplex cplexSolver = new Cplex(problem);
		Solution sol = cplexSolver.execute(null);
		double objFunction = sol.getObjectiveFunction();
		
		final double expectedObjFunction = 9575.00;	
		final double TOLERANCE = 0.10;
		
		assertEquals(expectedObjFunction, objFunction, TOLERANCE);
	}
}
