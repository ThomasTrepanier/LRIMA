package ThomasTest;

import java.util.ArrayList;
import java.util.HashMap;

public class Data2D<E> {
	
	private HashMap<E, Integer> colIndexMap;
	private ArrayList<Data1D<E>> data2D;
	
	public Data2D(ArrayList<Data1D<E>> data) {
		data2D = data;
		this.colIndexMap = new HashMap<E, Integer>();
		for(int i = 0; i < data2D.get(0).getDataSize(); i++) {
			this.colIndexMap.put(data2D.get(0).getValue1D(i), i);
		}
		
		for(int i = 1; i < data2D.get(0).getDataSize(); i++) {
			data2D.get(i).setIndexMap(this.colIndexMap);
		}
	}
	
	public Data1D<E> getLine(int id){
		return data2D.get(id);
	}
	public Data1D<E> getLine(E lineIdentifier){
		for(Data1D<E> list : data2D) {
			for(E value : list.getData1D()) {
				if(value.equals(lineIdentifier))
					return list;
			}
		}
		return null;
	}
	
	public E getValue2D(int lineId, int colId) {
		return data2D.get(lineId).getValue1D(colId);
	}
	
	public E getValue2D(int lineId, E colIdentifier) {
		return data2D.get(lineId).getValue1D(colIdentifier, colIndexMap);
	}
	
	public E getValue2D(E lineIdentifier, E colIdentifier) {
		int ligId = 0;
		int colId = colIndexMap.get(colIdentifier);
		
		for(int i = 0; i < data2D.size(); i++) {
			if(data2D.get(i).getValue1D(0).equals(lineIdentifier)) {
				ligId = i;
				break;
			}
		}
		return data2D.get(ligId).getValue1D(colId);
	}
	
	public ArrayList<Data1D<E>> getData(){
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
		for(Data1D<E> list : data2D) {
			System.out.print("[");
			for(E value : list.getData1D()) {
				System.out.print(value + ";");
			}
			System.out.println("]");
		}
	}
}
