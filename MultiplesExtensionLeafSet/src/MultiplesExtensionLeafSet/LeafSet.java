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

//=========================================================================
//Cette classe permet de creer le réseau de Petri de l'extension du LeafSet de taille size
//on utilise les classes AutomateNode et ExtremityLeafSet pour cela
// =========================================================================
public class LeafSet {
	private PNMLManipulation manip;
	private int x,y,size,x_centre,nb_breakdown;
	private PlaceHLAPI reservoir;
	private AutomateNode[] automatesNode;
	private PlaceHLAPI[][] placesCommNodes;
	private ExtremityLeafSet extremite;
	
	public LeafSet(int size,int nb_breakdown,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		this.nb_breakdown=nb_breakdown;
		x=700;
		y=700;
		x_centre=x+(size*(size-1)*300)/2;
		manip.place("BreakDownReservoir",x_centre,200,CSS2Color.GREEN,nb_breakdown);
		reservoir=manip.getPlace();
		automatesNode=new AutomateNode[size];
		placesCommNodes=new PlaceHLAPI[size][2];
		
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
		
		buildAutomatesNodes();
		buildExtremityLeafSet();
		extComm=extremite.getCommAutomate();
		
		for(int i=0;i<size;i++) {
			placeCommExtremity=automatesNode[i].getCommWithExtremity();
			p1_Lx=placeCommExtremity[0];
			p2_Lx=placeCommExtremity[1];
			p1_Rx=placeCommExtremity[2];
			p2_Rx=placeCommExtremity[3];
			for(int j=0;j<nb_breakdown;j++) {
				manip.arc(true,p1_Lx.get(j),extComm[0][j].get("Node"+i));
				manip.arc(false,p2_Lx.get(j),extComm[1][j].get("Node"+i));
				manip.arc(true,p1_Rx.get(j),extComm[2][j].get("Node"+i));
				manip.arc(false,p2_Rx.get(j),extComm[3][j].get("Node"+i));
			}
		}
		PlaceHLAPI placeLeft;
		PlaceHLAPI placeRight;
		ArrayList<TransitionHLAPI> transitionsLeft;
		ArrayList<TransitionHLAPI> transitionsRight;
		for(int i=0;i<nb_breakdown;i++) {
			placeLeft=extremite.getCommNodeBreakLeft(i);
			placeRight=extremite.getCommNodeBreakRight(i);
			for(int j=0;j<size;j++) {
				transitionsLeft=automatesNode[j].getExtLeft(i);
				transitionsRight=automatesNode[j].getExtRight(i);
				for(int k=0;k<transitionsLeft.size();k++) {
					manip.arc(true,placeLeft,transitionsLeft.get(k));
				}
				for(int k=0;k<transitionsRight.size();k++) {
					manip.arc(true,placeLeft,transitionsRight.get(k));
				}
			}
		}
		
	}
	
	//=========================================================================
	//On cree les automates des noeuds du LeafSet avec la classe AutomateNode et on
	//ajoute des places permettant de faire les communications avec les extremités
	// =========================================================================
	public void buildAutomatesNodes() {
		//on créer les automates des noeuds
		for(int i=0;i<size;i++) {
			automatesNode[i]=new AutomateNode(x,y,i,size,nb_breakdown,manip);
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
	
	//=========================================================================
	//On cree les automates des extremites du LeafSet avec la classe ExtremityLeafSet et on
	//ajoute les arcs permettant de faire les communications avec les automates des
	//noeuds du LeafSet
	// =========================================================================
	public void buildExtremityLeafSet() {
		PlaceHLAPI[] LxInTheLeafSet=new PlaceHLAPI[nb_breakdown];
		PlaceHLAPI[] RxInTheLeafSet=new PlaceHLAPI[nb_breakdown];
		ArrayList<TransitionHLAPI> tab;
		TransitionHLAPI tmp;
		AutomateNode automate;
		
		extremite=new ExtremityLeafSet(x,y+2000,size,nb_breakdown,manip);
		extremite.buildExtremity();
		for(int i=0;i<nb_breakdown;i++) {
			manip.place("ANodeFromTheLeafSetOfLx"+i+"IsACtiveInTheLeafSet",(size+i)*300,y+1800,CSS2Color.RED,false);
			LxInTheLeafSet[i]=manip.getPlace();
			manip.arc(true,LxInTheLeafSet[i],extremite.getActiveInLeafSetLeft(i));
		}
		for(int i=0;i<nb_breakdown;i++) {
			manip.place("ANodeFromTheLeafSetOfRx"+i+"IsACtiveInTheLeafSet",x-(size+i)*300,y+1800,CSS2Color.RED,false);
			RxInTheLeafSet[i]=manip.getPlace();
			manip.arc(true,RxInTheLeafSet[i],extremite.getActiveInLeafSetRight(i));
		}

		for(int i=0;i<size;i++) {
			automate=automatesNode[i];
			for(int iBranche=0;iBranche<nb_breakdown;iBranche++) {
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
