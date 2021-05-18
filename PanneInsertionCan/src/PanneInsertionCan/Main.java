package panneinsertioncan;
import java.io.File;
import java.io.IOException;


public class Main {
	public static void main (String[] args) {
		
		int size;
		try {
			//on récupère l'argument : size
			size  = Integer.parseInt(args[0]);
			//num = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Deux Arguments Sont Nécessaires ! ");			
			return;
		}
		
		System.out.println("Panne lors de l'insertion dans un CAN");
		
		//on créer une instance de PNMLManipulation 
		PNMLManipulation manip = new PNMLManipulation(400,400);
		//on crée un réseau de petri
		CAN can = new CAN(size,manip);
		can.buildAllnodes();
		//on génère le fichier pnml contenant le réseau construit
		manip.generate_file("Insertion_Panne_Can_Size"+size);
	}
}
