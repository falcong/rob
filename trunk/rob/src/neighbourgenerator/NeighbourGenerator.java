package neighbourgenerator;

import data.Solution;

public abstract class NeighbourGenerator {
	public abstract Solution generate(Solution solution, int distance);
}
