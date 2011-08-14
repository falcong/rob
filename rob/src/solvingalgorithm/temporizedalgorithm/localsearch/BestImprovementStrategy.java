package solvingalgorithm.temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;


/**
 * Implementa la strategia Best Improvement.
 *
 */
public class BestImprovementStrategy extends ExplorationStrategy {

	/**
	 * Esplora l'intorno secondo la strategia Best Improvement. La strategia
	 * genera {@code maxNeighboursNumber} vicini, li esamina tutti e ritorna il vicino
	 * con la funzione obiettivo migliore tra tutti (o {@code null} se nessun vicino migliora la funzione
	 * obiettivo rispetto a {@code solution}).
	 */
	@Override
	public Solution explore(Solution solution, int maxNeighboursNumber, NeighbourGenerator generator) {
			Solution best = solution;
			
			for (int created=0; created<maxNeighboursNumber; created++){				
				Solution neighbour = generator.generate(solution);
				
				if(neighbour.getObjectiveFunction() < best.getObjectiveFunction())
					best = neighbour;
			}
			
			if(best != solution){ 
				return best; //ho trovato una soluzione migliore
			}else{
				return null; //tutte le soluzioni trovate erano peggiori
			}
			
		}
	
}
