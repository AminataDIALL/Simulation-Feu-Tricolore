

import exception.DEVS_Exception;
import model.*;
import types.*;
import java.io.*;

public class Collecteur extends AtomicModel {
	// Definition of S :
	// there is only one single possible state which lasts for ever until a trigger is received 
	// a state variable is used for the current repository (a file)
	PrintStream trajectory;

	public Collecteur(String name, String desc) {
		super(name, desc);

		
		String[] couleurs = {"Vert", "Jaune", "Rouge", "Panne"};
		addInputPortStructure(new DEVS_Enum(couleurs), this.getName()+".OUTPUT", "Data to store");


		addInputPortStructure(new DEVS_Integer(), this.getName()+".INCAR", "Data to store for Car");

		// State initialization: the name of the file is xxx.txt if the name of the model is xxx
		try {
			trajectory = new PrintStream(name+".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public double ta() {
		return DEVS_Real.POSITIVE_INFINITY;
	}

	public void deltaInt() {
		// Not defined, since there is a single possible state which time advance value is +infinity
	}

	public void lambda() throws DEVS_Exception {
		// Not defined, for the same reasons
	}

	public void deltaExt(double e) throws DEVS_Exception {

		int receivedCar = ((DEVS_Integer)getInputPortData(this.getName()+".INCAR")).getInteger();
		// Let's get the value received and the simulation time it has been received
		String received = getInputPortData(this.getName()+".OUTPUT").toString();
		double when = this.getSimulator().getTL();

		// Then store them in the following shape:
		// simulation time : data received
		System.out.println("1 feu "+received + " et car reçu collecteur :"+receivedCar);
		trajectory.println(when + " : " + received);
	}
}