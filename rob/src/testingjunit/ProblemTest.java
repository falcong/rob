package testingjunit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.BoughtQuantityComparator;
import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;

public class ProblemTest {
	ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
	
	//metodo problem()
	/*
	 * Caso generico: controllo domanda totale.
	 */
	@Test
	public final void testProblem(){
		final String NAME = "name.ctqd";
		final String TYPE = "CTQD";
		final int PROBLEM_CLASS = 1;
		final int MAX_N_RANGE = 3;
		
		final int NUM_PRODUCTS = 2;
		int demand[] = new int[NUM_PRODUCTS+1];
		final int PRODUCT1_ID = 1;
		final int PRODUCT2_ID = 2;
		demand[PRODUCT1_ID] = 13;
		demand[PRODUCT2_ID] = 17;
		int totalDemand = 30;
		
		final int NUM_SUPPLIERS = 2;
		Supplier suppliers[] = new Supplier[NUM_SUPPLIERS+1];
		final int SUPPLIER1_ID = 1;
		final int SUPPLIER2_ID = 2;
		suppliers[SUPPLIER1_ID] = new Supplier(SUPPLIER1_ID);
		suppliers[SUPPLIER2_ID] = new Supplier(SUPPLIER2_ID);
				
		Problem problem = new Problem(NAME, TYPE, PROBLEM_CLASS, MAX_N_RANGE, demand, suppliers);
		
		//controllo domanda totale
		final double TOLERANCE = 0;
		assertEquals(totalDemand, problem.getTotalDeman(), TOLERANCE);
	}
	
	//metodo cellIsEmptiable()
	/*
	 * Caso generale con cell svuotabile.
	 */
	@Test
	public final void testCellIsEmptiable1(){
		final String PROBLEM_NAME = "problema7.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0 = {0, 0, 0};
		int [] s1 = {0, 50, 40};
		int [] s2 = {0, 5, 0};
		int [] s3 = {0, 5, 0};
		int [][] matrix = new int[problem.getDimension()+1][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		matrix[3] = s3;
		Solution sol = new Solution(matrix,problem);
		assertTrue(sol.isAdmissible(problem));
		
		final int CELL = 3;
		
		 ArrayList<Integer> otherCellsToEmpty = new ArrayList<Integer>();
		 otherCellsToEmpty.add(1);
		 otherCellsToEmpty.add(6);
		 
		 assertTrue(problem.cellIsEmptiable(CELL, sol, otherCellsToEmpty));
	}
	
	/*
	 * cell non svuotabile perchè le disponibilità residue di tutte le altre celle della stessa colonna sono 0. 
	 */
	@Test
	public final void testCellIsEmptiable2(){
		final String PROBLEM_NAME = "problema7a.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0 = {0, 0, 0};
		int [] s1 = {0, 50, 40};
		int [] s2 = {0, 5, 0};
		int [] s3 = {0, 5, 0};
		int [][] matrix = new int[problem.getDimension()+1][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		matrix[3] = s3;
		Solution sol = new Solution(matrix,problem);
		assertTrue(sol.isAdmissible(problem));
		
		final int CELL = 3;
		
		 ArrayList<Integer> otherCellsToEmpty = new ArrayList<Integer>();
		 otherCellsToEmpty.add(1);
		 otherCellsToEmpty.add(6);
		 
		 assertTrue(problem.cellIsEmptiable(CELL, sol, otherCellsToEmpty) == false);
	}
	
	/*
	 * cell non svuotabile perchè tutte le altre celle della stessa colonna appartengono a otherCellsToEmpty.
	 */
	@Test
	public final void testCellIsEmptiable3(){
		final String PROBLEM_NAME = "problema7.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0 = {0, 0, 0};
		int [] s1 = {0, 50, 40};
		int [] s2 = {0, 5, 0};
		int [] s3 = {0, 5, 0};
		int [][] matrix = new int[problem.getDimension()+1][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		matrix[3] = s3;
		Solution sol = new Solution(matrix,problem);
		assertTrue(sol.isAdmissible(problem));
		
		final int CELL = 3;
		
		 ArrayList<Integer> otherCellsToEmpty = new ArrayList<Integer>();
		 otherCellsToEmpty.add(1);
		 otherCellsToEmpty.add(5);
		 
		 assertTrue(problem.cellIsEmptiable(CELL, sol, otherCellsToEmpty) == false);
	}
	
	//metodo getCell()
	/*
	 * input =  1, 1
	 */
	@Test
	public final void testGetCell1(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int SUPPLIER = 1;
		final int PRODUCT = 1;
		final int CELL = 1;
		final double TOLERANCE = 0;
		assertEquals(CELL, problem.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}
	
	/*
	 * caso generale (input = 7, 53)
	 */
	@Test
	public final void testGetCell2(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int SUPPLIER = 7;
		final int PRODUCT = 53;
		final int CELL = 653;
		final double TOLERANCE = 0;
		assertEquals(CELL, problem.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}
	
	/*
	 * input = ultimo_fornitore, ultimo_prodotto
	 */
	@Test
	public final void testGetCell3(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int SUPPLIER = 10;
		final int PRODUCT = 100;
		final int CELL = 1000;
		final double TOLERANCE = 0;
		assertEquals(CELL, problem.getCell(SUPPLIER, PRODUCT), TOLERANCE);
	}
	
	//test di getSupplierFromCell()
	/*
	 * cella = 1
	 */
	@Test
	public final void testGetSupplierFromCell1(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 1;
		final int SUPPLIER = 1;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, problem.getSupplierFromCell(CELL), TOLERANCE);
	}
	
	/*
	 * caso generale (cella = 653)
	 */
	@Test
	public final void testGetSupplierFromCell2(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 653;
		final int SUPPLIER = 7;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, problem.getSupplierFromCell(CELL), TOLERANCE);
	}
	
	/*
	 * cella = ultima_cella
	 */
	@Test
	public final void testGetSupplierFromCell3(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 1000;
		final int SUPPLIER = 10;
		final double TOLERANCE = 0;
		assertEquals(SUPPLIER, problem.getSupplierFromCell(CELL), TOLERANCE);
	}
	
	//metodo getProductFromCell()
	/*
	 * cella = 1
	 */
	@Test
	public final void testGetProductFromCell1(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 1;
		final int PRODUCT = 1;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, problem.getProductFromCell(CELL), TOLERANCE);
	}
	
	/*
	 * cella = 653
	 */
	@Test
	public final void testGetProductFromCell2(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 653;
		final int PRODUCT = 53;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, problem.getProductFromCell(CELL), TOLERANCE);
	}
	
	/*
	 * cella = ultima_cella
	 */
	@Test
	public final void testGetProductFromCell3(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int CELL = 1000;
		final int PRODUCT = 100;
		final double TOLERANCE = 0;
		assertEquals(PRODUCT, problem.getProductFromCell(CELL), TOLERANCE);
	}
	
	//metodo getMaxQuantityBuyable()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testGetMaxQuantityBuyable1(){
		final String PROBLEM_NAME = "problema7.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int SUPPLIER = 2;
		/*
		 * max quantità comprabile presso il fornitore 2
		 * (tenendo conto delle disponibilità e della domanda)
		 */ 
		final int MAX_QUANTITY = 49;
		final double TOLERANCE = 0;
		assertEquals(MAX_QUANTITY, problem.getMaxQuantityBuyable(SUPPLIER), TOLERANCE);
	}
	
