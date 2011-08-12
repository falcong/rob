package neighbourgenerator;

import data.Problem;
import data.Solution;

public class RandomEmptyingStrategy extends SupplierEmptyingStrategy {

	public RandomEmptyingStrategy(Problem problem) {
		super(problem);
	}
	/**
	 * Questo metodo cerca di svuotare intere righe di soluzione corrispondenti ai
	 * i fornitori contenuti in list secondo questo criterio:<br>
	 * Toglie gli acquisti dai fornitori in list spostandoli negli altri fornitori
	 * partendo da un fornitore a caso seguendo l'odine specificato nel problema.<br>
	 * Se gli altri mercati non sono sufficienti a contenere i tutti i prodotti
	 * delle righe da svuotare, sposterà tutto quello è possibile spostare mantenendo l'ammissibilità,
	 * seguendo l'ordine di list.
	 */
	@Override
	void emptySuppliers(IdList list, Solution solution) {
		this.list=list;
		this.solution=solution;
		for(int i=0;i<list.getSize();i++){
			emptySupplier(list.getId(i));
		}
		
	}
	

	private void emptySupplier(int supplierId){
		int startSupplier = problem.getRandomSupplierId(list.getHashSet());
		int numSuppliers=problem.getDimension();
		int targetSupplier=startSupplier;
		boolean done=false;
		while (!done) {			
			for(int p=1;p<=problem.getNumProducts();p++){
				//quanto sto comprando attualmente del prodotto p presso il fornitore supplierId
				int currentlyBuying=solution.getQuantity(supplierId,p);
				if (currentlyBuying==0)
					continue; //allora non sto comprando niente, passo al prodotto successivo
				
				//disponibilità residua del targetSupplier
				int residual=problem.getSupplier(targetSupplier).getResidual(p,solution);
				//se ha disponibilità residua, sposto tutto il possibile da supplierId a targetSupplier
				if(residual>0)
					solution.moveQuantity(p,supplierId,targetSupplier,Math.min(currentlyBuying,residual),problem);				
			}
			
			//incremento ciclico del supplier
			targetSupplier = targetSupplier%numSuppliers+1;
			
			//il target supplier non va bene come destinazione se è contenuto nella banned list
			//quindi lo incremento ancora prima di ricominciare il ciclo
			while (list.contains(targetSupplier) && targetSupplier!=startSupplier) {
				targetSupplier = targetSupplier%numSuppliers+1;
			}
			//controllo se ho finito il giro
			if (targetSupplier==startSupplier)
				done=true;			
		}
	}
}
