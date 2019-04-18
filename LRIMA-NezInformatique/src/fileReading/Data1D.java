package fileReading;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to contain an array of values and an indexMap to get the ID of a 
 * specific data name.
 * 
 * @author Thomas Trepanier
 *
 * @param <E>
 */
public class Data1D<E> {

	/**
	 * {@link HashMap} containing a value as a key and the index of that value in the array
	 */
	private HashMap<E, Integer> colIndexMap;
	/**
	 * {@link ArrayList} containing the data
	 */
	private ArrayList<E> data1D;
	
	/**
	 * Initialize a new empty data array without index map
	 */
	public Data1D() {
		data1D = new ArrayList<E>();
		colIndexMap = null;
	}
	
	/**
	 * Initialize a new data array filled with data parameter and an empty index map
	 * @param data - {@link ArrayList} containing values
	 */
	public Data1D(ArrayList<E> data) {
		data1D = data;
		colIndexMap = null;
	}

	/**
	 * Initialize a new data array filled with data parameter and an existing index map
	 * @param data - {@link ArrayList} containing values
	 * @param indexMap - {@link HashMap} containing index mapping
	 */
	public Data1D(ArrayList<E> data, HashMap<E, Integer> indexMap) {
		data1D = data;
		colIndexMap = indexMap;
	}
	
	/**
	 * Gets the value in the array at the specified index
	 * @param index - Index to get value at
	 * @return Returns null if invalid index, the value otherwise
	 */
	public E getValue1D(int index) {
		if(index < 0 || index > getDataSize())
			return null;
		return data1D.get(index);
	}
	
	/**
	 * Get the value in the array at the key
	 * @param key - Key to get value at
	 * @return Returns null if invalid key, value otherwise
	 */
	public E getValue1D(E key) {
		int id = -1;
		if(colIndexMap != null) {
			id = colIndexMap.get(key);
			return getValue1D(id);
		}
		return null;
	}
	
	/**
	 * Gets the value in the array at the key in the indexMap
	 * @param key - Key to get value at
	 * @param indexMap - {@link HashMap} map to look for key
	 * @return Null if invalid key, the value at the key otherwise
	 */
	public E getValue1D(E key, HashMap<E, Integer> indexMap) {
		int id = -1;
		if(!indexMap.containsKey(key))
			return null;
		id = indexMap.get(key);
		return getValue1D(id);
	}
	
	/**
	 * Gets the {@link ArrayList} containing the data. USE CAREFULY
	 * @return {@link ArrayList} containing the data
	 */
	public ArrayList<E> getData1D(){
		return this.data1D;
	}
	/**
	 * Adds a value in the data array
	 * @param value - Value to add
	 * @return True if the data is added successfully, false if the data couldn't be added
	 */
	public boolean addValue(E value) {
		return this.data1D.add(value);
	}
	/**
	 * Add a value in the array at the specified index
	 * @param id - Index to add the value at
	 * @param value - Value to add
	 */
	public void addValueAtRange(int id, E value) {
		this.data1D.add(id, value);
	}
	/**
	 * Remove a value in the array at the specified index
	 * @param id - Index to remove value at
	 * @return the value removed, null if value couldn't be removed
	 */
	public E removeValue(int id) {
		return this.data1D.remove(id);
	}
	/**
	 * Remove the value in the array
	 * @param value - Value to remove
	 * @return True if value is removed, false if not removed
	 */
	public boolean removeValue(E value) {
		return this.data1D.remove(value);
	}
	/**
	 * Gets the size of the data {@link ArrayList} 
	 * @return Integer representing the number of values in the array
	 */
	public int getDataSize() {
		return this.data1D.size();
	}
	
	/**
	 * Sets the index map of the dataset
	 * @param map - {@link HashMap} to use as a map
	 */
	public void setIndexMap(HashMap<E, Integer> map) {
		this.colIndexMap = map;
	}
	
	/**
	 * Prints the data in the console
	 */
	public void print1DArray() {
		if(data1D == null) {
			System.out.println("1D data array is null");
			return;
		}
		System.out.print("[");
		for(E value : data1D) {
			System.out.print(value + ";");
		}
		System.out.println("]");
	}
}
