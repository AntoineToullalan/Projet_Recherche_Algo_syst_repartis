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

public class AutomateNode {
	private int num,size,x,y,master;
	private String name;
	private CSS2Color color;
	private PNMLManipulation manip;
	private PlaceHLAPI PrincipalNode;
	public AutomateNode(int x,int y,int num,int size,CSS2Color color,PNMLManipulation manip) {
		this.num=num;
		master=size/2;
		name="Node"+num;
		this.color=color;
		this.size=size;
		this.x=x;
		this.y=y;
		this.manip=manip;
	}
	public void buildAutomate() {
		manip.place(name+"IsActive",x+master*300,y,color,true);
		PrincipalNode=manip.getPlace();
		manip.transition(name+"BreaksDown",x+master*300-100,y-100,color);
		manip.arc(true);
		manip.place(name+"IsBrokenDown",x+master*300-180,x-180,color,false);
		manip.arc(false);
		int y_init=y+100;
		for(int i=0;i<size;i++) {
			if(i!=num) {
				buildBranch(x+i*300,y_init,i);
			}
		}
	}
	public void buildBranch(int xBranch,int yBranch,int iBranch) {
		String nameBranch="Node"+iBranch;
		String nameTransition;
		String namePlace;
		
		nameTransition=name+"DetectsBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,color);
		yBranch+=100;
		manip.arc(false,PrincipalNode,manip.getTransition());
		manip.arc(true,PrincipalNode,manip.getTransition());
		
		namePlace=name+"WantsToManageTheBreakDownOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,color,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"GetTheRightToManageTheBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,color);
		yBranch+=100;
		manip.arc(true);
		
		if(iBranch==master && (num==master-1 || num==master+1)) {
			namePlace=name+"isTheNodeMaster";
			manip.place(namePlace,xBranch-30,yBranch-50,color,false);
			manip.arc(false);
		}
		
		namePlace=name+"ManageTheBreakDownOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,color,false);
		yBranch+=100;
		manip.arc(false);
		
		String LxOrRx="Lx";
		if(iBranch<master) {
			LxOrRx="Lx";
		}
		else if(iBranch>master) {
			LxOrRx="Rx";
		}
		else {
			if(num<master) {
				LxOrRx="Lx";
			}
			else if(num>master) {
				LxOrRx="Rx";
			}
		}
		nameTransition=name+"AsksItsLeafSetTo"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,color);
		yBranch+=100;
		manip.arc(true);
		
		namePlace=name+"HasAskedItsLeafSetTo"+LxOrRx+"ToReplace"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,color,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"ReceiveTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,color);
		yBranch+=100;
		manip.arc(true);
		
		namePlace=name+"HasTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,color,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"SelectsANodeOfTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,color);
		yBranch+=100;
		manip.arc(true);
	}
}