	//test di getRandomSupplierId()
	/*
	 * Caso generico
	 */
	@Test
	public final void testGetRandomSupplierId1(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		final int NUM_SUPPLIERS = problem.getDimension();
		
		HashSet<Integer> blackList = new HashSet<Integer>();
		blackList.add(2);
		blackList.add(6);
		blackList.add(7);
		blackList.add(9);
		
		int supplier = problem.getRandomSupplierId(blackList);
		
		assertTrue(supplier >= 1);
		assertTrue(supplier <= NUM_SUPPLIERS);
		assertTrue(!blackList.contains(supplier));
	}
	
	/*
	 * Caso con tutti fornitori in blacklist.
	 */
	@Test
	public final void testGetRandomSupplierId2(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		final int NUM_SUPPLIERS = problem.getDimension();
		
		//black list contiene tutti i fornitori
		HashSet<Integer> blackList = new HashSet<Integer>();
		for(int i=1; i<=NUM_SUPPLIERS; i++){
			blackList.add(i);
		}
		
		int supplier = problem.getRandomSupplierId(blackList);
		
		//controllo che il metodo restituisca 0 perchè non è stato in grado di trovare alcun fornitore
		final int NO_SUPPLIER = 0;
		final double TOLERANCE = 0;
		assertEquals(NO_SUPPLIER, supplier, TOLERANCE);
	}
	
	//test di maxSegmentActivable()
	/*
	 * Caso generico
	 * (con il fornitore che presenta una fascia irraggiungibile)
	 */
	@Test
	public final void testMaxSegmentActivable1(){
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		final int SUPPLIER = 3;
		//massima fascia di sconto raggiungibile per fornitore 3
		final int MAX_SEGMENT = 1;
		final double TOLERANCE = 0;
		assertEquals(MAX_SEGMENT, problem.maxSegmentActivable(SUPPLIER), TOLERANCE);
	}
	
	//test di sortByBoughtQuantity()
	/*
	 * Caso generico.
	 */
	@Test
	public final void testSortByBoughtQuantity1(){
		final String PROBLEM_NAME = "problema7.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0 = {0, 0, 0};
		int [] s1 = {0, 50, 40};
		int [] s2 = {0, 4, 0};
		int [] s3 = {0, 6, 0};
		int [][] matrix = new int[problem.getDimension()+1][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		matrix[3] = s3;
		Solution sol = new Solution(matrix,problem);
		assertTrue(sol.isAdmissible(problem));
		
		//fornitori ordinati in base alla quantità acquistata totale presso essi
		Supplier suppliers[] = problem.sortByBoughtQuantity(sol);
		//controllo correttezza dell'ordine
		final double TOLERANCE = 0;
		assertEquals(1, suppliers[1].getId(), TOLERANCE);
		assertEquals(3, suppliers[2].getId(), TOLERANCE);
		assertEquals(2, suppliers[3].getId(), TOLERANCE);
	}
	
	//test di sortByCurrentPrice()
	/*
	 * caso generico
	 */
	@Test
	public final void testSortByCurrentPrice1(){
		final String PROBLEM_NAME = "problema9.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int s0[] = {0, 0, 0, 0};
		int s1[] = {0, 51, 42, 53};
		int s2[] = {0, 9, 0, 10};
		int matrix[][] = new int[3][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		Solution solution = new Solution(matrix,problem);
		assertTrue(solution.isAdmissible(problem));
		
		final int PRODUCT = 1;
		Supplier suppliers[] = problem.sortByCurrentPrice(PRODUCT, solution);
		
		//controllo che i prezzi siano ordinati in senso non decrescente
		double price1 = suppliers[1].getDiscountedPrice(PRODUCT, solution.totalQuantityBought(1));
		double price2 = suppliers[2].getDiscountedPrice(PRODUCT, solution.totalQuantityBought(2));
		assertTrue(price1 <= price2);
	}
}
