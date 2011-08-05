package solvingalgorithm; 

import neighbourgenerator.NeighbourGenerator;
import data.Problem;
import data.Solution;
import util.Utility;

public class VNS extends TemporizedAlgorithm{
	protected int kMax;
	protected Algorithm afterShaking;
	protected Solution currentSolution;
	protected NeighbourGenerator generator;
	//numero di volte in cui far ripartire la ricerca
	protected int restarts;
	final int INFINITY = -1;
	int increment=1;
	
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		restarts=0;
	}
	
	//maximumTime espresso in secondi
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem, int restarts,
			int maximumTime){
		this(kMax, afterShaking, generator, problem);
		this.restarts = restarts;
		timer = new Timer(maximumTime);
	}
	
	//maximumTime espresso in secondi
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem,
			int maximumTime){
		this(kMax, afterShaking, generator, problem);
		this.restarts = INFINITY;
		timer = new Timer(maximumTime);
	}
	
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, int restarts, Problem problem){
		this(kMax, afterShaking, generator, problem);
		this.restarts = restarts;
	}
	
	public Solution execute(Solution startSolution){
		if(timer!=null){
			timer.addObserver(this);
			timer.start();
		}
		
		currentSolution = startSolution;
		
		//eseguo restarts+1 VNS
		for(int i=0;i<=restarts  ||  restarts==INFINITY;i++){
			runVNS();
		}
		return currentSolution;
	}

	protected void runVNS(){
		int k=1;
		while(k<=kMax){
			//controllo di non avere superato tempo max
			if(stop){
				return;
			}
			
			Solution y=shaking(k);
			
			Solution nextSolution=afterShaking.execute(y);
			
			if(nextSolution.getObjectiveFunction()<currentSolution.getObjectiveFunction()){
				currentSolution=nextSolution; //move
				k=1;
			} else {
				k=k+increment; // aumento l'intorno
			}

		}
		System.out.println("Raggiunto kmax=" +k);
	}
	

	/*
	 * k = dimensione dell'intorno in cui effettuare lo shaking. La distanza del
	 * vicino generato è un numero casuale compreso tra 1 e k
	 */
	protected Solution shaking(int k){
		return generator.generate(currentSolution, k);
	}
		
	public void setIncrement(int value){
		this.increment=value;
	}
}