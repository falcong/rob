package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.SolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;

public class SolutionTest {
	Problem problem;
	ProblemParser parser;
	
	@Before
	public final void setUp() {
		parser= new ProblemParser(Utility.getConfigParameter("problemsPath"));
	}
	
	//test di Solution()
	/*
	 * caso generale
	 */
	@Test
	public final void testSolution1() {
		
	}
	
	//test di Solution(String, Problem)
	/*
	 * caso generale
	 */
	@Test
	public final void testSolutionString1() {
		
	}
	
	//test di calcDistance()
	/*
	 * caso generale
	 */
	@Test
	public final void testCalcDistance1() {
		
	}
	
	//test di export()
	/*
	 * 
	 */
	@Test
	public final void testExport1() {
		
	}
	
	//test di isAdmissible()
	/*
	 * caso generale sol ok + sol ko
	 */
	@Test
	public final void testIsAdmissible1() {
		
	}
	
	//test di moveQuantity()
	/*
	 * caso generale
	 */
	@Test
	public final void testMoveQuantity1() {
		
	}
	
	//test di totalQuantityBought()
	/*
	 * caso generale
	 */
	@Test
	public final void testTotalQuantityBought1() {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public final void testObjectiveFunction() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 24};
		int [] s2={0, 9, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		assertEquals(20150.52, solution.getObjectiveFunction(), 0.01);
	}
/*	@Test
	public final void testIsAdmissible1() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 24};
		int [] s2={0, 9, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
	}*/
	
	@Test
	public final void testIsAdmissible2() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 52, 42, 24};
		int [] s2={0, 8, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(!solution.isAdmissible(problem));
	}
	
	@Test
	public final void testIsAdmissible3() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 41, 24};
		int [] s2={0, 9, 1, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(!solution.isAdmissible(problem));
	}
	
	@Test
	public final void testIsAdmissible4() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 41, 24};
		int [] s2={0, 10, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(!solution.isAdmissible(problem));
	}
	
	@Test
	public final void testCalcDistance() {
		problem=parser.parse("problema1.txt");
		int [] A0={0, 0, 0, 0};
		int [] A1={0, 51, 42, 24};
		int [] A2={0, 9, 0, 39};
		int [][] matrixA=new int[3][];
		matrixA[0]=A0;
		matrixA[1]=A1;
		matrixA[2]=A2;
		Solution solutionA=new Solution(matrixA,problem);
		
		int [] B0={0, 0, 0, 0};
		int [] B1={0, 50, 42, 25};
		int [] B2={0, 10, 0, 38};
		int [][] matrixB=new int[3][];
		matrixB[0]=B0;
		matrixB[1]=B1;
		matrixB[2]=B2;
		Solution solutionB=new Solution(matrixB,problem);
		
		assertEquals(2,solutionA.calcDistance(solutionB));
	}
	
	@Test
	public final void testMoveQuantity() {
		problem=parser.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 24};
		int [] s2={0, 9, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		
		int [] newS0={0, 0, 0, 0};
		int [] newS1={0, 50, 42, 24};
		int [] newS2={0, 10, 0, 39}; //ho spostato un unità da s1[1] a s2[1]
		int [][] newMatrix=new int[3][];
		newMatrix[0]=newS0;
		newMatrix[1]=newS1;
		newMatrix[2]=newS2;
		Solution newSol=new Solution(newMatrix,problem);
		
		solution.moveQuantity(1, 1, 2, 1, problem); //solution dovrebbe diventare uguale a newSol (distanza=0)
		assertEquals(0,solution.calcDistance(newSol)); 
	}
	
	@Test
	public final void testImportExport() {
		problem=parser.parse("problema1.txt");
		SolutionGenerator generator=new TrivialSolutionGenerator(problem);
		Solution sol = generator.generate();
		sol.export(Utility.getConfigParameter("proveVarie")+System.getProperty("file.separator")+
				"mySol.txt");
		Solution sol2= new Solution(Utility.getConfigParameter("proveVarie")+
				System.getProperty("file.separator")+"mySol.txt",problem);
		assertEquals(0,sol.calcDistance(sol2));
	}
	
	@Test
	public final void testTotalQuantityBought() {
		problem=parser.parse("Cap.50.100.3.2.80.2.ctqd");
		
		SolutionGenerator generator=new TrivialSolutionGenerator(problem);
		//soluzione trivial di 56
		Solution sol = generator.generate();
	
		//fornitore 39
		assertEquals(283, sol.totalQuantityBought(39));
	}
	
}
