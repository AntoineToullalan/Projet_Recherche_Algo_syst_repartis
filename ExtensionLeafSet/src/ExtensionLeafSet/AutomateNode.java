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
import java.util.ArrayList;

public class AutomateNode {
	private int num,size,x,y,master;
	private String name;
	private PNMLManipulation manip;
	private PlaceHLAPI PrincipalNode;
	private TransitionHLAPI brokDown;
	private ArrayList<TransitionHLAPI> transition1,transition2,transition3Left,transition3Right,transition4Left,transition4Right,transition5Left,transition5Right;

	public AutomateNode(int x,int y,int num,int size,PNMLManipulation manip) {
		this.num=num;
		master=size/2;
		name="Node"+num;
		this.size=size;
		this.x=x;
		this.y=y;
		this.manip=manip;
		transition1=new ArrayList<TransitionHLAPI>();
		transition2=new ArrayList<TransitionHLAPI>();
		transition3Left=new ArrayList<TransitionHLAPI>();
		transition3Right=new ArrayList<TransitionHLAPI>();
		transition4Left=new ArrayList<TransitionHLAPI>();
		transition4Right=new ArrayList<TransitionHLAPI>();
		transition5Left=new ArrayList<TransitionHLAPI>();
		transition5Right=new ArrayList<TransitionHLAPI>();

	}
	public void buildAutomate() {
		manip.place(name+"IsActive",x+master*300,y,CSS2Color.BLACK,true);
		PrincipalNode=manip.getPlace();
		manip.transition(name+"BreaksDown",x+master*300,y-100,CSS2Color.GRAY);
		brokDown=manip.getTransition();
		manip.arc(true);
		manip.place(name+"Failure",x+master*300,x-180,CSS2Color.GRAY,false);
		manip.arc(false);
		int y_init=y+100;
		for(int i=0;i<size;i++) {
			if(i==num) {
				i+=1;
			}
			buildBranch(x+i*300,y_init,i);
			
		}
	}
	public void buildBranch(int xBranch,int yBranch,int iBranch) {
		String nameBranch="Node"+iBranch;
		String nameTransition;
		String namePlace;
		
		nameTransition=name+"DetectsBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
		transition1.add(manip.getTransition());
		yBranch+=100;
		manip.arc(false,PrincipalNode,manip.getTransition());
		manip.arc(true,PrincipalNode,manip.getTransition());
		
		namePlace=name+"WantsToManageTheBreakDownOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLUE,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"GetTheRightToManageTheBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLACK);
		transition2.add(manip.getTransition());
		yBranch+=100;
		manip.arc(true);
		
		if(iBranch==master && (num==master-1 || num==master+1)) {
			namePlace=name+"isTheNodeMaster";
			manip.place(namePlace,xBranch-30,yBranch-50,CSS2Color.BLACK,false);
			manip.arc(false);
		}
		
		namePlace=name+"ManageTheBreakDownOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLACK,false);
		yBranch+=100;
		manip.arc(false);
		
		String LxOrRx="Lx";
		boolean lx=true;
		if(iBranch<master) {
			LxOrRx="Lx";
		}
		else if(iBranch>master) {
			LxOrRx="Rx";
			lx=false;
		}
		else {
			if(num<master) {
				LxOrRx="Lx";
			}
			else if(num>master) {
				LxOrRx="Rx";
				lx=false;
			}
		}
		nameTransition=name+"AsksItsLeafSetTo"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
		yBranch+=100;
		manip.arc(true);
		if(lx) {
			transition3Left.add(manip.getTransition());
		}
		else {
			transition3Right.add(manip.getTransition());
		}
		
		namePlace=name+"HasAskedItsLeafSetTo"+LxOrRx+"ToReplace"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLUE,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"ReceiveTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLACK);
		yBranch+=100;
		manip.arc(true);
		if(lx) {
			transition4Left.add(manip.getTransition());
		}
		else {
			transition4Right.add(manip.getTransition());
		}
		
		namePlace=name+"HasTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLACK,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"SelectsANodeOfTheLeafSetOf"+LxOrRx+"ToReplace"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
		yBranch+=100;
		manip.arc(true);

		if(lx) {
			transition5Left.add(manip.getTransition());
		}
		else {
			transition5Right.add(manip.getTransition());
		}
		
		namePlace=nameBranch+"NodeFailureProcessedBy"+name;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLUE,false);
		manip.arc(false);
	}
	public TransitionHLAPI getBreaksDown() {
		return brokDown;
	}
	public ArrayList<TransitionHLAPI> getTabDetectsBreakDown() {
		return transition1;
	}
	public ArrayList<TransitionHLAPI> getTabGetTheRight() {
		return transition2;
	}
	public ArrayList<TransitionHLAPI> getTabAsksLeafSetToLx() {
		return transition3Left;
	}
	public ArrayList<TransitionHLAPI> getTabAsksLeafSetToRx() {
		return transition3Right;
	}
	public ArrayList<TransitionHLAPI> getTabReceiveLeafSetofLx() {
		return transition4Left;
	}
	public ArrayList<TransitionHLAPI> getTabReceiveLeafSetofRx() {
		return transition4Right;
	}
	public ArrayList<TransitionHLAPI> getTabSelectANodeofLx() {
		return transition5Left;
	}
	public ArrayList<TransitionHLAPI> getTabSelectANodeofRx() {
		return transition5Right;
	}
}
