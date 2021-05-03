package multiplesextensionleafset;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import java.util.Set;
import java.util.Iterator;

//=========================================================================
//Cette classe permet de creer le réseau de Petri de l'extension du LeafSet de taille size
//on utilise les classes AutomateNode et ExtremityLeafSet pour cela
// =========================================================================
public class LeafSet {
	private PNMLManipulation manip;
	private int x,y,size,x_centre,nb_Crash;
	private PlaceHLAPI reservoir,noCrash,masterExists;
	private AutomateNode[] automatesNode;
	private PlaceHLAPI[][] placesCommNodes;
	private ExtremityLeafSet extremite;
	
	public LeafSet(int size,int nb_Crash,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		this.nb_Crash=nb_Crash;
		x=700;
		y=700;
		x_centre=x+(size*(size-1)*300)/2;
		manip.place("CrashReservoir",x_centre,200,CSS2Color.GREEN,nb_Crash);
		reservoir=manip.getPlace();
		manip.place("NoCrashIsHappening",x_centre-100,100,CSS2Color.GREEN,true);
		noCrash=manip.getPlace();
		automatesNode=new AutomateNode[size];
		placesCommNodes=new PlaceHLAPI[size][2];
		manip.place("ThereIsAMasterInTheLeafSet",x_centre,400,CSS2Color.RED,false);
		masterExists=manip.getPlace();
		
	}
	
	//=========================================================================
	//Dans cette méthode on crée le reseau de l'extension du LeafSet en créabt les automates 
	//des noeuds du LeafSet avec buildAutomatesNodes et les automates des noeuds aux extremités
	//du LeafSet avec buildExtremityLeafSet puis on met en relation ces automates
	// =========================================================================
	public void buildAllLeafSet() {
		ArrayList<PlaceHLAPI>[] placeCommExtremity;
		ArrayList<PlaceHLAPI> p1_Lx,p2_Lx,p1_Rx,p2_Rx;
		Hashtable<String,TransitionHLAPI>[][] extComm;
		Hashtable<Integer,TransitionHLAPI> hashtable1,hashtable2;
		ArrayList<TransitionHLAPI[]> inputX3bis;
		ArrayList<Integer> warN;
		ArrayList<TransitionHLAPI> jpc;
		TransitionHLAPI inputX1,inputX2;
		TransitionHLAPI[] inputX3;
		PlaceHLAPI princip1,princip2,temp1,temp2,temp3;
		int key,key2;
		Set keys,keys2;
		Iterator itr,itr2;
		
		buildAutomatesNodes();
		buildExtremityLeafSet();
		extComm=extremite.getCommAutomate();
		
		for(int i=0;i<size;i++) {
			placeCommExtremity=automatesNode[i].getCommWithExtremity();
			jpc=automatesNode[i].getTmpJPC();
			for(int k=0;k<jpc.size();k++) {
				manip.arc(false,automatesNode[k].getPrincip2(),jpc.get(k));
				manip.arc(true,automatesNode[k].getPrincip2(),jpc.get(k));
				manip.arc(false,masterExists,jpc.get(k));
			}

			p1_Lx=placeCommExtremity[0];
			p2_Lx=placeCommExtremity[1];
			p1_Rx=placeCommExtremity[2];
			p2_Rx=placeCommExtremity[3];
			for(int j=0;j<nb_Crash;j++) {
				manip.arc(true,p1_Lx.get(j),extComm[0][j].get("Node"+i));
				manip.arc(false,p2_Lx.get(j),extComm[1][j].get("Node"+i));
				manip.arc(true,p1_Rx.get(j),extComm[2][j].get("Node"+i));
				manip.arc(false,p2_Rx.get(j),extComm[3][j].get("Node"+i));
			}
			hashtable1=automatesNode[i].getInputX1();
			hashtable2=automatesNode[i].getInputX2();
			keys=hashtable1.keySet();
		    itr=keys.iterator();
			while(itr.hasNext()) {
				key = (int)itr.next();
				inputX1=hashtable1.get(key);
				inputX2=hashtable2.get(key);
				
				princip1=automatesNode[key].getPrincip1();
				princip2=automatesNode[key].getPrincip2();
				
				manip.arc(false,princip1,inputX2);
				manip.arc(true,princip1,inputX2);
				
				manip.arc(true,princip2,inputX1);
				
				warN=automatesNode[i].getWarnNodeMaster(key);
				inputX3bis=automatesNode[i].getInputX3(key);
				if(inputX3bis!=null){
					
					for(int k=0;k<inputX3bis.size();k++) {
						key2=warN.get(k);
						inputX3=inputX3bis.get(k);
								
						temp1=automatesNode[key2].getTemp1();
						temp2=automatesNode[key2].getTemp2();
						temp3=automatesNode[key2].getTemp3();
						
						manip.arc(true,temp2,inputX3[1]);
						manip.arc(false,temp2,inputX3[1]);
						
						manip.arc(true,temp1,inputX3[0]);
						manip.arc(false,temp1,inputX3[0]);
						
						manip.arc(false,temp3,inputX3[0]);
						
					}
				}

			}
		}
		PlaceHLAPI placeLeft;
		PlaceHLAPI placeRight;
		ArrayList<TransitionHLAPI> transitionsLeft;
		ArrayList<TransitionHLAPI> transitionsRight;
		for(int it=0;it<nb_Crash;it++) {
			placeLeft=extremite.getCommNodeBreakLeft(it);
			placeRight=extremite.getCommNodeBreakRight(it);
			for(int j=0;j<size;j++) {
				transitionsLeft=automatesNode[j].getExtLeft(it);
				transitionsRight=automatesNode[j].getExtRight(it);
				for(int k=0;k<transitionsLeft.size();k++) {
					manip.arc(true,placeLeft,transitionsLeft.get(k));
					manip.arc(false,placeLeft,transitionsLeft.get(k));
				}
				for(int k=0;k<transitionsRight.size();k++) {
					manip.arc(true,placeRight,transitionsRight.get(k));
					manip.arc(false,placeRight,transitionsRight.get(k));
				}
			}
		}
	}
		

	
	//=========================================================================
	//On cree les automates des noeuds du LeafSet avec la classe AutomateNode et on
	//ajoute des places permettant de faire les communications avec les extremités
	// =========================================================================
	private void buildAutomatesNodes() {
		//on créer les automates des noeuds
		for(int i=0;i<size;i++) {
			automatesNode[i]=new AutomateNode(x,y,i,size,nb_Crash,manip);
			automatesNode[i].buildAutomate();
			manip.place("Node"+i+"DontAnswerToAnyNode",x-400,y+200,CSS2Color.RED,false);
			placesCommNodes[i][0]=manip.getPlace();
			manip.place("NoNodeManageTheCrashOfNode"+i,x-400,y+400,CSS2Color.RED,false);
			placesCommNodes[i][1]=manip.getPlace();
			x+=300*size;
		}
		
		//on crée les connections entre les automates
		TransitionHLAPI transitionCrash;
		AutomateNode automate;
		Hashtable<String,TransitionHLAPI> tableDetectsCrash;
		Hashtable<String,TransitionHLAPI> tableGetTheRight;
		for(int i=0;i<size;i++) {
			automate=automatesNode[i];
			transitionCrash=automate.getBreaksDown();
			manip.arc(true,reservoir,transitionCrash);
			manip.arc(true,noCrash,transitionCrash);
			
			manip.arc(false,placesCommNodes[i][0],transitionCrash);
			manip.arc(false,placesCommNodes[i][1],transitionCrash);
			tableDetectsCrash=automate.getTabDetectsCrash();
			tableGetTheRight=automate.getTabGetTheRight();
			for(int j=0;j<size;j++) {
				if(j!=i) {
					manip.arc(true,placesCommNodes[j][0],tableDetectsCrash.get("Node"+j));
					manip.arc(true,placesCommNodes[j][1],tableGetTheRight.get("Node"+j));
					manip.arc(true,masterExists,tableGetTheRight.get("Node"+j));
				}	
			}
			
		}
	
	}
	
