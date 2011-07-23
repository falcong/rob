package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BanFullNeighbourGenerator;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;

public class EmptyCellsNeighbourGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	//test di generate()
	/*
	 * caso generale, solo ammissibilità
	 */
	@Test
	public final void testGenerate(){
		
	}
	
	
	
	
	@Test
	public final void testEmptyCellsNeighbourGenerator() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = pp.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 30, 42, 30};
		int [] s2={0, 30, 0, 33};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		
		EmptyCellsNeighbourGenerator generator = new EmptyCellsNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testEmptyCellsNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		RandomSolutionGenerator gen = new RandomSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		EmptyCellsNeighbourGenerator generator = new EmptyCellsNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}

}
