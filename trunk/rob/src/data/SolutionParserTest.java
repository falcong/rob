package data;

import static org.junit.Assert.*;
import org.junit.Test;

import parser.ProblemParser;
import parser.SolutionParser;
import util.Constants;

public class SolutionParserTest {
	ProblemParser pp=new ProblemParser(Constants.TESTING_INPUT_PATH);

	//test di parse()
	/*
	 * caso generale:
	 * creo file con sol
	 * leggo la soluzione
	 * controllo che sol letta sia = sol del file
	 */
	@Test
	public final void testSolutionString1() throws Exception {
		final String PROBLEM_NAME="problema10.txt";
		Problem problem = pp.parse(PROBLEM_NAME);
		
		int [] s0={0, 0, 0, 0};
		int [] s1={0, 30, 10, 40};
		int [] s2={0, 20, 32, 23};
		int [][] matrix=new int[3][];
		matrix[0]=s0;
		matrix[1]=s1;
		matrix[2]=s2;
		
		Solution expectedSolution=new Solution(matrix,problem);
		

		final String inputFileName = PROBLEM_NAME+"_Solution.txt";
		final String inputFileFolder = Constants.TESTING_INPUT_PATH+System.getProperty("file.separator");
		SolutionParser solParser = new SolutionParser(inputFileFolder, problem);
		Solution importedSolution = solParser.parse(inputFileName);
	
		assertTrue(expectedSolution.calcDistance(importedSolution)==0);
	}

}
