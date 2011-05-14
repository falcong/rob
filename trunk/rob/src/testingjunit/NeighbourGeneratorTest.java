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

public class NeighbourGeneratorTest {
	private Problem problem;
	private NeighbourGenerator generator;
	ProblemParser parser;

	@Before
	public void setUp() throws Exception {
		parser= new ProblemParser(Utility.getConfigParameter("problemsPath"));
	}

	@Test
	public final void testGenerateFine() {
		problem=parser.parse("problema1.txt");
		generator=new BasicNeighbourGenerator(problem);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 24};
		int [] s2={0, 9, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		Solution neighbour=generator.generate(solution, 1);
		assertEquals(1,solution.calcDistance(neighbour));
	}
	@Test
	public final void testGenerate2() {
		problem=parser.parse("Cap.10.40.3.1.10.1.ctqd");
		//problem=parser.parse("Cap.50.40.5.2.99.1.ctqd");
		RandomSolutionGenerator randomGenerator=new RandomSolutionGenerator(problem);
		generator=new BasicNeighbourGenerator(problem);
		Solution solution=randomGenerator.generate();
		Solution neighbour=generator.generate(solution, 200);
		assertTrue(solution.isAdmissible(problem));
		assertTrue(neighbour.isAdmissible(problem));
		//assertEquals(8000,solution.calcDistance(neighbour));
	}
}
