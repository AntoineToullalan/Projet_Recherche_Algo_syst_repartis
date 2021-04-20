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

public class PNMLManipulation {
	private PageHLAPI syspage;
	private PlaceHLAPI place;
	private TransitionHLAPI transition;
	private ArcHLAPI arc;
	public static int cpt_arc= 0;
	
	public PNMLManipulation(PageHLAPI syspage) {
		this.syspage=syspage;
	}
	public void place(String name,int x,int y,CSS2Color color, boolean jeton) {
		try {
			place = new PlaceHLAPI(name,syspage);
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
	public void transition(String name,int x,int y,CSS2Color color) {
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
	
	public void arc(boolean placeVersTransition) {
		try {
			if(placeVersTransition) {
				arc = new ArcHLAPI("arc"+cpt_arc,place,transition,syspage);
			}
			else {
				arc = new ArcHLAPI("arc"+cpt_arc,transition,place,syspage);
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
