package parser;


import java.io.BufferedReader;
import java.io.File;
import java.util.Arrays;
import data.Problem;
import data.Supplier;
import util.Io;
import util.Utility;

public class ProblemParser extends Parser{
	int numProducts;
	String file;
	
	final boolean BOOLEAN_NOT_USED = false;
	
	final int NOT_FOUND = -1;
	final int DEMAND_NOT_FOUND = -1;
	final char COMMENT_DELIMITER = '#';
	final String DEMAND_SECTION_DELIMITER = "DEMAND_SECTION";
	final String OFFER_SECTION_DELIMITER = "OFFER_SECTION";
	final String DISCOUNT_SECTION_DELIMITER = "DISCOUNT_SECTION";
	
	//separatore dei vari valori nel file che descrive il problema
	final String SEPARATOR = "\\s[\\s]*";
	final String STRING_TO_STRIP = "[\\s]*:[\\s]*";
	
	
	/**
	 * Crea un parser in grado di leggere un file contenente la descrizione di un problema e di restituire 
	 * l'oggetto corrispondente.
	 * path indica la posizione (percorso assoluto) della cartella contenente il file di input (che viene specificato
	 * con il parametro inputFile del metodo parse).
	 */
	public ProblemParser(String path) {
		super(path);
	}
	
	

	/**
	 * Effettua il parsing del file di nome inputFile, crea e restituisce il Problem descritto in tale file
	 * [formato di Manerba].
	 */
	public Problem parse(String problemFile) throws Exception{
		file = path + File.separator + problemFile;
		
		//leggo da file i vari valori
		//NAME
		final String ATTRIBUTE = "NAME";
		String defaultStringValue = "senza nome";
		String name = readAttribute(ATTRIBUTE, defaultStringValue);
		
		//TYPE
		final String TYPE = "TYPE";
		defaultStringValue = "non specificato";
		String type = readAttribute(TYPE, defaultStringValue);
			
		//CLASS
		final String CLASS = "CLASS";
		int defaultIntValue = -1;
		int problemClass = Integer.parseInt(readAttribute(CLASS, Integer.toString(defaultIntValue)));
		
		//DIMENSION
		final String DIMENSION = "DIMENSION";
		int dimension = readMandatoryAttribute(DIMENSION);
		
		//MAX_N_RANGE
		final String MAX_N_RANGE = "MAX_N_RANGE";
		int maxNRange = readAttribute(MAX_N_RANGE, -1);
		
		//leggo DEMAND_SECTION
		int demand[] = readDemandSection();
		
		//creo array con suppliers
		Supplier suppliers[] = makeSuppliers(dimension);
			
		return new Problem(name, type, problemClass, maxNRange, demand, suppliers);
	}

	
	/*
	 * Legge il valore di attribute come String.
	 * Attribute può essere ad esempio "NAME", "TYPE", "CLASS", "DIMENSION", "MAX_N_RANGE", ...
	 * Restituisce null se l'attributo non viene trovato.
	 */
	private String readAttributeFromFile(String attribute) throws Exception{
		String value = null;
		BufferedReader bufferedReader = Io.openInFile(file);
		
		String line;
		boolean found = false;
		while((line = bufferedReader.readLine()) != null	&&	!found){
			if(line.length()>=attribute.length()){
				String temp=line.substring(0,attribute.length());
				if(temp.equals(attribute)){
					found = true;
					String lineElements[] = line.split(SEPARATOR);
					value=lineElements[2];
				}
			}
		}
		bufferedReader.close();
	
		return value;
	}
	
	/*
	 * Legge dal file contenente la descrizione del problema la domanda dei vari prodotti
	 * e la restituisce; lancia una eccezione se DEMAND_SECTION non viene trovata.
	 */
	private int[] readDemandSection() throws Exception{
		int demand[] = null;
		BufferedReader inputFile = Io.openInFile(file);
		
		findLine(inputFile, DEMAND_SECTION_DELIMITER);
		demand = readDemandFromFile(inputFile);
		
		inputFile.close();
		if(demand==null){
			//lancio 1 eccezione perchè non ho trovato DEMAND_SECTION
			Utility.exception("Non è stata trovata la sezione DEMAND_SECTION.");
		}
		return demand;
	}

