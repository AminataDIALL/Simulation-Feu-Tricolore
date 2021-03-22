

import exception.DEVS_Exception;
import model.*;
import types.*;
import java.io.*;
import java.util.Random;

public class Alea extends AtomicModel {

	int etat;
	Random ra;
	private double duree_min;
	private double duree_max;

	public double sigma;

	public Alea(String name, String desc,double min,double max) {
		super(name, desc);

		String[] commandes = {"off"};
		addOutputPortStructure(new DEVS_Enum(commandes), this.getName()+".OUTAlea", "Commande á envoyer");

		// State initialization
		etat = 0;
		duree_min =min;
		duree_max = max;
		ra = new Random();	
	}


	public void deltaInt() {
		//passage de Actif a Actif
		if(etat ==0){
			etat=0;
		}
	}


	public double ta() {
		sigma = ra.nextDouble()*duree_min + duree_max; 
		return sigma;
	}



	public void lambda() throws DEVS_Exception {

		String envoie="";
		if(etat == 0) {
			envoie = "off"; 
		} 
		this.setOutputPortData(this.getName()+".OUTAlea", envoie);	
	}

	public void deltaExt(double e) throws DEVS_Exception {}
}