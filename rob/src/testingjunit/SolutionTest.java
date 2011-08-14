package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import io.SolutionParser;

import org.junit.Test;

import data.Problem;
import data.Solution;

import solutiongenerator.RandomSolutionGenerator;
import solutiongenerator.SolutionGenerator;

public class SolutionTest {
	ProblemParser pp=new ProblemParser(Constants.TESTING_INPUT_PATH);
	
	//test di Solution()
	/*
	 * caso generale
	 */
	@Test
	public final void testSolution1() throws Exception {
		final String PROBLEM_NAME="problema10.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 30, 10, 40};
		int [] s2={0, 20, 32, 23};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		assertEquals(19346.75, solution.getObjectiveFunction(), 0.01);		
	}
	
	//test di Solution(String, Problem)
	/*
	 * caso generale:
	 * creo file con sol
	 * importo
	 * controllo che sol importata sia = sol del file
	 */
	//TODO deve essere spostato
	@Test
	public final void testSolutionString1() throws Exception {
		final String PROBLEM_NAME="problema10.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 30, 10, 40};
		int [] s2={0, 20, 32, 23};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution expectedSolution=new Solution(matrix,problem);
		

		final String inputFileName = PROBLEM_NAME+"_Solution.txt";
		final String inputFileFolder = Constants.TESTING_INPUT_PATH+System.getProperty("file.separator");
		SolutionParser solParser = new SolutionParser(inputFileFolder, problem);
		Solution importedSolution = solParser.parse(inputFileName);
	
		assertTrue(expectedSolution.calcDistance(importedSolution)==0);
	}
	
	//test di calcDistance()
	/*
	 * caso generale
	 */
	@Test
	public final void testCalcDistance1() throws Exception {
		final String PROBLEM_NAME="problema11.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		int [] A0={0, 0, 0, 0};
		int [] A1={0, 41, 42, 24};
		int [] A2={0, 19, 0, 39};
		int [][] matrixA=new int[3][];
		matrixA[0]=A0;
		matrixA[1]=A1;
		matrixA[2]=A2;
		Solution solutionA=new Solution(matrixA,problem);
		
		int [] B0={0, 0, 0, 0};
		int [] B1={0, 40, 42, 25};
		int [] B2={0, 20, 0, 38};
		int [][] matrixB=new int[3][];
		matrixB[0]=B0;
		matrixB[1]=B1;
		matrixB[2]=B2;
		Solution solutionB=new Solution(matrixB,problem);
		assertTrue(solutionA.isAdmissible()&&solutionB.isAdmissible());
		assertEquals(2,solutionA.calcDistance(solutionB));
	}
	
	//test di export()
	/*
	 * caso generale:
	 * new sol
	 * exp
	 * imp
	 * controllo =
	 */
	@Test
	//TODO deve essere spostato
	public final void testExport1() throws Exception {
		final String PROBLEM_NAME="Cap.50.40.5.1.10.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		SolutionGenerator sg=new RandomSolutionGenerator(problem);
		Solution sol = sg.generate();
		
		final String outputFileName=PROBLEM_NAME+"_Solution.txt";
		final String fileFolder = Constants.TESTING_OUTPUT_PATH+System.getProperty("file.separator");
		
		sol.export(fileFolder+outputFileName);
		
		SolutionParser solParser = new SolutionParser(fileFolder, problem);
		Solution importedSol = solParser.parse(outputFileName);
		
		assertTrue(sol.calcDistance(importedSol)==0);
		assertTrue(sol.getObjectiveFunction()==importedSol.getObjectiveFunction());		
	}
	
	//test di isAdmissible()
	/*
	 * Soluzione ammissibile
	 */
	@Test
	public final void testIsAdmissible1() throws Exception {
		final String PROBLEM_NAME="problema12.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 40, 50, 25};
		int [] s2={0, 20, 10, 38};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
	}
	
	/*
	 * Soluzione non ammissibile
	 */
	@Test
	public final void testIsAdmissible2() throws Exception {
		final String PROBLEM_NAME="problema12.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 40, 50, 25};
		int [] s2={0, 20, 50, 38}; //valore non ammissible
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(!solution.isAdmissible());
	}
	
