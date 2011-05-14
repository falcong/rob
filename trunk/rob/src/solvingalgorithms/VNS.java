package solvingalgorithms;

import rob.Problem;
import rob.Solution;
import solutionhandlers.NeighbourGenerator;

public class VNS extends Algorithm {
	protected int kMax;
	protected Algorithm afterShaking;
	protected Solution currentSolution;
	protected NeighbourGenerator generator;
	private long maximumTime; //tempo massimo dopo cui arrestare la ricerca. Se disabilitato è =-1
	private final boolean verbose = true;
	//numero di volte in cui far ripartire la ricerca
	protected int restarts;
	
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		maximumTime=-1;
		restarts=0;
	}
	
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem, int restarts,int maximumTime){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		this.maximumTime=maximumTime;
		this.restarts=restarts;
	}
	
	public Solution execute(Solution startSolution){
		Solution solution=startSolution;
		long startTime=System.currentTimeMillis();
		
		for(int i=0;i<=restarts;i++){
			solution=runVNS(solution);
			if (maximumTime>0 && System.currentTimeMillis()-startTime>=maximumTime){
				System.err.println("Warning: La VNS sta impiegando più tempo di quello specificato. Termino e ritorno l'ultimo risultato.");
				return currentSolution;
			}
		}
		return solution;
	}

	private Solution runVNS(Solution startSolution) {
		currentSolution=startSolution;
		//System.out.println("Soluzione iniziale");
		//currentSolution.print();
		int k=1;
		while(k<=kMax) {
			if(verbose){
				System.out.println("k = "+k);
				System.out.println("\t"+currentSolution.getObjectiveFunction());
				System.out.flush();
			}
			Solution y=shaking(k);
			Solution nextSolution=afterShaking.execute(y);
			if(nextSolution.getObjectiveFunction()<currentSolution.getObjectiveFunction()){
				currentSolution=nextSolution; //move				
				k=1;
			} else {
				k++; // aumento l'intorno
			}

		}
		System.out.println("Raggiunto kmax=" +k);
		return currentSolution;
	}
	
	/*
	 * k = dimensione dell'intorno in cui effettuare lo shaking. La distanza del
	 * vicino generato è un numero casuale compreso tra 1 e k
	 */
	protected Solution shaking(int k){
		int shakingAmount=k;
		return generator.generate(currentSolution, shakingAmount);
	}

	public void setMaximumTime(long maximumTime) {
		this.maximumTime = maximumTime;
	}

	public long getMaximumTime() {
		return maximumTime;
	}
	
	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		generator.setProblem(problem);
		afterShaking.setProblem(problem);
	}


	
}
