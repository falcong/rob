package solvingalgorithm.temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;

/**
 * Implementa la strategia First Improvement.
 */
public class FirstImprovementStrategy extends ExplorationStrategy {

	/**
	 * Esplora l'intorno secondo la strategia First Improvement. La strategia
	 * genera ed esamina un vicino alla volta ed esce al primo vicino che migliora la funzione
	 * obiettivo rispetto a {@code solution}.
	 */
	@Override
	public Solution explore(Solution solution, int maxNeighboursNumber,
			NeighbourGenerator generator) {
		for(int visited=1; visited<=maxNeighboursNumber; visited++) {
			Solution neighbour = generator.generate(solution);
			
			if (neighbour.getObjectiveFunction() < solution.getObjectiveFunction()){
				return neighbour; //Ho trovato un vicino migliore della soluzione corrente
			}
		}
		//se arrivo qui, nessun vicino visitato è migliore di solution.
		return null;
	}

}
