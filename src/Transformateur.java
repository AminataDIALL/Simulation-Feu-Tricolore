

import model.AtomicModel;
import exception.DEVS_Exception;
import types.DEVS_Enum;
import types.DEVS_Real;

import java.util.*;

public class Transformateur extends AtomicModel {
	// Definition of S : no state variable since there is only one single possible state

	// Additional attribute necessary for random number generation
	Random ra;
	int etat;
	public double sigma;

	public Transformateur(String name, String desc) {
		super(name, desc);


		String[] couleurs = {"Vert","Jaune","Rouge","Panne"};
		addInputPortStructure(new DEVS_Enum(couleurs), this.getName()+".INTransformateur", " Trigger received");

		String[] permissions = {"Vrai,Faux"};
		addOutputPortStructure(new DEVS_Enum(permissions), this.getName()+".OUTTransformateur", "My single output port");


		// Initialization of the additional attribute
		ra = new Random();
		etat = 0;
	}

	public void deltaInt() {
		// Nothing to say: we always return to the same implicit state
		if(etat ==0){
			etat=0;
			sigma=(ra.nextDouble()*2 + 5.0); 
		}

		if(etat ==1){
			etat=1;
			sigma= (ra.nextDouble()*2 + 5.0) ;
		}
	}

	public double ta() {
		// The real value returned is in [50, 80]
		if(etat ==0) sigma = DEVS_Real.POSITIVE_INFINITY;
		else if (etat ==1) sigma = (ra.nextDouble()*2.0 + 10.0);

		return sigma;
	}

	public void lambda() throws DEVS_Exception {
		// The value sent out is randomly "stop" or "start"

		String envoie = "";

		if(etat ==0)
			envoie= "Vrai";
		else if(etat == 1)
			envoie = "Faux";

		setOutputPortData(this.getName()+".OUTTransformateur", envoie);
	}

	public void deltaExt(double e) throws DEVS_Exception {
		// Not defined, since there is no input

		String received = this.getInputPortData(this.getName()+".INTransformateur").toString();
		if (received.compareToIgnoreCase("Rouge") == 0) etat = 1;
		else if ((received.compareToIgnoreCase("Vert") == 0) ) etat = 0;
		else if ((received.compareToIgnoreCase("Jaune") == 0) ) etat = 0;
		else if ((received.compareToIgnoreCase("Panne") == 0) ) etat = 0;

	}
}
