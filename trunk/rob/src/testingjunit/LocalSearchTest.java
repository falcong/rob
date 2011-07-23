package testingjunit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import io.ProblemParser;
import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.SolutionGenerator;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;

public class LocalSearchTest {
	

	NeighbourGenerator nGenerator;
	SolutionGenerator sGenerator;
	Problem problem;
	Solution startSolution;
	ProblemParser parser;
	
	@Before
	public void setUp() throws Exception {
		parser= new ProblemParser(Utility.getConfigParameter("problemsPath"));
	}
	
	//test di execute()
	/*
	 * 2 casi: best first + best improvement
	 * [2 casi generali ammissibilità e fo <=]
	 * testare n volte
	 */
	@Test
	public final void testExecute1() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		nGenerator=new BasicNeighbourGenerator(problem);
		sGenerator=new TrivialSolutionGenerator(problem);
		startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		LocalSearch localSearch=new LocalSearch(10, 100, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGenerator, problem);
		Solution result=localSearch.execute(startSolution);
		System.out.println("Funzione obiettivo BestFirst:" + result.getObjectiveFunction());
		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
	}
	
	@Test
	public final void testExecuteBestImprovement() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		nGenerator=new BasicNeighbourGenerator(problem);
		sGenerator=new RandomSolutionGenerator(problem);
		startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		
		LocalSearch localSearch=new LocalSearch(10, 100, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGenerator, problem);
		Solution result=localSearch.execute(startSolution);
		System.out.println("Funzione obiettivo BestImprovement:" + result.getObjectiveFunction());
		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
	}

}
