package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import neighbourgenerator.EmptyCellsRandomNeighbourGenerator;

import org.junit.Test;

import data.Problem;
import data.Supplier;

import util.Constants;

public class EmptyCellsRandomNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();
	
	//test di scramble()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testScramble1() throws Exception{
		//per controllare che floor() funzioni correttamente
		final double RATIO = 0.53; 
		
		final int N = 100;
		for(int i=1; i<=N; i++){
			testScramble(RATIO);
		}
	}	
	
	/*
	 * ratio = 0
	 */ 
	@Test
	public final void testScramble2() throws Exception{
		final double RATIO = 0; 
		testScramble(RATIO);
	}	
	
	private final void testScramble(double ratio) throws Exception{
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		final String PROBLEM_NAME = "Cap.10.100.3.1.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int numSuppliers = problem.getDimension();
		//array0 = input
		Supplier array0[] = new Supplier[numSuppliers+1];
		//non usato
		array0[0] = null;
		for(int i=1; i<=numSuppliers; i++){
			array0[i] = problem.getSupplier(i);
		}
		
		EmptyCellsRandomNeighbourGenerator generator = new  EmptyCellsRandomNeighbourGenerator(problem);
		//array1 = output
		Supplier array1[] = generator.scramble(array0, ratio);
		
		//numero dei primi fornitori il cui ordine è stato alterato
		final int NUM_DISORDERED_SUPPLIERS = (int)(numSuppliers*ratio);
		
		/*
		 * Controllo che i primi NUM_DISORDERED_SUPPLIERS fornitori non vengano eliminati o duplicati
		 * (al più ne può solo essere modificato l'ordine).
		 */  
		boolean found[] = new boolean[NUM_DISORDERED_SUPPLIERS+1];
		//non usato
		found[0] = true;
		for(int i=1; i<=NUM_DISORDERED_SUPPLIERS; i++){
			found[i] = false;
		}
		
		for(int i=1; i<=NUM_DISORDERED_SUPPLIERS; i++){
			int supplierId = array1[i].getId();
			
			//non devo già avere trovato questo fornitore
			assertTrue(found[supplierId] == false);
			
			//segno che ho trovato fornitore
			found[supplierId] = true;
		}
		
		//controllo di avere trovato ogni fornitore almeno una volta
		for(int i=1; i<=NUM_DISORDERED_SUPPLIERS; i++){
			assertTrue(found[i] == true);
		}
		
		
		 /*
		  * controllo che l'ordine degli ultimi fornitori non sia stato alterato
		  */
		 
		for(int i=NUM_DISORDERED_SUPPLIERS+1; i<=numSuppliers; i++){
			assertTrue(array1[i].getId() == i);
		}
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