	/*
	 * Legge la sezione DEMAND_SECTION di inputFile e restituisce un array contenente le domande.
	 */
	private int[] readDemandFromFile(BufferedReader inputFile) throws Exception {
		int[] demand = null;
		String line;
		String[] lineElements;
		//leggo la prima riga che indica il numero di prodotti
		line = inputFile.readLine();
		numProducts = Integer.parseInt(line);
		
		//leggo le altre righe (con la domanda di ogni prodotto), non viene utilizzato demand[0]  
		demand = new int[numProducts+1];
		/*
		 * demand[k]=DEMAND_NOT_FOUND significa che per il prodotto k la domanda non è stata specificata.
		 * All'inizio presuppongo di non trovare alcuna domanda.
		 */
		demand[0]= INT_NOT_USED;
		Arrays.fill(demand, 1, demand.length, DEMAND_NOT_FOUND);
		
		//vale true quando raggiungo la fine DEMAND_SECTION
		boolean end = false;
		while(!end){
			line = inputFile.readLine();
			lineElements = line.split(SEPARATOR);
			
			if(lineElements[0].equals(OFFER_SECTION_DELIMITER)){
				end = true;
			}else{
				//id del prodotto
				int productId = Integer.parseInt(lineElements[0]);
				if(productId<1 || productId>numProducts){
					Utility.exception("In DEMAND_SECTION del file "+file+" il prodotto "+productId+" non è accettabile");
				}
				//domanda
				int productDemand = Integer.parseInt(lineElements[1]);
				demand[productId] = productDemand;
			}
		}
		completeDemand(demand);
		return demand;
	}

	/*
	 * Controllo che siano state specificate tutte le domande; per i prodotti la cui domanda non è stata 
	 * specificata assegno domanda pari a DEFAULT_VALUE.
	 */
	private void completeDemand(int[] demand) {
		final int DEFAULT_VALUE = 0;
		for(int i=1; i<=demand.length-1; i++){
			if(demand[i]==DEMAND_NOT_FOUND){
				Utility.warning("In DEMAND_SECTION del file "+file+" la domanda del prodotto "+i+" non è stata specificata"+
						"Ad essa verrà assegnato il valore di default "+DEFAULT_VALUE+".\n");
				demand[i]=DEFAULT_VALUE;
			}
		}
	}
	
	
	/*
	 * Legge il file contenente la descrizione del problema e crea l'array con i supplier.
	 */
	private Supplier[] makeSuppliers(int numSuppliers) throws Exception{
		Supplier suppliers[] = null;
		
		suppliers = readOfferSection(numSuppliers);
		readDiscountSection(numSuppliers, suppliers);
		
		return suppliers; 
	}

	/*
	 * Porta inputFile alla riga uguale a tag in inputFile.
	 */
	private void findLine(BufferedReader inputFile, String tag)throws Exception {
		String line;
		//mi porto alla riga tag
		//vale true quando ho raggiunto tag
		boolean found = false;
		while(!found	&&	(line = inputFile.readLine()) != null){
			if(line.length()>=tag.length()){
				line = line.substring(0,tag.length());
				if(line.equals(tag)){
					found = true;	
				}
			}
		}
		//non ho trovato OFFER_SECTION dunque lancio un'eccezione
		if(!found){
			Utility.exception("Nel file del problema non è stato possibile trovare il tag "+tag+".\n");
		}
	}

