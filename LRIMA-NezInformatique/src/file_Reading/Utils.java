package file_Reading;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
	
	public static HashMap<Integer, String> switchHashMap(HashMap<String, Integer> map){
		HashMap<Integer, String> newMap = new HashMap<Integer, String>();
		
		for(String key : map.keySet()) {
			newMap.put(map.get(key), key);
		}
		
		return newMap;
	}
	
	public static String getAndRemove(ArrayList<String> list, int id) {
		String s = list.get(id);
		list.remove(id);
		return s;
	}
	
	public static ArrayList<Double> convertStringToDouble(ArrayList<String> list, boolean doDelete){
		ArrayList<Double> doubleList = new ArrayList<Double>();
		
		for(String s : list) {
			double d = 0;
			try {
				d = Double.parseDouble(s);
				if(doDelete) {
					list.remove(s);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			doubleList.add(d);
		}
		return doubleList;
	}
	
	public static boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}

