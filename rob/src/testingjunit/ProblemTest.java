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
	
	@Test
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
	}
		
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
