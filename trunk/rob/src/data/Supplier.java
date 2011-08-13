package data;

import data.Solution;

public class Supplier {
	
	private static final int NO_PRICE = -1;

	/**
	 * Identifica univocamente un fornitore
	 */
	private int id;
	
	/**
	 * Questo valore è usato per indicare che un prodotto non è disponibile presso il fornitore. 
	 */
	public final static int PRODUCT_NOT_PRESENT = NO_PRICE;
	
	/**
	 * Matrice dei prezzi di base.<br>
	 * <ul>
	 * <li>{@code basePrices[k]} = prezzo base del prodotto k;</li> 
	 * <li>{@code basePrices[0]} non viene utilizzato, dunque {@code basePrices.length = numProducts + 1};</li>
	 * <li>{@code baseprices[k]} = {@value #PRODUCT_NOT_PRESENT} significa che il prodotto k non è presente.</li>
	 * </ul>
	 */
	private int basePrices[];
	
	/**
	 * Contiene i prezzi dei vari prodotti per le varie fasce; 
	 * <ul>
	 * <li>{@code prices[k][r]}=prezzo del prodotto k nella fascia r;</li>
	 * <li>{@code prices[k][0]}=prezzo base del prodotto k (non scontato)</li>
	 * <li>{@code prices[k][0]}=basePrices[k];</li>
	 * <li>{@code prices[k][r]}= {@value #PRODUCT_NOT_PRESENT} significa che il prodotto k non è presente;</li>
	 * <li>{@code prices[0][r]} non viene utilizzato.</li>
	 * <u>
	 */
	double prices[][];
	
	
	/**
	 * Contiene le disponibilità dei vari prodotti;
	 * <ul> 
	 * <li>availability[k]=disponibilità del prodotto k-esimo;</li> 
	 * <li>availability[k]=-1 significa che il prodotto non è presente;</li>
	 * <li>availability[0] non viene utilizzato</li>
	 */
	private int availability[];

	/**
	 * Numero di fasce di sconto esclusa la fascia 0-esima (quella senza sconto).
	 */
	private int numSegments;
	
	/**
	 * Contiene gli estremi inferiori degli intervalli di sconto;
	 * <ul>
	 * <li>{@code [ lowerBounds[r]; lowerBounds[r+1]-1 ]} è l'intervallo r-esimo;</li>
	 * <li>{@code [ lowerBounds[0]; lowerBounds[1]-1 ]} è l'intervallo 0-esimo in cui
	 * non viene applicato alcuno sconto ({@code lowerBounds[0]=1}).</li>
	 * </ul>
	 */
	private int lowerBounds[];
	
	private int numOfferedProducts;
	
	/**
	 * Crea un oggetto supplier con {@link #id}={@code id} e tutti gli altri attributi non impostati. 
	 * @param id
	 */
	public Supplier(int id){
		this.id = id;
		this.basePrices = null;
		this.prices = null;
		numSegments = NO_PRICE;
		this.availability = null;
		this.lowerBounds = null;
	}
	
	/**
	 * Assegna il vettore dei prezzi di base.
	 * @param basePrices
	 */
	public void setBasePrices(int basePrices[]){
		this.basePrices = basePrices;
	}
	
	/*
	 * riempie la matrice prices[][] utilizzando basePrices;
	 * numBands = numero fasce di sconto escludendo la 0;
	 * discount[r] = sconto percentuale della fascia r
	 */
	/**
	 * Riempie la matrice {@link #prices} utilizzando basePrices;
	 * @param numSegments - numero di fasce di sconto (esclusa la fascia 0 senza sconto)
	 * @param discounts - vettore contenente gli sconti percentuali ({@code discounts[r]} è lo sconto percentuale della fascia r-esima)
	 */
	public void setPrices(int numSegments, int discounts[]){
		this.numSegments = numSegments;
		discounts[0]=0;
		int numProducts = basePrices.length-1;
		
		prices = new double[numProducts+1][numSegments+1];
		//inizializzo la matrice come vuota usando PRODUCT_NOT_PRESENT
		for(int i=0; i<=numProducts; i++){
			for(int j=0; j<=numSegments; j++){
				prices[i][j] = PRODUCT_NOT_PRESENT;
			}
		}
		/*
		 * per ogni prodotto k che possiedo riempio la corrispondente riga
		 * prices[k][r]
		 */
		for(int k=1; k<=numProducts; k++){
			if(basePrices[k]!=NO_PRICE){
				//possiedo il prodotto
				for(int r=0; r<=numSegments; r++){
					//sconto il prezzo base
					double discontedPrice = basePrices[k]*(100-discounts[r])*0.01;
					//arrotondo al centesimo
					double roundedPrice = (double)(Math.round(discontedPrice*100))/100;
					prices[k][r] = roundedPrice;
				}
			}
		}
	}
	
	/**
	 * Assegna il vettore {@link #availability}.
	 * @param availability
	 */
	public void setAvailability(int availability[]){
		this.availability = availability;
	}
	
	
	/**
	 * Assegna il vettore {@link #lowerBounds}.
	 * @param lowerBounds
	 */
	public void setLowerBounds(int lowerBounds[]) {
		this.lowerBounds = lowerBounds;
	}
	
