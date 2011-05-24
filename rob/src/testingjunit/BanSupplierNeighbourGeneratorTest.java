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
import solutionhandlers.DirectionedBanNeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;

public class BanSupplierNeighbourGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testBanSupplierNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new BanSupplierNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testBanFullNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new BanFullNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testDirectionedNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new DirectionedBanNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	
	@Test
	public final void testBanFullNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		BanSupplierNeighbourGenerator generator = new BanFullNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testDirectionedNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		RandomSolutionGenerator gen = new RandomSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		
		BanSupplierNeighbourGenerator generator = new DirectionedBanNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}

}