	//test di moveQuantity()
	/*
	 * caso generale
	 */
	@Test
	public final void testMoveQuantity1() throws Exception {
		final String PROBLEM_NAME="problema13.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 52, 24};
		int [] s2={0, 9, 13, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		
		final int QUANTITY=10;
		final int PRODUCT=2;
		final int SUP_FROM=1;
		final int SUP_TO=2;
		
		int [] expectedS0={0, 0, 0, 0};
		int [] expectedS1={0, s1[1], s1[2]-QUANTITY, s1[3]};
		int [] expectedS2={0, s2[1], s2[2]+QUANTITY, s2[3]}; //ho spostato QUANTITY unità da s1[2] a s2[2]
		int [][] expectedMatrix=new int[3][];
		expectedMatrix[0]=expectedS0;
		expectedMatrix[1]=expectedS1;
		expectedMatrix[2]=expectedS2;
		
		
		Solution expectedSol=new Solution(expectedMatrix,problem);
		
		solution.moveQuantity(PRODUCT, SUP_FROM, SUP_TO, QUANTITY); //solution dovrebbe diventare uguale a expectedSol (distanza=0)
		assertEquals(0,solution.calcDistance(expectedSol)); 
	}
	
	//test di totalQuantityBought()
	/*
	 * caso generale
	 */
	@Test
	public final void testTotalQuantityBought1() throws Exception {
		final String PROBLEM_NAME="problema14.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 52, 24};
		int [] s2={0, 9, 13, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		//quantità totale acquistata presso il fornitore 2
		final int TOTAL_QUANTITY = 61;
		assertEquals(TOTAL_QUANTITY, solution.totalQuantityBought(SUPPLIER));
	}

	//metodo getCell()
	/*
	 * input =  1, 1
	 */
	@Test
	public final void testGetCell1() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int SUPPLIER = 1;
		final int PRODUCT = 1;
		final int CELL = 1;
		final double TOLERANCE = 0;
		assertEquals(CELL, solution.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}

	/*
	 * caso generale (input = 7, 53)
	 */
	@Test
	public final void testGetCell2() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int SUPPLIER = 7;
		final int PRODUCT = 53;
		final int CELL = 653;
		final double TOLERANCE = 0;
		assertEquals(CELL, solution.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}

	/*
	 * input = ultimo_fornitore, ultimo_prodotto
	 */
	@Test
	public final void testGetCell3() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int SUPPLIER = 10;
		final int PRODUCT = 100;
		final int CELL = 1000;
		final double TOLERANCE = 0;
		assertEquals(CELL, solution.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}

	//test di getSupplierFromCell()
	/*
	 * cella = 1
	 */
	@Test
	public final void testGetSupplierFromCell1() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 1;
		final int SUPPLIER = 1;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, solution.getSupplierFromCell(CELL), TOLERANCE);
	}

	/*
	 * caso generale (cella = 653)
	 */
	@Test
	public final void testGetSupplierFromCell2() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 653;
		final int SUPPLIER = 7;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, solution.getSupplierFromCell(CELL), TOLERANCE);
	}

	/*
	 * cella = ultima_cella
	 */
	@Test
	public final void testGetSupplierFromCell3() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 1000;
		final int SUPPLIER = 10;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, solution.getSupplierFromCell(CELL), TOLERANCE);
	}

	//metodo getProductFromCell()
	/*
	 * cella = 1
	 */
	@Test
	public final void testGetProductFromCell1() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 1;
		final int PRODUCT = 1;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, solution.getProductFromCell(CELL), TOLERANCE);
	}

	/*
	 * cella = 653
	 */
	@Test
	public final void testGetProductFromCell2() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 653;
		final int PRODUCT = 53;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, solution.getProductFromCell(CELL), TOLERANCE);
	}

	/*
	 * cella = ultima_cella
	 */
	@Test
	public final void testGetProductFromCell3() throws Exception{
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution= new RandomSolutionGenerator(problem).generate();
		final int CELL = 1000;
		final int PRODUCT = 100;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, solution.getProductFromCell(CELL), TOLERANCE);
	}
}