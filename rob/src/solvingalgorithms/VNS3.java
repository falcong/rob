package solvingalgorithms;

import rob.Problem;
import rob.Solution;
import solutionhandlers.NeighbourGenerator;

public class VNS3 extends VNS {
	int threshold = 30;
	Solution bestSolution;
	
	public VNS3(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem){
		super(kMax, afterShaking, generator, problem);
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		//non ho limiti temporali
		finalTime = -1;
		afterShaking.setFinalTime(finalTime);
		afterShaking.setStartTime(System.currentTimeMillis());
		restarts=0;
	}
	
	//maximumTime espresso in secondi
	public VNS3(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem, int restarts,
			int maximumTime){
		super(kMax, afterShaking, generator, problem, restarts, maximumTime);
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
			if(nextSolution.getObjectiveFunction()<bestSolution.getObjectiveFunction()){
				currentSolution=nextSolution; //move
				bestSolution = nextSolution;
				//stampa s0
				printS0();
				k=1;
			} else {
				if(k==threshold  && nextSolution.getObjectiveFunction()<1.2*currentSolution.getObjectiveFunction()){
					bestSolution = currentSolution;
					currentSolution = nextSolution;
					k = 1;
				}else{
					k=k+increment; // aumento l'intorno
				}
			}

		}
		System.out.println("Raggiunto kmax=" +k);
	}
	
	
	public Solution execute(Solution startSolution){
		if(restarts<=-1 && finalTime<=0){
			throw new Error("VNS lanciata senza alcun limite");
		}
		
		currentSolution = startSolution;
		
		
		for(int i=0;i<=restarts  ||  restarts==-1;i++){
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return bestSolution;
			}
			//eseguo 1 main loop
			runVNS();
		}
		return bestSolution;
	}
}
