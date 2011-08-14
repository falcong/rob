package neighbourgenerator;

import data.Solution;

/**
 * 
 * Generico generatore di vicini in cui è possibile specificare la distanza del vicino da generare.
 *
 */
public interface DistancedNeighbourGenerator {
		public abstract Solution generate(Solution solution, int distance);
}
