package neighbourgenerator;

import data.Solution;

public interface DistancedNeighbourGenerator {
		public abstract Solution generate(Solution solution, int distance);
}
