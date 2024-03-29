package neighbourgenerator.bansupplier.emptyingstrategy;

import data.IdList;
import data.Problem;
import data.Solution;
import data.Supplier;

public class LowestPriceEmptyingStrategy extends SupplierEmptyingStrategy {
	/**
	 * 
	 * @param problem il problema in cui applicare la strategia LowestpriceEmptyingStrategy per un certo 
	 * BanSupplierNeighbourGenerator.
	 */
	public LowestPriceEmptyingStrategy(Problem problem) {
		super(problem);
	}

	
	/**
	 * Questo metodo cerca di svuotare intere righe di soluzione corrispondenti ai
	 * fornitori contenuti in list secondo questo criterio:<br>
	 * Per ciascun prodotto p del fornitore da svuotare, l'algoritmo mette in ordine 
	 * i restanti fornitori secondo il prezzo corrente di p (considerando anche le fasce
	 * di sconto) e quindi sposta gli acquisti del prodotto p verso i fornitori
	 * col prezzo più basso.<br>
	 * Se gli altri mercati non sono sufficienti a contenere tutti i prodotti
	 * delle righe da svuotare, sposterà tutto quello è possibile spostare mantenendo l'ammissibilità,
	 * seguendo l'ordine di list.
	 */
	/**
	 * 
	 */
	@Override
	public void emptySuppliers(IdList list, Solution solution) {
		this.list=list;
		this.solution=solution;
		for(int i=0; i<list.getSize(); i++){
			emptySupplier(list.getId(i));
		}
      }
	

	private void emptySupplier(int supplierId) {
	      int numProducts=problem.getNumProducts();
	      //ciclo sui prodotti e svuoto cella per cella
	      for(int product=1; product<=numProducts; product++){
	              if(solution.getQuantity(supplierId, product) == 0)
	                      continue;
	              Supplier [] orderedSuppliers = problem.sortByCurrentPrice(product, solution);
	              for(int s=1; s<=problem.getDimension(); s++){
	                      Supplier currentSupplier = orderedSuppliers[s];
	                      int targetSupId = currentSupplier.getId();
	                      if(list.contains(targetSupId))
	                              continue;
	                      int residual = currentSupplier.getResidual(product, solution);
	                      if (residual<=0)
	                              continue;
	                      int quantity=Math.min(residual, solution.getQuantity(supplierId, product));
	                      solution.moveQuantity(product, supplierId, targetSupId, quantity);
	                      }
	              }
	}
}
