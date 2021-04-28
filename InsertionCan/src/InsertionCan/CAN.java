package insertioncan;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;

//=========================================================================
//CAN.java a pour rôle de générer les automates et reliées les places et transistion de communication entre elles. 
//=========================================================================
	

public class CAN{
	private PNMLManipulation manip;
	private int x,y,size,ecart,absdep,orddep,tailleauto,departauto;
	private String name;
	private PlaceHLAPI cpt,canvide;
	private TransitionHLAPI trans1,trans2,trans3;
	private AutomateNode[] automatesNode;
	private AutomateNode automate;

	public CAN(int size,PNMLManipulation manip) {
		this.manip=manip;
		this.size=size;
		ecart = 200;
		absdep = 250;
		orddep = 400;
		tailleauto = size*absdep;

		//On met en place le compteur et le jeton de CAN vide
		manip.place("InsertedCount",size*(tailleauto+500)/2 + 200,y+300,CSS2Color.GREEN,false);
		cpt=manip.getPlace();
		manip.place("EmptyCAN",size*(tailleauto+500)/2, y+300,CSS2Color.GREEN,true);
		canvide=manip.getPlace();
		automatesNode = new AutomateNode[size];

	}

	public void buildAllnodes() {
		buildAutomatesNodes();
		buildConnexions(); 
		
	}
	//Construction de chacun des noeuds à insérer
	public void buildAutomatesNodes() {

		for (int i=0;i<size; i++) {
			departauto = (i)*(tailleauto+500)+20;
			automatesNode[i] = new AutomateNode(departauto,orddep,i,size,manip);
			automatesNode[i].buildAutomate();	
		}

		//création des élements de communication
		for (int i=0; i<size ;i++ ) {
			automatesNode[i].communicNodes();
		}
		//relier les élements de communication entre eux
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
