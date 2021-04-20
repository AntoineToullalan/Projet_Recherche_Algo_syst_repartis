package clientserver;
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
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

public class Transition {
	private TransitionHLAPI transition;
	public Transition(PageHLAPI syspage,String name,int x,int y,CSS2Color color) {
		try {
			transition = new TransitionHLAPI(name,syspage);
			NodeGraphicsHLAPI pg3 = new NodeGraphicsHLAPI(transition);
			PositionHLAPI pos3 = new PositionHLAPI(x,y,pg3);
			DimensionHLAPI dim3 = new DimensionHLAPI(10,25,pg3);
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
	public TransitionHLAPI getTransition() {
		return transition;
	}
}
