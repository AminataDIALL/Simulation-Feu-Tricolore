
import exception.DEVS_Exception;
import model.*;
import types.*;
import java.io.*;
import java.util.Random;

public class Emetteur extends AtomicModel {

	int etat;
	Random ra;
	private int duree_min;
	private int duree_max;
	public double sigma;

	public Emetteur(String name, String desc,int min,int max) {
		super(name, desc);

		String[] permissions = {"Vrai","Faux"};
		addInputPortStructure(new DEVS_Enum(permissions), this.getName()+".INEmetteur", " Trigger received");

		addOutputPortStructure(new DEVS_Integer(), this.getName()+".OUTEmetteur", "Car á envoyer");

		// State initialization
		etat = 1;
		ra = new Random();
		duree_min =min;
		duree_max = max;
	}


	public void deltaInt() {
		//passage de Agit a Neant
		if(etat == 1)
			etat = 1;
		sigma = ra.nextInt((duree_max - duree_min) +1) + duree_min;
	}


	public double ta() {

		if (etat == 0) sigma = DEVS_Real.POSITIVE_INFINITY;
		else if (etat == 1) 
			sigma = ra.nextInt((duree_max - duree_min) +1) + duree_min;
		return sigma;
	}



	public void lambda() throws DEVS_Exception {
		System.out.println("Lambda de Emetteur.");
		DEVS_Integer toSend = null;
		if(etat == 1) {
			toSend = new DEVS_Integer(1);				
		}else{
			toSend = new DEVS_Integer(0);
		}
		setOutputPortData(this.getName()+".OUTEmetteur", toSend);
		System.out.println("Envoi  GenerateurCar "+toSend);/*************************************************/
	}


	public void deltaExt(double e) throws DEVS_Exception {

		System.out.println("DeltaExt du Générateur de car.");
		String received = getInputPortData(this.getName()+".INEmetteur").toString();
		System.out.println("Recu generateurCar :"+received.toString());/*************************************/
		if((etat == 0) ) {
			if(received.compareTo("Faux") == 0) {
				etat = 0;
				sigma = DEVS_Real.POSITIVE_INFINITY;
			}else if(received.compareTo("Vrai") == 0) {
				etat =1;
				sigma = Tableau.loiAleatoire(duree_min,duree_max);
				//sigma = sigma -e;
			}		
		}else if((etat == 1)) {
			if(received.compareTo("Faux") == 0) {
				etat = 0;
				sigma = sigma-e;
			}else if(received.compareTo("Vrai") == 0) {
				etat =1;
				sigma = DEVS_Real.POSITIVE_INFINITY;
			}
		}else {
			
		}
	}	
}
