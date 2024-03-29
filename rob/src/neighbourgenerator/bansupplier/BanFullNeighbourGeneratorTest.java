package neighbourgenerator.bansupplier;

import static org.junit.Assert.*;


import org.junit.Test;

import parser.ProblemParser;

import data.Problem;
import data.Solution;

import solutiongenerator.RandomSolutionGenerator;
import util.Constants;

public class BanFullNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();
	
	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testGenerate1() throws Exception {
		final String methodName = new Exception().getStackTrace()[0].getMethodName(); 
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.100.3.1.10.1.ctqd";
		Problem problem = null;
	    problem = pp.parse(PROBLEM_NAME);
		
		final int numSuppliers = problem.getDimension(); 
		
		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
		//sol0 0 sol iniziale casuale
		Solution sol0 = randomGenerator.generate();
		
		BanFullNeighbourGenerator banGenerator = new BanFullNeighbourGenerator(problem); 
		
		//esegue il test N volte perché il metodo non è deterministico
		final int N = 10;
		for(int i=1; i<=N; i++){
			//provo tutte le possibili distanze
			final int MAX_DISTANCE = numSuppliers/2;
			for(int distance = 1; distance<=MAX_DISTANCE; distance++){
				Solution sol1 = banGenerator.generate(sol0, distance);
				//controllo ammissibilità della soluzione generata dal metodo
				boolean ok = sol1.isAdmissible();
				//verifico che il generatore abbia davvero prodotto
				//una nuova soluzione diversa
				assertTrue(sol0.calcDistance(sol1)!=0);
				
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
}
