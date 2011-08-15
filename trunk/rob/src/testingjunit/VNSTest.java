package testingjunit;

import static org.junit.Assert.*;
import neighbourgenerator.BasicNeighbourGenerator;
import org.junit.Before;
import org.junit.Test;

import parser.ProblemParser;
import data.Problem;
import data.Solution;
import solutiongenerator.RandomSolutionGenerator;
import solvingalgorithm.temporizedalgorithm.VNS;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch.StrategyName;

public class VNSTest {
	ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
	final String PROBLEM_NAME = "Cap.50.40.3.1.70.1.ctqd";
	final int K_MAX = 5;
	final int MAX_NEIGHBOUR_NUMBER = 10;
	final int MAX_STEPS_NUMBER = 10;
	Problem problem;
	VNS vnsSearch;
	Solution startSolution;
	RandomSolutionGenerator sGenerator;
	BasicNeighbourGenerator nGenerator;
	//tempo massimo di esecuzione per la VNS
	final long MAX_TIME = 2000;
	
	@Before
	public final void setUp() throws Exception{
		problem = pp.parse(PROBLEM_NAME);
		sGenerator=new RandomSolutionGenerator(problem);
		nGenerator=new BasicNeighbourGenerator(problem);
	}
	
	//test di execute()
	/*
	 * caso generale
	 */
	@Test
	public final void testExecute1() throws Exception {
		LocalSearch localSearch=new LocalSearch(MAX_NEIGHBOUR_NUMBER, MAX_STEPS_NUMBER, StrategyName.FIRST_IMPROVEMENT, nGenerator, problem);
		//nGenerator=new BasicNeighbourGenerator(problem);
		vnsSearch=new VNS(K_MAX, localSearch, nGenerator, problem);
		startSolution=sGenerator.generate();
		
		final int N=10;
		for(int i=1;i<=N;i++) {
			Solution result=vnsSearch.execute(startSolution);
			assertTrue(result.isAdmissible());
			assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());			
		}
	}
	
	
	/*
	 * mette in evidenza il baco per cui [nella versione 1 del programma] il tempo di esecuzione della VNS viene 
	 * erroneamente contato a partire dall'istante in cui l'oggetto VNS è creato e non quando ha inizio l'esecuzione
	 */
	@Test
	public final void testExecute2() throws Exception {
		LocalSearch localSearch=new LocalSearch(MAX_NEIGHBOUR_NUMBER, MAX_STEPS_NUMBER, StrategyName.FIRST_IMPROVEMENT, nGenerator, problem);
		final int RESTARTS = -1;
		final int MAX_TIME_IN_SECONDS = (int)MAX_TIME/1000;
		vnsSearch=new VNS(K_MAX, localSearch, nGenerator, problem, RESTARTS, MAX_TIME_IN_SECONDS);
		startSolution=sGenerator.generate();
		/*
		 * aspetto che sia passato un tempo pari a MAX_TIME; in questo modo se VNS contiene un baco e 
		 * conta MAX_TIME a partire dalla creazione dell'oggetto MAX_TIME sarà già esaurito
		 */
		Thread.sleep(MAX_TIME);
		
		long startTime = System.currentTimeMillis();
		vnsSearch.execute(startSolution);
		long finalTime = System.currentTimeMillis();
		long executionTime = finalTime-startTime;
		
		final long TOLERANCE = 1000;
		assertEquals(MAX_TIME, executionTime, TOLERANCE);
	}
}
