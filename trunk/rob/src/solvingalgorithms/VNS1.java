package solvingalgorithms;

import rob.Problem;
import rob.Solution;
import solutionhandlers.NeighbourGenerator;

public class VNS1 extends VNS{ 
	protected int firstIncrement;
	protected int secondIncrement;

	public VNS1(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem){
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
	public VNS1(int kMax, Algorithm afterShaking, NeighbourGenerator generator, Problem problem,
			int restarts,int maximumTime){
		super(kMax, afterShaking, generator, problem, restarts,maximumTime);
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
			if(nextSolution.getObjectiveFunction()<currentSolution.getObjectiveFunction()){
				currentSolution=nextSolution; //move
				//stampa s0
				printS0();
				k=1;
			} else {
				if(k==1){
					k = 1 + firstIncrement;
				}else{
					k += secondIncrement;
				}
				k=k+increment; // aumento l'intorno
			}

		}
		System.out.println("Raggiunto kmax=" +k);
	}
	
	
	public void setFirstIncrement(int increment){
		firstIncrement = increment;
	}
	
	public void setSecondIncrement(int increment){
		secondIncrement = increment;
	}
}
