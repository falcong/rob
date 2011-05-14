package testingjunit;

import static org.junit.Assert.*;

import java.io.File;

import io.InputFileParser;

import org.junit.Before;
import org.junit.Test;

import rob.Run;
import rob.Solution;
import rob.Utility;

public class ProveVarie{
	InputFileParser parser =  new InputFileParser();
	String inputFile;

	@Before
	public void setUp() throws Exception {
		
	}

/*	@Test
	public final void prova1(){
		//trovo ottimo con cplex del problema Files = Cap.10.40.3.1.99.1.ctqd
		inputFile = Utility.getConfigParameter("testRunFile")+ File.separator + "prova1.txt";
		Run run;
		try {
			 run = (Run) parser.parse(inputFile);
			 Solution solutions[] = run.execute();
			 
			 //tutte le soluzioni devono essere ammissibili
			 for (int i=0;i<solutions.length;i++) {
				 assertTrue(solutions[i].isAdmissible(run.getProblem(i)));
			 }			 
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception caught");
		}
	}*/
	
/*	@Test
	public final void prova2(){
		//trovo ottimo con cplex di 3 problemi
		inputFile = Utility.getConfigParameter("testRunFile")+ File.separator + "prova2.txt";
		Run run;
		try {
			 run = (Run) parser.parse(inputFile);
			 Solution solutions[] = run.execute();
			 
			 //tutte le soluzioni devono essere ammissibili
			 for (int i=0;i<solutions.length;i++) {
				 assertTrue(solutions[i].isAdmissible(run.getProblem(i)));
			 }			 
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception caught");
		}
	}*/
}