	/*
	 * Legge OFFER_SECTION di file e restituisce un array di fornitori settati con i valori trovati in
	 * OFFER_SECTION.
	 */
	private Supplier[] readOfferSection(int numSuppliers) throws Exception {
		Supplier[] suppliers;
		BufferedReader inputFile = Io.openInFile(file);
		
		//mi porto alla riga OFFER_SECTION
		findLine(inputFile, OFFER_SECTION_DELIMITER);
		
		//suppliers[0] non è usato
		suppliers = new Supplier[numSuppliers+1];
		Arrays.fill(suppliers, null);
		
		int supplierId;
		Supplier supplier;
		//found = true quando raggiungo la fine di OFFER_SECTION [=raggiungo la riga DISCOUNT_SECTION].
		boolean end = false;
		String line;
		String[] lineElements;
		String endTag = DISCOUNT_SECTION_DELIMITER;
		//leggo tuttte le righe di OFFER_SECTION
		while(!end){
			line = inputFile.readLine();
			
			if(line.replaceAll(STRING_TO_STRIP, "").equals(endTag)){
				//confronto le stringhe a meno dei : e degli spazi finali
				end = true;	
			}else{
				lineElements = line.split(SEPARATOR);
				
				//il primo elemento della riga è il supplierId
				supplierId = Integer.parseInt(lineElements[0]);
				if(supplierId<1 || supplierId>numSuppliers){
					Utility.exception("Nella sezione OFFER_SECTION del file "+file+" il supplier "+supplierId+
										" non è valido"+".\n");
				}
				supplier = new Supplier(supplierId);
				
				// il secondo elemento della riga è il numOfferedProducts 
				int numOfferedProducts = Integer.parseInt(lineElements[1]);
				supplier.setNumOfferedProducts(numOfferedProducts);
				
				/*
				 * offer contiene i prodotti offerti da supplierId con il relativo costo e disponibilità.
				 * offer[0]=id_prodotto1;	offer[1]=costo_p1;	offer[2]=disponibilità_p1;
				 * offer[3]=id_p2;			offer[4]=costo_p2;	offer[5]=disponibilità_p2;
				 * ...
				 * Copio in offer tutti gli elementi di lineElements a partire da quello n° 2
				 */
				int offer[] = new int[lineElements.length-2];
				for(int i=0; i<=offer.length-1; i++){
					offer[i] = Integer.parseInt(lineElements[i+2]);
				}
				
				setOffer(supplier, offer, numProducts);
				suppliers[supplierId] = supplier;
			}
		}
		inputFile.close();
		
		//ho finito di analizzare OFFER_SECTION
		//controllo che siano stati forniti tutti i fornitori
		for(int i=1; i<=suppliers.length-1; i++){
			if(suppliers[i]==null){
				Utility.exception("In OFFER_SECTION non è stato specificato il fornitore "+i+".\n");
			}
		}
		return suppliers;
	}
	
