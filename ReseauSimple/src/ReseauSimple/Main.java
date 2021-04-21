package reseausimple;
public class Main {
	public static void main (String[] args) {
		
		int nb;
		int length;
		int x;
		int y;
		
		try {
			//on récupère les arguments : nb,length,x,y
			nb = Integer.parseInt(args[0]);
			length  = Integer.parseInt(args[1]);
			x = Integer.parseInt(args[2]);
			y  = Integer.parseInt(args[3]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ./cliser_os nb length");			
			return;
		}
		
		System.out.println("Pretty Simple Model ");
		//on créer une instance de PNMLManipulation 
		
		PNMLManipulation manip = new PNMLManipulation(x,y);
		//on crée un réseau de petri
		manip.init(nb,length);
		//on génère le fichier pnml contenant le réseau construit
		manip.generate_file();
	}
}
