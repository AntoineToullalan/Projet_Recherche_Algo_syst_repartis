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
//AutomateNode est une classe permettant de créer les automates pour chaque 
//noeud dans un LeafSet.
//=========================================================================
public class AutomateNode {
	private int num,size,x,y,master,nb_breakdown;
	private String name;
	private PNMLManipulation manip;
	private PlaceHLAPI PrincipalNode,IsMaster;
	private ArrayList<PlaceHLAPI> p1,p2,p3,p4;
	private TransitionHLAPI brokDown,newMaster,detectNewMaster;
	private Hashtable<String,TransitionHLAPI> transition1,transition2;
	private ArrayList<TransitionHLAPI>[] transition3Left,transition3Right,transition4Left,transition4Right,transition5Left,transition5Right;
	
	//=========================================================================
	//transition1,transition2,transition3Left,transition3Right,transition4Left,
	//transition4Right,transition5Left,transition5Right servent à conserver les transitions qui
	//vont servir à communiquer entre transitions.
	//=========================================================================
	public AutomateNode(int x,int y,int num,int size,int nb_breakdown,PNMLManipulation manip) {
		this.num=num;
		master=size/2;
		name="Node"+num;
		this.size=size;
		this.x=x;
		this.y=y;
		this.nb_breakdown=nb_breakdown;
		this.manip=manip;
		p1=new ArrayList<PlaceHLAPI>();
		p2=new ArrayList<PlaceHLAPI>();
		p3=new ArrayList<PlaceHLAPI>();
		p4=new ArrayList<PlaceHLAPI>();
		transition1=new Hashtable<String,TransitionHLAPI>();
		transition2=new Hashtable<String,TransitionHLAPI>();
		transition3Left=new ArrayList[nb_breakdown];
		transition3Right=new ArrayList[nb_breakdown];
		transition4Left=new ArrayList[nb_breakdown];
		transition4Right=new ArrayList[nb_breakdown];
		transition5Left=new ArrayList[nb_breakdown];
		transition5Right=new ArrayList[nb_breakdown];
		for(int i=0;i<nb_breakdown;i++) {
			transition3Left[i]=new ArrayList<TransitionHLAPI>();
			transition3Right[i]=new ArrayList<TransitionHLAPI>();
			transition4Left[i]=new ArrayList<TransitionHLAPI>();
			transition4Right[i]=new ArrayList<TransitionHLAPI>();
			transition5Left[i]=new ArrayList<TransitionHLAPI>();
			transition5Right[i]=new ArrayList<TransitionHLAPI>();
		}

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
		for(int k=0;k<nb_breakdown;k++) {
			manip.place("RequestOf"+name+"IsSentToLx"+k,x+k*300,y+1200,CSS2Color.RED,false);
			p1.add(manip.getPlace());
			manip.place("LeafSetOfLx"+k+"IsSentTo"+name,x+k*300,y+1300,CSS2Color.RED,false);
			p2.add(manip.getPlace());
			manip.place("RequestOf"+name+"IsSentToRx"+k,x+(size-2+k)*300,y+1200,CSS2Color.RED,false);
			p3.add(manip.getPlace());
			manip.place("LeafSetOfRx"+k+"IsSentTo"+name,x+(size-2+k)*300,y+1300,CSS2Color.RED,false);
			p4.add(manip.getPlace());
			for(int i=0;i<transition3Left[k].size();i++) {
				manip.arc(false,p1.get(k),transition3Left[k].get(i));
			}
			for(int i=0;i<transition3Right[k].size();i++) {
				manip.arc(false,p3.get(k),transition3Right[k].get(i));
			}
			for(int i=0;i<transition4Left[k].size();i++) {
				manip.arc(true,p2.get(k),transition4Left[k].get(i));
			}
			for(int i=0;i<transition4Right[k].size();i++) {
				manip.arc(true,p4.get(k),transition4Right[k].get(i));
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
		PlaceHLAPI origine=manip.getPlace();
		yBranch+=100;
		manip.arc(false);
		
		namePlace=nameBranch+"FailureProcessedBy"+name;
		manip.place(namePlace,xBranch,yBranch+2000,CSS2Color.BLUE,false);
		PlaceHLAPI fin = manip.getPlace();
		
		String LxOrRx="Lx";
		boolean lx=true;
		
		for(int i=0;i<nb_breakdown;i++) {
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
			LxOrRx=LxOrRx+i;
			nameTransition=name+"AsksItsLeafSetTo"+LxOrRx+"ToReplace"+nameBranch;
			manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
			yBranch+=100;
			manip.arc(true,origine,manip.getTransition());
			if(lx) {
				transition3Left[i].add(manip.getTransition());
			}
			else {
				transition3Right[i].add(manip.getTransition());
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
				transition4Left[i].add(manip.getTransition());
			}
			else {
				transition4Right[i].add(manip.getTransition());
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
				transition5Left[i].add(manip.getTransition());
			}
			else {
				transition5Right[i].add(manip.getTransition());
			}
			
			manip.arc(false,fin,manip.getTransition());
		}
		
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
	public ArrayList<TransitionHLAPI> getTabSelectANodeofLx(int i) {
		return transition5Left[i];
	}
	public ArrayList<TransitionHLAPI> getTabSelectANodeofRx(int i) {
		return transition5Right[i];
	}
	public ArrayList<TransitionHLAPI> getExtLeft(int i) {
		return transition3Left[i];
	}
	public ArrayList<TransitionHLAPI> getExtRight(int i) {
		return transition3Right[i];
	}
	public ArrayList<PlaceHLAPI>[] getCommWithExtremity() {
		ArrayList[] res ={p1,p2,p3,p4};
		return res;
	}
}
