package ThomasTest;

import java.util.ArrayList;
import java.util.HashMap;

public class Data1D<E> {

	private ArrayList<E> data1D;

	
	public Data1D(ArrayList<E> data) {
		data1D = data;
	}
	
	
	public E getValue1D(int index) {
		return data1D.get(index);
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
