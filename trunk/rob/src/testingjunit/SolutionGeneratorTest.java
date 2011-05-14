package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solutionhandlers.RandomSolutionGenerator;

public class SolutionGeneratorTest {
	private Problem problem;
	ProblemParser parser;

	@Before
	public void setUp() throws Exception {
		parser= new ProblemParser(Utility.getConfigParameter("problemsPath"));

	}

	@Test
	public void testRandomGenerate() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		RandomSolutionGenerator generator=new RandomSolutionGenerator(problem);
		Solution solution=generator.generate();
		System.out.println("Random: " + solution.getObjectiveFunction());
		assertTrue(solution.getObjectiveFunction()>0);
		assertTrue(solution.isAdmissible(problem));
	}
	
	@Test
	public void testTrivialGenerate() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		TrivialSolutionGenerator generator=new TrivialSolutionGenerator(problem);
		Solution solution=generator.generate();
		System.out.println("Trivial: " + solution.getObjectiveFunction());
		assertTrue(solution.getObjectiveFunction()>0);
		assertTrue(solution.isAdmissible(problem));
	}
	
	@Test
	public void testLineGenerate() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		LinesSolutionGenerator generator=new LinesSolutionGenerator(problem);
		Solution solution=generator.generate();
		System.out.println("Line: " + solution.getObjectiveFunction());
		assertTrue(solution.getObjectiveFunction()>0);
		assertTrue(solution.isAdmissible(problem));
	}
	
	@Test
	public void testLine2Generate() {
		problem=parser.parse("Cap.50.100.5.1.10.1.ctqd");
		LinesSolutionGenerator generator=new LinesSolutionGenerator(problem);
		Solution solution=generator.generate();
		System.out.println("Line2: " + solution.getObjectiveFunction());
		assertTrue(solution.getObjectiveFunction()>0);
		assertTrue(solution.isAdmissible(problem));
	}

}
