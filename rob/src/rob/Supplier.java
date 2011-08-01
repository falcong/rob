package rob;

import rob.Solution;
import util.Utility;

public class Supplier {
	private int id;
	/*
	 * basePrices[k] = prezzo base del prodotto k; 
	 * basePrices[0] non viene utilizzato dunque basePrices.length = numProducts + 1
	 * baseprices[k] = -1 significa che il prodotto k non è presente
	 */
	private int basePrices[];
	/*
	 * contiene i prezzi dei vari prodotti per le varie fasce; 
	 * prices[k][r]=prezzo del prodotto k nella fascia r;
	 * prices[k][0]=prezzo base del prodotto k (non scontato)
	 * prices[k][0]=basePrices[k];
	 * prices[k][r]=-1 significa che il prodotto k non è presente;
	 * prices[0][r] non viene utilizzato;
	 */
	double prices[][];
	/*
	 * contiene le disponibilità dei vari prodotti; availability[k]=disponibilità
	 * del prodotto k-esimo; availability[k]=-1 significa che il prodotto non è 
	 * presente;
	 * availability[0] non viene utilizzato
	 */
	//numero di fasce di sconto esclusa la fascia 0-esima [quella senza sconto]
	private int numSegments;
	private int availability[];

	/*
	 * contiene gli estremi inferiori degli intervalli di sconto;
	 * [ lowerBounds[r]; lowerBounds[r+1]-1 ] è l'intervallo r-esimo;
	 * [ lowerBounds[0]; lowerBounds[1]-1 ] è l'intervallo 0-esimo in cui
	 * non viene applicato alcuno sconto (lowerBounds[0]=1).
	 */
	private int lowerBounds[];
	/*
	 * averagePrices[k] = media pesata dei prezzi del prodotto k nelle varie fasce
	 * averagePrices[0] non è usato
	 */
	//private double averagePrices[];
	
	private int numOfferedProducts;
	
	//t
	public Supplier(int id){
		this.id = id;
		this.basePrices = null;
		this.prices = null;
		numSegments = -1;
		this.availability = null;
		this.lowerBounds = null;
	}
	
	//t
	public void setBasePrices(int basePrices[]){
		this.basePrices = basePrices;
	}
	
	/*
	 * riempie la matrice prices[][] utilizzando basePrices;
	 * numBands = numero fasce di sconto escludendo la 0;
	 * discount[r] = sconto percentuale della fascia r
	 */
	//t
	public void setPrices(int numBands, int discounts[]){
		this.numSegments = numBands;
		discounts[0]=0;
		int numProducts = basePrices.length-1;
		
		prices = new double[numProducts+1][numBands+1];
		for(int i=0; i<=numProducts; i++){
			for(int j=0; j<=numBands; j++){
				prices[i][j] = -1;
			}
		}
		
		/*
		 * per ogni prodotto k che possiedo riempio la corrispondente riga
		 * prices[k][r]
		 */
		for(int k=1; k<=numProducts; k++){
			if(basePrices[k]!=-1){
				//possiedo il prodotto
				for(int r=0; r<=numBands; r++){
					//sconto il prezzo base
					double discontedPrice = basePrices[k]*(100-discounts[r])*0.01;
					//arrotondo al centesimo
					double roundedPrice = (double)(Math.round(discontedPrice*100))/100;
					prices[k][r] = roundedPrice;
				}
			}
		}
	}
	
/*	public void setPrices(double prices[][]){
		this.prices = prices;
	}*/
	
	//t
	public void setAvailability(int availability[]){
		this.availability = availability;
	}
	
	
	//t
	public void setLowerBounds(int lowerBounds[]) {
		this.lowerBounds = lowerBounds;
	}
	
	//t
	public int getId(){
		return id;
	}
	
	/*
	 * ritorna la disponibilità residua di product in base alla soluzione corrente
	 */
	//t
	public int getResidual(int product, Solution solution) {
		int residual=availability[product]-solution.getQuantity(id, product);
		return residual;
	}
	
	/*
	 * Metodo overloaded che accetta solo la matrice della soluzione
	 */
	public int getResidual(int product, int [][] solutionMatrix) {
		int residual=availability[product]- solutionMatrix[id][product];
		return residual;
	}
	
	//t
	public int getAvailability(int product) {
		return availability[product];
	}
	
	//restituisce true se la disponibilità di product>=0 (false altrimenti). 
	//In pratica vede se il fornitore tratta quel tal prodotto o no, quindi restituisce true anche se il prodotto è esaurito
	//t
	public boolean checkAvailability(int product) {
		if (availability[product]<0)
			return false;
		else
			return true;
	}
	
