

import model.AtomicModel;
import exception.DEVS_Exception;
import types.DEVS_Enum;
import types.DEVS_Real;

import java.util.*;

public class Controlleur extends AtomicModel {

	private int etat;

	Random ra; 
	private int duree_min;
	private int duree_max;

	private double sigma;

	public Controlleur(String name, String desc,int min,int max) {
		super(name, desc);
		// TODO Auto-generated constructor stub

		String[] commands ={"on"};
		addOutputPortStructure(new DEVS_Enum(commands), this.getName()+".OUTControlleur", "Commands envoyé");

		String[] couleurs = { "Vert","Jaune", "Rouge","Panne"};
		addInputPortStructure(new DEVS_Enum(couleurs), this.getName()+".INControlleur", "Trigger received");
		etat = 0;
		sigma =DEVS_Real.POSITIVE_INFINITY;
		duree_min = min;
		duree_max = max;
		ra = new Random();
	}

	@Override
	public void deltaExt(double arg0) throws DEVS_Exception {
		// TODO Auto-generated method stub
		String received = this.getInputPortData(this.getName()+".INControlleur").toString();

		if(etat== 0  && (received.compareTo("Vert") == 0 || received.compareTo("Jaune") == 0 || received.compareTo("Rouge") == 0 )) 
		{
			etat = 0;
			sigma = DEVS_Real.POSITIVE_INFINITY;
		}else if(etat == 0 && received.compareTo("Panne") == 0) {
			etat = 1;
			sigma = Tableau.loiAleatoire(duree_min, duree_max);
		}
		else {
			System.out.println("Valeur non attendu á l'entrée " + this.getName() + ":" + received);
		}
	}

	@Override
	public void deltaInt() {
		// TODO Auto-generated method stub

		if(etat ==1){
			etat=0;
		}
	}

	@Override
	public void lambda() throws DEVS_Exception {
		// TODO Auto-generated method stub

		String envoie = "";

		if(etat ==1)
			envoie= "on";

		setOutputPortData(this.getName()+".OUTControlleur", envoie);
	}

	@Override
	public double ta() {
		// TODO Auto-generated method stub
		if(etat ==0) sigma = DEVS_Real.POSITIVE_INFINITY;
		else if (etat ==1) sigma = Tableau.loiAleatoire(duree_min, duree_max);

		return sigma;
	}

}