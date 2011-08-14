package data;

import java.util.HashSet;
/**
 * Questa classe crea una lista di identificatori.
 */
public class IdList {
	/**
	 * Lista di identificatori in hash.
	 */
	private HashSet<Integer> chosenId;
	/**
	 * Lista di identificatori  in array di int.
	 */
	private int[] chosenIdArray;
	
	/**
	 * Costruisce un oggetto IDList.
	 * @param size
	 */
	public IdList(int size){
		chosenIdArray=new int[size];
		chosenId =new HashSet<Integer>();
	}
	
	/**
	 * Questo metodo verifica se un certo {@code Id} è contenuto nella lista.
	 * @param Id
	 * @return {@code true} se {@code Id} è presente nella lista, {@code false} altrimenti.
	 */
	public boolean contains(int Id){
		return chosenId.contains(Id);
	}
	
	/**
	 * Questo metodo ritorna l'{@code Id} alla posizione {@code index}.
	 * @param index
	 * @return L'identificatore {@code Id} corrispondente alla posizione {@code index}.
	 */
    public int getId(int index){
    	return chosenIdArray[index];
    }
    
    /**
     * Questo metoto aggiunge l'identificatore {@code Id} alla lista.
     * @param id
     * @param index
     */
    public void add(int id, int index){
    	chosenIdArray[index] = id;
    	chosenId.add(id);	
    }
    
    /**
     * Questo metodo ritorna la lunghezza della lista di identificatori.
     * @return lunghezza della lista 
     */
    public int getSize(){
    	return chosenIdArray.length;
    }
    
    /**
     * Ritorna la lista di identificatori in hash.
     *@return {@link #chosenId} 
     */
    public HashSet<Integer> getHashSet(){
    	return chosenId;
    }
}
