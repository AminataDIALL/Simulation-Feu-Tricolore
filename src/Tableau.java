

import java.util.Random;

public class Tableau {

	Car[] tab;
	int nbrelt = 0;
	int taille;
	Car v;
	static Random r = new Random();

	public Tableau(int dist){
		taille = dist+1;
		tab = new Car[taille];
	}


	/**
	 * Lister le tableau
	 */
	public void listerTableau() {
		System.out.println("---------------------------------------------------------------------");
		for (int i = 1; i < taille; i++) {
			if(tab[i] == null)
				System.out.println(i+" ==> null");
			else
				System.out.println((i)+" ==> "+tab[i].getImmatriculation());
		}
	}


	/**
	 * Methode pour ajouter des cars dans la voie
	 * @param n
	 */
	
	public void addCar(int n) {
		if((nbreElt() < taille) && (n > 0))
			tab[taille-1] = new Car(n);
	}


	/**
	 * Methode pour determiner la position premiere
	 * @param n
	 */
	
	public int posPrem() {
		for (int i = 1; i < taille; i++) {
			if (tab[i] != null) {
				return i;
			}
		}
		return -1;
	}

	
	/**
	 * Methode pour tester si il y'a de la place dans la voie
	 * @param n
	 */
	public boolean estVide() {
		boolean b = true;
		for (Car car : tab) {
			if(car != null) 
				b = false;
		}
		return b;
	}


	/**
	 * Methode pour tester si la voie est peine
	 */
	public boolean isFull(){
		boolean b = false;
		for(Car c : tab){
			if(c != null){
				b= true;
			}
		}
		return b;
	}


	/**
	 * Methode pour décaler
	 * @param n
	 */
	public void decaler(int n){
		if((posPrem() > n) && (nbreElt() < taille) && (n > 0)) {
			for (int i = posPrem(); i < taille; i++) {
				tab[i-n] = tab[i];
				tab[i] = null;

			}
		}
	}


	/**
	 * méthode Rapprocher Car
	 * @param n
	 */
	public void rapprocher(int n) {
		for (int i = 0; i <= taille-1; i++) {
			for(int j=n; j>=0; j--) {
				if(((i-j) > 0) && (tab[i-j] == null)) {
					tab[i-j] = tab[i];
					tab[i] = null;
				}
			}			
		}
	}


	/**
	 * méthode pour faire sortir le car se trouvant a la premiere position
	 */
	public int getOutFirst(){
		int i = posPrem();
		Car v = tab[i];
		tab[i] = null;
		return v.getImmatriculation();
	}


	/**
	 * Nombre d'élément dans la voie
	 * @return
	 */
	public int nbreElt() {
		int nombre = 0;
		for (Car car : tab) {
			if(car != null) nombre ++;
		}
		return nombre;
	}


	/**
	 *loi aleatoire
	 * @return
	 */
	public static double loiAleatoire(int min, int max) {
		double result;
		if(min == 0) {
			result = r.nextInt(max);
		}else{
			result = (r.nextInt(max-min + 1)+(min));
		}
		return result;
	}
}
