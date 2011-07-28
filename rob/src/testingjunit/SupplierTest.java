//abiola
package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;
import solutionhandlers.RandomSolutionGenerator;

public class SupplierTest {
	ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);

	//test di activatedSegment() 
	//fasce di sconto con limiti 50 e 70
	/*
	 * Caso con totalQuantityBought < di 50
	 */
	@Test
	public final void testActivatedSegment1(){
		final String PROBLEM_NAME = "problema1_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 0; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	/*
	 * Caso con totalQuantityBought pari a 49 (Boundary test)
	 */
	@Test
	public final void testActivatedSegment2(){
		final String PROBLEM_NAME = "problema2_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 0; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}

	/*
	 * Caso con totalQuantityBought pari a 50 (Boundary test)
	 */
	@Test
	public final void testActivatedSegment3(){
		final String PROBLEM_NAME = "problema3_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 1; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	/*
	 * Caso con totalQuantityBought > 70
	 */
	@Test
	public final void testActivatedSegment4(){
		final String PROBLEM_NAME = "problema4_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 2; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	private final void testActivatedSegment(String problemName, int activatedSegmentExpected){
		Problem problem = pp.parse(problemName);
		
		RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
		Solution solution = generator.generate();
		
		//considero solo il fornitore1
		final int SUPPLIER1_ID = 1;
		Supplier supplier = problem.getSupplier(SUPPLIER1_ID);
		int totalQuantityBought = solution.totalQuantityBought(SUPPLIER1_ID);
		int activatedSegmentInSol = supplier.activatedSegment(totalQuantityBought);
		
		assertTrue(activatedSegmentExpected == activatedSegmentInSol);
	}
	
	//test di checkAvailability()
	/*
	 * Caso in cui il prodotto non c'è
	 */
	@Test
	public final void testCheckAvailability1(){
		final String PROBLEM_NAME = "problema5_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, false);
	}
	
	/*
	 * Caso in cui il prodotto c'è con la disponibilità = 0
	 */
	@Test
	public final void testCheckAvailability2(){
		final String PROBLEM_NAME = "problema6_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, true);
	}
	
	/*
	 * Caso in cui il prodotto c'è con la disponibilità = n
	 */
	@Test
	public final void testCheckAvailability3(){
		final String PROBLEM_NAME = "problema7_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, true);
	}
	
	private final void testCheckAvailability(String problemName, boolean bool){
		Problem problem = pp.parse(problemName);
		final int SUPPLIER1_ID = 1;
		final int PRODUCT1_ID = 1;
		
		Supplier supplier = problem.getSupplier(SUPPLIER1_ID);
		assertTrue(supplier.checkAvailability(PRODUCT1_ID) == bool);
	}
	
	//test di getDiscountedPrice()
	//fasce di sconto con limiti 50 e 70
    /*4 casi di test : es tre fascie di sconto con limiti 100 e 200
     *50,99,100,250
     */
	/*
     *Caso in cui si ricade nella prima fascia di sconto
     */
	@Test
	public final void testGetDiscountedPrice1(){
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 0, 0};
		int [] s2={0, 40, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		testGetDiscountedPrice(matrix);
	}
	
	
	/*
     *Caso in cui si ricade su un boundary (=49)
     */
	@Test
	public final void testGetDiscountedPrice2(){
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 19, 30, 0};
		int [] s2={0, 41, 12, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		testGetDiscountedPrice(matrix);
	}
	
	/*
     *Caso in cui si ricade dopo l'ultimo boundary (> 70)
     */
	@Test
	public final void testGetDiscountedPrice3(){
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 30, 27};
		int [] s2={0, 40, 12, 36};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		testGetDiscountedPrice(matrix);	
	}
	/*
     *Caso in cui si ricade sul primo boundary (=50)
     */
	@Test
	public final void testGetDiscountedPrice4(){
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 30, 0};
		int [] s2={0, 40, 12, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		testGetDiscountedPrice(matrix);	
	}
	private final void testGetDiscountedPrice(int matrix [][]){
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER1_ID = 1;
		final int PRODUCT1_ID = 1;
		
		Supplier supplier = problem.getSupplier(SUPPLIER1_ID);
		int totalQuantityBought = solution.totalQuantityBought(SUPPLIER1_ID);
		double discountedPriceProduct1 = supplier.getDiscountedPrice(PRODUCT1_ID, totalQuantityBought);	
		double expectedDiscountedPrice = supplier.getPrice(PRODUCT1_ID, supplier.activatedSegment(solution));
		final double TOLERANCE = 0.1;
		
		assertEquals(expectedDiscountedPrice, discountedPriceProduct1, TOLERANCE);
	}
	
	//test di getResidual(int, int[][])
	/*
	 *Caso generale
	 */ 
	@Test
	public final void testGetResidual1(){
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 30, 0};
		int [] s2={0, 40, 12, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER2_ID = 2;
		final int PRODUCT2_ID = 2;
		
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		int availability = supplier.getAvailability(PRODUCT2_ID);
		
		int resAvailabilityExpected = availability - matrix[SUPPLIER2_ID][PRODUCT2_ID];
		int resAvailability = supplier.getResidual(PRODUCT2_ID, matrix);
		
		assertTrue(resAvailability == resAvailabilityExpected);
	}
	 
	//test di getResidual(int, Solution)
	
	 /*caso generale
	 
	@Test
	public final void testGetResidualSolution1(){
		
	}
	 
	//test di getTotalResidualAvailability()
	/*
	 * Caso generale.
	 */
	 @Test
	 public final void testGetTotalResidualAvailability1(){
		final String PROBLEM_NAME = "problema1.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 40, 53};
		int [] s2={0, 9, 2, 10};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//disponibilità totale residua presso il fornitore 2
		final int TOTAL_AVAILABILITY = 93;
		
		assertEquals(TOTAL_AVAILABILITY, supplier.getTotalResidualAvailability(solution));
	}
	
	//test di quantityToIncreaseSegment()
	/*
	 * 4 casi di test : es tre fascie di sconto con limiti 100 e 200
	 * 50,99,100,250
	 */
	/*
	 * quantitàTotale = 21
	 */
	@Test
	public final void testQuantityToIncreaseSegment1(){
		final String PROBLEM_NAME = "problema15.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 40, 53};
		int [] s2={0, 9, 2, 10};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 9;
		
		assertEquals(QUANTITY, supplier.quantityToIncreaseSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 29
	 */
	@Test
	public final void testQuantityToIncreaseSegment2(){
		final String PROBLEM_NAME = "problema15.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 40, 45};
		int [] s2={0, 9, 2, 18};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 1;
		
		assertEquals(QUANTITY, supplier.quantityToIncreaseSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 30
	 */
	@Test
	public final void testQuantityToIncreaseSegment3(){
		final String PROBLEM_NAME = "problema15.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 40, 44};
		int [] s2={0, 9, 2, 19};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 30;
		
		assertEquals(QUANTITY, supplier.quantityToIncreaseSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 74
	 */
	@Test
	public final void testQuantityToIncreaseSegment4(){
		final String PROBLEM_NAME = "problema15.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 40, 0};
		int [] s2={0, 9, 2, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per saturare il secondo fornitore
		final int QUANTITY = 40;
		
		assertEquals(QUANTITY, supplier.quantityToIncreaseSegment(solution));		
	}
	
	//test di quantityToNotDecreaseSegment()
	/*
	 * caso generico + soglia + soglia-1
	 */
	@Test
	public final void testQuantityToNotDecreaseSegment1(){
		
	}
	
	//test di setPrices()
	/*
	 * caso generale
	 */
	@Test
	public final void testSetPrices1(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
//TODO da cancellare
	
	/*@Test
	public final void testActivatedSegment() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem p = pp.parse("problema1.txt");
		Supplier s = p.getSupplier(2);
		
		assertEquals(0, s.activatedSegment(1));
		assertEquals(0, s.activatedSegment(9));
		assertEquals(1, s.activatedSegment(10));
		assertEquals(2, s.activatedSegment(69));
		assertEquals(3, s.activatedSegment(70));
		
	}
	
	@Test
	public final void testGetTotalResidualAvailability() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem=pp.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 24};
		int [] s2={0, 9, 0, 39};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		Supplier sup1 = problem.getSupplier(1);
		int residual=sup1.getTotalResidualAvailability(solution);
		Supplier sup2 = problem.getSupplier(2);
		int residual2=sup2.getTotalResidualAvailability(solution);
		assertEquals(39,residual);
		assertEquals(28,residual2);
	}
	
	
	@Test
	public final void testQuantityToIncreaseSegment(){
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem=pp.parse("problema2.txt");
		Supplier s = problem.getSupplier(1);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 1, 0, 0};
		int [] s2={0, 59, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution1=new Solution(matrix,problem);
		assertTrue(solution1.isAdmissible(problem));
		assertEquals(49,s.quantityToIncreaseSegment(solution1));
		
		
		int [] s20={0, 0, 0, 0};
		int [] s21={0, 51, 18, 0};
		int [] s22={0, 9, 24, 63};
		matrix=new int[3][];
		matrix[0]=s20;
		matrix[1]=s21;
		matrix[2]=s22;
		Solution solution2=new Solution(matrix,problem);
		assertTrue(solution2.isAdmissible(problem));
		assertEquals(1,s.quantityToIncreaseSegment(solution2));
		
		int [] s30={0, 0, 0, 0};
		int [] s31={0, 51, 19, 0};
		int [] s32={0, 9, 23, 63};
		matrix=new int[3][];
		matrix[0]=s30;
		matrix[1]=s31;
		matrix[2]=s32;
		Solution solution3=new Solution(matrix,problem);
		assertTrue(solution3.isAdmissible(problem));
		assertEquals(86,s.quantityToIncreaseSegment(solution3));
	}
	
	
	@Test
	public final void testQuantityToNotDecreaseSegment(){
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem=pp.parse("problema2.txt");
		Supplier s = problem.getSupplier(1);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 50, 0, 0};
		int [] s2={0, 10, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution1=new Solution(matrix,problem);
		assertTrue(solution1.isAdmissible(problem));
		assertEquals(0, s.quantityToNotDecreaseSegment(solution1));
		
		
		int [] s20={0, 0, 0, 0};
		int [] s21={0, 9, 42, 0};
		int [] s22={0, 51, 0, 63};
		matrix=new int[3][];
		matrix[0]=s20;
		matrix[1]=s21;
		matrix[2]=s22;
		Solution solution2=new Solution(matrix,problem);
		assertTrue(solution2.isAdmissible(problem));
		assertEquals(1,s.quantityToNotDecreaseSegment(solution2));
	}*/
}
