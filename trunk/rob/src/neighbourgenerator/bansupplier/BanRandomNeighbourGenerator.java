package neighbourgenerator.bansupplier;


import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import neighbourgenerator.orderstrategy.RandomOrderStrategy;
import data.Problem;

public class BanRandomNeighbourGenerator extends BanSupplierNeighbourGenerator {

	public BanRandomNeighbourGenerator(Problem problem) {
		super(problem, new RandomEmptyingStrategy(problem), new RandomOrderStrategy(problem));
	}

}
