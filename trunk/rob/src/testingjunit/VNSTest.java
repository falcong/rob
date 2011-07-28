//annarosa
package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solvingalgorithms.Algorithm;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;
import solvingalgorithms.VNS;

public class VNSTest {
	Problem problem;
	ProblemParser parser;

	@Before
	public void setUp() throws Exception {
		parser= new ProblemParser(Utility.getConfigParameter("problemsPath"));
	}

	//test di VNS(int, Algorithm, NeighbourGenerator, Problem, int,int)
	/*
	 * caso generale
	 */
	@Test
	public final void testVNS1(){
		
	}
	
	//test di execute()
	/*
	 * caso generale ammissibilità + fo <=
	 * n volte
	 */
	@Test
	public final void testExecute() {
		//problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		problem=parser.parse("Cap.10.40.3.1.10.1.ctqd");
		RandomSolutionGenerator sGenerator=new RandomSolutionGenerator(problem);
		NeighbourGenerator nGenerator=new BasicNeighbourGenerator(problem);
		
		LocalSearch localSearch=new LocalSearch(10, 10, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGenerator, problem);
		
		VNS vnsSearch=new VNS(2, localSearch, nGenerator, problem);
		Solution startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		Solution result=vnsSearch.execute(startSolution);
		System.out.println("Funzione obiettivo dopo VNS:" + result.getObjectiveFunction());

		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public final void testExecute2() {
		//problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		problem=parser.parse("Cap.50.40.5.2.99.1.ctqd");
		//problem=parser.parse("Cap.100.100.5.1.10.5.ctqd");
		RandomSolutionGenerator sGenerator=new RandomSolutionGenerator(problem);
		NeighbourGenerator nGenerator=new BasicNeighbourGenerator(problem);
		LocalSearch localSearch=new LocalSearch(100, 100, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGenerator, problem);
		VNS vnsSearch=new VNS(50, localSearch, nGenerator, problem);
		Solution startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		Solution result=vnsSearch.execute(startSolution);
		System.out.println("Funzione obiettivo dopo VNS:" + result.getObjectiveFunction());

		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
	}
	
	@Test
	public final void testExecuteWithTimeLimit() {
		//problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		problem=parser.parse("Cap.50.40.5.2.99.1.ctqd");
		//problem=parser.parse("Cap.100.100.5.1.10.5.ctqd");
		RandomSolutionGenerator sGenerator=new RandomSolutionGenerator(problem);
		NeighbourGenerator nGenerator=new BasicNeighbourGenerator(problem);
		LocalSearch localSearch=new LocalSearch(100, 100, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGenerator, problem);
		VNS vnsSearch=new VNS(50, localSearch, nGenerator, problem);
		vnsSearch.setFinalTime(System.currentTimeMillis()+5000);
		Solution startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		Solution result=vnsSearch.execute(startSolution);
		System.out.println("Funzione obiettivo dopo VNS:" + result.getObjectiveFunction());

		assertTrue(result.isAdmissible(problem));
	}
	

}
