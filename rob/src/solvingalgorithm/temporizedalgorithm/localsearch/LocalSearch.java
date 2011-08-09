package solvingalgorithm.temporizedalgorithm.localsearch;


import neighbourgenerator.NeighbourGenerator;
import data.Problem;
import data.Solution;
import solvingalgorithm.temporizedalgorithm.TemporizedAlgorithm;
import solvingalgorithm.temporizedalgorithm.Timer;

public class LocalSearch extends TemporizedAlgorithm{
	
	ExplorationStrategy strategy;
	
	public enum SuccessorChoiceMethod{
		FIRST_IMPROVEMENT, BEST_IMPROVEMENT 
	}
	
	private int maxNeighboursNumber; //Numero massimo di vicini da esplorare
	private int maxStepsNumber; //numero massimo di passi prima di arrestare
	private NeighbourGenerator generator;
	
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, SuccessorChoiceMethod successorChoice,
			NeighbourGenerator generator, Problem problem){
		this.problem=problem;
		this.maxNeighboursNumber=maxNeighboursNumber;
		this.maxStepsNumber=maxStepsNumber;
		//this.successorChoice=successorChoice;
		this.generator= generator;
		switch(successorChoice) {
		case FIRST_IMPROVEMENT: strategy =new FirstImprovementStrategy();break;
		case BEST_IMPROVEMENT: strategy =new BestImprovementStrategy();break;
		}
		
	}
	
	//maximumTime espresso in secondi
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, SuccessorChoiceMethod successorChoice,
			NeighbourGenerator generator, Problem problem, int maxTime){
		this(maxTime, maxTime, successorChoice, generator, problem);
		timer = new Timer(maxTime);
	}
	
	public Solution execute(Solution startSolution){
		if(timer!=null){
			timer.addObserver(this);
			startTimer();
		}
		
		Solution currentSolution=startSolution;
		int step;
		for (step=1;step<=maxStepsNumber;step++) {
			//controllo che il timer non mi abbia detto di fermarmi
			if(stop){
				return currentSolution;
			}
			
			Solution successor;
			successor = strategy.explore(currentSolution, maxNeighboursNumber, generator);
			if(successor!=null){ 
				//ho un successore, continuo la ricerca
				currentSolution=successor;
			}
			else{
				//l'esplorazione del vicinato ha ritornato null, siamo in un minimo locale
				return currentSolution;
			}
		}
		return currentSolution; //arrivo qui se esaurisco il numero di passi
	}

}













