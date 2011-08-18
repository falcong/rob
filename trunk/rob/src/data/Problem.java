package data;

import java.util.Arrays;
import java.util.HashSet;
import data.Supplier;
import data.comparator.BoughtQuantityComparator;
import data.comparator.CurrentPriceComparator;
/**
 * Questa classe contiene i dati del problema e vari metodi 
 * per accedere ai dati stessi. 
 *
 */

public class Problem {
	
	/**
	 * Nome del problema.
	 */
	private String name;
	
	/**
	 * Contenuto nei file di istanza, attualmente non usato.
	 */
	private String type;
	
	/**
	 * Classe del problema.
	 */
	private int problemClass;
	
	/**
	 * Numero dei fornitori.
	 */
	int dimension;
	
	/**
	 * Numero massimo degli intervalli di sconto,
	 * inclusa la fascia 0-esima in cui non è applicato alcuno sconto.
	 * Se questo attributo vale -1 vuol dire che non è stato specificato.<br>
	 * Attualmente questo attributo non è usato.
	 */
	private int maxNRange;
	
	/**
	 * Numero di prodotti.
	 */
	private int numProducts;
	
	
	/**
	 *  Vettore contenente la domanda per ciascun prodotto; demand[k]=domanda del 
	 *  prodotto k-esimo; demand[0] non è usato quindi demand.length=numProducts+1.
	 */
	private int demand [];
	
	/**
	 * Numero totale di prodotti da acquistare.
	 */
	private int totalDemand;
	
	
	/**
	 * Contiene i fornitori; suppliers[0] non è usato.
	 */
	private Supplier suppliers[];
	
	/**
	 * Costruisce un oggetto problema.
	 * @param name
	 * @param type
	 * @param problemClass
	 * @param maxNRange
	 * @param demand
	 * @param suppliers
	 */
	public Problem(String name, String type, int problemClass, int maxNRange,
		int demand [], Supplier suppliers[]){
		this.name 			= name;
		this.type 			= type;
		this.problemClass	= problemClass;
		dimension			= suppliers.length-1;
		this.maxNRange		= maxNRange;
		numProducts			= demand.length-1;
		this.demand 		= demand;//
		totalDemand = 0;
		for(int p=1; p<=numProducts; p++){
			totalDemand += demand[p];
		}
		this.suppliers			= suppliers;

	}
	
	/**
	 * Ritorna il nome del problema.
	 * @return l'attributo name.
	 */
	public String getName(){
		return name;
	}

	/**
	 * Restituisce un fornitore scelto a caso che non sia contenuto nella blacklist.
	 * @param suppBlacklist - HashSet contenente gli id dei fornitori da escludere dall'estrazione.
	 * @return un oggetto di tipo Supplier.
	 */
	public Supplier getRandomSupplier(HashSet<Integer> suppBlacklist) {
		int id = getRandomSupplierId(suppBlacklist);
		if(id==0)
			return null;
		return getSupplier(id);
	}
	
	/**
	 * Restituisce l'id di un fornitore scelto a caso che non sia contenuto nella blacklist.
	 * @param suppBlacklist - HashSet contenente gli id dei fornitori da escludere dall'estrazione.
	 * @return un intero corrispondente all'id del fornitore estratto.
	 */
	public int getRandomSupplierId(HashSet<Integer> suppBlacklist) {
		int id;
		if (suppBlacklist.size()>=getDimension())
			return 0;
		boolean ok=false;
		do{
			id=1+(int)(Math.random()*getDimension());
			if (suppBlacklist.contains(id))
				continue;
			else
				ok=true;
		} while(!ok);
		return id;
	}
	
	/**
	 * Questo metodo equivale a {@link #getRandomSupplier(HashSet)}, ma non richiede in ingresso una blacklist poiché genera
	 * automaticamente una blacklist vuota.
	 * @return un oggetto Supplier estratto casualmente tra tutti i fornitori del problema.
	 */
	public Supplier getRandomSupplier() {
		HashSet<Integer> blacklist = new HashSet<Integer>();
		return getRandomSupplier(blacklist);
	}
	
	/**
	 * Ritorna il numero di fornitori del problema.
	 * @return {@link #dimension}.
	 */
	public int getDimension() {
		return dimension;
	}


	/**
	 * Ritorna il numero di prodotti del problema.
	 * @return {@link #numProducts}.
	 */
	public int getNumProducts() {
		return numProducts;
	}

	/**
	 * Ritorna il vettore della domanda.
	 * @return {@link #demand}
	 */
	public int[] getDemand() {
		return demand;
	}
	

