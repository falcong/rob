package solvingalgorithms;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.NeighbourGenerator;

public class VNS extends Algorithm {
	protected int kMax;
	protected Algorithm afterShaking;
	protected Solution currentSolution;
	protected NeighbourGenerator generator;
	private long maximumTime; //tempo massimo dopo cui arrestare la ricerca. Se disabilitato è =-1
	//numero di volte in cui far ripartire la ricerca
	protected int restarts;
	/*
	 * indica quali informazioni stampare
	 * info = 0 non stampa nulla
	 * info = 1 stampa label0, label1, label2 (per statistiche 5) in corrispondenza di s0, s1 e s2 
	 */
	int info = 0;
	String label;
	String outputFile;
	Solution cplex;
	
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
		//stampa s0
		printS0();
		int k=1;
		while(k<=kMax) {
			Solution y=shaking(k);
			//stampa s1
			printS1(y, k);
			Solution nextSolution=afterShaking.execute(y);
			//stampa s2
			printS2(nextSolution);
			if(nextSolution.getObjectiveFunction()<currentSolution.getObjectiveFunction()){
				currentSolution=nextSolution; //move
				//stampa s0
				printS0();
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

	
	public void setStatistics(int info, int statistics, String label){
		this.info = info;
		outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistics+".txt";
		this.label = label;
	}
	
	public void setCplex(Solution sol){
		cplex = sol;
	}
		
	
	private void printS0(){
		switch(info){
			case 0: 
				break;
			case 1:
				//label + 0 + time + 1 tab + fo + 1tab + 4 tab + new line
				Utility.write(outputFile, label+0+"\t"+(int)System.currentTimeMillis()/1000+"\t"+
						currentSolution.getObjectiveFunction()+"\t\t\t\t\t"+
						currentSolution.calcDistance(cplex)+
						System.getProperty("line.separator"));
				break;
		}
	}
	
	
	private void printS1(Solution sol, int k){
		switch(info){
			case 0: 
				break;
			case 1:
				//label + 1 + time + 1 tab + fo + 1tab + k + 1 tab + 3 tab + new line
				Utility.write(outputFile, label+1+"\t"+(int)System.currentTimeMillis()/1000+"\t"+
						sol.getObjectiveFunction()+"\t"+k+"\t\t\t\t"+
						sol.calcDistance(cplex)+
						System.getProperty("line.separator"));
				break;
		}
	}
	
	
	private void printS2(Solution sol){
		switch(info){
			case 0: 
				break;
			case 1:
				//label + 2 + time + 1 tab + fo + 1tab + 4 tab + new line
				Utility.write(outputFile, label+2+"\t"+(int)System.currentTimeMillis()/1000+"\t"+
						sol.getObjectiveFunction()+"\t\t\t\t\t"+
						sol.calcDistance(cplex)+
						System.getProperty("line.separator"));
				break;
		}
	}
}
