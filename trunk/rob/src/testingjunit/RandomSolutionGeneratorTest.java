package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.RandomSolutionGenerator;

public class RandomSolutionGeneratorTest {

	//test di generate()
	/*
	 * Caso generale. Testo N volte per il non-determinismo
	 */
	@Test
	public void testGenerate() {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.50.100.3.2.10.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int N = 100;
		
		for (int i=1 ; i<=N;i++) {
			RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
			//sol generata
			Solution sol = generator.generate();
			//controllo ammissibilità
			assertTrue(sol.isAdmissible(problem));
		}
				
	}
}