	/**
	 * Ritorna il valore della domanda per il prodotto product.
	 * @return {@link demand[product]}
	 */
	public int getProductDemand(int product){
		return demand[product];
	}

	/**
	 * Restituisce l'array contente tutti i fornitori del problema.
	 * @return {@link #suppliers}.
	 */
	public Supplier[] getSuppliers() {
		return suppliers;
	}

	/**
	 * Restituisce il fornitore corrispondente all'identificatore {@code id}.
	 * @param id
	 * @return l'oggetto Supplier con identificatore pari a quello in ingresso.
	 */
	public Supplier getSupplier(int id) {
		return suppliers[id];
	}

	/**
	 * Restituisce la disponibilità totale del prodotto {@code  product} presso il fornitore {@code supplier}. 
	 * @param supplier - id del fornitore
	 * @param product - numero del prodotto
	 * @return availability se è il prodotto è presente, 0 altrimenti
	 */
	public int getAvailability(int supplier, int product) { 
		int availability = suppliers[supplier].getAvailability(product);
		if(availability==Supplier.PRODUCT_NOT_PRESENT){
			return 0;
		}else{
			return availability;
		}
	}
	
	/**
	 * Restituisce il numero di fasce di sconto del fornitore con identificatore {@code supplierId}.
	 * La fascia di base (quella senza sconto) non è inclusa nel numero.
	 * 
	 * @param supplierId
	 * @return il numero di fasce di sconto che hanno sconto maggiore di 0.
	 */
	public int getNumSegments(int supplierId) {
		return suppliers[supplierId].getNumSegments();
	}

	/**
	 * Restituisce la classe del problema.
	 * @return {@link #problemClass}
	 */
	public int getProblemClass() {
		return problemClass;
	}
	
	/**
	 * Restituisce la massima quantità di prodotti che è possibile acquistare presso il fornitore identificato da {@code supplierId}.
	 * Se il fornitore offre una quantità di prodotto maggiore della domanda, viene considerato il valore della domanda.
	 * @param supplierId
	 * @return la somma del minimo tra la disponibilità del fornitore e la domanda di ciascun prodotto.
	 */
	public int getMaxBuyableQuantity(int supplierId){
		int sum = 0;
		for(int p=1; p<=numProducts; p++){
			int availability = getSupplier(supplierId).getAvailability(p);
			if(availability==Supplier.PRODUCT_NOT_PRESENT){
				availability=0;
			}
			
			sum += Math.min(demand[p], availability);
		}
		
		return sum;
	}
	
	/**
	 * Ritorna la domana totale
	 * @return {@link #totalDemand}
	 */
	public int getTotalDemand(){
		return totalDemand;
	}
	
	/**
	 * Restituisce la massima fascia di sconto attivabile nel fornitore identificato da {@code supplier} 
	 * supponendo di comprare da esso il valore restituito da {@link #getMaxBuyableQuantity(int)}. 
	 * @param supplier
	 * @return il numero della fascia di sconto.
	 */
	public int maxSegmentActivable(int supplier){
		return suppliers[supplier].activatedSegment(getMaxBuyableQuantity(supplier));
	}

	
	/**
	 * Ordina i fornitori in base al prezzo del prodotto {@code product} e alla soluzione {@code solution}. 
	 * @param product
	 * @param solution
	 * @return un array di oggetti Supplier ordinati.
	 */
	public Supplier[] sortByCurrentPrice(int product, Solution solution) {
		Supplier[] suppliersCopy=suppliers.clone();
		CurrentPriceComparator comparator= new CurrentPriceComparator(solution,product);
		Arrays.sort(suppliersCopy, 1, dimension+1, comparator);
		return suppliersCopy;
	}
	
	/**
	 * Ordina i fornitori in base alla quantità totale acquistata presso ciascuno nella soluzione {@code solution}.
	 * @param solution
	 * @return un array di oggetti Supplier ordinati.
	 */
	public Supplier[] sortByBoughtQuantity(Solution solution) {
		Supplier[] suppliersCopy=suppliers.clone();
		BoughtQuantityComparator comparator= new BoughtQuantityComparator(solution);
		//ordino l'array in base alle quantità totali acquistate in senso crescente
		Arrays.sort(suppliersCopy, 1, dimension+1, comparator);
		//inverto l'ordine dell'array per avere l'ordine decrescente
		Supplier result[] = new Supplier[dimension+1];
		result[0] = null;
		for(int i=1; i<=dimension; i++){
			result[i] = suppliersCopy[dimension-i+1];
		}
		return result;
	}
}