	/*
	 * product = id prodotto;
	 * quantity = quantità totale aquistata (di tutti i prodotti)
	 * [se quantity = 0 restituisce -1] 
	 */
	//t
	public double getDiscountedPrice(int product,int quantity){
		if(quantity==0  ||  prices[product][0]==-1){
			return -1;
		}
		int i;
		for (i=1; i<=lowerBounds.length-1;i++){
			if (quantity<lowerBounds[i])
				return prices[product][i-1];				
		}
		//restituisco il prezzo dell'ultima fascia 
		return prices[product][i-1];
	}
	
	//t
	public void print(){
		System.out.println("Fornitore "+id);
		
		//prezzi base
		if(basePrices!=null){
			System.out.println("Prezzi base:");
			for(int i=1; i<=basePrices.length-1; i++){
				System.out.print("p"+i+"="+basePrices[i]+"\t\t");
			}
			System.out.println("\n");
		}
		
		//matrice dei prezzi (prodotti*fasce)
		if(prices!=null){
			System.out.println("Matrice dei prezzi (prodotti*fasce-sconto):");
			Utility.printMatrix2D(prices, "p", "f", 1, 0, 2, 3, 2, 2);
		}
		
		//availability
		if(availability!=null){
			System.out.println("Disponibilità dei prodotti:");
			for(int i=1; i<=availability.length-1; i++){
				System.out.print("d"+i+"="+availability[i]+"\t\t");
			}
			System.out.println("\n");
		}
		
		//lower bounds
		if(lowerBounds!=null){
			System.out.println("Lower Bounds delle Fasce di Sconto:");
			for(int i=0; i<=lowerBounds.length-1; i++){
				System.out.print("lb"+i+"="+lowerBounds[i]+"\t\t");
			}
			System.out.println("\n");
		}
	}

	public double getBasePrice(int product) {
		return basePrices[product];
	}

//	public double getAveragePrice() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	public double[][] getPrices(){
		return prices;
	}

	public int getNumSegments() {
		return numSegments;
	}
	
	public int[] getLowerBounds(){
		return lowerBounds;
	}

	public int[] getAvailabilities() {
		return availability;
	}
	
	/*
	 * restituisce il prezzo che possiede product in segment
	 */
	public double getPrice(int product, int segment) {
		return prices[product][segment];
	}

	public void setNumOfferedProducts(int numOfferedProducts) {
		this.numOfferedProducts = numOfferedProducts;
	}

	public int getNumOfferedProducts() {
		return numOfferedProducts;
	}
	
	//restituisce il numero della fascia di sconto attiva comprando totalQuantity prodotti 
	public int activatedSegment(int totalQuantity){
		int activatedSegment = 0;
		while(totalQuantity>=lowerBounds[activatedSegment+1]){
			activatedSegment++;
			if(activatedSegment==numSegments){
				break;
			}
		}
		
		return activatedSegment;
	}
	
	
	//restituisce il numero della fascia di sconto attiva in base a solution 
	public int activatedSegment(Solution solution){
		return activatedSegment(solution.totalQuantityBought(id));
	}
	
	
	//restituisce la disponibilità residua totale di tutti i prodotti
	public int getTotalResidualAvailability(Solution s) {
		int sum = 0;
		for (int i=1; i<availability.length; i++){
			int currentAvailability=availability[i];
			if (currentAvailability == -1)
				currentAvailability=0;
			sum += currentAvailability - s.getQuantity(id,i);
		}
		return sum;
	}

	
	/*
	 * calcola di quanto la quantità totale di acquisti presso il fornitore deve essere aumentata 
	 * per far aumentare di 1 la fascia di sconto attiva;
	 * se il fornitore si trova già nell'ultima fascia di sconto viene fornita la disponibilità residua
	 */
	public int quantityToIncreaseSegment(Solution solution){
		//quantità totale acquistata presso il fornitore
		int totalQuantity = solution.totalQuantityBought(id);
		int segment = activatedSegment(totalQuantity);
		if(segment==numSegments){
			return getTotalResidualAvailability(solution);
		}
		return lowerBounds[segment+1]-totalQuantity;
	}

	public int quantityToNotDecreaseSegment(Solution solution) {
		int totalQuantity = solution.totalQuantityBought(id);
		if (totalQuantity==0)
			return 0;
		int segment = activatedSegment(totalQuantity);
		return totalQuantity-lowerBounds[segment];
	}
}
