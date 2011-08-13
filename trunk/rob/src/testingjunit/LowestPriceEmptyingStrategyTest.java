package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import neighbourgenerator.bansupplier.emptyingstrategy.LowestPriceEmptyingStrategy;
import org.junit.Test;
import data.IdList;
import data.Problem;
import data.Solution;

public class LowestPriceEmptyingStrategyTest {

	/*
	 * caso generale:
	 * ammissibilità sol
	 * tutti i fornitori della lista diminuiti di almeno 1 prodotto
	 * (dire che sol e lista sono creati t.c ciò sia possibile)
	 * guardare i prezzi [fare lista di 1 elemento o problema minuscolo]
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
		final int NUM_SUPPLIERS = 1;
		IdList toEmpty = new IdList(NUM_SUPPLIERS);
		toEmpty.add(1, 0);
		
		//sol1 = soluzione con i fornitori svuotati
		Solution sol1 = new Solution(sol0);
		LowestPriceEmptyingStrategy strategy = new LowestPriceEmptyingStrategy(problem);
		strategy.emptySuppliers(toEmpty, sol1);
		
		//controllo ammissibilità soluzione
		assertTrue(sol1.isAdmissible(problem));
		
		/*
		 * sol0 è fatta in modo che svuotando il fornitore 1 tutti i prodotti vadano a finire nel fornitore 3
		 * controllo che almeno 1 prodotto del fornitore 1 sia stato spostato altrove
		 */
		int totalBoughtSol0 = sol0.totalQuantityBought(1);
		int totalBoughtSol1 = sol1.totalQuantityBought(1);
		assertTrue(totalBoughtSol1 < totalBoughtSol0);
		
		//presso il fornitore 2 non deve cambiare nulla
		totalBoughtSol0 = sol0.totalQuantityBought(2);
		totalBoughtSol1 = sol1.totalQuantityBought(2);
		assertTrue(totalBoughtSol1 == totalBoughtSol0);
		
		//il fornitore 3 deve ricevere tutti i prodotti di 1
		totalBoughtSol0 = sol0.totalQuantityBought(3);
		totalBoughtSol1 = sol1.totalQuantityBought(3);
		assertTrue(totalBoughtSol1 > totalBoughtSol0);
		/*
		 * non controllo che la variazione del numero di prodotti totali acquistati presso il fornitore 1 sia 
		 * opposta a quella presso il fornitore 3 perchè questo mi è garantito dalla ammissibilità di sol0 e
		 * sol1 
		 */
	}
}
