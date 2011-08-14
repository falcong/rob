package neighbourgenerator;

import data.Solution;

/**
 * 
 * Generico generatore di vicini di una soluzione data.
 *
 */
public abstract class NeighbourGenerator {
	/**
	 * 
	 * @param solution - la soluzione di partenza
	 * @return la nuova soluzione trovata.
	 */
	public abstract Solution generate(Solution solution);
}
