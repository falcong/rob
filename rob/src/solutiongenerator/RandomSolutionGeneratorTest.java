package solutiongenerator;

import static org.junit.Assert.*;
import org.junit.Test;

import parser.ProblemParser;
import data.Problem;
import data.Solution;

import util.Constants;

public class RandomSolutionGeneratorTest {

	//test di generate()
	/*
	 * Caso generale. Testo N volte per il non-determinismo
	 */
	@Test
	public void testGenerate() throws Exception {
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.50.100.3.2.10.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int N = 100;
		
		for (int i=1 ; i<=N;i++) {
			RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
			//sol generata
			Solution sol = generator.generate();
			//controllo ammissibilità
			assertTrue(sol.isAdmissible());
		}
				
	}
}
