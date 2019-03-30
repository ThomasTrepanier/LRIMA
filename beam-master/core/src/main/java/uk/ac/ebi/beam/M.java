package uk.ac.ebi.beam;

import java.io.IOException;

public class M {

	public static void main(String[] args) throws IOException {
		
		Graph  furan        = Graph.fromSmiles("o1cccc1");
		Graph  furan_kekule = furan.kekule();
		String smi          = furan_kekule.toSmiles();
		//smi.equals("O1C=CC=C1");
		
		System.out.println(smi);
	
	}

}
