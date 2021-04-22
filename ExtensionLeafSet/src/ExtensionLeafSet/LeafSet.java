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
import java.util.Hashtable;

import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;

public class LeafSet {
	private PNMLManipulation manip;
	private int x,y,size,x_centre;
	private PlaceHLAPI reservoir;
	private AutomateNode[] automatesNode;
	private PlaceHLAPI[][] placesCommNodes;
	
	public LeafSet(int size,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		x=700;
		y=700;
		x_centre=x+(size*(size-1)*300)/2;
		manip.place("BreakDownReservoir",x_centre,200,CSS2Color.GREEN,true);
		reservoir=manip.getPlace();
		automatesNode=new AutomateNode[size];
		placesCommNodes=new PlaceHLAPI[size][2];
	}
	public void buildAllLeafSet() {
		buildAutomatesNodes();
		ExtremityLeafSet extremite=new ExtremityLeafSet(x_centre-size*300,y+2000,size,manip);
		extremite.buildExtremity();
	}
	public void buildAutomatesNodes() {
		//on créer les automates des noeuds
		for(int i=0;i<size;i++) {
			automatesNode[i]=new AutomateNode(x,y,i,size,manip);
			automatesNode[i].buildAutomate();
			manip.place("Node"+i+"DontAnswerToAnyNode",x-400,y+200,CSS2Color.RED,false);
			placesCommNodes[i][0]=manip.getPlace();
			manip.place("NoNodeManageTheBreakDownOfNode"+i,x-400,y+400,CSS2Color.RED,false);
			placesCommNodes[i][1]=manip.getPlace();
			x+=300*size;
		}
		PlaceHLAPI leftNewMaster=null;
		PlaceHLAPI rightNewMaster=null;
		if(size>3) {
			manip.place("LeftNodeHasDetectedBreakDownOfMaster",x/2-100,1000,CSS2Color.RED,false);
			leftNewMaster=manip.getPlace();
		}
		if(size>4) {
			manip.place("RightNodeHasDetectedBreakDownOfMaster",x/2+100,1000,CSS2Color.RED,false);
			rightNewMaster=manip.getPlace();
		}
		
		//on crée les connections entre les automates
		TransitionHLAPI transitionBreakDown;
		AutomateNode automate;
		Hashtable<String,TransitionHLAPI> tableDetectsBreakdown;
		Hashtable<String,TransitionHLAPI> tableGetTheRight;
		for(int i=0;i<size;i++) {
			automate=automatesNode[i];
			transitionBreakDown=automate.getBreaksDown();
			manip.arc(true,reservoir,transitionBreakDown);
			manip.arc(false,placesCommNodes[i][0],transitionBreakDown);
			manip.arc(false,placesCommNodes[i][1],transitionBreakDown);
			tableDetectsBreakdown=automate.getTabDetectsBreakDown();
			tableGetTheRight=automate.getTabGetTheRight();
			for(int j=0;j<size;j++) {
				if(j!=i) {
					manip.arc(true,placesCommNodes[j][0],tableDetectsBreakdown.get("Node"+j));
					manip.arc(true,placesCommNodes[j][1],tableGetTheRight.get("Node"+j));
				}	
			}
			if(size>4) {
				
				if(i==size/2+1) {
					manip.arc(true,rightNewMaster,automate.getnewMaster());
				}
				else if(i>size/2){
					manip.arc(false,rightNewMaster,automate.getdetectNewMaster());
				}
				
			}
			if(size>3) {
				if(i==size/2-1) { 
					manip.arc(true,leftNewMaster,automate.getnewMaster());
				}
				else if(i<size/2){
					manip.arc(false,leftNewMaster,automate.getdetectNewMaster());
				}
			}
		}
	
	}

}
