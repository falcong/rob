package neighbourgenerator.bansupplier;


import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import neighbourgenerator.bansupplier.selectionstrategy.RandomSelectionStrategy;
import data.Problem;

/**
 * 
 * Questo generatore per creare i vicini di una soluzione svuota dei fornitori scelti casualmente e ne va a riempire
 * altri anch'essi scelti casualmente.
 *
 */
public class BanRandomNeighbourGenerator extends BanSupplierNeighbourGenerator {

	public BanRandomNeighbourGenerator(Problem problem) {
		super(problem, new RandomSelectionStrategy(problem), new RandomEmptyingStrategy(problem));
	}

}
