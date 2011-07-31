package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.RandomSolutionGenerator;
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
		Problem problem = null;
		try {
			problem = pp.parse(PROBLEM_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LinesSolutionGenerator generator = new LinesSolutionGenerator(problem);
		//sol generata
		Solution sol = generator.generate();
		//controllo ammissibilità
		assertTrue(sol.isAdmissible(problem));
	}
}
