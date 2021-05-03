package multiplesextensionleafset;
import java.io.File;
import java.io.IOException;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import java.util.ArrayList;
import java.util.Hashtable;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;

//=========================================================================
//ExtremityLeafSet construit les automates de Gx1, Dx1,Gx2, Dx2...
//=========================================================================
public class ExtremityLeafSet {
	private int x,y,size,nb_breakdown;
	private PNMLManipulation manip;
	private PlaceHLAPI principalNode;
	private PlaceHLAPI[] CommNodeBreakLeft,CommNodeBreakRight;
	private TransitionHLAPI[] activeInLeafSetLeft,activeInLeafSetRight;
	private Hashtable<String,TransitionHLAPI>[] transitionLx1,transitionLx2,transitionRx1,transitionRx2;
	public ExtremityLeafSet(int x,int y,int size,int nb_breakdown,PNMLManipulation manip) {
		transitionLx1=new Hashtable[nb_breakdown];
		transitionLx2=new Hashtable[nb_breakdown];
		transitionRx1=new Hashtable[nb_breakdown];
		transitionRx2=new Hashtable[nb_breakdown];
		for(int i=0;i<nb_breakdown;i++) {
			transitionLx1[i]=new Hashtable<String,TransitionHLAPI>();
			transitionLx2[i]=new Hashtable<String,TransitionHLAPI>();
			transitionRx1[i]=new Hashtable<String,TransitionHLAPI>();
			transitionRx2[i]=new Hashtable<String,TransitionHLAPI>();
		}
		this.x=x;
		this.y=y;
		this.size=size;
		this.manip=manip;
		this.nb_breakdown=nb_breakdown;
		activeInLeafSetLeft=new TransitionHLAPI[nb_breakdown];
		activeInLeafSetRight=new TransitionHLAPI[nb_breakdown];
		CommNodeBreakLeft=new PlaceHLAPI[nb_breakdown];
		CommNodeBreakRight=new PlaceHLAPI[nb_breakdown];
	}
	
	public void buildExtremity() {
		int y_init=y;
		int x_init=x;
		x=(size/2)*300;
		for(int i=0;i<nb_breakdown;i++) {
			LDx(true,i);
			x+=(size*300);
		}
		x=x_init-size*300;
		y=y_init;
		for(int i=0;i<nb_breakdown;i++) {
			LDx(false,i);
			x-=(size*300);
		}
	}
	
	//=========================================================================
	//LDx construit l'automate de Gxi si LxOrRx vaut true, Dxi sinon
	//=========================================================================
	private void LDx(boolean LxOrRx,int i) {
		String name;
		int xplace=0;
		if(LxOrRx) {
			xplace=x+(size/2-1)*300;
			name="Lx"+i;
		}
		else {
			xplace=x+(size/2+1)*300;
			name="Rx"+i;
		}
		manip.place(name+"IsActiveAndNotInTheLeafSet",xplace,y,CSS2Color.BLACK,true);
		principalNode=manip.getPlace();
		
		manip.transition(name+"EntersTheLeafSet",xplace,y-100,CSS2Color.BLACK);
		if(LxOrRx) {
			activeInLeafSetLeft[i]=manip.getTransition();
		}
		else {
			activeInLeafSetRight[i]=manip.getTransition();
		}
		manip.arc(true);
		
		manip.place(name+"IsActiveInTheLeafSet",xplace,y-200,CSS2Color.BLACK,false);
		manip.arc(false);
		y+=100;
		for(int j=0;j<size;j++) {
			buildBranchLDx(LxOrRx,x+j*300,y+100,j,i);
		}
		if(LxOrRx) {
			manip.place(name+"IsAtTheLeftExtremityOfTheLeafSet",xplace,y,CSS2Color.RED,i==0);
			CommNodeBreakLeft[i]=manip.getPlace();
			manip.arc(true, CommNodeBreakLeft[i], activeInLeafSetLeft[i]);
			if(i>0) {
				manip.arc(false, CommNodeBreakLeft[i], activeInLeafSetLeft[i-1]);
			}
		}
		else {
			manip.place(name+"IsAtTheRightExtremityOfTheLeafSet",xplace,y,CSS2Color.RED,i==0);
			CommNodeBreakRight[i]=manip.getPlace();
			manip.arc(true, CommNodeBreakRight[i], activeInLeafSetRight[i]);
			if(i>0) {
				manip.arc(false, CommNodeBreakRight[i], activeInLeafSetRight[i-1]);
			}
		}
		
	}
	
	//=========================================================================
	//buildBranchLDx construit 1 branche gérant l'envoi de son LeafSet au Node d'id iBranch
	//dans l'automate de Gxi ou Dxi
	//=========================================================================
	private void buildBranchLDx(boolean LxOrRx,int xBranch,int yBranch,int iBranch,int i) {
		String name;
		String nameBranch="Node"+iBranch;
		if(LxOrRx) {
			name="Lx"+i;
		}
		else {
			name="Rx"+i;
		}
		manip.transition(name+"ReceiveARequestToSendItsLeafSetTo"+nameBranch,xBranch,yBranch,CSS2Color.BLUE);
		if(LxOrRx) {
			transitionLx1[i].put(nameBranch,manip.getTransition());
		}
		else {
			transitionRx1[i].put(nameBranch,manip.getTransition());
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
			transitionLx2[i].put(nameBranch,manip.getTransition());
		}
		else {
			transitionRx2[i].put(nameBranch,manip.getTransition());
		}
		
	}
	public TransitionHLAPI getActiveInLeafSetLeft(int i) {
		return activeInLeafSetLeft[i];
	}
	public TransitionHLAPI getActiveInLeafSetRight(int i) {
		return activeInLeafSetRight[i];
	}
	public Hashtable<String,TransitionHLAPI>[][] getCommAutomate(){
		Hashtable<String,TransitionHLAPI>[][] res = new Hashtable[4][];
		res[0]=transitionLx1;
		res[1]=transitionLx2;
		res[2]=transitionRx1;
		res[3]=transitionRx2;
		return res;
	}
	public PlaceHLAPI getCommNodeBreakLeft(int i) {
		return CommNodeBreakLeft[i];
	}
	public PlaceHLAPI getCommNodeBreakRight(int i) {
		return CommNodeBreakRight[i];
	}
}
