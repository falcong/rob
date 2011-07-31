package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.BoughtQuantityComparator;
import rob.Problem;
import rob.Solution;
import rob.Supplier;

public class BoughtQuantityComparatorTest {
	ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
	
	final String PROBLEM_NAME = "problema3.txt";
	Problem problem = setProblem();
	
	final int sup1Id = 1;
	Supplier sup1 = problem.getSupplier(sup1Id);
	final int sup2Id = 2;
	Supplier sup2 = problem.getSupplier(sup2Id);
	
	//TODO vedere1
	public Problem setProblem(){
		Problem problem = null;
		try {
			problem = pp.parse(PROBLEM_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//TODO vedere 6 in realtà qui non sarebbe meglio lanciare errore?
			fail("Il parser non è stato in grado di creare il problema.");
		}
		return problem;
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
