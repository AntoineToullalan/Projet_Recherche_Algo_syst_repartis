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
import java.lang.Math;


//=========================================================================
//AutomateNode est une classe permettant de créer les automates pour chaque 
//noeud dans un LeafSet.
//=========================================================================
public class AutomateNode {
	private int num,size,x,y,master,nb_Crash;
	private String name;
	private PNMLManipulation manip;
	private PlaceHLAPI PrincipalNode,IsMaster,temp3,temp2,temp1,isThereMaster;
	private ArrayList<PlaceHLAPI> p1,p2,p3,p4;
	private ArrayList<TransitionHLAPI> tmpJPC;
	private TransitionHLAPI brokDown,newMaster,detectNewMaster;
	private Hashtable<String,TransitionHLAPI> transition1,transition2;
	private Hashtable<Integer,TransitionHLAPI> inputX1,inputX2;
	private Hashtable<Integer,ArrayList> inputX3;
	private ArrayList<TransitionHLAPI[]> inputX3bis;
	private ArrayList<TransitionHLAPI>[] transition3Left,transition3Right,transition4Left,transition4Right,transition5Left,transition5Right;
	
	//=========================================================================
	//transition1,transition2,transition3Left,transition3Right,transition4Left,
	//transition4Right,transition5Left,transition5Right servent à conserver les transitions qui
	//vont servir à communiquer entre transitions.
	//=========================================================================
	public AutomateNode(int x,int y,int num,int size,int nb_Crash,PNMLManipulation manip) {
		this.num=num;
		master=size/2;
		name="Node"+num;
		this.size=size;
		this.x=x;
		this.y=y;
		this.nb_Crash=nb_Crash;
		this.manip=manip;
		p1=new ArrayList<PlaceHLAPI>();
		p2=new ArrayList<PlaceHLAPI>();
		p3=new ArrayList<PlaceHLAPI>();
		p4=new ArrayList<PlaceHLAPI>();
		transition1=new Hashtable<String,TransitionHLAPI>();
		transition2=new Hashtable<String,TransitionHLAPI>();
		inputX1=new Hashtable<Integer,TransitionHLAPI>();
		inputX2=new Hashtable<Integer,TransitionHLAPI>();
		inputX3=new Hashtable<Integer,ArrayList>();
		transition3Left=new ArrayList[nb_Crash];
		transition3Right=new ArrayList[nb_Crash];
		transition4Left=new ArrayList[nb_Crash];
		transition4Right=new ArrayList[nb_Crash];
		transition5Left=new ArrayList[nb_Crash];
		transition5Right=new ArrayList[nb_Crash];
		tmpJPC=new ArrayList<TransitionHLAPI>();
		for(int i=0;i<nb_Crash;i++) {
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
		int y_init=y+100;
		int x_init=x+600;
		int j=0;
		String namePlace,nameTransition;
		
		manip.place(name+"IsActive",x+master*100,y,CSS2Color.BLACK,true);
		PrincipalNode=manip.getPlace();
		
		manip.transition(name+"Crash",x+master*100,y-100,CSS2Color.GRAY);
		brokDown=manip.getTransition();
		manip.arc(true,PrincipalNode,brokDown);
		
		manip.place(name+"HasCrashed",x+master*100,y-180,CSS2Color.GRAY,false);
		manip.arc(false,manip.getPlace(),brokDown);

		manip.place(name+"NotifyThatHeIsActive",x+master*100-300,y,CSS2Color.RED,true);
		temp1=manip.getPlace();
		manip.arc(true,temp1,brokDown);
		
		manip.place("NodesCanDetectThat"+name+"HasCrashed",x+master*100-200,y,CSS2Color.RED,false);
		temp2=manip.getPlace();
		manip.arc(false,temp2,brokDown);
		
		namePlace=name+"IsTheNodeMaster";
		manip.place(namePlace,x-300,y,CSS2Color.RED,num==master);
		IsMaster=manip.getPlace();
				
		for(int k=0;k<size;k++) {
			nameTransition="Node"+k+"InformsHeIsTheNodeMasterTo"+name;
			manip.transition(nameTransition, x,y-100+100*k, CSS2Color.BLACK);
			tmpJPC.add(manip.getTransition());
			
		}	
		namePlace=name+"AsksIsThereANodeMaster";
		manip.place(namePlace, x,y-200,CSS2Color.BLACK,false);
		isThereMaster=manip.getPlace();
		
		for(int k1=0;k1<size;k1++) {
			manip.arc(true,isThereMaster,tmpJPC.get(k1));	
		}

		nameTransition=name+"IsBecomingTheNewNodeMaster";
		manip.transition(nameTransition, x+300,y+300, CSS2Color.BLACK);
		TransitionHLAPI tmp=manip.getTransition();
		manip.arc(false,IsMaster,manip.getTransition());
		
		namePlace=name+"IsNotifiedThatHeIsTheNewMaster";
		manip.place(namePlace,x+300,y+1300,CSS2Color.RED,false);
		temp3=manip.getPlace();
		manip.arc(true,temp3,tmp); 
		
		for(int i=0;i<size;i++) {
			if(i!=num) {
				buildBranch(x+j*300,y_init,i);
				j+=1;
			}			
		}	
		
		
		for(int k=0;k<nb_Crash;k++) {
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
	//WarnNodeMaster prend en paramètres l'Id du noeud qui est tombé en panne que
	//l'on nomme "oldMaster" car cela peut être le noeud maitre. Un arrayList
	//contenant les Id des noeuds qu'il doit successivement alerté de cette panne 
	//du noeud maitre jusqu'à alerter un noeud qui n'est pas en panne qui deviendra 
	//le nouveau noeud maitre.
	//=========================================================================
	private ArrayList<Integer> WarnNodeMaster(int oldMaster) {
		int niveau_num=Math.abs(master-num);
		int niveau_oldMaster=Math.abs(master-oldMaster);
		int tmp;
		ArrayList<Integer> res=new ArrayList<Integer>();
		if(niveau_num<=niveau_oldMaster) {
			return res;
		}
		//noeud côté gauche
		if(num<master) {
			for(int j=niveau_oldMaster;j<niveau_num;j++) {
				tmp=master-j;
				if(tmp!=oldMaster && tmp!=num) {
					res.add(tmp);
				}
				tmp=master+j;
				if(tmp!=oldMaster && tmp!=num) {
					res.add(tmp);
				}
			}
		}
		//noeud du côté droit
		else {
			for(int j=niveau_oldMaster;j<niveau_num;j++) {
				tmp=master+j;
				if(tmp!=oldMaster && tmp!=num) {
					res.add(tmp);
				}
				tmp=master-j;
				if(tmp!=oldMaster && tmp!=num) {
					res.add(tmp);
				}
			}
		}
		return res;
	}
	
	//=========================================================================
	//buildBranch va construire 1 branche dans l'automate
	//=========================================================================
	private void buildBranch(int xBranch,int yBranch,int iBranch) {
		String nameBranch="Node"+iBranch;
		String namePlace,nameTransition;
		TransitionHLAPI detectsCrash;
		ArrayList<Integer> warNode=WarnNodeMaster(iBranch);

		nameTransition=name+"DetectsCrashOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLUE);
		detectsCrash=manip.getTransition();
		transition1.put(nameBranch,manip.getTransition());
		yBranch+=100;
		manip.arc(false,isThereMaster,manip.getTransition());
		
		manip.arc(true,PrincipalNode,manip.getTransition());
		manip.arc(false,PrincipalNode,manip.getTransition());

		namePlace=name+"WantsToManageTheCrashOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLUE,false);
		yBranch+=100;
		manip.arc(false);
		
		nameTransition=name+"GetTheRightToManageTheCrashOf"+nameBranch;
		manip.transition(nameTransition,xBranch,yBranch,CSS2Color.BLACK);
		transition2.put(nameBranch,manip.getTransition());
		yBranch+=100;
		manip.arc(true);
		
		namePlace=name+"ManageTheCrashOf"+nameBranch;
		manip.place(namePlace,xBranch,yBranch,CSS2Color.BLACK,false);
		PlaceHLAPI origine=manip.getPlace();
		yBranch+=100;
		manip.arc(false);
		
		
		//début de la gestion de l'élection du nouveau noeud maitre
		
		namePlace=name+"HasDetectedCrashOf"+nameBranch;
		manip.place(namePlace, xBranch, yBranch, CSS2Color.BLACK, false);
		PlaceHLAPI croire=manip.getPlace();
		manip.arc(false, croire, detectsCrash);
		manip.arc(true,croire,transition2.get(nameBranch));

		nameTransition=name+"SeeksANewMasterToReplace"+nameBranch;
		manip.transition(nameTransition, xBranch, yBranch+300, CSS2Color.BLACK);
		TransitionHLAPI t=manip.getTransition();
		inputX1.put(iBranch,t);
		manip.arc(true);
		
		yBranch+=600;
		TransitionHLAPI[] input;
		inputX3bis=new ArrayList<TransitionHLAPI[]>();
		for(int i=0;i<warNode.size();i++) {
			inputX3bis.add(new TransitionHLAPI[2]);
			namePlace=name+"ThinksNode"+warNode.get(i)+"ShouldBeNodeMasterToReplaceNode"+iBranch;
			manip.place(namePlace,xBranch-100,yBranch+i*300+200,CSS2Color.BLACK,false);
			manip.arc(false);
			
			nameTransition=name+"DetectsThatNode"+warNode.get(i)+"CanReplaceNode"+iBranch;
			manip.transition(nameTransition,xBranch+100,i*300+yBranch,CSS2Color.BLACK);
			inputX3bis.get(i)[0]=manip.getTransition();
			manip.arc(true);
			manip.arc(false,croire,manip.getTransition());

			nameTransition="Node"+warNode.get(i)+"IsNotRespondingTo"+name+"ToReplaceNode"+iBranch;
			manip.transition(nameTransition,xBranch-100,i*300+yBranch,CSS2Color.BLACK);
			inputX3bis.get(i)[1]=manip.getTransition();
			t=manip.getTransition();
			manip.arc(true);
					
		}
		
		inputX3.put(iBranch,inputX3bis);
		manip.arc(false,temp3,t);
		manip.arc(false,croire,t);
		
		String LxOrRx="Lx";
		boolean lx=true;
		
		for(int i=0;i<nb_Crash;i++) {
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
			
		}
		
	}
	public TransitionHLAPI getBreaksDown() {
		return brokDown;
	}
	public Hashtable<String,TransitionHLAPI> getTabDetectsCrash() {
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
	public PlaceHLAPI getIsActive() {
		return PrincipalNode;
	}
	public PlaceHLAPI getTemp3() {
		return temp3;
	}
	public PlaceHLAPI getTemp2() {
		return temp2;
	}
	public PlaceHLAPI getTemp1() {
		return temp1;
	}
	public PlaceHLAPI getPrincip2() {
		return IsMaster;
	}
	public Hashtable<Integer,TransitionHLAPI> getInputX1(){
		return inputX1;
	}
	public Hashtable<Integer,TransitionHLAPI> getInputX2(){
		return inputX2;
	}
	public ArrayList<TransitionHLAPI[]> getInputX3(int key){
		return inputX3.get(key);
	}
	public ArrayList<Integer> getWarnNodeMaster(int iBranche){
		return WarnNodeMaster(iBranche);
	}
	public ArrayList<PlaceHLAPI>[] getCommWithExtremity() {
		ArrayList[] res ={p1,p2,p3,p4};
		return res;
	}
	public ArrayList<TransitionHLAPI> getTmpJPC() {
		return tmpJPC;
	}
}
