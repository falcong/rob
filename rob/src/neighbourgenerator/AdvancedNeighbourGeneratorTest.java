package neighbourgenerator;

import static org.junit.Assert.*;
import org.junit.Test;

import parser.ProblemParser;
import util.Constants;
import data.Problem;
import data.Solution;
import data.Supplier;



public class AdvancedNeighbourGeneratorTest {
	
	//test di generate()
	/*
	 * Forzo il metodo a far scattare il fornitore1 (i prodotti acquistati presso il fornitore2 non possono
	 * essere aumentati perché la disponibilità è già esaurita).
	 */
	@Test
	public final void testGenerate1() throws Exception{
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		final String PROBLEM_NAME = "problema1.txt";
		Problem problem = null;
		problem = pp.parse(PROBLEM_NAME);


		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 0, 0};
		int [] s2={0, 9, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		//prima
		//solution1 = soluzione iniziale
		Solution solution1 = new Solution(matrix,problem);
		assertTrue(solution1.isAdmissible());
		Supplier sup1 = problem.getSupplier(1);
		Supplier sup2 = problem.getSupplier(2);
		//fasce di sconto attive inizialmente
		int sup1Segment1 = sup1.activatedSegment(solution1);
		int sup2Segment1 = sup2.activatedSegment(solution1);
		
		//provo il metodo più volte perchè non è deterministico
		final int N = 10;
		for (int i = 0; i < N; i++) {
			System.out.println("esecuzione "+(i+1));
			System.out.println("prima:\n" + "fascia s1 = " + sup1Segment1
					+ "\n" + "fascia s2 = " + sup2Segment1 + "\n");
			//chiamata metodo
			//solution2 = soluzione restituita dal metodo
			AdvancedNeighbourGenerator generator = new AdvancedNeighbourGenerator(
					problem);
			System.out.println("chiamata generate");
			Solution solution2 = generator.generate(solution1);
			//dopo
			assertTrue(solution2.isAdmissible());
			assertTrue(solution1.calcDistance(solution2)!=0);
			int sup1Segment2 = sup1.activatedSegment(solution2);
			int sup2Segment2 = sup2.activatedSegment(solution2);
			System.out.println("\ndopo:\n" + "fascia s1 = " + sup1Segment2
					+ "\n" + "fascia s2 = " + sup2Segment2 + "\n");
			assertTrue(sup1Segment2 == sup1Segment1 + 1);
			assertTrue(sup2Segment2 == sup2Segment1);
		}
	}
	
	/*
	 * sol iniziale:
	 * 51	52	53
	 * 9	42	63
	 * 
	 *  Necessariamente deve scattare fornitore1 e retrocedere fornitore2, cioè
	 *  sol finale
	 *  52	52	53
	 *  8	42	63
	 */
	@Test
	public final void testGenerate2() throws Exception{
		ProblemParser pp = new ProblemParser(Constants.TESTING_INPUT_PATH);
		final String PROBLEM_NAME = "problema2.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 51, 52, 53};
		int [] s2={0, 9, 42, 63};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		//prima
		//solution1 = soluzione iniziale
		Solution solution1 = new Solution(matrix,problem);
		assertTrue(solution1.isAdmissible());
		
		//solution2expected 
		int s20[] = {0, 0, 0, 0};
		int s21[] = {0, 52, 52, 53};
		int s22[] = {0, 8, 42, 63};
		matrix[0]=s20;
		matrix[1]=s21;
		matrix[2]=s22;
		Solution solution2Expected = new Solution(matrix,problem);
		assertTrue(solution2Expected.isAdmissible());
		
		//solution2 = generate(solution1)
		AdvancedNeighbourGenerator generator = new AdvancedNeighbourGenerator(problem);
		//il parametro in ingresso 0 viene ignorato
		Solution solution2 = generator.generate(solution1);
		
		//controllo che solution2 = solution2Expected (se e solo se distanza=0)
		assertTrue(solution2.calcDistance(solution2Expected)==0);	
	}
}
