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
	//test di VNS(int, Algorithm, NeighbourGenerator, Problem, int,int)
	/*
	 * caso generale
	 */
	@Test
	public final void testVNS1(){
		//cosa c'era da testare qui?????
	}
	
	//test di execute()
	/*
	 * caso generale ammissibilità + fo <=
	 * n volte
	 */
	@Test
	public final void testExecute1() {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		final String PROBLEM_NAME = "Cap.50.40.3.1.70.1.ctqd";
		final int K_MAX=5;
		final int MAX_NEIGHBOUR_NUMBER = 10;
		final int MAX_STEPS_NUMBER = 10;
		
		Problem problem=pp.parse(PROBLEM_NAME);
		
		RandomSolutionGenerator sGenerator=new RandomSolutionGenerator(problem);
		NeighbourGenerator nGenerator=new BasicNeighbourGenerator(problem);
		LocalSearch localSearch=new LocalSearch(MAX_NEIGHBOUR_NUMBER, MAX_STEPS_NUMBER, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGenerator, problem);
		
		VNS vnsSearch=new VNS(K_MAX, localSearch, nGenerator, problem);
		Solution startSolution=sGenerator.generate();
		
		final int N=10;
		for(int i=1;i<=N;i++) {
			Solution result=vnsSearch.execute(startSolution);
			assertTrue(result.isAdmissible(problem));
			assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());			
		}
	}
	
	
	
	
	
	
	
	
	
	
	

/*	@Test
	public final void testExecuteWithTimeLimit() {
		//problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		Problem problem=pp.parse("Cap.50.40.3.1.70.1.ctqd");
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
	}*/
	

}
