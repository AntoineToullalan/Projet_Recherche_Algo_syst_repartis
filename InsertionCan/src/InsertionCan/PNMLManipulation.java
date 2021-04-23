package insertioncan;
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

// =========================================================================
//PNMLManipulation est une classe utilisant la bibliothèque pnmlframework 
// qui permet de créer des reseaux de Petri de manière plus intuitive qu'avec
// les fonctions de pnmlframework.
// =========================================================================


public class PNMLManipulation {
	private PageHLAPI page;
	private PlaceHLAPI place;
	private TransitionHLAPI transition;
	private ArcHLAPI arc;
	public static int cpt_arc= 0;
	private int x,y,num,size;
	private PetriNetDocHLAPI doc;

	// =======================================================================
	//le constructeur crée la page sur laquelle le réseau sera construit
	// x et y indique ou sera le coin supérieur gauche du réseau
	// =========================================================================
	public PNMLManipulation(int x,int y) {
		try {
			ModelRepository.getInstance().createDocumentWorkspace("generator");
			doc = new PetriNetDocHLAPI();
			PetriNetHLAPI net = new PetriNetHLAPI("gen", PNTypeHLAPI.PTNET, new NameHLAPI("gen"), doc);
			page = new PageHLAPI("toppage", new NameHLAPI("gen"), null, net);
			this.x=x;
			this.y=y;
		}
		
		//catch (InvalidIDException e) {
			//System.out.println("InvalidIDException caught by while running generator");
			//e.printStackTrace();}
		catch (VoidRepositoryException e) {
			System.out.println("VoidRepositoryException caught by while running generator");
			e.printStackTrace();	
		} 
		//catch (IOException e) {
			//System.out.println(e.getMessage());
			//e.printStackTrace();}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public PNMLManipulation() {
		this(0,0);
	}
	
	// =========================================================================
	//exemple permet de faire un réseau composé de n suite de couples (place,transition) de longueur m 
	// =========================================================================
	public void exemple(int n,int m) {
		
		int y_init=y;
		for(int i=0;i<n;i++) {
			y=y_init;
			x+=50;
			for(int j=0;j<m;j++) {
				place("place"+i+"_"+j);
				if(j!=0) {
					arc(false);
				}
				transition("transition"+i+"_"+j);
				arc(true);
			}
		}
	}
	
	// =========================================================================
	//generate_file (comme son nom l'indique) permet de créer le fichier .pnml avec le réseau construit
	// =========================================================================
	public void generate_file(String nameFile) {	
		try {
			//System.out.println("Everything generated");
			File dir = new File (System.getProperty("user.dir")+"/testmodel");
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			PnmlExport pex = new PnmlExport();
			pex.exportObject(doc,"testmodel/"+nameFile+".pnml");
			System.out.println("File "+nameFile+".pnml exported to testmodel directory.");
			ModelRepository.getInstance().destroyCurrentWorkspace();
		} 
		//catch (InvalidIDException e) {
			//System.out.println("InvalidIDException caught by while running generator");
			//e.printStackTrace();}
		catch (VoidRepositoryException e) {
			System.out.println("VoidRepositoryException caught by while running generator");
			e.printStackTrace();	
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// =========================================================================
	// place créer une place dans la page de nom "name", couleur "color" et de coordonnées
	//(x,y), l'argument jeton indique si la place contient un jeton ou non.
	// =========================================================================
	public void place(String name,int x,int y,CSS2Color color, boolean jeton) {
		
		try {
			place = new PlaceHLAPI(name,page);
			if(jeton) {
				place.setInitialMarkingHLAPI(new PTMarkingHLAPI(1L));
			}	
			NodeGraphicsHLAPI pg1 = new NodeGraphicsHLAPI(place);
			PositionHLAPI pos1 = new PositionHLAPI(x,y,pg1);
			DimensionHLAPI dim1 = new DimensionHLAPI(25,25,pg1);
			OffsetHLAPI o1 = new OffsetHLAPI(-12,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(place.getId(),place)));
			OffsetHLAPI omk = new OffsetHLAPI(-5,-10,new AnnotationGraphicsHLAPI(place.getInitialMarkingHLAPI()));
			LineHLAPI l1 = new LineHLAPI(pg1);
			l1.setColorHLAPI(color);
			
		}
		catch(InvalidIDException e) {
			e.printStackTrace();
		}
		catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	
	// =========================================================================
	// place créer une place dans la page de nom "name", couleur "color" et de coordonnées
	//(x,y).
	// =========================================================================
	public void transition(String name,int x,int y,CSS2Color color) {
		
		try {
			transition = new TransitionHLAPI(name,page);
			NodeGraphicsHLAPI pg3 = new NodeGraphicsHLAPI(transition);
			PositionHLAPI pos3 = new PositionHLAPI(x,y,pg3);
			DimensionHLAPI dim3 = new DimensionHLAPI(25,25,pg3);
			OffsetHLAPI o3 = new OffsetHLAPI(-30,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(transition.getId(),transition)));
			LineHLAPI l3 = new LineHLAPI(pg3);
			l3.setColorHLAPI(color);
			
		}
		catch(InvalidIDException e) {
			e.printStackTrace();
		}
		catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	
	// =========================================================================
	//arc crée un arc entre la place p et la transition t, la direction de l'arc est indiqué
	// par placeVersTransition, qui vaut true si l'arc va de la place vers la transition
	// =========================================================================
	public void arc(boolean placeVersTransition,PlaceHLAPI p,TransitionHLAPI t) {
		
		try {
			if(placeVersTransition) {
				arc = new ArcHLAPI("arc"+cpt_arc,p,t,page);
			}
			else {
				arc = new ArcHLAPI("arc"+cpt_arc,t,p,page);
			}
			cpt_arc+=1;
			LineHLAPI al1 = new LineHLAPI(new ArcGraphicsHLAPI(arc));
		}
		catch(InvalidIDException e) {
			e.printStackTrace();
		}
		catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	public void place(String name) {
		place(name,x,y,CSS2Color.BLACK, false);
		y+=100;
	}
	public void transition(String name) {
		transition(name,x,y,CSS2Color.BLACK);
		y+=100;
	}
	public void arc(boolean placeVersTransition) {
		arc(placeVersTransition,place,transition);
	}
	public TransitionHLAPI getTransition() {
		return transition;
	}
	public PlaceHLAPI getPlace() {
		return place;
	}
	public ArcHLAPI getArc() {
		return arc;
	}

}
