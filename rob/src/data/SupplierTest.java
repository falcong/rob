package data;

import static org.junit.Assert.*;
import org.junit.Test;

import parser.ProblemParser;
import solutiongenerator.RandomSolutionGenerator;
import util.Constants;

public class SupplierTest {
	ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);

	//test di activatedSegment() fasce di sconto con limiti 50 e 70
	/*
	 * Caso1: totalQuantityBought < di 50
	 */
	@Test
	public final void testActivatedSegment1() throws Exception{
		final String PROBLEM_NAME = "problema1_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 0; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	/*
	 * Caso2: totalQuantityBought = 49 (Boundary test)
	 */
	@Test
	public final void testActivatedSegment2() throws Exception{
		final String PROBLEM_NAME = "problema2_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 0; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}

	/*
	 * Caso3: totalQuantityBought pari a 50 (Boundary test)
	 */
	@Test
	public final void testActivatedSegment3() throws Exception{
		final String PROBLEM_NAME = "problema3_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 1; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	/*
	 * Caso4: totalQuantityBought > 70
	 */
	@Test
	public final void testActivatedSegment4() throws Exception{
		final String PROBLEM_NAME = "problema4_abiola.txt";
		final int ACTIVATED_SEGMENT_EXPECTED = 2; 
		testActivatedSegment(PROBLEM_NAME, ACTIVATED_SEGMENT_EXPECTED);
	}
	
	private final void testActivatedSegment(String problemName, int activatedSegmentExpected) throws Exception{
		Problem problem = null;
	    problem = pp.parse(problemName);
		
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
	 * Caso1: il prodotto non c'è
	 */
	@Test
	public final void testCheckAvailability1() throws Exception{
		final String PROBLEM_NAME = "problema5_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, false);
	}
	
	/*
	 * Caso2: il prodotto c'è con disponibilità = 0
	 */
	@Test
	public final void testCheckAvailability2() throws Exception{
		final String PROBLEM_NAME = "problema6_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, true);
	}
	
	/*
	 * Caso3: il prodotto c'è con disponibilità > 0
	 */
	@Test
	public final void testCheckAvailability3() throws Exception{
		final String PROBLEM_NAME = "problema7_abiola.txt";
		testCheckAvailability(PROBLEM_NAME, true);
	}
	
	private final void testCheckAvailability(String problemName, boolean bool) throws Exception{
		Problem problem = null;
		problem = pp.parse(problemName);
		
		final int SUPPLIER1_ID = 1;
		final int PRODUCT1_ID = 1;
		
		Supplier supplier = problem.getSupplier(SUPPLIER1_ID);
		assertTrue(supplier.checkAvailability(PRODUCT1_ID) == bool);
	}
	
	//test di getDiscountedPrice()
	/* Considero il prodotto 1 presso il fornitore 1 avente i limiti delle fasce di sconto pari a 50 e 70 
    //Casi di test verificati con valori 50,49,100,250.
    /*
     * Caso1: si cade nella prima fascia di sconto (quantità_totale_acquistata = 20)
     */
	@Test
	public final void testGetDiscountedPrice1() throws Exception{
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
     *Caso2: boundary (quantità_totale_acquistata = 49)
     */
	@Test
	public final void testGetDiscountedPrice2() throws Exception{
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
     *Caso3: boundary (quantità_totale_acquistata = 50)
     */
	@Test
	public final void testGetDiscountedPrice3() throws Exception{
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 30, 0};
		int [] s2={0, 40, 12, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		testGetDiscountedPrice(matrix);	
	}
	
	/*
     *Caso4: si cade nell'ultima fascia di sconto (quantità_totale_acquistata = 77)
     */
	@Test
	public final void testGetDiscountedPrice4() throws Exception{
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
	 * Controlla la correttezza del prezzo del prodotto 1 presso il fornitore 1
	 * [usato dai test di getDiscountedPrice()]
	 */
	private final void testGetDiscountedPrice(int matrix [][]) throws Exception{
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		
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
	public final void testGetResidual1() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;
		final int PRODUCT2_ID = 2;
		
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		int availability = supplier.getAvailability(PRODUCT2_ID);
		
		int resAvailabilityExpected = availability - matrix[SUPPLIER2_ID][PRODUCT2_ID];
		int resAvailability = supplier.getResidual(PRODUCT2_ID, matrix);
		
		assertTrue(resAvailability == resAvailabilityExpected);
	}
	
	//test di getResidual(int, Solution)
	/*
	 *Caso generale
	 */
	 @Test
	public final void testGetResidualSolution1() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;
		final int PRODUCT2_ID = 2;
		
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		int availability = supplier.getAvailability(PRODUCT2_ID);
		
		int resAvailabilityExpected = availability - matrix[SUPPLIER2_ID][PRODUCT2_ID];
		int resAvailability = supplier.getResidual(PRODUCT2_ID, solution);
		
		assertTrue(resAvailability == resAvailabilityExpected);
		
	}
	
	//test di getTotalResidualAvailability()
	/*
	 * Caso generale.
	 */
	 @Test
	 public final void testGetTotalResidualAvailability1() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//disponibilità totale residua presso il fornitore 2
		final int TOTAL_AVAILABILITY = 93;
		
		assertEquals(TOTAL_AVAILABILITY, supplier.getTotalResidualAvailability(solution));
	}
	
	//test di quantityToIncreaseSegment()
	/*
	 * considero il fornitore 2 avente 2 fasce di sconto con limiti 30 e 60
	/*
	 * quantitàTotale = 21
	 */
	@Test
	public final void testQuantityToIncreaseSegment1() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 9;
		
		assertEquals(QUANTITY, supplier.quantityToNextSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 29 (boundary)
	 */
	@Test
	public final void testQuantityToIncreaseSegment2() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 1;
		
		assertEquals(QUANTITY, supplier.quantityToNextSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 30 (boundary)
	 */
	@Test
	public final void testQuantityToIncreaseSegment3() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per far scattare la fascia di sconto attiva nel secondo fornitore
		final int QUANTITY = 30;
		
		assertEquals(QUANTITY, supplier.quantityToNextSegment(solution));		
	}
	
	/*
	 * quantitàTotale = 74
	 */
	@Test
	public final void testQuantityToIncreaseSegment4() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER);
		//quantità necessaria per saturare il secondo fornitore
		final int QUANTITY = 40;
		
		assertEquals(QUANTITY, supplier.quantityToNextSegment(solution));		
	}
	
	//test di quantityToNotDecreaseSegment()
	//considero il fornitore 2 avente 2 fasce di sconto con limiti 10, 60 e 120
	/*
	 *Caso generico (quantità_totale_comprata = 90)
	 */
	@Test
	public final void testQuantityToNotDecreaseSegment1() throws Exception{
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 32, 23};
		int [] s2={0, 40, 10, 40};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		//quantità necessaria per saturare il secondo fornitore
		final int QUANTITY = 30;
		
		assertEquals(QUANTITY, supplier.quantityToPreviousSegment(solution));
	}
	
	
	/*
	 * boundary (quantità_totale_comprata = 59)
	 */
	@Test
	public final void testQuantityToNotDecreaseSegment2() throws Exception{
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 33, 53};
		int [] s2={0, 40, 9, 10};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		//quantità necessaria per saturare il secondo fornitore
		final int QUANTITY = 49;
		
		assertEquals(QUANTITY, supplier.quantityToPreviousSegment(solution));
	}
	
	
	/*
	 *boundary (quantità_totale_comprata = 60)
	 */
	@Test
	public final void testQuantityToNotDecreaseSegment3() throws Exception{
		final String PROBLEM_NAME = "problema8_abiola.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 20, 32, 53};
		int [] s2={0, 40, 10, 10};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;
		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		//quantità necessaria per saturare il secondo fornitore
		final int QUANTITY = 0;
		
		assertEquals(QUANTITY, supplier.quantityToPreviousSegment(solution));
	}
	
	
	//test di setPrices
	/*
	 * caso generale
	 */
	@Test
	public final void testSetPrices1() throws Exception{
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
		assertTrue(solution.isAdmissible());
		
		final int SUPPLIER2_ID = 2;

		Supplier supplier = problem.getSupplier(SUPPLIER2_ID);
		int numSegments = problem.getNumSegments(SUPPLIER2_ID);
		int numProducts = supplier.getNumOfferedProducts();
		
		final double TOLERANCE = 0.1;
		final int [] DISCOUNTS = {0, 3, 5, 20};
		
		supplier.setPrices(numSegments, DISCOUNTS);
		double [][] expectedPrice = supplier.getPrices();
		
		for(int i=1; i<=numProducts; i++){
			for(int j=1; j<=numSegments;j++){
				double discountedPriceProduct = supplier.getPrice(i, j);
				assertEquals(expectedPrice[i][j], discountedPriceProduct, TOLERANCE);
			}
		}	
	}
}
