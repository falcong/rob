package neighbourgenerator.bansupplier;

import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import neighbourgenerator.bansupplier.selectionstrategy.FullestFirstSelectionStrategy;
import data.Problem;

/**
 * 
 * Questo generatore per creare i vicini di una soluzione sceglie alcuni 
 * fornitori dalla metà dei fornitori più pieni e va a riempire
 * altri fornitori scelti casualmente.
 *
 */
public class BanFullNeighbourGenerator extends BanSupplierNeighbourGenerator{
	
	
	public BanFullNeighbourGenerator(Problem problem) {
		super(problem, new FullestFirstSelectionStrategy(problem), new RandomEmptyingStrategy(problem));
		}
}





