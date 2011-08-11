package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import neighbourgenerator.EmptyCellsNeighbourGenerator;

import org.junit.Test;

import data.Problem;
import data.Solution;
import data.Supplier;

import solutiongenerator.RandomSolutionGenerator;

public class EmptyCellsNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();

	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testGenerate1() throws Exception{
		final String methodName = new Exception().getStackTrace()[0].getMethodName(); 
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.40.5.2.70.1.ctqd";
		Problem problem = pp.parse(PROBLEM_NAME);
		final int numSuppliers = problem.getDimension();
		final int numProducts = problem.getNumProducts();
		final int numCells = numSuppliers*numProducts;
		
		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
		//sol0 0 sol iniziale casuale
		Solution sol0 = randomGenerator.generate();
		
		EmptyCellsNeighbourGenerator emptyGenerator = new EmptyCellsNeighbourGenerator(problem); 
		
		//esegue il test N volte perché il metodo non è deterministico
		final int N = 10;
		for(int i=1; i<=N; i++){
			//provo tutte le possibili distanze
			final int MAX_DISTANCE = numCells/2;
			for(int distance = 1; distance<=MAX_DISTANCE; distance+=10){
				Solution sol1 = emptyGenerator.generate(sol0, distance);
				//controllo ammissibilità della soluzione generata dal metodo
				boolean ok = sol1.isAdmissible(problem);
				
				
				if(!ok){
					System.out.println("fallimento di "+CLASS_NAME+"."+methodName+"\n"+
							"num di test = "+i+"\n"+
							"distance = "+distance);
					final String FILE_NAME_SOL0 = CLASS_NAME+"_"+methodName+"_"+i+"_"+distance+"_sol0";
					final String FILE_NAME_SOL1 = CLASS_NAME+"_"+methodName+"_"+i+"_"+distance+"_sol1";
					sol0.export(FILE_NAME_SOL0);
					sol1.export(FILE_NAME_SOL1);
				}
				
				assertTrue(ok);
			}
		}				
	}
	
	//test di scramble()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testScramble1() throws Exception{
		//per controllare che floor() funzioni correttamente
		final double RAND_FACTOR = 0.53; 
		
		final int N = 100;
		for(int i=1; i<=N; i++){
			testScramble(RAND_FACTOR);
		}
	}	
	
	/*
	 * ratio = 0
	 */ 
	@Test
	public final void testScramble2() throws Exception{
		final double RAND_FACTOR = 0; 
		testScramble(RAND_FACTOR);
	}	
	
	private final void testScramble(double randomizationFactor) throws Exception{
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
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
		
		EmptyCellsNeighbourGenerator generator = new  EmptyCellsNeighbourGenerator(problem);
		generator.setRandomizationFactor(randomizationFactor);
		//array1 = output
		Supplier array1[] = generator.scramble(array0);
		
		//numero dei primi fornitori il cui ordine è stato alterato
		final int NUM_DISORDERED_SUPPLIERS = (int)(numSuppliers*randomizationFactor);
		
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
