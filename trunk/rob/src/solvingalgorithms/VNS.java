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
	
	int increment=1;
	
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		//non ho limiti temporali
		finalTime = -1;
		afterShaking.setFinalTime(finalTime);
		//TODO startTime non deve essere settato
		afterShaking.setStartTime(System.currentTimeMillis());
		restarts=0;
	}
	
	//maximumTime espresso in secondi
	public VNS(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem, int restarts,int maximumTime){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		if(maximumTime==-1){
			finalTime = -1;
		}else{
			finalTime = System.currentTimeMillis()+maximumTime*1000;
		}
		afterShaking.setFinalTime(finalTime);
		this.setStartTime(System.currentTimeMillis());
		this.restarts=restarts;
	}
	
	public Solution execute(Solution startSolution){
		if(restarts<=-1 && finalTime<=0){
			throw new Error("VNS lanciata senza alcun limite");
		}
		
		currentSolution = startSolution;
		
		
		for(int i=0;i<=restarts  ||  restarts==-1;i++){
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return currentSolution;
			}
			//eseguo 1 main loop
			runVNS();
		}
		return currentSolution;
	}

	protected void runVNS(){
		//stampa s0
		printS0();
		int k=1;
		while(k<=kMax){
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return;
			}
			
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
	
	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		generator.setProblem(problem);
		afterShaking.setProblem(problem);
	}

	
	public void setStatistics(int info, String outputFile, String label){
		this.info = info;
		this.outputFile = outputFile;
		this.label = label;
	}
	
	public void setCplex(Solution sol){
		cplex = sol;
	}
		
	
	protected void printS0(){
		switch(info){
			case 0: 
				break;
			case 1:
				//tempo trascorso in secondi dal lancio della VNS
				long elapsedTime = (System.currentTimeMillis()-startTime)/1000;
				//label + 0 + time + 1 tab + fo + 1tab + 4 tab + new line
				Utility.write(outputFile, label+0+"\t"+elapsedTime+"\t"+
						currentSolution.getObjectiveFunction()+"\t\t\t\t"+
						//currentSolution.calcDistance(cplex)+
						System.getProperty("line.separator"));
				//console
				System.out.println(label+0+"\t"+currentSolution.getObjectiveFunction());
				break;
		}
	}
	
	
	protected void printS1(Solution sol, int k){
		switch(info){
			case 0: 
				break;
			case 1:
				//tempo trascorso in secondi dal lancio della VNS
				long elapsedTime = (System.currentTimeMillis()-startTime)/1000;
				//label + 1 + time + 1 tab + fo + 1tab + k + 1 tab + 3 tab + new line
				Utility.write(outputFile, label+1+"\t"+elapsedTime+"\t"+
						sol.getObjectiveFunction()+"\t"+k+"\t\t\t"+
						//sol.calcDistance(cplex)+
						System.getProperty("line.separator"));
				//console
				System.out.println(label+1+"\t"+sol.getObjectiveFunction());
				break;
		}
	}
	
	
	protected void printS2(Solution sol){
		switch(info){
			case 0: 
				break;
			case 1:
				//tempo trascorso in secondi dal lancio della VNS
				long elapsedTime = (System.currentTimeMillis()-startTime)/1000;
				//label + 2 + time + 1 tab + fo + 1tab + 4 tab + new line
				Utility.write(outputFile, label+2+"\t"+elapsedTime+"\t"+
						sol.getObjectiveFunction()+"\t\t\t\t"+
						//sol.calcDistance(cplex)+
						System.getProperty("line.separator"));
				//console
				System.out.println(label+2+"\t"+sol.getObjectiveFunction());
				break;
		}
	}
	
	public void setIncrement(int value){
		this.increment=value;
	}

	public void setFinalTime(long finalTime){
		this.finalTime = finalTime;
		afterShaking.setFinalTime(finalTime);
	}
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
		afterShaking.setStartTime(startTime);
	}
}