package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;

import org.junit.Test;

import solutiongenerator.RandomSolutionGenerator;
import data.IdList;
import data.Problem;
import data.Solution;

public class RandomSelectionStrategyTest {

	//test di createList()
	/*
	 * caso generale
	 * controllare: num fornitori, non ripetuti, ammissibili
	 */
	@Test
	public void testCreateList1() throws Exception {
		final String PROBLEM_NAME = "Cap.50.40.5.1.10.1.ctqd";
		ProblemParser parser = new ProblemParser(Constants.TESTING_INPUT_PATH);
		Problem problem = parser.parse(PROBLEM_NAME);
		
		RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
		//sol0 = soluzione iniziale
		Solution sol0 = generator.generate();
		
		//lista con i fornitori da svuotare = {1,10,21}
		final int NUM_SUPPLIERS = 3;
		IdList toEmpty = new IdList(NUM_SUPPLIERS);
		toEmpty.add(1, 0);
		toEmpty.add(10, 1);
		toEmpty.add(21, 2);
		
		//sol1 = soluzione con i fornitori svuotati
		Solution sol1 = new Solution(sol0);
		RandomEmptyingStrategy strategy = new RandomEmptyingStrategy(problem);
		strategy.emptySuppliers(toEmpty, sol1);
		
		//controllo ammissibilità soluzione
		assertTrue(sol1.isAdmissible(problem));
		
		//controllo che per ogni fornitore almeno 1 prodotto sia stato spostato
		for(int s=0; s<toEmpty.getSize(); s++){
			int totalBoughtSol0 = sol0.totalQuantityBought(toEmpty.getId(s));
			int totalBoughtSol1 = sol1.totalQuantityBought(toEmpty.getId(s));
			assertTrue(totalBoughtSol1 < totalBoughtSol0);
		}
	}

}
