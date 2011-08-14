package neighbourgenerator;

import data.Solution;

/**
 * 
 * Generico generatore di vicini di una soluzione data.
 *
 */
public abstract class NeighbourGenerator {
	public abstract Solution generate(Solution solution);
}
