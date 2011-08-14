package data.comparator;

import java.util.Comparator;

import data.Solution;
import data.Supplier;

public class CurrentPriceComparator implements Comparator<Supplier> {
	/**
	 * Soluzione di un problema
	 */
	Solution solution;
	
	/**
	 * Intero che indica l'identificatore di un prodotto.
	 */
	int productId;
	
	/**
	 * Crea un oggetto CurrentPriceComparator data la soluzione {@code sol} e l'identificatore 
	 * di un prodotto {@code product}.
	 * @param sol
	 * @param productId
	 */
	public CurrentPriceComparator(Solution sol, int productId) {
		this.solution = sol;
		this.productId = productId;
	}
	
	/**
	 *Questo metodo fornisce il criterio di ordinamento per oggetti {@code s1} e {@code s2} di tipo 
	 *{@code Supplier}<br> 
	 *secondo il prezzo(scontato) praticato per un dato prodotto {@link #productId}
	 *@param s1
	 *@param s2
	 *@return -1 se il prezzo {@code s1} è minore di {@code s2}, 0 se sono uguali, altrimenti 1.
	 */
	@Override
	public int compare(Supplier s1, Supplier s2) {
		double priceS1 = s1.getDiscountedPrice(productId, solution.totalQuantityBought(s1.getId()));
		if(priceS1<0)
			priceS1=Double.MAX_VALUE;
		double priceS2 = s2.getDiscountedPrice(productId, solution.totalQuantityBought(s2.getId()));
		if(priceS2<0)
			priceS2=Double.MAX_VALUE;

		if (priceS1<priceS2)
			return -1;
		else if (priceS1==priceS2)
			return 0;
		else
			return 1;
	}
}
