package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;
import solutionhandlers.EmptyCellsRandomNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;

public class EmptyCellsRandomNeighbourGeneratorTest {
	
	//test di scramble()
	/*
	 * casi:
	 * - generale
	 * - ratio = 0
	 */
	@Test
	public final void testScramble1(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public final void testScramble(){
		//prob 63
		String problemName = "Cap.50.100.3.2.10.1.ctqd";
		ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = probParser.parse(problemName);
		
		LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
      	Solution sol = linesGenerator.generate();
      
      	int p = 13;
      	Supplier sups[] = problem.sortByCurrentPrice(p, sol);
      	
/*      	for(int i=1; i<=sups.length-2; i++){
      		double price1 = sups[i].getDiscountedPrice(p, sol.
      				getQuantity(sups[i].getId(), p));
      		double price2 = sups[i+1].getDiscountedPrice(p, sol.
      				getQuantity(sups[i+1].getId(), p));
      		if(price1!=-1 && price2!=-1){
      			assertTrue(price1<=price2);
      		}
      	}*/
      	
      	//stampo id ordinati
      	for(int i=1; i<=sups.length-1; i++){
      		System.out.print(sups[i].getId()+"\t");
      	}
      	System.out.println();
      	
      	sups = (new EmptyCellsRandomNeighbourGenerator(problem)).scramble(sups, 0.1);
      	
      	//stampo id disordinati
      	for(int i=1; i<=sups.length-1; i++){
      		System.out.print(sups[i].getId()+"\t");
      	}
      	System.out.println();
      	
	}
}
