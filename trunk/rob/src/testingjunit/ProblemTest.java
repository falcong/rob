package testingjunit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;

public class ProblemTest {
	Problem p;
	ProblemParser parser;
	
	@Before
	public void setUp() throws Exception {
		parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//Problem p = parser.parse("problema1.txt");
		//p = parser.parse("Cap.50.100.5.1.10.1.ctqd");
	}
	
	//metodo problem()
	/*
	 * caso generico e controllo domanda totale
	 */
	@Test
	public final void testProblem(){
		;
	}
	
	//metodo cellIsEmptiable()
	/*
	 * caso generale con cell svuotabile
	 */
	@Test
	public final void testCellIsEmptiable1(){
		
	}
	
	/*
	 * cell non svuotabile perchè disponibilità residue tutte 0 
	 */
	@Test
	public final void testCellIsEmptiable2(){
		
	}
	
	/*
	 * cell non svuotabile perchè tutta la colonna di cell è vietata
	 */
	@Test
	public final void testCellIsEmptiable3(){
		
	}
	
	//metodo getCell()
	/*
	 * testare cella 1,1 generica ultima
	 */
	@Test
	public final void testGetCell1(){
		
	}
	
	//metodo getMaxQuantityBuyable()
	/*
	 * caso generale
	 */
	@Test
	public final void testGetMaxQuantityBuyable1(){
		
	}
	
	//metodo getProductFromCell()
	/*
	 * testare cella 1,1 generica ultima
	 */
	@Test
	public final void testGetProductFromCell(){
		
	}
	
	//test di getRandomSupplierId()
	/*
	 * caso generico
	 */
	@Test
	public final void testGetRandomSupplierId1(){
		
	}
	
	/*
	 * caso con tutti fornitori in blacklist
	 */
	@Test
	public final void testGetRandomSupplierId2(){
		
	}
	
	//test di getSupplierFromCell()
	/*
	 * testare cella 1,1 generica ultima
	 */
	@Test
	public final void testGetSupplierFromCell1(){
		
	}
	
	//test di maxSegmentActivable()
	/*
	 * caso generico con fascia irraggiungibile
	 */
	@Test
	public final void testMaxSegmentActivable1(){
		
	}
	
	//test di sortByBoughtQuantity()
	/*
	 * caso generico
	 */
	@Test
	public final void tetsSortByBoughtQuantity1(){
		
	}
	
	//test di sortByCurrentPrice()
	/*
	 * caso generico
	 */
	@Test
	public final void tetsSortByCurrentPrice1(){
		
	}
	
	
	
	
	
	
	
	@Test
	public final void testGetMaxQuantityBuyable(){
		p = parser.parse("problema1.txt");
		assertEquals(76, p.getMaxQuantityBuyable(2));
	}
	
	@Test
	public final void testMaxSegmentActivable(){
		p = parser.parse("problema1.txt");
		assertEquals(2, p.maxSegmentActivable(1));
	}
	
	@Test
	public final void testGetRandomSupplierId(){
		p = parser.parse("problema1.txt");
		int randSupId = p.getRandomSupplierId(new HashSet<Integer>());
		assertTrue(randSupId == 1 || randSupId == 2);
	}
	
/*	@Test
	public final void testCellIsEmptiable(){
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = pp.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 42, 53};
		int [] s2={0, 9, 0, 10};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		int cell = 1;
		ArrayList<Integer> otherCellsToEmpty=new ArrayList<Integer>();
		boolean e=problem.cellIsEmptiable(cell, solution, otherCellsToEmpty);
		assertTrue(e);
		otherCellsToEmpty.add(4);
		boolean e1=problem.cellIsEmptiable(cell, solution, otherCellsToEmpty);
		assertTrue(!e1);

		int cell2 = 2;
		ArrayList<Integer> otherCellsToEmpty2=new ArrayList<Integer>();
		boolean e2=problem.cellIsEmptiable(cell2, solution, otherCellsToEmpty2);
		assertTrue(!e2);
	}*/
		
		@Test
		public final void testSortByCurrentPrice(){
			ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = pp.parse("problema1.txt");
			int [] s0={0, 0, 0, 0};
			int [] s1={0, 51, 42, 53};
			int [] s2={0, 9, 0, 10};
			int [][] matrix=new int[3][];
			matrix[0]=s0;
			matrix[1]=s1;
			matrix[2]=s2;
			Solution solution=new Solution(matrix,problem);
			
			int product = 1;
			
			Supplier [] sups=problem.sortByCurrentPrice(product, solution);
			assertTrue(sups[1].getDiscountedPrice(product, solution.getQuantity(sups[1].getId(), product))< sups[2].getDiscountedPrice(product, solution.getQuantity(sups[2].getId(), product)));
		}


}
