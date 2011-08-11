/*
 * m = n° tot fornitori
 * ordina i fornitori in base al numero totale di prodotti acquistati (decrescente)
 * fra i primi m/2 fornitori ne estrae k casuali
 * ogni fornitore viene svuotato così:
 * considero un prodotto alla volta e cerco di spostare il prodotto dove costa meno
 * (fascio di sconto attualmente attiva)
 */

package neighbourgenerator;

import java.util.HashSet;

import data.Problem;
import data.Solution;
import data.Supplier;


public class DirectionedBanNeighbourGenerator extends
		BanFullNeighbourGenerator {

	public DirectionedBanNeighbourGenerator(Problem problem) {
		super(problem);
		this.problem=problem;
	}

	/*
	 * Cerca per quanto possibile di svuotare completamente il fornitore bannedSupId
	 * prendendo i fornitori in ordine di prezzo.
	 * Se gli altri mercati non sono sufficienti a contenere i suoi prodotti svuota tutto
	 * quello è possibile spostare mantenendo l'ammissibilità
	 */
	@Override
	protected void emptySupplier(int bannedSupId, HashSet<Integer> banned, Solution solution) {
		int numProducts=problem.getNumProducts();
		//ciclo sui prodotti e svuoto cella per cella
		for(int p=1;p<=numProducts;p++){
			if(solution.getQuantity(bannedSupId, p)==0)
				continue;
			Supplier [] orderedSuppliers = problem.sortByCurrentPrice(p, solution);
			for(int s=1; s<=problem.getDimension();s++){
				Supplier currentSupplier = orderedSuppliers[s];
				int currentSupId = currentSupplier.getId();
				if(banned.contains(currentSupId))
					continue;
				int residual = currentSupplier.getResidual(p, solution);
				if (residual<=0)
					continue;
				int quantity=Math.min(residual, solution.getQuantity(bannedSupId, p));
				solution.moveQuantity(p, bannedSupId, currentSupId, quantity, problem);
			}
		}
	}
}












