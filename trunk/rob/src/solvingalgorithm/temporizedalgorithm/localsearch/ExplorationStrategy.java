package solvingalgorithm.temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;
/**
 * Le classi che estendono questa classe astratta implementano una strategia
 * per esplorare il vicinato di una soluzione data e trovare una soluzione
 * che migliora quella di partenza.
 *
 */
public abstract class ExplorationStrategy {
	
	/**
	 * Esplora l'intorno secondo una certa strategia e ritorna la soluzione
	 * migliore trovata.
	 * 
	 * @param solution - la soluzione di partenza
	 * @param maxNeighboursNumber - il massimo numero di vicini da esaminare
	 * @param generator - l'algoritmo con cui generare i vicini
	 * @return La soluzione migliore trovata durante l'esplorazione, oppure {@code null}
	 * se sono stati esplorati {@code maxNeighboursNumber} vicini e nessuno di essi era migliore di {@code solution}.
	 */
 public abstract Solution explore(Solution solution, int maxNeighboursNumber,
		 														NeighbourGenerator generator);
}
