package clientserver;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import java.io.File;
import java.io.IOException;

public class Init {

	public Init(String NamePlace, String NameTrans, int dim1, int dim2) {	

		try {
			ModelRepository.getInstance().createDocumentWorkspace("generator");
			PetriNetDocHLAPI doc = new PetriNetDocHLAPI();
			PetriNetHLAPI net = new PetriNetHLAPI("gen", PNTypeHLAPI.PTNET, new NameHLAPI("gen"), doc);
			PageHLAPI page = new PageHLAPI("toppage", new NameHLAPI("gen"), null, net);
			
			Place place = new Place(page,NamePlace,100,100,CSS2Color.ORANGE,true);
			Transition transition = new Transition(page,NameTrans,400,300,CSS2Color.RED);
			Arc arc = new Arc(page,place,transition);
			
			//System.out.println("Everything generated");
			File dir = new File (System.getProperty("user.dir")+"/testmodel");
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			PnmlExport pex = new PnmlExport();
			pex.exportObject(doc,"testmodel/ReseauSimple"+NamePlace+"_"+NameTrans+".pnml");
			System.out.println("File ReseauSimple"+NamePlace+"_"+NameTrans+".pnml exported to testmodel directory.");
			ModelRepository.getInstance().destroyCurrentWorkspace();
		} catch (InvalidIDException e) {
			System.out.println("InvalidIDException caught by while running generator");
			e.printStackTrace();
		} catch (VoidRepositoryException e) {
			System.out.println("VoidRepositoryException caught by while running generator");
			e.printStackTrace();	
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

