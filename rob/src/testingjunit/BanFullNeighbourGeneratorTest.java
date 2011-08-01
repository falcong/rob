package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.BanFullNeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import util.Constants;
import util.Utility;

public class BanFullNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();
	
	//test di generate()
	/*
	 * Caso generale.
	 */
	//TODO aggiungere controllo che sol1 != sol0 qui e in tutti i metodi analoghi
	@Test
	public final void testGenerate1() throws Exception {
		final String methodName = new Exception().getStackTrace()[0].getMethodName(); 
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
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
	
	
	//TODO cancellare seguente!
	//###########################################################################
/*	@Test
	public void testProva(){
		//System.out.println(this.getClass().getName());
		//System.out.println(CLASS_NAME);
		
		//System.out.println(new Exception().getStackTrace()[0].getMethodName());
		
		System.out.println("prima");
		assertTrue(false);
		System.out.println("ciao");
	}*/
}
