package neighbourgenerator;

import java.util.HashSet;

public class IdList {

	private HashSet<Integer> chosenId;
	private int[] chosenIdArray;
	
	public IdList(int size){
		chosenIdArray=new int[size];
		chosenId =new HashSet<Integer>();
	}
	
	public boolean contains(int Id){
		return chosenId.contains(Id);
	}
	
    public int getId(int index){
    	return chosenIdArray[index];
    }
    
    public void add(int id, int index){
    	chosenIdArray[index] = id;
    	chosenId.add(id);	
    }
    public int getSize(){
    	return chosenIdArray.length;
    }
    public HashSet<Integer> getHashSet(){
    	return chosenId;
    }
}
