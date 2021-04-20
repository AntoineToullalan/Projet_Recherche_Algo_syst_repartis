package reseausimple;
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

	public Init(int nb, int length, int dim1, int dim2) {	

		try {
			ModelRepository.getInstance().createDocumentWorkspace("generator");
			PetriNetDocHLAPI doc = new PetriNetDocHLAPI();
			PetriNetHLAPI net = new PetriNetHLAPI("gen", PNTypeHLAPI.PTNET, new NameHLAPI("gen"), doc);
			PageHLAPI page = new PageHLAPI("toppage", new NameHLAPI("gen"), null, net);
			
			PNMLManipulationSimple manip;
			for(int i=0;i<nb;i++) {
				manip = new PNMLManipulationSimple(page,40+i*100,40);
				for(int j=0;j<length;j++) {
					manip.placeSimple("place"+i+"_"+j);
					if(j!=0) {
						manip.arc(false);
					}
					manip.transitionSimple("transition"+i+"_"+j);
					manip.arc(true);
				}
			}

			//System.out.println("Everything generated");
			File dir = new File (System.getProperty("user.dir")+"/testmodel");
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			PnmlExport pex = new PnmlExport();
			pex.exportObject(doc,"testmodel/ReseauSimple_"+nb+"_"+length+".pnml");
			System.out.println("File ReseauSimple_"+nb+"_"+length+".pnml exported to testmodel directory.");
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

