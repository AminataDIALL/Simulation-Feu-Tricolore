

public class Car {

	private int immatriculation;
	public static int nbreCarCreer = 0;
		
	@Override
	public String toString() {
		return "Car [immatriculation=" + immatriculation + "]";
	}

	
	
	public Car(int identifiant) {
		immatriculation = identifiant;
	}
	
	public void setImmatriculation(int immatriculation) {
		this.immatriculation = immatriculation;
	}
	
	public int getImmatriculation() {
		return immatriculation;
	}
}