	/**
	 * Restituisce l'id del fornitore.
	 * @return {@link #id}
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Ritorna la disponibilità residua di product in base alla soluzione corrente.
	 * @param product
	 * @param solution
	 * @return La disponibilità di {@code product} meno gli acquisti presenti in {@code solution}.
	 */
	public int getResidual(int product, Solution solution) {
		int residual=availability[product]-solution.getQuantity(id, product);
		return residual;
	}
	
	/**
	 * Metodo overloaded di {@link #getResidual(int, Solution)}, che accetta la matrice della soluzione invece dell'oggetto di tipo Solution.
	 * 
	 * @param product
	 * @param solutionMatrix
	 */
	public int getResidual(int product, int [][] solutionMatrix) {
		int residual=availability[product]- solutionMatrix[id][product];
		return residual;
	}
	
	/**
	 * Ritorna il vettore delle disponibilità.
	 * @param product
	 * @return {@link #availability}
	 */
	public int getAvailability(int product) {
		return availability[product];
	}
	
	/**
	 * Questo metodo controlla se il fornitore tratta o meno questo prodotto. Ritorna true anche se la disponibilità è zero,
	 * mentre torna false solo quando il prodotto non è presente.
	 * Questo metodo non è attualmente usato da altri metodi ed è stato lasciato per eventuali sviluppi.  
	 * @param product
	 * @return {@code true} se la disponibilità di {@code product} è maggiore o uguale di zero, {@code false} altrimenti).
	 */
	public boolean checkAvailability(int product) {
		if (availability[product] < 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Ritorna il prezzo scontato di {@code product} acquistando una quantità totale di prodotti {@code quantity}.
	 * 
	 * @param product
	 * @param quantity
	 * @return il prezzo scontato di {@code product}. Se {@code quantity} è pari a 0 restituisce {@value #NO_PRICE}. 
	 */
	public double getDiscountedPrice(int product,int quantity){
		if(quantity==0  ||  prices[product][0]==NO_PRICE){
			return NO_PRICE;
		}
		int i;
		for (i=1; i<=lowerBounds.length-1;i++){
			if (quantity<lowerBounds[i])
				return prices[product][i-1];				
		}
		//restituisco il prezzo dell'ultima fascia 
		return prices[product][i-1];
	}
	
	/**
	 * Stampa a video i vari attributi del fornitore.
	 */
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
			DataPrinter.printMatrix2D(prices, "p", "f", 1, 0, 2, 3, 2, 2);
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

	/**
	 * Restituisce il prezzo base di product.
	 * @param product
	 * @return {@link #basePrices}{@code [product]}
	 */
	public double getBasePrice(int product) {
		return basePrices[product];
	}

	/**
	 * Ritorna la matrice dei prezzi.
	 * @return {@link #prices}[][]
	 */
	public double[][] getPrices(){
		return prices;
	}

	/**
	 * Ritorna il numero di fasce di sconto. 
	 * @return {@link #numSegments}
	 */
	public int getNumSegments() {
		return numSegments;
	}
	
	/**
	 * Ritorna il vettore dei lower bounds delle fasce di sconto.
	 * @return {@link #lowerBounds}[]
	 */
	public int[] getLowerBounds(){
		return lowerBounds;
	}

	/**
	 * Ritorna il vettore delle disponibilità.
	 * @return {@link #availability}[]
	 */
	public int[] getAvailabilities() {
		return availability;
	}
	
	/**
	 * @return Il prezzo di {@code product} nella fascia di sconto {@code segment}.
	 * @param product
	 * @param segment
	 * 
	 */
	public double getPrice(int product, int segment) {
		return prices[product][segment];
	}


	/**
	 * Assegna il numero di prodotti offerti da questo fornitore.
	 * @param numOfferedProducts
	 */
	public void setNumOfferedProducts(int numOfferedProducts) {
		this.numOfferedProducts = numOfferedProducts;
	}

	/**
	 * 
	 * @return il numero di prodotti offerti da questo fornitore.
	 */
	public int getNumOfferedProducts() {
		return numOfferedProducts;
	}
	
	
	/**
	 *  
	 * @param totalQuantity
	 * @return Il numero della fascia di sconto attiva comprando {@code totalQuantity} prodotti.
	 */
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
	
	
	/**
	 * Metodo overload di {@link #activatedSegment(int)} che riceve in ingresso un oggetto Solution.
	 * @param solution
	 * @return il numero della fascia di sconto attiva in base a solution.
	 */
	public int activatedSegment(Solution solution){
		return activatedSegment(solution.totalQuantityBought(id));
	}
	
	
	/**
	 * 
	 * @param solution
	 * @return la disponibilità residua totale di tutti i prodotti.
	 */
	public int getTotalResidualAvailability(Solution solution) {
		int sum = 0;
		for (int i=1; i<availability.length; i++){
			int currentAvailability = availability[i];
			if (currentAvailability == NO_PRICE)
				currentAvailability=0;
			sum += currentAvailability - solution.getQuantity(id,i);
		}
		return sum;
	}

	/**
	 * Calcola di quanto la quantità totale di acquisti presso il fornitore deve essere aumentata 
	 * per far aumentare di 1 la fascia di sconto attiva;
	 * se il fornitore si trova già nell'ultima fascia di sconto viene fornita la disponibilità residua
	 *
	 * @param solution
	 * @return
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