	//=========================================================================
	//On cree les automates des extremites du LeafSet avec la classe ExtremityLeafSet et on
	//ajoute les arcs permettant de faire les communications avec les automates des
	//noeuds du LeafSet
	// =========================================================================
	private void buildExtremityLeafSet() {
		PlaceHLAPI[] LxInTheLeafSet=new PlaceHLAPI[nb_Crash];
		PlaceHLAPI[] RxInTheLeafSet=new PlaceHLAPI[nb_Crash];
		ArrayList<TransitionHLAPI> tab;
		TransitionHLAPI tmp;
		AutomateNode automate;
		
		extremite=new ExtremityLeafSet(x,y+3000,size,nb_Crash,manip);
		extremite.buildExtremity();
		for(int i=0;i<nb_Crash;i++) {
			manip.place("ANodeFromTheLeafSetOfLx"+i+"IsACtiveInTheLeafSet",(size+i)*300,y+1800,CSS2Color.RED,false);
			LxInTheLeafSet[i]=manip.getPlace();
			manip.arc(true,LxInTheLeafSet[i],extremite.getActiveInLeafSetLeft(i));
			manip.arc(false,noCrash,extremite.getActiveInLeafSetLeft(i));
		}
		for(int i=0;i<nb_Crash;i++) {
			manip.place("ANodeFromTheLeafSetOfRx"+i+"IsACtiveInTheLeafSet",x-(size+i)*300,y+1800,CSS2Color.RED,false);
			RxInTheLeafSet[i]=manip.getPlace();
			manip.arc(true,RxInTheLeafSet[i],extremite.getActiveInLeafSetRight(i));
			manip.arc(false,noCrash,extremite.getActiveInLeafSetRight(i));
		}

		for(int i=0;i<size;i++) {
			automate=automatesNode[i];
			for(int iBranche=0;iBranche<nb_Crash;iBranche++) {
				tab=automate.getTabSelectANodeofLx(iBranche);
				for(int j=0;j<tab.size();j++) {
					tmp=tab.get(j);
					manip.arc(false,LxInTheLeafSet[iBranche],tmp);
				}
				tab=automate.getTabSelectANodeofRx(iBranche);
				for(int j=0;j<tab.size();j++) {
					tmp=tab.get(j);
					manip.arc(false,RxInTheLeafSet[iBranche],tmp);
				}
			}
			
		}


	}

}
