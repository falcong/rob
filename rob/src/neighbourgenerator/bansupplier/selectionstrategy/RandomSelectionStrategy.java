package neighbourgenerator.bansupplier.selectionstrategy;

import java.util.HashSet;

import data.IdList;
import data.Problem;
import data.Solution;

public class RandomSelectionStrategy extends SupplierSelectionStrategy {

	public RandomSelectionStrategy(Problem problem) {
		super(problem);
	}

	/**
	 * Questo metodo crea una lista di dimensione {@code size} contenente ID di fornitori presi casualmente
	 * tra tutti i fornitori del problema che sono presenti in soluzione con almeno un'unità 
	 * di prodotto acquistata. <br>
	 * Se non vi sono sufficienti fornitori che soddisfano le condizioni, il metodo tornerà
	 * una lista di dimensione inferiore.</br>
	 * È assunta la precondizione che {@code size} sia minore o uguale della dimensione del problema.
	 * 
	 */
	@Override
	public IdList createList(Solution solution, int size) {
		IdList list = new IdList(size);
		//In questa lista metterò i fornitori che non possono essere scelti
		HashSet<Integer> rejected = new HashSet<Integer>();
		/*
		 * Questo ciclo termina se
		 * - Ho trovato size fornitori
		 * - list.getSize+rejected.size() è uguale alla dimensione del problema
		 */
		for (int i=0; i<size && (list.getSize()+rejected.size()<problem.getDimension()); i++) {
			int randomSup = problem.getRandomSupplierId(list.getHashSet());
			int total=solution.totalQuantityBought(randomSup);
			//Non sto comprando nulla da questo fornitore, lo scarto
			if (total==0) {
				i--;
				rejected.add(randomSup);
				continue;
			}
			list.add(randomSup,i);
		}
		return list;
	}
}
