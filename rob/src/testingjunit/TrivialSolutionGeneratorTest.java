package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.TrivialSolutionGenerator;
import util.Constants;

public class TrivialSolutionGeneratorTest {
	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public void testGenerate() {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.50.100.3.1.99.1.ctqd";
		Problem problem = null;
		try{
			problem = pp.parse(PROBLEM_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TrivialSolutionGenerator generator = new TrivialSolutionGenerator(problem);
		//sol generata
		Solution sol = generator.generate();
		//controllo ammissibilità
		assertTrue(sol.isAdmissible(problem));
	}

}
