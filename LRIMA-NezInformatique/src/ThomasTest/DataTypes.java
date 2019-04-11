package ThomasTest;

public class DataTypes<E> {
	E[] dataTypes;
	
	public DataTypes(E[] types) {
		dataTypes = (E[]) new Object[types.length];
		for(int i = 0; i < types.length; i++) {
			dataTypes[i] = types[i];
		}
	}
	
	public E getType(int index) {
		if(index > -1 && index < dataTypes.length) {
			return dataTypes[index];
		}
		else
			return null;
	}
}
