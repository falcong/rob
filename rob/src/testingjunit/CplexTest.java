package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

//MOD rob2.Problem
import rob.Problem;
import rob.Utility;
//MOD
import rob.Solution;
import solvingalgorithms.Cplex;

public class CplexTest {
	

	//test di execute()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testExecute1() {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.40.3.1.99.1.ctqd";
		Problem problem;
		try {
			problem = pp.parse(PROBLEM_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Cplex cplexSolver = new Cplex(problem);
		Solution sol = cplexSolver.execute(null);
		double objFunction = sol.getObjectiveFunction();
		
		final double expectedObjFunction = 9575.00;	
		final double TOLERANCE = 0.10;
		
		assertEquals(expectedObjFunction, objFunction, TOLERANCE);
	}
}
