package solvingalgorithm.temporizedalgorithm.localsearch;


import neighbourgenerator.NeighbourGenerator;
import data.Problem;
import data.Solution;
import solvingalgorithm.temporizedalgorithm.TemporizedAlgorithm;
import solvingalgorithm.temporizedalgorithm.Timer;
/**
 * Questa classe implementa un algoritmo di ricerca locale. 
 *
 */
public class LocalSearch extends TemporizedAlgorithm{
	
	private ExplorationStrategy strategy;
	
	/**
	 * Questa enumerazione serve per impostare la strategia di esplorazione
	 * degli intorni nella ricerca locale quando viene costruito l'oggetto LocalSearch.
	 */
	public enum StrategyName{
		/**
		 * Strategia First Improvement
		 * @see FirstImprovementStrategy
		 */
		FIRST_IMPROVEMENT,
		/**
		 * Strategia Best Improvement
		 * @see BestImprovementStrategy
		 */
		BEST_IMPROVEMENT 
	}
	
	private int maxNeighboursNumber; 
	private int maxStepsNumber; //numero massimo di passi prima di arrestare
	private NeighbourGenerator generator;
	
	/**
	 * Costruisce un oggetto LocalSearch.
	 * @param maxNeighboursNumber - massimo numero di vicini da esaminare ad ogni passo
	 * @param maxStepsNumber - massimo numero di passi prima di arrestare la ricerca
	 * @param explorationStrategy - strategia di esplorazione dell'intorno, vedi {@link StrategyName}.
	 * @param generator - algoritmo di generazione dei vicini.
	 * @param problem - il problema da risolvere.
	 */
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, StrategyName explorationStrategy,
			NeighbourGenerator generator, Problem problem){
		this.problem = problem;
		this.maxNeighboursNumber = maxNeighboursNumber;
		this.maxStepsNumber = maxStepsNumber;
		this.generator = generator;
		
		switch(explorationStrategy) {
		case FIRST_IMPROVEMENT: strategy =new FirstImprovementStrategy();break;
		case BEST_IMPROVEMENT: strategy =new BestImprovementStrategy();break;
		}
		
	}
	
	/**
	 * Costruisce un oggetto LocalSearch ricevendo in ingresso anche il tempo massimo di esecuzione.
	 * @see #LocalSearch(int, int, StrategyName, NeighbourGenerator, Problem)
	 * @see Timer
	 * @param maxNeighboursNumber
	 * @param maxStepsNumber
	 * @param successorChoice
	 * @param generator
	 * @param problem
	 * @param maxTime - tempo massimo di esecuzione espresso in secondi.
	 */
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, StrategyName successorChoice,
			NeighbourGenerator generator, Problem problem, int maxTime){
		this(maxTime, maxTime, successorChoice, generator, problem);
		timer = new Timer(maxTime);
	}
	
	/**
	 * Esegue la ricerca locale a partire da {@code startSolution}.
	 * @return una soluzione la cui funzione obiettivo sia migliore o uguale a {@code startSolution}.
	 */
	public Solution execute(Solution startSolution){
		if(timer != null){
			timer.addObserver(this);
			startTimer();
		}
		
		Solution currentSolution=startSolution;
		int step;
		for (step=1; step<=maxStepsNumber; step++) {
			//controllo che il timer non mi abbia detto di fermarmi
			if(stop){
				return currentSolution;
			}
			
			Solution successor;
			successor = strategy.explore(currentSolution, maxNeighboursNumber, generator);
			if(successor != null){ 
				//ho un successore, continuo la ricerca
				currentSolution = successor;
			}
			else{
				//l'esplorazione del vicinato ha ritornato null, siamo in un minimo locale
				return currentSolution;
			}
		}
		return currentSolution; //arrivo qui se esaurisco il numero di passi
	}

}
