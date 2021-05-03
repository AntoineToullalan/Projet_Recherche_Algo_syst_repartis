package multiplesextensionleafset;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;



public class Main {
	public static void main (String[] args) {
		
		int size;
		int nb_breakdown;
		String nameFile;
		try {
			//on récupère l'argument : size
			size  = Integer.parseInt(args[0]);
			nb_breakdown = Integer.parseInt(args[1]);
			nameFile=args[2];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ./cliser_os size nb_breakdown");			
			return;
		}
		
		System.out.println("Extension of a Leafset ");
		
		//on créer une instance de PNMLManipulation 
		PNMLManipulation manip = new PNMLManipulation(400,400);
		//on crée un réseau de petri
		LeafSet leafset = new LeafSet(size+1,nb_breakdown,manip);
		leafset.buildAllLeafSet();
		//on génère le fichier pnml contenant le réseau construit
		manip.generate_file(nameFile);
	}
}
