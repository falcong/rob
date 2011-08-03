package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import neighbourgenerator.BanFullNeighbourGenerator;
import neighbourgenerator.BanSupplierNeighbourGenerator;
import neighbourgenerator.DirectionedBanNeighbourGenerator;

import org.junit.Before;
import org.junit.Test;

import data.Problem;
import data.Solution;

import solutiongenerator.RandomSolutionGenerator;
import solutiongenerator.TrivialSolutionGenerator;
import util.Constants;
import util.Utility;

public class BanSupplierNeighbourGeneratorTest {
	final String CLASS_NAME = this.getClass().getName();

	@Before
	public void setUp() throws Exception {
	}

	//test di generate()
	/*
	 * Caso generale.
	 */
	@Test
	public final void testGenerate1() throws Exception{
		final String methodName = new Exception().getStackTrace()[0].getMethodName();
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		
		final String PROBLEM_NAME = "Cap.10.40.3.2.99.1.ctqd";
		Problem problem = null;
		problem = pp.parse(PROBLEM_NAME);
		
		final int numSuppliers = problem.getDimension();
		
		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
		//sol0 0 sol iniziale casuale
		Solution sol0 = randomGenerator.generate();

		
		BanSupplierNeighbourGenerator banGenerator = new BanSupplierNeighbourGenerator(problem); 
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	//TODO cancellare tutto!
	/*@Test
	public final void testBanSupplierNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new BanSupplierNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testBanFullNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new BanFullNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testDirectionedNeighbourGenerator() {
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
		
		BanSupplierNeighbourGenerator generator = new DirectionedBanNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	
	@Test
	public final void testBanFullNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		BanSupplierNeighbourGenerator generator = new BanFullNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
	
	@Test
	public final void testDirectionedNeighbourGeneratorBigProblem() {
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		RandomSolutionGenerator gen = new RandomSolutionGenerator(problem);
		Solution solution = gen.generate();		
		
		
		BanSupplierNeighbourGenerator generator = new DirectionedBanNeighbourGenerator(problem);
		
		//prima
		solution.print();
		
		//dopo
		Solution result=generator.generate(solution,1);
		assertTrue(result.isAdmissible(problem));
		result.print();
	}
*/
}
