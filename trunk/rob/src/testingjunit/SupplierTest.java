package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;

public class SupplierTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testGetAveragePriceInt() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem p = pp.parse("Cap.10.40.3.1.10.1.ctqd");
		Supplier s = p.getSupplier(1);
		System.out.println(s.getAveragePrice(1));
		
	}
	
	@Test
	public final void testGetAveragePriceInt2() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem p = pp.parse("problema1.txt");
		Supplier s = p.getSupplier(2);
		//System.out.println(s.getAveragePrice(2));
		assertEquals(s.getAveragePrice(2), -1, 0.01);
		
	}
	
	@Test
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
	}
}
