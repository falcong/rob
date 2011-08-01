package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import solutionhandlers.BanFullNeighbourGenerator;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import util.Constants;
import util.Utility;

public class EmptyCellsNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();

	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testGenerate1(){
		final String methodName = new Exception().getStackTrace()[0].getMethodName(); 
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.40.5.2.70.1.ctqd";
		Problem problem = null;
		try {
			problem = pp.parse(PROBLEM_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	
//TODO eliminare sotto!	
/*	@Test
	public final void testEmptyCellsNeighbourGenerator() {
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = pp.parse("problema1.txt");
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 30, 42, 30};
		int [] s2={0, 30, 0, 33};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		Solution solution=new Solution(matrix,problem);
		
		EmptyCellsNeighbourGenerator generator = new EmptyCellsNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testEmptyCellsNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		RandomSolutionGenerator gen = new RandomSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		EmptyCellsNeighbourGenerator generator = new EmptyCellsNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
*/
}
