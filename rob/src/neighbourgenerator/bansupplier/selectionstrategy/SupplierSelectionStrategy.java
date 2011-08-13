package neighbourgenerator.bansupplier.selectionstrategy;

import data.IdList;
import data.Problem;
import data.Solution;

public abstract class SupplierSelectionStrategy {
	protected Problem problem;
	
	public SupplierSelectionStrategy(Problem problem){
		this.problem=problem;
	}
	
	/**
	 * Crea una lista di id di fornitori secondo una certa strategia.
	 * @param solution
	 * @param distance
	 * @return una IdList di interi.
	 */
	abstract public IdList createList(Solution solution, int distance);
}
