package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import org.junit.Test;
import solutiongenerator.RandomSolutionGenerator;
import data.IdList;
import data.Problem;
import data.Solution;

public class RandomEmptyingStrategyTest {

	//test di emptySuppliers()
	/*
	 * caso generale:
	 * ammissibilità sol
	 * tutti i fornitori della lista diminuiti di almeno 1 prodotto
	 * (dire che sol e lista sono creati t.c ciò sia possibile)
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
		assertTrue(sol0.isAdmissible(problem));
		
		//lista con i fornitori da svuotare = {1}
		IdList toEmpty = new IdList(1);
		toEmpty.add(1, 0);
		
		//sol1 = soluzione con i fornitori svuotati
		Solution sol1 = new Solution(sol0);
		RandomEmptyingStrategy strategy = new RandomEmptyingStrategy(problem);
		strategy.emptySuppliers(toEmpty, sol1);
		
		//controllo ammissibilità soluzione
		assertTrue(sol1.isAdmissible(problem));
		
		//controllo che almeno 1 prodotto sia stato spostato
			int totalBoughtSol0 = sol0.totalQuantityBought(1);
			int totalBoughtSol1 = sol1.totalQuantityBought(1);
			assertTrue(totalBoughtSol1 < totalBoughtSol0);
	}
}