	/*
	 * Legge in file DISCOUNT_SECTION e setta i valori letti ai suppliers.
	 */
	private void readDiscountSection(int numSuppliers, Supplier[] suppliers) throws Exception {
		BufferedReader inputFile = Io.openInFile(file);
		//mi porto alla linea DISCOUNT_SECTION
		findLine(inputFile, DISCOUNT_SECTION_DELIMITER);
		
		int supplierId;
		String tag = "EOF";
		//per controllare che in DISCOUNT_SECTION siano definiti tutti i fornitori
		boolean definedSupplier[]	= new boolean[numSuppliers+1];
		definedSupplier[0] = BOOLEAN_NOT_USED;
		Arrays.fill(definedSupplier, 1, definedSupplier.length,false);
		
		//end = true quando raggiungo la fine di DISCOUNT_SECTION [=raggiungo la riga EOF].
		boolean end = false;
		String line;
		String[] lineElements;
		//ora leggo tutte le righe di DISCOUNT_SECTION
		while(!end){
			line = inputFile.readLine();
			
			if(line.trim().equals(tag)){
				//confronto le stringhe a meno degli spazi iniziali e finali
				end = true;
			}else{
				lineElements = line.split(SEPARATOR);
				
				//il primo elemento della riga è il supplierId
				supplierId = Integer.parseInt(lineElements[0]);
				if(supplierId<1 || supplierId>numSuppliers){
					Utility.exception("Nella sezione DISCOUNT_SECTION del file "+file+" il supplier "+
										supplierId+" non è valido.\n");
				}
				definedSupplier[supplierId] = true;
				
				/*
				 * numero fasce di sconto senza contare quella 0 (prezzo base);
				 * è il secondo elemento della riga
				 */
				int numSegments = Integer.parseInt(lineElements[1]);
				/*
				 * controllo che la riga presenti il giusto numero di elementi. Devono essere presenti
				 * idSupplier, numSegments e 1 coppia di valori per ogni fascia di sconto 
				 * 
				 */
				if(lineElements.length	!=	2+numSegments*2){
					Utility.exception("In DISCOUNT_SECTION non sono state specificate correttamente le fasce di sconto per " +
							"il fornitore "+supplierId);
				}
				
				//discounts[r] = sconto percentuale della fascia r; discount[0] = 0
				int discounts[]		= new int[numSegments+1];
				discounts[0]		= INT_NOT_USED;
				
				/*
				 * lowerBounds[r] = lower bound della fascia r di sconto;
				 * lowerBounds[0] = 1
				 */
				int lowerBounds[]	= new int[numSegments+1];
				lowerBounds[0]		= INT_NOT_USED;
				
				//leggo da lineElements i lower bounds e le percentuali di sconto per le varie fasce
				for(int i=2; i<=lineElements.length-2; i+=2){
					lowerBounds[i/2]		= Integer.parseInt(lineElements[i]);
					discounts[i/2]			= Integer.parseInt(lineElements[i+1]);
				}
				
				suppliers[supplierId].setPrices(numSegments, discounts);
				suppliers[supplierId].setLowerBounds(lowerBounds);
			}
		}
		//controllo che in DISCOUNT_SECTION siano stati definiti tutti i fornitori
		for(int i=1; i<=definedSupplier.length-1; i++){
			if(!definedSupplier[i]){
				Utility.exception("In DISCOUNT_SECTION non è stato definito il supplier "+i+".\n");
			}
		}
		
		inputFile.close();
	}
	
	
	/*
	 * Setta in supplier i prezzi base e le disponibilità dei prodotti (contenuti in offer).
	 * numProducts = numero di prodotti totale.
	 * offer[0]=id_prodotto1;	offer[1]=costo_p1;	offer[2]=disponibilità_p1;
	 * offer[3]=id_p2;			offer[4]=costo_p2;	offer[5]=disponibilità_p2;
	 * ...
	 */
	private void setOffer(Supplier supplier, int offer[], int numProducts) throws Exception{
		//basePrices[0] e availability[0] non sono utilizzati
		int basePrices[]		= new int[numProducts+1];
		int availability[]		= new int[numProducts+1];
		/*Prima di leggere la riga non so quali prodotti saranno presenti in supplier dunque indico che non è
		 * presente alcun prodotto
		 */
		basePrices[0] = INT_NOT_USED;
		availability[0] = INT_NOT_USED;
		Arrays.fill(basePrices, 1, basePrices.length, Supplier.PRODUCT_NOT_PRESENT);
		Arrays.fill(availability, 1, availability.length, Supplier.PRODUCT_NOT_PRESENT);
		
		//leggo tutte le triplette (prodotto, costo, disponibilità) presenti in offer
		for(int i=0; i<=offer.length-3; i+=3){
			int productId			= offer[i];
			int basePrice			= offer[i+1];
			int productAvailability	= offer[i+2];
			
			if(productId<1 || productId>numProducts){
				Utility.exception("In OFFER_SECTION di "+file+" il prodotto "+productId+" del fornitore "+
									supplier.getId()+" non è ammesso");
			}
			basePrices[productId]	= basePrice;
			availability[productId]	= productAvailability;
		}
		
		//setto i prezzi base
		supplier.setBasePrices(basePrices);
		//setto la disponibilità
		supplier.setAvailability(availability);
		
		return;
	}
	
	/*
	 * Legge il valore di attribute (di tipo String) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * restituisce defaultValue.
	 */
	private String readAttribute(String attribute, String defaultValue) throws Exception{
		String value;
		if((value = readAttributeFromFile(attribute))	==	null){
			value = defaultValue;
			Utility.warning("Nel file "+file+" non è stato trovato l'attributo "+attribute+".\n"+
					"Ad esso verrà assegnato il valore "+value+".\n");
		}	
			
		return value;
	}
	
	/*
	 * Legge il valore di attribute (di tipo int) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * restituisce defaultValue.
	 */
	private int readAttribute(String attribute, int defaultValue) throws Exception{
		return Integer.parseInt((readAttribute(attribute, Integer.toString(defaultValue))));
	}
	
	/*
	 * Legge il valore di attribute (di tipo int) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * lancia un'eccezione.
	 */
	private int readMandatoryAttribute(String attribute) throws Exception{
		int value = readAttribute(attribute, NOT_FOUND);
		if(value==NOT_FOUND){
			Utility.exception("Nel file "+file+" non è stato trovato l'attributo "+attribute);
		}
		return value;
	}
}
