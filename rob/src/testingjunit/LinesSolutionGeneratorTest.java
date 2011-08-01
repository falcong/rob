package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solvingalgorithms.Cplex;
import util.Constants;

public class LinesSolutionGeneratorTest {

	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public void testGenerate() throws Exception {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.50.40.3.1.10.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		LinesSolutionGenerator generator = new LinesSolutionGenerator(problem);
		//sol generata
		Solution sol = generator.generate();
		//controllo ammissibilità
		assertTrue(sol.isAdmissible(problem));
	}
}
