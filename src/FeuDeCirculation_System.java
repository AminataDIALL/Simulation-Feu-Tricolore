


import model.*;

import java.util.*;



public  class FeuDeCirculation_System extends CoupledModel {

	public FeuDeCirculation_System(String name, String desc,double min_panne,double max_panne,int longueur,int vitesse1,int vitesse2,int vitesse3,int min_ctl,int max_ctl,int min_emet1,int max_emet1,int min_emet2,int max_emet2,int min_emet3,int max_emet3,int vert,int jaune,int rouge) {
		super(name, desc);

//		// Definition of {Md}
		Controlleur ModelCtl = new Controlleur("Controlleur","Generator of random events",min_ctl,max_ctl);
		FeuDeCirculation ModelFeu = new FeuDeCirculation("FeuTricolore","System under study",vert,jaune,rouge);
		Collecteur ModelColl = new Collecteur("Collecteur","Repository of results");
		Collecteur ModelCollCar = new Collecteur("RES1","Repository of results");
		Alea Ale = new Alea("Alea","pertubateur feu",min_panne,max_panne);
		
		Transformateur ModelTransform = new Transformateur("Transformateur","interpreteur du feu");
		
		Emetteur Memet1 = new Emetteur("Emetteur1", "",min_emet1,max_emet1);
		Emetteur Memet2 = new Emetteur("Emetteur2", "",min_emet2,max_emet2);
		Emetteur Memet3 = new Emetteur("Emetteur3", "",min_emet3,max_emet3);
		
		Voie Mvoie1 = new Voie("Voie1", "", longueur,vitesse1);
		Voie Mvoie2 = new Voie("Voie2", "", longueur,vitesse2);
		Voie Mvoie3 = new Voie("Voie3", "", longueur,vitesse3);
		
//		// Definition of D
		addSubModel(ModelCtl);
		addSubModel(ModelFeu);
		addSubModel(ModelColl);
		addSubModel(ModelCollCar);
		addSubModel(Ale);
		
		addSubModel(ModelTransform);
		
		addSubModel(Memet1);
		addSubModel(Memet2);
		addSubModel(Memet3);
		
		addSubModel(Mvoie1);
		addSubModel(Mvoie2);
		addSubModel(Mvoie3);
		
//		// There is no EIC, since the global model is input-free
//
//		// Definition of IC:
		addIC(Ale.getOutputPortStructure("Alea.OUTAlea"), ModelFeu.getInputPortStructure("FeuTricolore.INFeu")) ;
		addIC(ModelCtl.getOutputPortStructure("Controlleur.OUTControlleur"), ModelFeu.getInputPortStructure("FeuTricolore.INFeu")) ;
		addIC(ModelFeu.getOutputPortStructure("FeuTricolore.OUTFeu"), ModelCtl.getInputPortStructure("Controlleur.INControlleur")) ;
		addIC(ModelFeu.getOutputPortStructure("FeuTricolore.OUTFeu"), ModelColl.getInputPortStructure("Collecteur.OUTPUT")) ;
		
		addIC(ModelFeu.getOutputPortStructure("FeuTricolore.OUTFeu"), ModelTransform.getInputPortStructure("Transformateur.INTransformateur")) ;
		
		addIC(Memet1.getOutputPortStructure("Emetteur1.OUTEmetteur"), Mvoie1.getInputPortStructure("Voie1.INCar"));
		addIC(Memet2.getOutputPortStructure("Emetteur2.OUTEmetteur"), Mvoie2.getInputPortStructure("Voie2.INCar"));
		addIC(Memet3.getOutputPortStructure("Emetteur3.OUTEmetteur"), Mvoie3.getInputPortStructure("Voie3.INCar"));
		
		addIC(ModelTransform.getOutputPortStructure("Transformateur.OUTTransformateur"), Mvoie1.getInputPortStructure("Voie1.INPerm"));
		addIC(ModelTransform.getOutputPortStructure("Transfo.OUTTransformateur"), Mvoie2.getInputPortStructure("Voie2.INPerm"));
		addIC(ModelTransform.getOutputPortStructure("Transfo.OUTTransformateur"), Mvoie3.getInputPortStructure("Voie3.INPerm"));
		
		addIC(Mvoie1.getOutputPortStructure("Voie1.OUTPerm"), Memet1.getInputPortStructure("Emetteur1.INEmetteur"));
		addIC(Mvoie2.getOutputPortStructure("Voie2.OUTPerm"), Memet2.getInputPortStructure("Emetteur2.INEmetteur"));
		addIC(Mvoie3.getOutputPortStructure("Voie3.OUTPerm"), Memet3.getInputPortStructure("Emetteur3.InEmetteur"));
		
	}
	
	public Model select(ArrayList<Model> possibleModels) {
		// The traffic light has the higher priority
		// Otherwise a kind of random choice is done
		for ( Model m : possibleModels ) {
			if ( m instanceof FeuDeCirculation ) return m ;
		}
		return possibleModels.get(0);
	}
}
