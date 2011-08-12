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


public class DirectionedBanNeighbourGenerator extends BanSupplierNeighbourGenerator {
		


public DirectionedBanNeighbourGenerator(Problem problem) {
	super(problem, new LowestPriceEmptyingStrategy(problem), new FullestFirstOrderStrategy(problem));
	}
}










