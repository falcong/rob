package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import java.util.Arrays;
import neighbourgenerator.bansupplier.selectionstrategy.RandomSelectionStrategy;
import org.junit.Test;
import solutiongenerator.RandomSolutionGenerator;
import data.IdList;
import data.Problem;
import data.Solution;

public class FullestFirstSelectionStrategyTest {

	//test di createList()
	/*
	 * caso generale
	 * controllare: num fornitori, non ripetuti, ammissibili
	 * + controllo che quelli scelti siano fra i primi size più pieni (se non è troppo lungo da fare???)
	 */
	@Test
	public void testCreateList1() throws Exception {
		final String PROBLEM_NAME = "Cap.50.40.5.1.10.1.ctqd";
		ProblemParser parser = new ProblemParser(Constants.TESTING_INPUT_PATH);
		Problem problem = parser.parse(PROBLEM_NAME);
		
		RandomSolutionGenerator generator = new RandomSolutionGenerator(problem);
		//sol0 = soluzione iniziale
		Solution sol0 = generator.generate();
		
		RandomSelectionStrategy strategy = new RandomSelectionStrategy(problem);
		
		//ripeto N volte il test a causa del non determinismo del metodo
		final int N = 10;
		for(int k=0; k<N; k++){
			//lista di 10 fornitori presso cui compravo almeno 1 prodotto estratti casualmente 
			IdList list = strategy.createList(sol0, 10);
			
			for(int i=0; i<list.getSize(); i++){
				//controllo l'ammissibilità del fornitore
				assertTrue(list.getId(i)>=1	&&	list.getId(i)<=problem.getDimension());
				
				//controllo che presso il fornitore sia acquistato almeno un prodotto
				assertTrue(sol0.totalQuantityBought(list.getId(i)) >= 1);
			}
			
			//controllo che la lista non contenga duplicati
			boolean found[] = new boolean[problem.getDimension()+1];
			Arrays.fill(found, false);
			
			for(int i=0; i<list.getSize(); i++){
				if(found[list.getId(i)]){
					fail("La lista contiene un fornitore duplicato");
				}
				found[list.getId(i)] = true;
			}
		}
	}

}
