//MOD
//era tutto commentato
package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

//MOD rob2.Problem
import rob.Problem;
import rob.Utility;
//MOD
import rob.Solution;
import solvingalgorithms.Cplex;

public class CplexTest {
	Problem problem;
	ProblemParser parser;

	@Before
	public void setUp() throws Exception {
		//MOD non c'era argomento
		parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
	}

	//test di execute()
	/*
	 * caso generale: solo ammissibilità
	 */
	@Test
	public final void testExecute() {
		problem = parser.parse("problema1.txt");
		//problem = parser.parse("Cap.100.100.5.1.10.5.ctqd");
		//problem = parser.parse("Cap.50.100.5.2.10.1.ctqd");
		Cplex c = new Cplex(problem);
		Solution s = c.execute(null);
		System.out.println("funzione obiettivo = "+s.getObjectiveFunction());
	}

}
