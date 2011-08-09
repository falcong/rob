package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import comparator.BoughtQuantityComparator;

import data.Problem;
import data.Solution;
import data.Supplier;

import util.Constants;

public class BoughtQuantityComparatorTest {
	ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
	
	Problem problem;
	Supplier sup1;
	Supplier sup2;
	
	@Before
	public final void setUp() throws Exception {
		final String PROBLEM_NAME = "problema3.txt";
		problem = pp.parse(PROBLEM_NAME);
		
		final int sup1Id = 1;
		sup1 = problem.getSupplier(sup1Id);
		final int sup2Id = 2;
		sup2 = problem.getSupplier(sup2Id);
	}
	
	/*
	 * sup1 < sup2
	 */
	@Test
	public void testCompare1() {
		int s0[] = {0, 0, 0, 0};
		int s1[] = {0, 51, 28, 0};
		int s2[] = {0, 9, 12, 60};
		int matrix[][] = new int[3][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		Solution sol = new Solution(matrix, problem);
		assertTrue(sol.isAdmissible(problem));
		
		BoughtQuantityComparator comparator = new BoughtQuantityComparator(sol);
		assertTrue(comparator.compare(sup1, sup2) < 0);
	}
	
	/*
	 * sup1 = sup2
	 */
	@Test
	public void testCompare2() {
		int s0[] = {0, 0, 0, 0};
		int s1[] = {0, 51, 29, 0};
		int s2[] = {0, 9, 11, 60};
		int matrix[][] = new int[3][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		Solution sol = new Solution(matrix, problem);
		assertTrue(sol.isAdmissible(problem));
		
		BoughtQuantityComparator comparator = new BoughtQuantityComparator(sol);
		assertTrue(comparator.compare(sup1, sup2) == 0);
	}
	
	/*
	 * sup1 > sup2
	 */
	@Test
	public void testCompare3() {
		int s0[] = {0, 0, 0, 0};
		int s1[] = {0, 51, 40, 53};
		int s2[] = {0, 9, 0, 7};
		int matrix[][] = new int[3][];
		matrix[0] = s0;
		matrix[1] = s1;
		matrix[2] = s2;
		Solution sol = new Solution(matrix, problem);
		assertTrue(sol.isAdmissible(problem));
		
		BoughtQuantityComparator comparator = new BoughtQuantityComparator(sol);
		assertTrue(comparator.compare(sup1, sup2) > 0);
	}

}
