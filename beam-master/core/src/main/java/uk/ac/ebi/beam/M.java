package uk.ac.ebi.beam;

import java.io.IOException;

public class M {

	public static void main(String[] args) throws IOException {
		
		Graph  furan        = Graph.fromSmiles("[CH3]CHO");
		Graph  furan_coll = Functions.collapse(furan);
		//smi.equals("O1C=CC=C1");
		
		System.out.println(furan_coll.toSmiles());
	
	}

}
