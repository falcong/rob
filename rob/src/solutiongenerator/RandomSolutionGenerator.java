package solutiongenerator;

import java.util.Arrays;
import java.util.HashSet;
import data.Problem;
import data.Solution;
import data.Supplier;


public class RandomSolutionGenerator extends SolutionGenerator {

	/**
	 * Crea un generatore di soluzione casuale per il problema problem.
	 */
	public RandomSolutionGenerator(Problem problem) {
		super(problem);
	}

	/**
	 * Genera la soluzione.
	 */
	public Solution generate() {
		
		int purchased[][] = new int[problem.getDimension() + 1][problem.getNumProducts() + 1]; 
		// dimension=numero fornitori
		
		// Riempio la matrice purchased di zeri
		for (int supId = 0; supId <= problem.getDimension(); supId++) {
			Arrays.fill(purchased[supId], 0);
		}
		/*
		 * Questa blacklist serve ad evitare di ripescare un fornitore che non ha
		 * disponibilità di un certo prodotto
		 */
		HashSet<Integer> suppBlacklist = new HashSet<Integer>();

		for (int prodId = 1; prodId <= problem.getNumProducts(); prodId++) {
			int boughtQuantity = 0;
			while (boughtQuantity < problem.getDemand()[prodId] && suppBlacklist.size() < problem.getDimension()) {
				Supplier supplier = problem.getRandomSupplier(suppBlacklist);
				if (supplier.getResidual(prodId, purchased) <= 0) {
					// aggiungo il supplier in blacklist se non ha disponibilità
					// del prodotto prodId
					suppBlacklist.add(supplier.getId());
					continue;
				}
				int quantity = calcQuantity(purchased, prodId, boughtQuantity, supplier);
				boughtQuantity = boughtQuantity + quantity;
				purchased[supplier.getId()][prodId] = purchased[supplier.getId()][prodId] + quantity;
			}

			if (suppBlacklist.size() == problem.getDimension()) {
				throw new Error("Impossibile soddisfare le condizioni! Controlla l'ammissibilità dei dati.");
			}
			suppBlacklist.clear();
		}
		Solution solution = new Solution(purchased, problem);
		return solution;
	}

	private int calcQuantity(int [][] solutionMatrix, int prodId, int boughtQuantity, Supplier supplier) {
		int quantity = Math.min(problem.getDemand()[prodId] - boughtQuantity,
				1 + (int) (Math.random() * supplier.getResidual(prodId, solutionMatrix)));
		return quantity;
	}

}
