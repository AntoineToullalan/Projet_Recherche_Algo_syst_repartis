package insertioncan;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.ptnet.hlapi.AnnotationGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi  .ArcGraphicsHLAPI;
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
	private PlaceHLAPI principalNode,dhtinfo,estinsere,oKshare,nOshare,req;
	private TransitionHLAPI firstinsert,moreinsert,associate,asksharing,isinsert;
	
	//=========================================================================
	//transition1,transition2,transition3Left,transition3Right,transition4Left,
	//transition4Right,transition5Left,transition5Right servent à conserver les transitions qui
	//vont servir à communiquer entre transitions.
	//=========================================================================
	public AutomateNode(int x,int y,int num,int size,PNMLManipulation manip) {
		this.num=num;
		name="Node"+num;
		this.size=size;
		master = size/2;
		this.x=x;
		this.y=y;
		this.manip=manip;

	}
	
	//=========================================================================
	//buildAutomate va construire l'automate pour le noeud num avec les size-1 branches 
	//=========================================================================
	public void buildAutomate() {

		InsertInEmptyCan();
		insertInNotEmptyCan();

	}


	public void InsertInEmptyCan(){

		int absdep = 250;
		int ecart = 75;
		int tailleauto = size*absdep;
		


		//Noeud à insérer
		manip.place(name+"AInserer",x + tailleauto/2, y,CSS2Color.BLACK,true);
		principalNode=manip.getPlace();
		//Première insertion
		manip.transition(name+"FirstInsertInCAN",x + tailleauto/4,y+100,CSS2Color.BLACK);
		firstinsert=manip.getTransition();
		//Nieme insertion
		manip.transition(name+"NInsertInCAN",x + 3*tailleauto/4,y+100,CSS2Color.BLACK);
		moreinsert=manip.getTransition();

		manip.arc(true,principalNode,firstinsert);
		manip.arc(true,principalNode,moreinsert);

		int b = y + 100;

		//construction branche première insertion:
		
		manip.place(name+"DHTinfo",x + tailleauto/4, b+ecart ,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),firstinsert);

		manip.transition(name+"ChooseID",x + tailleauto/4, b+2*ecart ,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());

		manip.place(name+"ID",x + tailleauto/4, b+3*ecart, CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.transition(name+"GetsDHTInfo",x + tailleauto/4, b+5*ecart ,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());

		manip.place(name+"ResZone",x + tailleauto/4, b+6*ecart ,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.transition(name+"AssociatesDHTInfo",x + tailleauto/4, b+7*ecart ,CSS2Color.BLACK);
		associate = manip.getTransition();
		manip.arc(true,manip.getPlace(),associate);

		manip.place(name+"IsInserted",x + tailleauto/4, b+9*ecart ,CSS2Color.BLACK,false);
		estinsere = manip.getPlace();
		manip.arc(false,estinsere,associate);

	}

	public void insertInNotEmptyCan(){

		int absdep = 250;
		int ecart = 75;
		int tailleauto = size*absdep;
		int b = y + 100;

		//construction branche Nième insertion:

		manip.place(name+"DHTinfo2",x + 3*tailleauto/4, b+ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),moreinsert);

		manip.transition(name+"ChooseID2",x + 3*tailleauto/4, b+2*ecart,CSS2Color.BLACK);
		dhtinfo = manip.getPlace();
		manip.arc(true,dhtinfo,manip.getTransition());

		manip.place(name+"ID2",x + 3*tailleauto/4, b+3*ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.transition(name+"ChooseBootstrap",x + 3*tailleauto/4,b+4*ecart,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());

		manip.place(name+"Bootstrap",x + 3*tailleauto/4,b+5*ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.transition(name+"SendOfaJoinMsg",x + 3*tailleauto/4,b+6*ecart,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());

		manip.place(name+"JoinMsg",x + 3*tailleauto/4,b+7*ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.transition(name+"AsksSharing",x + 3*tailleauto/4,b+8*ecart,CSS2Color.BLACK);
		asksharing = manip.getTransition();
		manip.arc(true,manip.getPlace(),asksharing);

		manip.place(name+"Request",x+100+3*tailleauto/4,b+9*ecart,CSS2Color.RED,false);
		req = manip.getPlace();
		manip.arc(false,manip.getPlace(),manip.getTransition());

		manip.place(name+"WaitAnswer",x-ecart+3*tailleauto/4, b+8*ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());
		
		manip.transition(name+"InsertNode",x-ecart+3*tailleauto/4,b+9*ecart,CSS2Color.BLACK);
		isinsert = manip.getTransition();
		manip.arc(true,manip.getPlace(),isinsert);
		manip.arc(false,estinsere,isinsert);

		manip.transition(name+"NotInsertNode",x-2*ecart+3*tailleauto/4,b+8*ecart,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());

		manip.place(name+"OKshare",x-ecart+3*tailleauto/4,b+10*ecart,CSS2Color.ORANGE,false);
		oKshare = manip.getPlace();
		manip.arc(true,oKshare,isinsert);
		manip.place(name+"NOshare",x-2*ecart+3*tailleauto/4,b+10*ecart,CSS2Color.ORANGE,false);
		nOshare = manip.getPlace();
		manip.arc(true,nOshare,manip.getTransition());

		manip.place(name+"IsNonInsertedNode",x-2*ecart+3*tailleauto/4,b+3*ecart,CSS2Color.BLACK,false);
		manip.arc(false,manip.getPlace(),manip.getTransition());
		manip.transition(name+"RetryInsertion",x-2*ecart+3*tailleauto/4,b+ecart,CSS2Color.BLACK);
		manip.arc(true,manip.getPlace(),manip.getTransition());
		manip.arc(false,dhtinfo,manip.getTransition());



	}
	
	public PlaceHLAPI getreq(){
		return req;
	}

	public PlaceHLAPI getnoshare(){
		return nOshare;
	}

	public PlaceHLAPI getokshare(){
		return oKshare;
	}

	public PlaceHLAPI getestinsere(){
		return estinsere;
	}

	public TransitionHLAPI getmoreinsert(){
		return moreinsert;
	}

	public TransitionHLAPI getassociate(){
		return associate;
	}

	public TransitionHLAPI getisinsert(){
		return isinsert;
	}

	public TransitionHLAPI getfirstinsert(){
		return firstinsert;
	}

	public TransitionHLAPI getaskshare(){
		return asksharing;
	}
	
	

}