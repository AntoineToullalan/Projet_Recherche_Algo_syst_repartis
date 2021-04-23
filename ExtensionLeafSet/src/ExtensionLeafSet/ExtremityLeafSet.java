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
//ExtremityLeafSet construit les automates de Gx et Dx
//=========================================================================
public class ExtremityLeafSet {
	private int x,y,size;
	private PNMLManipulation manip;
	private PlaceHLAPI principalNode;
	private TransitionHLAPI activeInLeafSetLeft,activeInLeafSetRight;
	private Hashtable<String,TransitionHLAPI> transitionLx1,transitionLx2,transitionRx1,transitionRx2;
	public ExtremityLeafSet(int x,int y,int size,PNMLManipulation manip) {
		transitionLx1=new Hashtable<String,TransitionHLAPI>();
		transitionLx2=new Hashtable<String,TransitionHLAPI>();
		transitionRx1=new Hashtable<String,TransitionHLAPI>();
		transitionRx2=new Hashtable<String,TransitionHLAPI>();
		this.x=x;
		this.y=y;
		this.size=size;
		this.manip=manip;
	}
	
	public void buildExtremity() {
		int y_init=y;
		int x_init=x;
		x=(size/2)*300;
		LDx(true);
		x=x_init-size*300;
		y=y_init;
		LDx(false);
	}
	
	//=========================================================================
	//LDx construit l'automate de Gx si LxOrRx vaut true, Dx sinon
	//=========================================================================
	public void LDx(boolean LxOrRx) {
		String name;
		int xplace=0;
		if(LxOrRx) {
			xplace=x+(size/2-1)*300;
			name="Lx";
		}
		else {
			xplace=x+(size/2+1)*300;
			name="Rx";
		}
		manip.place(name+"IsActiveAndNotInTheLeafSet",xplace,y,CSS2Color.BLACK,true);
		principalNode=manip.getPlace();
		
		manip.transition(name+"EntersTheLeafSet",xplace,y-100,CSS2Color.BLACK);
		if(LxOrRx) {
			activeInLeafSetLeft=manip.getTransition();
		}
		else {
			activeInLeafSetRight=manip.getTransition();
		}
		manip.arc(true);
		
		manip.place(name+"IsActiveInTheLeafSet",xplace,y-200,CSS2Color.BLACK,false);
		manip.arc(false);
		y+=100;
		for(int i=0;i<size;i++) {
			buildBranchLDx(LxOrRx,x+i*300,y+100,i);
		}
		
	}
	
	//=========================================================================
	//buildBranchLDx construit 1 branche dans l'automate de Gx ou Dx
	//=========================================================================
	public void buildBranchLDx(boolean LxOrRx,int xBranch,int yBranch,int iBranch) {
		String name;
		String nameBranch="Node"+iBranch;
		if(LxOrRx) {
			name="Lx";
		}
		else {
			name="Rx";
		}
		manip.transition(name+"ReceiveARequestToSendItsLeafSetTo"+nameBranch,xBranch,yBranch,CSS2Color.BLUE);
		if(LxOrRx) {
			transitionLx1.put(nameBranch,manip.getTransition());
		}
		else {
			transitionRx1.put(nameBranch,manip.getTransition());
		}
		
		yBranch+=100;
		manip.arc(false,principalNode,manip.getTransition());
		manip.arc(true,principalNode,manip.getTransition());
		
		manip.place(name+"HasReceivedTheRequestOf"+nameBranch,xBranch,yBranch,CSS2Color.BLUE,false);
		yBranch+=100;
		manip.arc(false);
		
		manip.transition(name+"SendsItsLeafSetTo"+nameBranch,xBranch,yBranch,CSS2Color.BLUE);
		manip.arc(true);
		if(LxOrRx) {
			transitionLx2.put(nameBranch,manip.getTransition());
		}
		else {
			transitionRx2.put(nameBranch,manip.getTransition());
		}
		
	}
	public TransitionHLAPI getActiveInLeafSetLeft() {
		return activeInLeafSetLeft;
	}
	public TransitionHLAPI getActiveInLeafSetRight() {
		return activeInLeafSetRight;
	}
	public Hashtable<String,TransitionHLAPI>[] getCommAutomate(){
		Hashtable<String,TransitionHLAPI>[] res = new Hashtable[4];
		res[0]=transitionLx1;
		res[1]=transitionLx2;
		res[2]=transitionRx1;
		res[3]=transitionRx2;
		return res;
	}
}
