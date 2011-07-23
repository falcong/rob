package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;
import solutionhandlers.AdvancedNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.SolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;


public class AdvancedNeighbourGeneratorTest {
	
	//test di generate()
	/*
	 * casi di test:
	 *1. creare una solution dove il fornitore scelto sia unico e tutti gli altri non retrocedano
	 *2. creare una solution dove per far scattare un fornitore almeno un'altro fornitore deve retrocedere 
	 *(non posso far forzare il fornitore da far scattare)
	 */
	@Test
	public final void testGenerate1(){
		
	}
	
	
	
	
	
	

//	@Test
//	public final void testFindSupplierImproveablePublic(){
//		/*
//		 * problema1.txt
//		 * soluzione tale che
//		 * s1 -> fascia di sconto 2 (massima)
//		 * s2 -> fascia di sconto 1
//		 */
//		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
//		Problem problem = pp.parse("problema1.txt");
//		int [] s0={0, 0, 0, 0};
//		int [] s1={0, 51, 42, 53};
//		int [] s2={0, 9, 0, 10};
//		int [][] matrix=new int[3][];
//		matrix[0]=s0;
//		matrix[1]=s1;
//		matrix[2]=s2;
//		Solution solution=new Solution(matrix,problem);
//		assertTrue(solution.isAdmissible(problem));
//		
//		AdvancedNeighbourGenerator2 generator = new AdvancedNeighbourGenerator2(problem);
//		assertEquals(2, generator.findSupplierImproveablePublic(solution));
//	}
//	
//	@Test
//	public final void testFindSupplierNotSaturatedPublic(){
//		/*
//		 * problema1.txt
//		 * soluzione tale che
//		 * s1 -> fascia di sconto 2 (massima)
//		 * s2 -> fascia di sconto 3 (massima) + saturazione
//		 */
//		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
//		Problem problem = pp.parse("problema1.txt");
//		int [] s0={0, 0, 0, 0};
//		int [] s1={0, 23, 42, 24};
//		int [] s2={0, 37, 0, 39};
//		int [][] matrix=new int[3][];
//		matrix[0]=s0;
//		matrix[1]=s1;
//		matrix[2]=s2;
//		Solution solution=new Solution(matrix,problem);
//		assertTrue(solution.isAdmissible(problem));
//		
//		AdvancedNeighbourGenerator2 generator = new AdvancedNeighbourGenerator2(problem);
//		assertEquals(1, generator.findSupplierNotSaturatedPublic(solution));
//	}
//	
//	
//	@Test
//	public final void testFindTargetSupplierPublic(){
//		/*
//		 * problema1.txt
//		 * soluzione tale che
//		 * s1 -> fascia di sconto 2 (massima)
//		 * s2 -> fascia di sconto 3 (massima) + saturazione
//		 */
//		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
//		Problem problem = pp.parse("problema1.txt");
//		int [] s0={0, 0, 0, 0};
//		int [] s1={0, 23, 42, 24};
//		int [] s2={0, 37, 0, 39};
//		int [][] matrix=new int[3][];
//		matrix[0]=s0;
//		matrix[1]=s1;
//		matrix[2]=s2;
//		Solution solution=new Solution(matrix,problem);
//		assertTrue(solution.isAdmissible(problem));
//		
//		AdvancedNeighbourGenerator2 generator = new AdvancedNeighbourGenerator2(problem);
//		assertEquals(1, generator.findTargetSupplierPublic(solution));
//	}
//	
	@Test
	public final void testGenerate(){
		ProblemParser pp = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem=pp.parse("problema3.txt");
		Supplier s = problem.getSupplier(1);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 0, 0};
		int [] s2={0, 9, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		//prima
		Solution solution1 = new Solution(matrix,problem);
		assertTrue(solution1.isAdmissible(problem));
		Supplier sup1 = problem.getSupplier(1);
		Supplier sup2 = problem.getSupplier(2);
		int sup1Segment1 = sup1.activatedSegment(solution1);
		int sup2Segment1 = sup2.activatedSegment(solution1);
		System.out.println("prima\n"+
				"fascia s1 = "+sup1Segment1+"\n"+
				"fascia s2 = "+sup2Segment1);
		//solution1.print();
		
		//chiamata metodo
		AdvancedNeighbourGenerator generator = new AdvancedNeighbourGenerator(problem);
		Solution solution2 = generator.generate(solution1, 0);
		
		//dopo
		assertTrue(solution2.isAdmissible(problem));
		int sup1Segment2 = sup1.activatedSegment(solution2);
		int sup2Segment2 = sup2.activatedSegment(solution2);
		System.out.println("\ndopo\n"+
				"fascia s1 = "+sup1Segment2+"\n"+
				"fascia s2 = "+sup2Segment2);
		//solution2.print();
		assertTrue(sup1Segment2==sup1Segment1+1);
	}
	
	@Test
	public final void testGenerate2(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.100.100.5.1.10.1.ctqd");
		SolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution tSol = gen.generate();
		System.out.println("fo trivial="+tSol.getObjectiveFunction());
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		
		Solution neighbour=nGen.generate(tSol, 1);
		System.out.println("fo vicino="+neighbour.getObjectiveFunction());
		assertTrue(neighbour.isAdmissible(problem));
		}
}
