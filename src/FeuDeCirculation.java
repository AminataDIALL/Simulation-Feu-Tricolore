

import exception.DEVS_Exception;
import model.*;
import types.*;

public class FeuDeCirculation extends AtomicModel {
	// Definition of S: a state variable is used for the current color

	int stateColor;
	private double dV ;
	private double dO ;
	private double dR ;
	
	 	
		public double sigma;
	public FeuDeCirculation(String name, String desc,int vert,int jaune,int rouge) {
		super(name, desc);
		
		String[] couleurs = {"Vert","Jaune","Rouge","Panne"};
		addOutputPortStructure(new DEVS_Enum(couleurs), this.getName()+".OUTFeu", "Color sent out for users");
		String[] commandes = {"off","on"};
		addInputPortStructure(new DEVS_Enum(commandes), this.getName()+".INFeu", "Trigger received");
		
		// State initialization: the light starts Green
		stateColor = 0;
		sigma = 0.0;
		dV = vert;
		dO = jaune;
		dR = rouge;
	}

	public void deltaInt() {
		
		//passage du vert au jaune
		if(stateColor ==0){
			stateColor=1;
			sigma=dO;
			//passage du jaune au rouge
		}else if(stateColor==1){
			stateColor=2;
			sigma=dR;
			//passage du rouge au vert 
		}else if(stateColor==2){
			stateColor=0;
			sigma=dV;
			//passage du preEteint à Eteint
		}else if(stateColor==3){
			stateColor=4;
			sigma=DEVS_Real.POSITIVE_INFINITY;	
			//passage du PostEteint au jaune
		}
		else if (stateColor==5){
			stateColor=0;
			sigma=dV;
		}
	}

	
	public double ta() {
	 
		if (stateColor == 0) sigma= dV;
		else if (stateColor == 1) sigma = dO;
		else if (stateColor == 2) sigma= dR;
		else if (stateColor == 3) sigma =0.0;
		else if (stateColor == 4) sigma = DEVS_Real.POSITIVE_INFINITY;
		else if (stateColor == 5) sigma=0.0;
		return sigma;
	}
	 
	public void lambda() throws DEVS_Exception {
		String toSend = "";
	  if(stateColor==0)
		  toSend = "Jaune";
	  else if(stateColor==1)
		  toSend = "Rouge";
	  else if(stateColor==2)
		  toSend = "Vert";
	 else if(stateColor==3)
		  toSend = "Panne";
	 else if(stateColor==5)
		  toSend = "Vert";
	  
	//  afficher le nom de la lumiere sur le port de sortie du système
	  
			this.setOutputPortData(this.getName()+".OUTFeu", toSend);	
	}

	public void deltaExt(double e) throws DEVS_Exception {
		// Let's get the value received
		String received = this.getInputPortData(this.getName()+".INFeu").toString();
		
		if( received.equals("on")  && stateColor == 0) {
			stateColor = 0;
			sigma = dV-e;
		}else if(received.equals("on") && stateColor == 1) {
			stateColor = 1;
			sigma = dO-e;
		}else if(received.equals("on") && stateColor == 2) {
			stateColor = 2;
			sigma = dR-e;
		}else if(received.equals("off") && stateColor == 0) {
			stateColor = 3;
			sigma = 0.0;
			
		}
		else if(received.equals("off") &&  stateColor == 1) {
			stateColor = 3;
			sigma = 0.0;
			
		}else if(received.equals("off") && stateColor == 2) {
			stateColor = 3;
			sigma = 0.0;
		}else if(received.equals("off") && stateColor == 4) {
			stateColor = 4;
			sigma = DEVS_Real.POSITIVE_INFINITY;//0.0;
		}else if(received.equals("on") && stateColor == 4) {
			stateColor = 5;
			sigma = 0.0;
			
		}else
		{
			System.out.println("Unexpected value received in " + this.getName() + ":" + received);

		}
	}
}