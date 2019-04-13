package ThomasTest;

import java.util.ArrayList;
import java.util.HashMap;

public class Data2D<E> {
	
	private HashMap<E, Integer> colIndexMap;
	private ArrayList<ArrayList<E>> data2D;
	
	public Data2D(ArrayList<ArrayList<E>> data) {
		data2D = data;
		this.colIndexMap = new HashMap<E, Integer>();
		for(int i = 0; i < data2D.get(0).size(); i++) {
			this.colIndexMap.put(data2D.get(0).get(i), i);
		}
	}
	
	public ArrayList<E> getLine(int id){
		return data2D.get(id);
	}
	public ArrayList<E> getLine(E lineIdentifier){
		for(ArrayList<E> list : data2D) {
			for(E value : list) {
				if(value.equals(lineIdentifier))
					return list;
			}
		}
		return null;
	}
	public E getValue2D(int lineId, int colId) {
		return data2D.get(lineId).get(colId);
	}
	
	public E getValue2D(int lineId, E colIdentifier) {
		return data2D.get(lineId).get(colIndexMap.get(colIdentifier));
	}
	
	public E getValue2D(E lineIdentifier, E colIdentifier) {
		int ligId = 0;
		int colId = colIndexMap.get(colIdentifier);
		
		for(int i = 0; i < data2D.size(); i++) {
			if(data2D.get(i).get(0).equals(lineIdentifier)) {
				ligId = i;
				break;
			}
		}
		return data2D.get(ligId).get(colId);
	}
	
	public ArrayList<ArrayList<E>> getData(){
		return data2D;
	}
	public HashMap<E, Integer> getIndexMap(){
		return this.colIndexMap;
	}
	
	public int getColIndex(E value) {
		return colIndexMap.get(value);
	}
	public void print2DArray() {
		if(data2D == null) {
			System.out.println("2D data array is null");
			return;
		}
		System.out.print("[");
		for(ArrayList<E> list : data2D) {
			System.out.print("[");
			for(E value : list) {
				System.out.print(value + ";");
			}
			System.out.println("]");
		}
	}
}
