package solvingalgorithm.temporizedalgorithm; 

import neighbourgenerator.DistancedNeighbourGenerator;
import data.Problem;
import data.Solution;
/**
 * Questa classe implementa la metaeuristica Variable Neighbourhood Search.
 *
 */
public class VNS extends TemporizedAlgorithm{
	private int kMax;
	private TemporizedAlgorithm afterShaking;
	private Solution currentSolution;
	private DistancedNeighbourGenerator generator;
	/**
	 * Numero di volte in cui far ripartire la ricerca. Questo numero
	 * non include la prima esecuzione. (Ad esempio, {@code restart=10} significa eseguire
	 * 11 main loop della VNS).
	 */
	private int restarts;
	private final int INFINITY = -1;
	
	private int increment=1;
	
	/**
	 * Costruisce un oggetto VNS.
	 * @param kMax - dimensione massima del vicinato durante lo shaking.
	 * @param afterShaking - algoritmo da eseguire dopo la fase di shaking (es. ricerca locale).
	 * @param generator - generatore di vicini da usare per lo shaking.
	 * @param problem - il problema da risolvere.
	 */
	public VNS(int kMax, TemporizedAlgorithm afterShaking, DistancedNeighbourGenerator generator, Problem problem){
		this.afterShaking=afterShaking;
		this.kMax=kMax;
		this.problem=problem;
		this.generator=generator;
		restarts=0;
	}
	
	/**
	 * Come {@link #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem)}, con in aggiunta 
	 * il parametro di tempo massimo ed il numero di volte in cui far ripartire la ricerca.
	 * @see #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem) 
	 * @see Timer
	 * @param kMax 
	 * @param afterShaking
	 * @param generator
	 * @param problem
	 * @param restarts - numero di ripartenze dell'algoritmo. Vedi {@link #restarts}.
	 * @param maximumTime - tempo massimo di esecuzione.
	 */
	public VNS(int kMax, TemporizedAlgorithm afterShaking, DistancedNeighbourGenerator generator, Problem problem, int restarts,
			int maximumTime){
		this(kMax, afterShaking, generator, problem);
		this.restarts = restarts;
		timer = new Timer(maximumTime);
	}
	

	/**
	 * Come {@link #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem, int, int)} ma accetta solo il tempo massimo, ed
	 * imposta infiniti restart di default.
	 * @see #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem)
	 * @see #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem, int, int)
	 * @param kMax
	 * @param afterShaking
	 * @param generator
	 * @param problem
	 * @param maximumTime
	 */
	public VNS(int kMax, TemporizedAlgorithm afterShaking, DistancedNeighbourGenerator generator, Problem problem,
			int maximumTime){
		this(kMax, afterShaking, generator, problem);
		this.restarts = INFINITY;
		timer = new Timer(maximumTime);
	}
	
	/**
	 * Come {@link #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem, int, int)} ma accetta solo il numero di restart
	 * e non imposta alcun timer.
	 * @see #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem)
	 * @see #VNS(int, TemporizedAlgorithm, DistancedNeighbourGenerator, Problem, int, int)
	 * 
	 * @param kMax
	 * @param afterShaking
	 * @param generator
	 * @param restarts
	 * @param problem
	 */
	public VNS(int kMax, TemporizedAlgorithm afterShaking, DistancedNeighbourGenerator generator, int restarts, Problem problem){
		this(kMax, afterShaking, generator, problem);
		this.restarts = restarts;
	}
	
	/**
	 * Esegue l'algoritmo Variable Neighbourhood Search sul problema a partire da {@code startSolution}.
	 * @return una soluzione la cui funzione obiettivo sia migliore o uguale a {@code startSolution}.
	 */
	public Solution execute(Solution startSolution){
		//se è stato impostato un timer, lo faccio partire.
		if(timer != null){
			timer.addObserver(this);
			timer.addObserver(afterShaking);
			startTimer();
		}
		
		currentSolution = startSolution;
		
		//eseguo restarts+1 VNS
		for(int i=0;(i<=restarts  ||  restarts==INFINITY) && !stop;i++){
			runVNS();
		}
		return currentSolution;
	}

	/**
	 * Main loop della VNS.
	 */
	private void runVNS(){
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
				k = 1;
			} else {
				k = k + increment; // aumento l'intorno
			}

		}
		System.out.println("Raggiunto kmax=" +(k-1));
	}
	
	/**
	 * Esegue lo shaking.
	 * @param k - dimensione dell'intorno di shaking.
	 * @return la soluzione trovata dopo l'operazione.
	 */
	private Solution shaking(int k){
		return generator.generate(currentSolution, k);
	}
	
	
	/**
	 * Questo metodo permette di cambiare il passo di incremento
	 * del parametro di shaking, di default pari a 1.<br>
	 * Ad esempio, impostando un incremento pari a 2, la
	 * VNS eseguirà gli shaking con {@code k} pari a 1, 3, 5, etc... 
	 * @param value - nuovo passo di incremento.
	 */
	public void setIncrement(int value){
		this.increment = value;
	}
}