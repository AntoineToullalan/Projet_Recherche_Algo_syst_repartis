package insertioncan;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;

public class CAN{
	private PNMLManipulation manip;
	private int x,y,size,num;
	private String name;
	private PlaceHLAPI cpt,canvide;
	private TransitionHLAPI trans1,trans2,trans3;
	private TransitionHLAPI[][] askreq,gestion,accept,refus;
	private AutomateNode[] automatesNode;
	private AutomateNode automate;
	private PlaceHLAPI[][] placescomm,placescomm2;
	private List<Integer> list;

	public CAN(int size,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		// x=(size/2)*1500;
		// y=100;

		int ecart = 200;
		int absdep = 250;
		int orddep = 400;
		int tailleauto = size*absdep;

		if (size>2) {

			manip.place("Compteur",size*(tailleauto+500)/2 -500 ,y+1500,CSS2Color.GREEN,false);
			cpt=manip.getPlace();
			manip.place("CANVide",size*(tailleauto+500)/2 -500, y+100,CSS2Color.GREEN,true);
			canvide=manip.getPlace();
			
		}
		else{
			manip.place("Compteur",size*(tailleauto+500)/2 ,y+1500,CSS2Color.GREEN,false);
			cpt=manip.getPlace();
			manip.place("CANVide",size*(tailleauto+500)/2,y+100,CSS2Color.GREEN,true);
			canvide=manip.getPlace();
		}
		

		

		automatesNode = new AutomateNode[size];
		placescomm = new PlaceHLAPI[size][size];
		placescomm2 = new PlaceHLAPI[size][size];
		askreq = new TransitionHLAPI[size][size];
		gestion = new TransitionHLAPI[size][size];
		accept = new TransitionHLAPI[size][size];
		refus = new TransitionHLAPI[size][size];

	}

	public void buildAllnodes() {
		buildAutomatesNodes();
		buildConnexions(); 
		
	}

	public void buildAutomatesNodes() {

		int ecart = 200;
		int absdep = 250;
		int orddep = 400;
		int tailleauto = size*absdep;
		int departauto = ecart;

		for (int i=0;i<size; i++) {
			
			automatesNode[i] = new AutomateNode(departauto,orddep,i,size,manip);
			automatesNode[i].buildAutomate();
			departauto = 500+(i+1)*tailleauto;
			
		}

	}




	public void buildConnexions() {
		
		//on créer les automates des noeuds
		for(int i=0; i<size; i++) {

			/*
			int abs=0;
			int ord = 0;
			if (i%2==0) {
				if (i<size/2) {
					automatesNode[i]=new AutomateNode(x-size*200,y-i*500,i,size,manip);
					abs = x-size*200;
					ord = y-i*200;
					
				}
				else{
					automatesNode[i]=new AutomateNode(x-size*200,y+i*500,i,size,manip);
					abs = x-size*200;
					ord = y+i*200;
				}
			}
			else{
				if (i<size/2) {
					automatesNode[i]=new AutomateNode(x+size*200,y-i*500,i,size,manip);
					abs = x+size*200;
					ord = y-i*200;
					
				}
				else{
					automatesNode[i]=new AutomateNode(x+size*200,y+i*500,i,size,manip);
					abs = x+size*200;
					ord = y+i*500;
				}				
				
			}
			
			name="Node"+i;
			automatesNode[i].buildAutomate(); */
			//créer les places de communication


			//relier compteur et transitions : 
			TransitionHLAPI trans1 = automatesNode[i].getmoreinsert();
			TransitionHLAPI trans2 = automatesNode[i].getisinsert();
			TransitionHLAPI trans3 = automatesNode[i].getassociate();
			TransitionHLAPI trans4 = automatesNode[i].getfirstinsert();
			PlaceHLAPI estdanscan = automatesNode[i].getestinsere();
			PlaceHLAPI req = automatesNode[i].getreq();

			manip.arc(true,cpt,trans1);
			manip.arc(false,cpt,trans1);
			manip.arc(true,canvide,trans4);
			manip.arc(false,cpt,trans2);
			manip.arc(false,cpt,trans3);
		}
	}
			/*
			//créer autant de transisions que de noeuds - 1
			for (int j=0; j<size; j++ ) {

				if (j!=i) {
				
					manip.transition(name+"AskReq"+j,abs-450,ord-600+j*100,CSS2Color.RED);
					askreq[i][j] = manip.getTransition();
					
					manip.arc(true,req,manip.getTransition());
					manip.place(name+"requestsent"+j,abs-400,ord-700+j*100,CSS2Color.RED,false);
					placescomm[i][j] = manip.getPlace();
					
					manip.arc(false,manip.getPlace(),manip.getTransition());
					manip.transition(name+"Gestion"+j,abs+300+j*100,ord-400+j*100,CSS2Color.BLUE);
					gestion[i][j] = manip.getTransition();
					
					manip.arc(true,estdanscan,manip.getTransition());
					manip.arc(false,estdanscan,manip.getTransition());
					manip.place(name+"GiveAnswer"+j,abs+400+j*100,ord-500+j*100,CSS2Color.BLUE,false);
					placescomm2[i][j] = manip.getPlace();
					
					manip.arc(false,manip.getPlace(),manip.getTransition());
					manip.transition(name+"SendAccept"+j,abs+300+j*100,ord-600+j*100,CSS2Color.BLUE);
					accept[i][j] = manip.getTransition();
					
					manip.arc(true,manip.getPlace(),manip.getTransition());
					manip.transition(name+"SendRefuse"+j,abs+300+j*100,ord-500+j*100,CSS2Color.BLUE);
					refus[i][j] = manip.getTransition();
					
					manip.arc(true,manip.getPlace(),manip.getTransition());
				}

			}
		}

		//on crée les connections entre les automates
		
		for(int i=0;i<size;i++) {
			automate = automatesNode[i];

			for (int j=0;j<size ;j++ ) {
				if (j != i) {

					manip.arc(false,automatesNode[j].getokshare(),accept[i][j]);
					manip.arc(false,automatesNode[j].getnoshare(),refus[i][j]);
					manip.arc(true,automatesNode[j].getestinsere(),askreq[i][j]);
					manip.arc(false,automatesNode[j].getestinsere(),askreq[i][j]);
					manip.arc(true,placescomm[j][i],gestion[i][j]);

				}
				
			}

		}
		
	}
*/

}
