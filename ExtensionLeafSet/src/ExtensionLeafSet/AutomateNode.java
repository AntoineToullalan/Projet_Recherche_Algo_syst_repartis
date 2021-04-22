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
import java.util.Hashtable;

//=========================================================================
//AutomateNode est une classe permettant de créer les automates pour chaque 
//noeud dans un LeafSet.
//=========================================================================
public class AutomateNode {
	private int num,size,x,y,master;
	private String name;
	private PNMLManipulation manip;
	private PlaceHLAPI PrincipalNode,IsMaster;
	private TransitionHLAPI brokDown,newMaster,detectNewMaster;
	private Hashtable<String,TransitionHLAPI> transition1,transition2;
	private ArrayList<TransitionHLAPI> transition3Left,transition3Right,transition4Left,transition4Right,transition5Left,transition5Right;
	
	//=========================================================================
	//transition1,transition2,transition3Left,transition3Right,transition4Left,
	//transition4Right,transition5Left,transition5Right servent à conserver les transitions qui
	//vont servir à communiquer entre transitions.
	//=========================================================================
	public AutomateNode(int x,int y,int num,int size,PNMLManipulation manip) {
		this.num=num;
		master=size/2;
		name="Node"+num;
		this.size=size;
		this.x=x;
		this.y=y;
		this.manip=manip;
		transition1=new Hashtable<String,TransitionHLAPI>();
		transition2=new Hashtable<String,TransitionHLAPI>();
		transition3Left=new ArrayList<TransitionHLAPI>();
		transition3Right=new ArrayList<TransitionHLAPI>();
		transition4Left=new ArrayList<TransitionHLAPI>();
		transition4Right=new ArrayList<TransitionHLAPI>();
		transition5Left=new ArrayList<TransitionHLAPI>();
		transition5Right=new ArrayList<TransitionHLAPI>();

	}
	
	//=========================================================================
	//buildAutomate va construire l'automate pour le noeud num avec les size-1 branches 
	//=========================================================================
	public void buildAutomate() {
		manip.place(name+"IsActive",x+master*100,y,CSS2Color.BLACK,true);
		PrincipalNode=manip.getPlace();
		manip.transition(name+"BreaksDown",x+master*100,y-100,CSS2Color.GRAY);
		brokDown=manip.getTransition();
		manip.arc(true,PrincipalNode,brokDown);
		if(num==size/2) {
			manip.place(name+"isTheNodeMaster",x+master*100+100,y-100,CSS2Color.YELLOW,true);
			manip.arc(true,manip.getPlace(),brokDown);
		}
		manip.place(name+"Failure",x+master*100,y-180,CSS2Color.GRAY,false);
		manip.arc(false,manip.getPlace(),brokDown);
		int y_init=y+100;
		int j=0;
		if(size>4 && num==size/2+1) {
			manip.transition(name+"DetectsTheBreakDownOfTheNodeMaster",x+(master-1)*300,y-100,CSS2Color.GRAY);
			newMaster=manip.getTransition();
		}
		if(size>3 && num==size/2-1) {
			manip.transition(name+"DetectsTheBreakDownOfTheNodeMaster",x+(master-1)*300,y-100,CSS2Color.GRAY);
			newMaster=manip.getTransition();
		}
		for(int i=0;i<size;i++) {
			if(i!=num) {
				buildBranch(x+j*300,y_init,i);
				j+=1;
			}			
		}
		
	}
	
	//=========================================================================
	//buildBranch va construire 1 branche dans l'automate
	//=========================================================================
	public void buildBranch(int xBranch,int yBranch,int iBranch) {
		String nameBranch="Node"+iBranch;
		String nameTransition;
		String namePlace;
		
		nameTransition=name+"DetectsBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
		transition1.put(nameBranch,manip.getTransition());
		yBranch+=100;
		manip.arc(false,PrincipalNode,manip.getTransition());
		manip.arc(true,PrincipalNode,manip.getTransition());
		if(iBranch==master && (num!=size/2-1 || num!=size/2+1)) {
			detectNewMaster=manip.getTransition();
		}
		
		namePlace=name+"WantsToManageTheBreakDownOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLUE,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"GetTheRightToManageTheBreakDownOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLACK);
		transition2.put(nameBranch,manip.getTransition());
		yBranch+=100;
		manip.arc(true);
		
		if(iBranch==master && (num==master-1 || num==master+1)) {
			namePlace=name+"isTheNodeMaster";
			manip.place(namePlace,xBranch-30,yBranch-50,CSS2Color.BLACK,false);
			manip.arc(false);
			if(size>4 && num==master+1) {
				manip.arc(false,manip.getPlace(),newMaster);
			}
			if(size>3 && num==master-1) {
				manip.arc(false,manip.getPlace(),newMaster);
			}
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
	public TransitionHLAPI getnewMaster() {
		return newMaster;
	}
	public TransitionHLAPI getdetectNewMaster() {
		return detectNewMaster;
	}
	public Hashtable<String,TransitionHLAPI> getTabDetectsBreakDown() {
		return transition1;
	}
	public Hashtable<String,TransitionHLAPI> getTabGetTheRight() {
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
