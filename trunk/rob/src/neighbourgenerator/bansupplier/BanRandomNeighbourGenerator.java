package neighbourgenerator.bansupplier;


import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import neighbourgenerator.bansupplier.selectionstrategy.RandomSelectionStrategy;
import data.Problem;

public class BanRandomNeighbourGenerator extends BanSupplierNeighbourGenerator {

	public BanRandomNeighbourGenerator(Problem problem) {
		super(problem, new RandomSelectionStrategy(problem), new RandomEmptyingStrategy(problem));
	}

}
