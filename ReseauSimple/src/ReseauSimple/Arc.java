package clientserver;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.AnnotationGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.DimensionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.OffsetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.LineHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

public class Arc {
	public static int cpt= 0;
	private ArcHLAPI arc;
	public Arc(PageHLAPI syspage,Place place, Transition trans) {
		try {

			arc = new ArcHLAPI("arc"+cpt,place.getPlace(),trans.getTransition(),syspage);
			cpt+=1;
			LineHLAPI al1 = new LineHLAPI(new ArcGraphicsHLAPI(arc));
		}
		catch(InvalidIDException e) {
			e.printStackTrace();
		}
		catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	public ArcHLAPI getArc() {
		return arc;
	}
}
