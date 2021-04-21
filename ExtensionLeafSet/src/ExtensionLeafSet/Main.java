package extensionleafset;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.ptnet.hlapi.AnnotationGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.DimensionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.LineHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.OffsetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import java.io.File;
import java.io.IOException;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;


public class Main {
	public static void main (String[] args) {
		
		int num;
		int size;
		int x;
		int y;
		
		try {
			//on récupère les arguments : num,size,x,y
			num = Integer.parseInt(args[0]);
			size  = Integer.parseInt(args[1]);
			x = Integer.parseInt(args[2]);
			y  = Integer.parseInt(args[3]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ./cliser_os num size x y");			
			return;
		}
		
		System.out.println("Extension of a Leafset ");
		
		//on créer une instance de PNMLManipulation 
		PNMLManipulation manip = new PNMLManipulation(x,y);
		//on crée un réseau de petri
		AutomateNode automate = new AutomateNode(x,y,num,size,manip);
		automate.buildAutomate();
		//on génère le fichier pnml contenant le réseau construit
		manip.generate_file("ExtensioLeafSet_Size"+size);
	}
}
