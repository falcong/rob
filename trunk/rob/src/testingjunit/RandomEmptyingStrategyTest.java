package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import org.junit.Test;
import data.IdList;
import data.Problem;
import data.Solution;

public class RandomEmptyingStrategyTest {

	//test di emptySuppliers()
	/*
	 * Caso generale.
	 */
	@Test
	public void testEmptySuppliers1() throws Exception {
		final String PROBLEM_NAME = "problema16.txt";
		ProblemParser parser = new ProblemParser(Constants.TESTING_INPUT_PATH);
		Problem problem = parser.parse(PROBLEM_NAME);
		
		//sol0 = soluzione iniziale
		int r0[] = {0, 0, 0, 0};
		int r1[] = {0, 40, 20, 23};
		int r2[] = {0, 10, 12, 30};
		int r3[] = {0, 10, 10, 10};
		int matrix[][] = new int[4][];
		matrix[0] = r0;
		matrix[1] = r1;
		matrix[2] = r2;
		matrix[3] = r3;
		Solution sol0 = new Solution(matrix, problem);
		assertTrue(sol0.isAdmissible());
		
		//lista con i fornitori da svuotare = {1}
		IdList toEmpty = new IdList(1);
		toEmpty.add(1, 0);
		
		//eseguo il test N volte perché emptySuppliers non è deterministico
		final int N = 10;
		for(int i=1; i<N; i++){
			//sol1 = soluzione con i fornitori svuotati
			Solution sol1 = new Solution(sol0);
			RandomEmptyingStrategy strategy = new RandomEmptyingStrategy(problem);
			strategy.emptySuppliers(toEmpty, sol1);
			
			//controllo ammissibilità soluzione
			assertTrue(sol1.isAdmissible());
			
			/*
			 * controllo che tutto il fornitore 1 sia stato svuotato (la soluzione è fatta in modo che ciò 
			 * sia possibile)
			 */
			int totalBoughtSol1 = sol1.totalQuantityBought(1);
			assertTrue(totalBoughtSol1 == 0);
		}
	}
}
