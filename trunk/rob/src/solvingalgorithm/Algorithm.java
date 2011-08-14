package solvingalgorithm;

import data.Problem;
import data.Solution;

public abstract class Algorithm{
	protected Problem problem;
	
	/**
	 * Esegue l'algoritmo a partire dalla soluzione {@code startSolution}.
	 * @param startSolution - soluzione di partenza
	 * @return un oggetto soluzione risultante dall'esecuzione dell'algoritmo.
	 */
	public abstract Solution execute(Solution startSolution);
}
