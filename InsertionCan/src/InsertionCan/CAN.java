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
	private int x,y,size;
	private String name;
	private PlaceHLAPI cpt,canvide;
	private TransitionHLAPI trans1,trans2,trans3;
	private AutomateNode[] automatesNode;
	private AutomateNode automate;

	public CAN(int size,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		// x=(size/2)*1500;
		// y=100;

		int ecart = 200;
		int absdep = 250;
		int orddep = 400;
		int tailleauto = size*absdep;

		manip.place("Compteur",size*(tailleauto+500)/2 + 200,y+100,CSS2Color.GREEN,false);
		cpt=manip.getPlace();
		manip.place("CANVide",size*(tailleauto+500)/2, y+100,CSS2Color.GREEN,true);
		canvide=manip.getPlace();

		automatesNode = new AutomateNode[size];

	}

	public void buildAllnodes() {
		buildAutomatesNodes();
		buildConnexions(); 
		
	}

	public void buildAutomatesNodes() {

		int absdep = 250;
		int orddep = 400;
		int tailleauto = size*absdep;
		int departauto = 100;

		for (int i=0;i<size; i++) {
			departauto = (i)*(tailleauto+1000);
			automatesNode[i] = new AutomateNode(departauto,orddep,i,size,manip);
			automatesNode[i].buildAutomate();
			
			
		}

		for (int i=0; i<size ;i++ ) {
			automatesNode[i].communicNodes();
		}



		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (j != i) {

					AutomateNode me = automatesNode[i];
					AutomateNode autrej = automatesNode[j];

					manip.arc(false,me.getokshare(),autrej.getaccepte(i));
					manip.arc(false,me.getnoshare(),autrej.getrefus(i));

					manip.arc(true,autrej.getplacescomm(i),me.getgestion(j));

					manip.arc(false,me.getestinsere(),autrej.getaskreq(i));
					manip.arc(true,me.getestinsere(),autrej.getaskreq(i));

				
				}

			}
			
		}
					
	}


	public void buildConnexions() {
		
		//on créer les automates des noeuds
		for(int i=0; i<size; i++) {

			//relier compteur et transitions : 
			TransitionHLAPI trans1 = automatesNode[i].getmoreinsert();
			TransitionHLAPI trans2 = automatesNode[i].getisinsert();
			TransitionHLAPI trans3 = automatesNode[i].getassociate();
			TransitionHLAPI trans4 = automatesNode[i].getfirstinsert();
			
			manip.arc(true,cpt,trans1);
			manip.arc(false,cpt,trans1);
			manip.arc(true,canvide,trans4);
			manip.arc(false,cpt,trans2);
			manip.arc(false,cpt,trans3);
		}
	}
		

}
