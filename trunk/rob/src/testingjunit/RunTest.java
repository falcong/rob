package testingjunit;

import static org.junit.Assert.*;

import java.io.File;

import io.InputFileParser;

import org.junit.Before;
import org.junit.Test;

import rob.Run;
import rob.Solution;
import rob.Utility;

public class RunTest {
	InputFileParser parser;
	String inputFile;

	@Before
	public void setUp() throws Exception {
		parser = new InputFileParser();
		inputFile=Utility.getConfigParameter("testRunFile")+ File.separator + "runFile.txt";
		
	}

	@Test
	public final void testExecute() {
		Run run;
		try {
			 run= (Run) parser.parse(inputFile);
			 Solution [] s=run.execute();
			 boolean ok=true;
			 for (int i=0;i<s.length;i++) {
				 ok= ok && s[i].isAdmissible(run.getProblem(i));
			 }
			 assertTrue(ok);			 
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception caught");
		}
		
		
	}

}
