package testingjunit;

import static org.junit.Assert.*;

import io.InputFileParser;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import rob.Run;
import rob.Solver;
import rob.Utility;

public class SolverTest{
	Run [] runs;
	@Before
	public void setUp() throws Exception {
		String inputFile=Utility.getConfigParameter("testRunFile")+ File.separator + "runFile.txt";
		InputFileParser parser= new InputFileParser();
		Run run = (Run) parser.parse(inputFile);
		runs=new Run[1];
		runs[0]=run;
	}

	@Test
	public final void testSolve() {		
		try{
			Solver.solve(runs);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}