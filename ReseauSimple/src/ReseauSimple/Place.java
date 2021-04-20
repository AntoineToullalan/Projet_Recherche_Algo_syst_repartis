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

public class Place {
	private PlaceHLAPI place;
	public Place(PageHLAPI syspage,String name,int x,int y,CSS2Color color, boolean jeton) {
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
	public PlaceHLAPI getPlace() {
		return place;
	}

}
