package testingjunit;

import static org.junit.Assert.*;

import neighbourgenerator.BasicNeighbourGenerator;

import org.junit.Test;

import data.Problem;
import data.Solution;

import io.ProblemParser;
import solutiongenerator.RandomSolutionGenerator;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch.StrategyName;

public class LocalSearchTest {
	final String CLASS_NAME = this.getClass().getName();
	
	//test di execute()
	/*
	 * Caso generale con first improvement. 
	 */
	@Test
	public final void testExecute1() throws Exception{
		final StrategyName successorChoice = StrategyName.FIRST_IMPROVEMENT;
		testExecute(successorChoice);
	}
	
	/*
	 * Caso generale con best improvement.
	 */
	@Test
	public final void testExecute2() throws Exception{
		final StrategyName successorChoice = StrategyName.BEST_IMPROVEMENT;
		testExecute(successorChoice);
	}
	
	private final void testExecute(StrategyName successorChoice) throws Exception {
		final String methodName = new Exception().getStackTrace()[0].getMethodName(); 
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		final String PROBLEM_NAME = "Cap.10.100.5.1.10.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
		Solution sol0 = generator.generate();
		
		final int MAX_NEIGHBOURS_NUMBER = 5;
		final int MAX_STEPS_NUMBER = 5;
		BasicNeighbourGenerator basicGenerator = new BasicNeighbourGenerator(problem); 
		
		LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS_NUMBER, MAX_STEPS_NUMBER, successorChoice,
							  basicGenerator, problem);
		
		//Eseguo il test N volte perchè il metodo non è deterministico
		final int N = 5;
		for(int i=1; i<=N; i++){
			Solution sol1 = localSearch.execute(sol0);
			
			//controllo ammissibilità
			assertTrue(sol1.isAdmissible(problem));
			//controllo che la funzione obiettivo non sia peggiorata
			assertTrue(sol1.getObjectiveFunction() <= sol0.getObjectiveFunction());
		}
	}
}
