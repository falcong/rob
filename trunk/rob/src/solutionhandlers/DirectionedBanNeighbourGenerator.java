/*
 * m = n° tot fornitori
 * ordina i fornitori in base al numero totale di prodotti acquistati (decrescente)
 * fra i primi m/2 fornitori ne estrae k casuali
 * ogni fornitore viene svuotato così:
 * considero un prodotto alla volta e cerco di spostare il prodotto dove costa meno
 * (fascio di sconto attualmente attiva)
 */

package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;

public class DirectionedBanNeighbourGenerator extends
		BanFullNeighbourGenerator {

	public DirectionedBanNeighbourGenerator(Problem problem) {
		super(problem);
		this.problem=problem;
	}

	/*
	 * Cerca per quanto possibile di svuotare completamente il fornitore bannedSupId
	 * Se gli altri mercati non sono sufficienti a contenere i suoi prodotti svuota tutto
	 * quello è possibile spostare mantenendo l'ammissibilità
	 */
	@Override
	protected void emptySupplier(int bannedSupId, HashSet<Integer> banned, Solution solution) {
		int numProducts=problem.getNumProducts();
		//ciclo sui prodotti e svuoto cella per cella
		for(int p=1;p<=numProducts;p++){
			int quantityToMove = solution.getQuantity(bannedSupId, p);
			if( quantityToMove==0)
				continue;
			Supplier [] orderedSuppliers = problem.sortByCurrentPrice(p, solution);
			for(int s=1; s<=problem.getDimension();s++){
				Supplier currentSupplier = orderedSuppliers[s];
				int currentSupId = currentSupplier.getId();
				if(currentSupId==bannedSupId ||banned.contains(currentSupId))
					continue;
				int residual = currentSupplier.getResidual(p, solution);
				if (residual<=0)
					continue;
				int quantity=Math.min(residual, quantityToMove);
				solution.moveQuantity(p, bannedSupId, currentSupId, quantity, problem);
				quantityToMove-=quantity;
			}
		}
	}
}
