package temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;

public abstract class ExplorationStrategy {
	
 public abstract Solution explore(Solution solution, int maxNeighboursNumber,
		 														NeighbourGenerator generator);
}
