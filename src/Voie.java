

import exception.DEVS_Exception;
import model.*;
import types.*;
import java.io.*;
import java.util.Random;

public class Voie extends AtomicModel {

	int etat;
	boolean mem;
	int taille;
	Tableau tab;
	double duree;
	PrintStream trajectory;

	public Voie(String name, String desc, int longVoie,int vitesse) {
		super(name, desc);

		addInputPortStructure(new DEVS_Integer(), this.getName()+".INCar", "l'entrée des cars dans la voie");

		addOutputPortStructure(new DEVS_Integer(), this.getName()+".OUTCar", "la sortie des cars dans la voie");

		String[] entreePerm = {"Vrai", "Faux"};
		addInputPortStructure(new DEVS_Enum(entreePerm), this.getName()+".INPerm", "l'entrée de la permission");

		String[] sortieFull = {"Vrai", "Faux"};
		addOutputPortStructure(new DEVS_Enum(sortieFull), this.getName()+".OUTPerm", "Autorisation d'emission des cars par le système");


		//etat0 aucune voiture dans la voie, etat1 voie non vide, etat2 transitoire
		etat = 0;
		mem = true; // permission autorisée
		taille = longVoie; // la longueur de la voie
		tab = new Tableau(taille); //une instance de la classe tableau
		duree = DEVS_Real.POSITIVE_INFINITY; //la durée des états
		//tab.listerTableau();// la liste des éléments du tableau

		try {
			trajectory = new PrintStream(name + ".txt");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deltaInt() {
		System.err.println("******__DeltaInt de Voie.__******");
		if(etat == 1) {
			if(mem) {
				if(tab.estVide()) {
					etat = 0;
					duree = DEVS_Real.POSITIVE_INFINITY;
					System.out.println("etat1 ==> etat0 et mem(true) = "+ mem +" et posPrem = "+tab.posPrem());
				}
				else {
					etat = 1;
					duree = tab.posPrem();
					System.err.println("etat1 ==> etat1 et mem(true) = "+ mem +" et estVide = "+tab.estVide()+" ; et posPrem = "+tab.posPrem()+" durée ="+duree);
				}
			}
			else {// si mémoire est fausse(pas de permission)
				System.err.println("Etat = etat1 et mem = "+ mem +"  : Rapprochement de "+tab.posPrem());
				tab.rapprocher(tab.posPrem());
				etat = 1;
				duree = tab.posPrem();
				System.err.println("etat1 ==> etat1 et mem = "+ mem +" ; et posPrem = "+tab.posPrem()+"; duré ="+duree);
				tab.listerTableau();
			}
		}
		else if(etat == 2) {
			etat = 1;
			duree = tab.posPrem();
			System.err.println("etat2 ==> etat1 et posPrem = "+tab.posPrem());
			tab.listerTableau();
		}
	}

	@Override
	public void lambda() throws DEVS_Exception {
		System.err.println("******__Lambda de Voie.__******");
		DEVS_Integer sendOut = null;
		String sendFull = "";
		double whenout = this.getSimulator().getTL();
		if(etat == 1) {
			if(mem) {
				sendOut = new DEVS_Integer(tab.getOutFirst());
				setOutputPortData(this.getName()+".OUTCar", sendOut);
				sendFull = "FAUX";
				System.err.println("Etat = etat1 et mem ="+ mem +" ; Car sorti : getOutFist :"+sendOut+" et  Decalement de "+tab.posPrem());
				tab.decaler(tab.posPrem());
				tab.listerTableau();

				trajectory.println(whenout + " :"+" outCar " +sendOut+" "+this.getName());
			}
		}
		else if(etat == 2){
			if(tab.isFull()) {
				sendFull = "VRAI";
				tab.listerTableau();
			}else {
				sendFull = "FAUX";
			}
			System.err.println("sendFull dans 2 else et file n'est pas plein:"+sendFull);
		}
		System.err.println("sendout :"+sendOut+" et memoire = "+ mem +" isFull = "+tab.isFull()+" sendFull= "+sendFull);
		setOutputPortData(this.getName()+".sortieFull", sendFull);

	}

	@Override
	public double ta() {
		System.err.println("******__ta() de Voie.__******");
		if(etat == 0) duree = DEVS_Real.POSITIVE_INFINITY;
		else if(etat == 1) duree = tab.posPrem();
		else duree = 0.0;
		System.err.println("ta ="+duree);
		return duree;
	}

	@Override
	public void deltaExt(double e) throws DEVS_Exception {
		System.err.println("DeltaExt de Voie.");
		int receivedCar = ((DEVS_Integer)getInputPortData(this.getName()+".InCar")).getInteger();
		String receivedPerm = getInputPortData(this.getName()+".InPerm").toString();

		double whendeltaext = this.getSimulator().getTL();

		System.err.println("Car : "+receivedCar+"; Perm :"+receivedPerm.toString()+" e= "+(int)e+" ta = "+ta());/****************************************/
		if(etat == 0) {
			if(receivedCar >0 && receivedPerm.compareTo("Vrai")==0) {
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				System.err.println("Ajout du car "+car +" dans la voie");
				tab.addCar(car);
				tab.listerTableau();

				trajectory.println(whendeltaext + " : "+ receivedPerm);
				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());

				mem = true;
			}
			else if(receivedCar >0 && receivedPerm.compareTo("Faux")==0){
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				System.err.println("Ajout du car "+car +" dans la voie");
				tab.addCar(car);
				tab.listerTableau();

				trajectory.println(whendeltaext + " : "+ receivedPerm);
				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());
				mem = false;	
			}

			else if(receivedPerm.compareTo("Vrai") == 0) {
				mem = true;
				etat = 0;
				duree = DEVS_Real.POSITIVE_INFINITY;
				tab.listerTableau();

				trajectory.println(whendeltaext + " : "+ receivedPerm);
			}
			else if(receivedPerm.compareTo("Faux") == 0) {
				mem = false;
				etat = 0;
				duree = DEVS_Real.POSITIVE_INFINITY;
				tab.listerTableau();

				trajectory.println(whendeltaext + " : "+ receivedPerm);
			}

			else if(receivedCar > 0) {
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				tab.addCar(car);
				tab.listerTableau();

				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());
			}

		}

