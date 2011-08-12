package neighbourgenerator;

import data.Problem;

public class BanRandomNeighbourGenerator extends BanSupplierNeighbourGenerator {

	public BanRandomNeighbourGenerator(Problem problem) {
		super(problem, new RandomEmptyingStrategy(problem), new RandomOrderStrategy(problem));
	}

}
