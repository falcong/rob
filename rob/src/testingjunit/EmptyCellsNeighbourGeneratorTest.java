package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;

public class EmptyCellsNeighbourGeneratorTest {

	@Before
	public void setUp() throws Exception {
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

}
