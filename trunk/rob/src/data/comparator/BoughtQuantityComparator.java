package data.comparator;

import java.util.Comparator;


import data.Solution;
import data.Supplier;

/**
 *Questa classe fornisce il criterio di ordinamento sulla quantità totale di prodotti comperati.
 *
 */

public class BoughtQuantityComparator implements Comparator<Supplier> {
	/**
	 * Soluzione di un problema.
	 */
	Solution solution;
	
	/**
	 * Crea un oggetto BoughtQuantityComparator data la soluzione {@code sol}.
	 * @param sol
	 */
	public BoughtQuantityComparator(Solution sol) {
		this.solution = sol;
	}
	
	/**
	 * Questo metodo fornisce il criterio di ordinamento per oggetti {@code s1} e {@code s2} di tipo 
	 * {@code Supplier} <br> 
	 * secondo la quantità di prodotto comperato.
	 * @param s1
	 * @param s2
	 * @return -1 se {@code s1} è minore di {@code s2}, 0 se sono uguali, altrimenti 1.
	 */
	@Override
	public int compare(Supplier s1, Supplier s2) {
		int totalS1=solution.totalQuantityBought(s1.getId());
		int totalS2=solution.totalQuantityBought(s2.getId());
		if(totalS1<totalS2)
			return -1;
		else if (totalS1==totalS2)
			return 0;
		else
			return 1;
	}

}