		else if(etat == 1) {
			if(receivedCar>0 && (receivedPerm.compareTo("Vrai")==0)) {
				mem = true;
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				tab.decaler((int)e);
				tab.addCar(car);
				tab.listerTableau();
				System.out.println("1-Etat =1; receivePerm ="+ receivedPerm+" et car ="+car);

				trajectory.println(whendeltaext + " : "+ receivedPerm);
				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());
			}
			else if(receivedCar>0 && (receivedPerm.compareTo("Faux")==0)) {
				mem = false;
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				tab.decaler((int)e);
				tab.addCar(car);
				tab.listerTableau();
				System.out.println("1-Etat =1; receivePerm ="+ receivedPerm+" et car ="+car);

				trajectory.println(whendeltaext + " : "+ receivedPerm);
				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());
			}
			else if((receivedPerm.compareTo("VRAI") == 0)) {
				mem = true;
				etat = 1;
				duree = (tab.posPrem() - e);
				tab.decaler((int)e);
				tab.listerTableau();
				System.out.println("2-Etat =1; receivePerm ="+ receivedPerm);

				trajectory.println(whendeltaext + " : "+ receivedPerm);
			}
			else if(receivedPerm.compareTo("FAUX") == 0) {
				mem = false;
				etat = 1;
				duree = (tab.posPrem() - e);
				tab.decaler((int)e);
				tab.listerTableau();
				System.out.println("3-Etat =1; receivePerm ="+ receivedPerm.toUpperCase());

				trajectory.println(whendeltaext + " : "+ receivedPerm);
			}
			else if(receivedCar > 0){
				etat = 2;
				duree = 0;
				int car = ++(Car.nbreCarCreer);
				tab.decaler((int)e);
				tab.addCar(car);
				tab.listerTableau();
				System.out.println("4-Etat =1; receivePerm ="+ receivedPerm.toUpperCase()+" et car ="+car);

				trajectory.println(whendeltaext + " :"+" add "+ Car.nbreCarCreer+" "+this.getName());
			}
		}
	}
}