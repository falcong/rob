package testingjunit;

import static org.junit.Assert.*;
import neighbourgenerator.BasicNeighbourGenerator;
import org.junit.Test;

import parser.ProblemParser;
import solutiongenerator.RandomSolutionGenerator;
import solvingalgorithm.temporizedalgorithm.localsearch.FirstImprovementStrategy;
import data.Problem;
import data.Solution;

public class FirstImprovementStrategyTest {

	//test di explore()
	/*
	 * caso generale
	 */
	@Test
	public void testExplore() throws Exception {
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		final String PROBLEM_NAME = "Cap.10.40.3.1.99.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
		Solution sol0 = generator.generate();
				
		FirstImprovementStrategy strategy = new FirstImprovementStrategy();
		final int MAX_NEIGHBOUR_NUMBER = 10;
		final BasicNeighbourGenerator neighbourGenerator = new BasicNeighbourGenerator(problem);
		Solution sol1 = strategy.explore(sol0, MAX_NEIGHBOUR_NUMBER, neighbourGenerator);
		
		//controllo ammissibilità soluzione generata e che la funzione obiettivo non sia peggiorata
		assertTrue(sol1.isAdmissible());
		assertTrue(sol1.getObjectiveFunction() <= sol0.getObjectiveFunction());
	}
}
