package ThomasTest;

import java.util.ArrayList;
import java.util.HashMap;

public class Data1D<E> {

	private HashMap<E, Integer> colIndexMap;
	private ArrayList<E> data1D;
	
	public Data1D() {
		data1D = new ArrayList<E>();
		colIndexMap = null;
	}
	
	public Data1D(ArrayList<E> data) {
		data1D = data;
		colIndexMap = null;
	}

	public Data1D(ArrayList<E> data, HashMap<E, Integer> indexMap) {
		data1D = data;
		colIndexMap = indexMap;
	}
	
	public E getValue1D(int index) {
		return data1D.get(index);
	}
	
	public E getValue1D(E value) {
		int id = -1;
		if(colIndexMap != null) {
			id = colIndexMap.get(value);
			return getValue1D(id);
		}
		return null;
	}
	public E getValue1D(E value, HashMap<E, Integer> indexMap) {
		int id = -1;
		id = indexMap.get(value);
		return getValue1D(id);
	}
	
	public ArrayList<E> getData1D(){
		return this.data1D;
	}
	public boolean addData(E value) {
		return this.data1D.add(value);
	}
	public void addRangeData(int id, E value) {
		this.data1D.add(id, value);
	}
	public E removeData(int id) {
		return this.data1D.remove(id);
	}
	public int getDataSize() {
		return this.data1D.size();
	}
	
	public void setIndexMap(HashMap<E, Integer> map) {
		this.colIndexMap = map;
	}
	
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
