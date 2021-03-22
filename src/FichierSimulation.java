

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FichierSimulation {

	public static final File Voie1 = new File("Voie1.txt");
	public static final File Voie2 = new File("Voie2.txt");
	public static final File Voie3 = new File("Voie3.txt");
	public static final File Feu = new File("Collecteur.txt");
	private static ArrayList<Tempo> liste;


	public static void lecture(File file) throws IOException{
		BufferedReader br1 = new BufferedReader(new FileReader(Voie1));
		BufferedReader br2 = new BufferedReader(new FileReader(Voie2));
		BufferedReader br3 = new BufferedReader(new FileReader(Voie3));
		BufferedReader br4 = new BufferedReader(new FileReader(Feu));
		String line1;
		String line2;
		String line3;
		String line4;
		FileWriter fileWriter = new FileWriter(file);
		while((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null && (line3 = br3.readLine()) != null && (line4 = br4.readLine()) != null){
			fileWriter.write(line1+"\n");
			fileWriter.write(line2+"\n");
			fileWriter.write(line3+"\n");
			fileWriter.write(line4+"\n");
		}

		fileWriter.flush();
		fileWriter.close();
	}


	public static void fichierATableau(File file) throws IOException{

		liste = new ArrayList<>();		
		BufferedReader r = new BufferedReader(new FileReader(file));
		String line = "";

		while((line = r.readLine()) != null) {
			String[] t = line.split(":");
			if(t.length == 2) {
				double d = Double.parseDouble(t[0]);
				Tempo temp = new Tempo(t[0],t[1]);
				liste.add(temp);
			}
		}

	}



	public static void tableauAFichier(File file) throws IOException{

		Tempo model;		
		for(int i =0; i < liste.size(); i++) {
			Tempo model_i =  liste.get(i);

			for(int j =i+1; j < liste.size(); j++) {
				Tempo model_j = liste.get(j);

				double temps_i = Double.parseDouble(model_i.getTemps());
				double temps_j = Double.parseDouble(model_j.getTemps());

				if(temps_i >= temps_j){
					model = model_i;
					model_i = model_j;
					model_j = model;

					liste.set(i,model_i);
					liste.set(j,model_j);
				}

			}
		}

		FileWriter writer = new FileWriter(file);
		for(int i =0; i < liste.size(); i++) {

			Tempo model_i =  liste.get(i);
			writer.write(model_i.getTemps()+ " : "+ model_i.getAction() +"\r\n");

		}

	}

}
