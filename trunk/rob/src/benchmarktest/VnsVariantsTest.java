package benchmarktest;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.VNS;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;

public class VnsVariantsTest {

	Problem problem;
	ProblemParser parser;
	BasicNeighbourGenerator basicGenerator = new BasicNeighbourGenerator(null);
	LinesSolutionGenerator lsg = new LinesSolutionGenerator();

	@Before
	public void setUp() throws Exception {
		parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		
	}

	@Test
	public final void testStandardVNS() {
		problem=parser.parse("Cap.100.100.5.1.10.5.ctqd");
		System.out.println("Eseguo " + problem.getName() + " con VNS standard.");
		TrivialSolutionGenerator sGenerator=new TrivialSolutionGenerator(problem);
		
		LocalSearch localSearch=new LocalSearch(100, 100, SuccessorChoiceMethod.FIRST_IMPROVEMENT, null, problem);
		VNS vnsSearch=new VNS(50, localSearch, null, problem);
		Solution startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		Solution result=vnsSearch.execute(startSolution);
		System.out.println("Funzione obiettivo dopo VNS:" + result.getObjectiveFunction());

		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
		
		
	}
	
	@Test
	public final void testVNSFromLineGen() {
		problem=parser.parse("Cap.50.100.3.2.10.2.ctqd");
		System.out.println("Eseguo " + problem.getName() + " con VNS e LineSolutionGenerator.");
		LinesSolutionGenerator sGenerator=new LinesSolutionGenerator(problem);
		
		LocalSearch search=new LocalSearch(100, 1000,SuccessorChoiceMethod.FIRST_IMPROVEMENT, null, problem);
		VNS vnsSearch=new VNS(50, search, null, problem);
		Solution startSolution=sGenerator.generate();
		System.out.println("Funzione obiettivo startSolution:" + startSolution.getObjectiveFunction());
		Solution result=vnsSearch.execute(startSolution);
		System.out.println("Funzione obiettivo dopo VNS:" + result.getObjectiveFunction());

		assertTrue(result.isAdmissible(problem));
		assertTrue(result.getObjectiveFunction()<=startSolution.getObjectiveFunction());
	}

}
