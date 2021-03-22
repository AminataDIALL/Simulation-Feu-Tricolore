

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;


import exception.DEVS_Exception;
import simulator.RootCoordinator;

import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JMenu;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;



import java.awt.Component;
import javax.swing.border.LineBorder;

public class Visualiseur extends JFrame implements Runnable {

	private JFrame frame;
	private JTextField id_text_vert;
	// JLabel which contains the image of the traffic light
	protected JLabel image_;
	private JLabel id_label_file_chose; 

	// Image of the traffic light off
	protected ImageIcon f_panne;

	// Image of the green traffic light
	protected ImageIcon f_vert;

	// Image of the orange traffic light
	protected ImageIcon f_jaune;

	// Image of the red traffic light
	protected ImageIcon f_rouge;

	// Image of the red traffic light
	protected ImageIcon image_car;

	// Current time of the simulation
	protected double time;

	int speed = 5;
	int timeToSleep ;
	protected boolean ready = false;
	Timer timer;
	// List of the states and dates of times of the traffic light
	protected ArrayList<String[]> infos_;

	protected JFileChooser fileChooser_;
	private JTextField id_text_jaune;
	private JTextField id_text_rouge;
	private JTextField id_text_panne_mini;
	private JTextField id_text_panne_maxi;
	private JTextField id_text_reparation_mini;
	private JTextField id_text_reparation_maxi;
	private JTextField id_text_emetteur_mini;
	private JTextField id_text_emetteur_maxi;
	private JTextField id_text_simulation_mini;
	private JTextField id_text_simulation_maxi;
	private GridBagLayout gridBag;
	private GridBagConstraints constraints;
	private int taille_table ;
	private ImageIcon default_car;
	private static Visualiseur window;
	private JTextField id_text_longuer_voie;
	private JLabel[][] tableau_voie;
	private JPanel panel_voie;
	private JLabel id_label_chemin_fichier;
	private String chemin;
	private Panel id_panel_feu_tricolor;
	protected boolean pause = false;

	private static String[][] tableau;
	static int longueur;
	private File  file;
	FichierSimulation fichier;
	private ArrayList<Tempo> list;
	private JTextField id_text_emetteur2_mini;
	private JTextField id_text_emetteur2_maxi;
	private JTextField id_text_emetteur3_mini;
	private JTextField id_text_emetteur3_maxi;
	private JTextField id_text_longueur_1;
	private JTextField id_text_longueur_2;
	private JTextField id_text_longueur_3;
	private JButton btnRalentir;
	private JButton btnReprendre;
	private JButton btnAccelerer;
	private JButton btnPause;
	private JButton btnOpenFile;
	JButton btnLancerAnimation;
	private ArrayList<Component> component;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Visualiseur();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Visualiseur() {
		infos_ = new ArrayList<String[]>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Modelisation & Simulation");
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);

		frame.setBounds(0, 0, 1080, 684);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileChooser_ = new JFileChooser();
		JLabel lblParamtres = new JLabel("PARAMETRES DE SIMULATION");
		lblParamtres.setBounds(56, 11, 333, 19);
		lblParamtres.setFont(new Font("Times New Roman", Font.BOLD, 20));


		JLabel lblLaDureDes = new JLabel("Dur\u00E9e des etats du feu");
		lblLaDureDes.setBounds(10, 313, 172, 14);
		lblLaDureDes.setFont(new Font("Times New Roman", Font.BOLD, 18));

		JLabel lblLoiAleatoire = new JLabel("Loi al\u00E9atoire");
		lblLoiAleatoire.setBounds(283, 313, 106, 14);
		lblLoiAleatoire.setFont(new Font("Times New Roman", Font.BOLD, 18));

		JLabel lblContrleDesExpriences = new JLabel("Contr\u00F4le des exp\u00E9riences");
		lblContrleDesExpriences.setBounds(117, 440, 202, 28);
		lblContrleDesExpriences.setFont(new Font("Times New Roman", Font.BOLD, 18));

		JOptionPane dialog = new JOptionPane();
		JButton btnLancerLaSimulation = new JButton("Lancer la simulation");
		btnLancerLaSimulation.setEnabled(false);
		btnLancerLaSimulation.setBounds(128, 574, 172, 28);
		btnLancerLaSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//System.out.println(taille_table);
				//dessinevoie(taille_table);
				//panel_voie.repaint();
				if( !(id_text_longueur_1.getText().toString().equals(""))&& !(id_text_longueur_2.getText().toString().equals("")) && !(id_text_longueur_3.getText().toString().equals("")) && !(id_text_panne_maxi.getText().toString().equals("")) && !(id_text_panne_mini.getText().toString().equals(""))  && !(id_text_reparation_mini.getText().toString().equals("")) && !(id_text_reparation_maxi.getText().toString().equals(""))
						&& !(id_text_emetteur_maxi.getText().toString().equals(""))  && !(id_text_emetteur_mini.getText().toString().equals(""))  && !(id_text_emetteur2_maxi.getText().toString().equals(""))&& !(id_text_emetteur2_mini.getText().toString().equals(""))
						&& !(id_text_emetteur3_maxi.getText().toString().equals("")) && !(id_text_emetteur3_mini.getText().toString().equals("")) && !(id_text_simulation_mini.getText().toString().equals("")) &&
						!(id_text_simulation_maxi.getText().toString().equals(""))) {
					//la longueur des différentes voies
					//if(!id_text_longueur_1.getText().toString().equals("")) {
					int vitesse_v1 = Integer.parseInt(id_text_longueur_1.getText());
					int vitesse_v2 = Integer.parseInt(id_text_longueur_2.getText());
					int vitesse_v3 = Integer.parseInt(id_text_longueur_3.getText());

					//la durée des états du feu
					int vert = Integer.parseInt(id_text_vert.getText());
					int jaune = Integer.parseInt(id_text_jaune.getText());
					int rouge = Integer.parseInt(id_text_rouge.getText());

					//la durée aléatoire reparation 
					int reparation_mini = Integer.parseInt(id_text_reparation_mini.getText());
					int reparation_maxi = Integer.parseInt(id_text_reparation_maxi.getText());

					//la durée aléatoire panne
					double panne_maxi = Double.parseDouble(id_text_panne_maxi.getText());
					double panne_mini = Double.parseDouble(id_text_panne_mini.getText());

					//la durée aléatoire voie
					int emet1_maxi = Integer.parseInt(id_text_emetteur_maxi.getText());
					int emet1_mini = Integer.parseInt(id_text_emetteur_mini.getText());

					int emet2_maxi = Integer.parseInt(id_text_emetteur2_maxi.getText());
					int emet2_mini = Integer.parseInt(id_text_emetteur2_mini.getText());

					int emet3_maxi = Integer.parseInt(id_text_emetteur3_maxi.getText());
					int emet3_mini = Integer.parseInt(id_text_emetteur3_mini.getText());

					//la durée de la simulation
					int simulation_mini = Integer.parseInt(id_text_simulation_mini.getText());
					int simulation_maxi = Integer.parseInt(id_text_simulation_maxi.getText());


					//vérification des valeurs saisies dans le visualiser
					if((panne_maxi > panne_mini))
					{
						if((reparation_maxi > reparation_mini))
						{
							if((emet1_maxi > emet1_mini))
							{
								if((emet2_maxi > emet2_mini))
								{
									if( (emet3_maxi > emet3_mini)  ) {
										if((simulation_maxi > simulation_mini))
										{
											taille_table = Integer.parseInt(id_text_longuer_voie.getText().toString());

											tableau_voie = new JLabel[3][taille_table];
											//default_car = createImageIcon("image_init.jpg", "defaut");
											for(int i = 0; i<=2 ; i++)
											{
												for(int j=0;j<taille_table; j++)
												{
													JLabel jLabel  = new JLabel();
													jLabel.setIcon(default_car);
													tableau_voie[i][j] = jLabel;
													constraints.gridx = i;
													constraints.gridy = j;
													panel_voie.add(tableau_voie[i][j],constraints);
												}
											}
											panel_voie.setLayout(gridBag);
											panel_voie.repaint();
											panel_voie.validate();

											FeuDeCirculation_System study = new FeuDeCirculation_System("TFS","Experimentation of a feu system",panne_mini,panne_maxi,reparation_mini,reparation_maxi,emet1_mini,emet1_maxi,emet2_mini,emet2_maxi,emet3_mini,emet3_maxi,taille_table,vert,jaune,rouge,vitesse_v1,vitesse_v2,vitesse_v3);


											//lancement de la simulation
											RootCoordinator root = new RootCoordinator(study.getSimulator());

											// Experimentation:
											// initial time is 0.0
											// final time is 1000.0
											try {
												root.init(simulation_mini);
												root.run(simulation_maxi);

												file = new File("tempo.txt");
												classer(file);
												fileToArray(file);
												arrayToFile(new File(chemin));

											} catch (DEVS_Exception e) {
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											btnOpenFile.setEnabled(true);



										}else
											JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la durée de la simulation", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);
									}else
										JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la loi aléatoire d'arrivée par voie 3", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);
								}else {
									JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la loi aléatoire d'arrivée par voie 2", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);									
								}
							}else {
								JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la loi aléatoire d'arrivée par voie 1", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);
							}

						}else {
							JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la réparation du feu", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);

						}

					}else {
						JOptionPane.showMessageDialog(null, "Maxi doit être superieur au mini au niveau de la panne", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);
					}

				}else {
					JOptionPane.showMessageDialog(null, "Veuillez renseigner tous les champs!!!", "Alerte!!!", JOptionPane.INFORMATION_MESSAGE);

				}
				//}
			}
		});
		btnLancerLaSimulation.setFont(new Font("Times New Roman", Font.BOLD, 13));

		JSeparator separator = new JSeparator();
		separator.setBounds(436, 11, 4, 637);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBackground(Color.BLACK);
		separator.setForeground(Color.BLUE);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(731, 11, 2, 637);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.BLUE);
		separator_1.setBackground(Color.BLACK);

		id_panel_feu_tricolor = new Panel();
		id_panel_feu_tricolor.setBounds(766, 25, 132, 258);
		id_panel_feu_tricolor.setBackground(Color.LIGHT_GRAY);

		JLabel lblFeuTricolor = new JLabel("Feu tricolore");
		lblFeuTricolor.setBounds(793, 289, 105, 28);
		lblFeuTricolor.setToolTipText("");
		lblFeuTricolor.setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblParamtres);
		frame.getContentPane().add(lblLaDureDes);
		frame.getContentPane().add(lblLoiAleatoire);
		frame.getContentPane().add(lblContrleDesExpriences);
		frame.getContentPane().add(btnLancerLaSimulation);
		frame.getContentPane().add(separator);
		frame.getContentPane().add(separator_1);
		frame.getContentPane().add(id_panel_feu_tricolor);
		id_panel_feu_tricolor.setLayout(null);

		image_ = new JLabel("");
		image_.setBounds(10, 11, 122, 236);
		image_.setForeground(Color.WHITE);
		image_.setBackground(Color.WHITE);
		id_panel_feu_tricolor.add(image_);
		frame.getContentPane().add(lblFeuTricolor);

		JLabel lblVitesse_1 = new JLabel("Vitesses et Longueur des voies");
		lblVitesse_1.setBounds(90, 62, 262, 19);
		lblVitesse_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.getContentPane().add(lblVitesse_1);

		JPanel panel_13 = new JPanel();
		panel_13.setBounds(203, 338, 116, 90);
		panel_13.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		frame.getContentPane().add(panel_13);
		panel_13.setLayout(null);

		JLabel lblPanne = new JLabel("Panne");
		lblPanne.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblPanne.setBounds(40, 10, 53, 14);
		panel_13.add(lblPanne);

		JLabel lblMini = new JLabel("Mini :");
		lblMini.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblMini.setBounds(10, 38, 33, 14);
		panel_13.add(lblMini);

		id_text_panne_mini = new JTextField();
		id_text_panne_mini.setColumns(10);
		id_text_panne_mini.setBounds(63, 35, 43, 20);
		panel_13.add(id_text_panne_mini);

		id_text_panne_maxi = new JTextField();
		id_text_panne_maxi.setColumns(10);
		id_text_panne_maxi.setBounds(63, 59, 43, 20);
		panel_13.add(id_text_panne_maxi);

		JLabel lblMaxi = new JLabel("Maxi :");
		lblMaxi.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblMaxi.setBounds(10, 62, 46, 14);
		panel_13.add(lblMaxi);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.WHITE);
		separator_6.setBackground(Color.BLACK);
		separator_6.setBounds(0, 25, 116, 2);
		panel_13.add(separator_6);

		JPanel panel_14 = new JPanel();
		panel_14.setBounds(320, 338, 106, 90);
		panel_14.setLayout(null);
		panel_14.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		frame.getContentPane().add(panel_14);

		JLabel lblRparationFeu = new JLabel("R\u00E9paration feu");
		lblRparationFeu.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblRparationFeu.setBounds(10, 11, 102, 14);
		panel_14.add(lblRparationFeu);

		JLabel label_3 = new JLabel("Mini :");
		label_3.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_3.setBounds(10, 38, 33, 14);
		panel_14.add(label_3);

		id_text_reparation_mini = new JTextField();
		id_text_reparation_mini.setColumns(10);
		id_text_reparation_mini.setBounds(53, 35, 40, 20);
		panel_14.add(id_text_reparation_mini);


		id_text_reparation_maxi = new JTextField();
		id_text_reparation_maxi.setColumns(10);
		id_text_reparation_maxi.setBounds(53, 59, 40, 20);
		panel_14.add(id_text_reparation_maxi);

		JLabel label_6 = new JLabel("Maxi :");
		label_6.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_6.setBounds(10, 62, 46, 14);
		panel_14.add(label_6);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(Color.WHITE);
		separator_7.setBackground(Color.BLACK);
		separator_7.setBounds(-4, 25, 116, 2);
		panel_14.add(separator_7);

		JPanel panel_15 = new JPanel();
		panel_15.setBounds(23, 204, 125, 90);
		panel_15.setLayout(null);
		panel_15.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		frame.getContentPane().add(panel_15);

		JLabel lblVoie = new JLabel("Voie 1");
		lblVoie.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblVoie.setBounds(44, 6, 46, 14);
		panel_15.add(lblVoie);

		JLabel label_8 = new JLabel("Mini :");
		label_8.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_8.setBounds(10, 34, 33, 14);
		panel_15.add(label_8);

		id_text_emetteur_mini = new JTextField();
		id_text_emetteur_mini.setColumns(10);
		id_text_emetteur_mini.setBounds(53, 31, 53, 20);
		panel_15.add(id_text_emetteur_mini);

		id_text_emetteur_maxi = new JTextField();
		id_text_emetteur_maxi.setColumns(10);
		id_text_emetteur_maxi.setBounds(53, 62, 53, 20);
		panel_15.add(id_text_emetteur_maxi);

		JLabel label_9 = new JLabel("Maxi :");
		label_9.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_9.setBounds(10, 62, 46, 14);
		panel_15.add(label_9);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(0, 25, 125, 2);
		panel_15.add(separator_4);
		separator_4.setForeground(Color.WHITE);
		separator_4.setBackground(Color.BLACK);

		JPanel panel_16 = new JPanel();
		panel_16.setBounds(10, 472, 172, 90);
		panel_16.setLayout(null);
		panel_16.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		frame.getContentPane().add(panel_16);

		JLabel lblLaDureDe = new JLabel("Dur\u00E9e de la simulation");
		lblLaDureDe.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblLaDureDe.setBounds(10, 6, 162, 14);
		panel_16.add(lblLaDureDe);

		JLabel label_7 = new JLabel("Mini :");
		label_7.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_7.setBounds(20, 31, 33, 14);
		panel_16.add(label_7);

		id_text_simulation_mini = new JTextField();
		id_text_simulation_mini.setColumns(10);
		id_text_simulation_mini.setBounds(86, 31, 63, 20);
		panel_16.add(id_text_simulation_mini);

		id_text_simulation_maxi = new JTextField();
		id_text_simulation_maxi.setColumns(10);
		id_text_simulation_maxi.setBounds(86, 59, 63, 20);
		panel_16.add(id_text_simulation_maxi);

		JLabel label_10 = new JLabel("Maxi :");
		label_10.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_10.setBounds(20, 62, 39, 14);
		panel_16.add(label_10);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setForeground(Color.WHITE);
		separator_8.setBackground(Color.BLACK);
		separator_8.setBounds(0, 25, 172, 2);
		panel_16.add(separator_8);

		panel_voie = new JPanel();
		panel_voie.setBackground(Color.DARK_GRAY);
		panel_voie.setBounds(450, 25, 271, 623);
		panel_voie.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		frame.getContentPane().add(panel_voie);
		gridBag = new GridBagLayout();

		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 20, 5, 0);
		panel_voie.setLayout(gridBag);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(165, 204, 125, 88);
		frame.getContentPane().add(panel);

		JLabel lblVoie_3 = new JLabel("Voie 2");
		lblVoie_3.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblVoie_3.setBounds(47, 8, 46, 14);
		panel.add(lblVoie_3);

		JLabel label_4 = new JLabel("Mini :");
		label_4.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_4.setBounds(20, 33, 33, 14);
		panel.add(label_4);

		id_text_emetteur2_mini = new JTextField();
		id_text_emetteur2_mini.setColumns(10);
		id_text_emetteur2_mini.setBounds(68, 30, 50, 20);
		panel.add(id_text_emetteur2_mini);

		id_text_emetteur2_maxi = new JTextField();
		id_text_emetteur2_maxi.setColumns(10);
		id_text_emetteur2_maxi.setBounds(68, 57, 50, 20);
		panel.add(id_text_emetteur2_maxi);


		JLabel label_11 = new JLabel("Maxi :");
		label_11.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_11.setBounds(22, 60, 46, 14);
		panel.add(label_11);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.WHITE);
		separator_3.setBackground(Color.BLACK);
		separator_3.setBounds(-148, 25, 324, 2);
		panel.add(separator_3);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_1.setBounds(302, 204, 124, 88);
		frame.getContentPane().add(panel_1);

		JLabel lblVoie_4 = new JLabel("Voie 3");
		lblVoie_4.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblVoie_4.setBounds(44, 7, 46, 14);
		panel_1.add(lblVoie_4);

		JLabel label_13 = new JLabel("Mini :");
		label_13.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_13.setBounds(10, 35, 33, 14);
		panel_1.add(label_13);

		id_text_emetteur3_mini = new JTextField();
		id_text_emetteur3_mini.setColumns(10);
		id_text_emetteur3_mini.setBounds(60, 32, 50, 20);
		panel_1.add(id_text_emetteur3_mini);

		id_text_emetteur3_maxi = new JTextField();
		id_text_emetteur3_maxi.setColumns(10);
		id_text_emetteur3_maxi.setBounds(60, 57, 50, 20);
		panel_1.add(id_text_emetteur3_maxi);

		JLabel label_14 = new JLabel("Maxi :");
		label_14.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_14.setBounds(10, 60, 46, 14);
		panel_1.add(label_14);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.WHITE);
		separator_5.setBackground(Color.BLACK);
		separator_5.setBounds(0, 25, 125, 2);
		panel_1.add(separator_5);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_2.setBounds(43, 85, 354, 81);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel label_2 = new JLabel("Voie 1");
		label_2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label_2.setBounds(10, 14, 46, 14);
		panel_2.add(label_2);

		id_text_longueur_1 = new JTextField();
		id_text_longueur_1.setColumns(10);
		id_text_longueur_1.setBounds(66, 11, 38, 20);
		panel_2.add(id_text_longueur_1);

		JLabel label_5 = new JLabel("Voie 2");
		label_5.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label_5.setBounds(121, 12, 46, 19);
		panel_2.add(label_5);

		id_text_longueur_2 = new JTextField();
		id_text_longueur_2.setColumns(10);
		id_text_longueur_2.setBounds(166, 12, 38, 20);
		panel_2.add(id_text_longueur_2);

		JLabel label_12 = new JLabel("Voie 3");
		label_12.setFont(new Font("Times New Roman", Font.BOLD, 14));
		label_12.setBounds(224, 14, 46, 14);
		panel_2.add(label_12);

		id_text_longueur_3 = new JTextField();
		id_text_longueur_3.setColumns(10);
		id_text_longueur_3.setBounds(280, 12, 38, 20);
		panel_2.add(id_text_longueur_3);

		id_text_longuer_voie = new JTextField();
		id_text_longuer_voie.setBounds(176, 52, 73, 20);
		panel_2.add(id_text_longuer_voie);
		id_text_longuer_voie.setColumns(10);

		JLabel lblLonguer = new JLabel("Longueur :");
		lblLonguer.setBounds(94, 52, 73, 21);
		panel_2.add(lblLonguer);
		lblLonguer.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(20, 39, 324, 2);
		panel_2.add(separator_2);
		separator_2.setForeground(Color.WHITE);
		separator_2.setBackground(Color.BLACK);

		JLabel lblLoiAlatoirePar = new JLabel("Loi al\u00E9atoire d'arriv\u00E9e par voie");
		lblLoiAlatoirePar.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblLoiAlatoirePar.setBounds(94, 177, 258, 23);
		frame.getContentPane().add(lblLoiAlatoirePar);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_3.setBounds(10, 338, 153, 90);
		frame.getContentPane().add(panel_3);

		id_text_vert = new JTextField();
		id_text_vert.setBounds(77, 11, 38, 20);
		panel_3.add(id_text_vert);
		id_text_vert.setColumns(10);

		JLabel lblVert = new JLabel("Vert :");
		lblVert.setBounds(21, 14, 46, 14);
		panel_3.add(lblVert);
		lblVert.setFont(new Font("Times New Roman", Font.BOLD, 12));

		id_text_jaune = new JTextField();
		id_text_jaune.setBounds(77, 36, 38, 20);
		panel_3.add(id_text_jaune);
		id_text_jaune.setColumns(10);

		JLabel label = new JLabel("Jaune :");
		label.setBounds(21, 42, 46, 14);
		panel_3.add(label);
		label.setFont(new Font("Times New Roman", Font.BOLD, 13));

		id_text_rouge = new JTextField();
		id_text_rouge.setBounds(77, 64, 38, 20);
		panel_3.add(id_text_rouge);
		id_text_rouge.setColumns(10);

		JLabel label_1 = new JLabel("Rouge :");
		label_1.setBounds(21, 70, 46, 14);
		panel_3.add(label_1);
		label_1.setFont(new Font("Times New Roman", Font.BOLD, 13));

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_4.setBounds(192, 472, 236, 91);
		frame.getContentPane().add(panel_4);

		JButton btnChoisir_fichier = new JButton("Choisir");
		btnChoisir_fichier.setBounds(56, 42, 89, 23);
		panel_4.add(btnChoisir_fichier);
		btnChoisir_fichier.setFont(new Font("Times New Roman", Font.BOLD, 13));

		id_label_file_chose = new JLabel("Cr\u00E9ation du fichier de sauvegarde");
		id_label_file_chose.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		id_label_file_chose.setBounds(22, 0, 192, 23);
		panel_4.add(id_label_file_chose);

		id_label_chemin_fichier = new JLabel("");
		id_label_chemin_fichier.setBounds(23, 66, 181, 14);
		panel_4.add(id_label_chemin_fichier);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setForeground(Color.WHITE);
		separator_9.setBackground(Color.BLACK);
		separator_9.setBounds(0, 29, 236, 2);
		panel_4.add(separator_9);

		JLabel label_15 = new JLabel("Param\u00E8tres de l'affichage");
		label_15.setFont(new Font("Times New Roman", Font.BOLD, 18));
		label_15.setBounds(803, 403, 202, 19);
		frame.getContentPane().add(label_15);

		JLabel label_16 = new JLabel("Choir le fichier de sauvegarde :");
		label_16.setFont(new Font("Times New Roman", Font.BOLD, 13));
		label_16.setBounds(747, 449, 191, 14);
		frame.getContentPane().add(label_16);

		btnLancerAnimation = new JButton("Lancer l'animation");
		btnLancerAnimation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//String speed_string = id_text_vitesse.getText();
				//if(speed_string != null) {
					//speed = Integer.parseInt(speed_string);

					new Thread(window).start();
					btnRalentir.setEnabled(true);
					btnPause.setEnabled(true);
					btnReprendre.setEnabled(true);
					btnAccelerer.setEnabled(true);

				//}
			
			}
		});
		btnLancerAnimation.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnLancerAnimation.setEnabled(false);
		btnLancerAnimation.setBounds(819, 472, 172, 23);
		frame.getContentPane().add(btnLancerAnimation);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_5.setBounds(776, 506, 252, 135);
		frame.getContentPane().add(panel_5);

		JLabel lblModesAnimation = new JLabel("Modes Animation");
		lblModesAnimation.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblModesAnimation.setBounds(64, 8, 139, 14);
		panel_5.add(lblModesAnimation);

		btnAccelerer = new JButton("Acc\u00E9l\u00E9rer");
		btnAccelerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = speed+2;
				if(a > 0)
				{
					speed = a;
					System.out.println("speed ralentire = "+speed);

				}
			}
		});
		btnAccelerer.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnAccelerer.setEnabled(false);
		btnAccelerer.setBounds(10, 33, 86, 23);
		panel_5.add(btnAccelerer);

		btnRalentir = new JButton("Ralentir");
		btnRalentir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				int a = speed-2;
				if((a > 0) && (a <= timeToSleep))
				{
					speed = a;
					System.out.println("speed accelere = "+speed);
				}
			}
		});
		btnRalentir.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnRalentir.setEnabled(false);
		btnRalentir.setBounds(10, 67, 85, 23);
		panel_5.add(btnRalentir);

		btnPause = new JButton("Pause");
		btnPause.setEnabled(false);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause = true;
			}
		});
		btnPause.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnPause.setEnabled(false);
		btnPause.setBounds(10, 101, 86, 23);
		panel_5.add(btnPause);

		btnReprendre = new JButton("Reprendre");
		btnReprendre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pause = false;
				synchronized (Visualiseur.this) {
					Visualiseur.this.notify();	
				}
			}
		});
		btnReprendre.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnReprendre.setEnabled(false);
		btnReprendre.setBounds(137, 33, 105, 23);
		panel_5.add(btnReprendre);

		JButton button_6 = new JButton("Reinitialiser ");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Initialisation des etats de la simulation
				id_text_panne_mini.setText("");
				id_text_panne_maxi.setText("");
				id_text_reparation_mini.setText("");
				id_text_reparation_maxi.setText("");

				id_text_emetteur_maxi.setText("");
				id_text_emetteur_mini.setText("");
				
				id_text_emetteur2_maxi.setText("");
				id_text_emetteur2_mini.setText("");
				
				id_text_emetteur3_maxi.setText("");
				id_text_emetteur3_mini.setText("");
				
				//id_text_vitesse.setText("");
				id_text_longueur_1.setText("");
				id_text_longueur_2.setText("");
				id_text_longueur_3.setText("");

				id_text_vert.setText("");
				id_text_jaune.setText("");
				id_text_rouge.setText("");
				
				panel_voie.setLayout(null);
				id_panel_feu_tricolor.setLayout(null);
			}
		});
		button_6.setFont(new Font("Times New Roman", Font.BOLD, 13));
		button_6.setBounds(137, 67, 105, 23);
		panel_5.add(button_6);

		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);//close the windows JFrame.
			}
		});
		btnQuitter.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnQuitter.setBounds(137, 101, 105, 23);
		panel_5.add(btnQuitter);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setForeground(Color.WHITE);
		separator_10.setBackground(Color.BLACK);
		separator_10.setBounds(0, 22, 252, 2);
		panel_5.add(separator_10);
		
		btnOpenFile = new JButton("Ouvrir");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file= getDocument();
				if(file != null)
				{	
					initSim(file);
					btnLancerAnimation.setEnabled(true);

				}else {
					//mettre un JDialog 
				}
			}
		});
		btnOpenFile.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnOpenFile.setEnabled(false);
		btnOpenFile.setBounds(948, 445, 86, 20);
		frame.getContentPane().add(btnOpenFile);
		btnChoisir_fichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chemin = getDocumentPatch();
				if( chemin != null)
				{
					id_label_chemin_fichier.setText(chemin);
					btnLancerLaSimulation.setEnabled(true);

				}else {

				}
			}
		});
		//default_car =
		// Let's load the traffic light images
		// created as image icons
		f_panne = createImageIcon("images/f_panne.jpg", "traffic light off");
		f_vert = createImageIcon("images/f_vert.jpg", "traffic light green");
		f_jaune = createImageIcon("images/f_jaune.jpg",	"traffic light orange");
		f_rouge= createImageIcon("images/f_rouge.jpg", "traffic light red");
		default_car = createImageIcon("images/image_init.jpg", "defaut");
		image_car = createImageIcon("images/image_car.jpg", "Car ");
	}


	// **********************************
	// Creates and returns an image icon,
	// or null if the path was invalid
	// **********************************
	protected static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = Visualiseur.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	// **********************************
	// Gets a document via a file chooser
	// **********************************
	protected File getDocument() {
		File file = null;
		int returnVal = fileChooser_.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser_.getSelectedFile();
		}
		return file;
	}

	//Definir le fichier de sauvergarder
	protected String getDocumentPatch() {
		String fileString = null;
		//fileChooser_.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser_.setDialogTitle("Créer un fichier de sauvegarde");
		int returnVal = fileChooser_.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser_.getSelectedFile();
			fileString = file.getPath();
		}
		return fileString;
	}
	// ********************************************
	// Initialize the data list from the file given
	// ********************************************
	protected void initSim(File file) {
		// Load the file
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			// Here BufferedInputStream is added for fast reading
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			// dis.available() returns 0 if the file does not have more lines
			while (dis.available() != 0) {
				String line = dis.readLine();
				String[] infos = line.split(" : ");
				infos_.add(infos);

			}
			// Dispose all the resources after using them
			fis.close();
			bis.close();
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Now the visualization is allowed
		ready = true;		
	}

	public void dessinevoie(int taille) {
		tableau_voie = new JLabel[3][taille];
		//default_car = createImageIcon("image_init.jpg", "defaut");
		for(int i = 0; i<=2 ; i++)
		{
			for(int j=0;j<taille; j++)
			{
				JLabel jLabel  = new JLabel();
				jLabel.setIcon(default_car);
				tableau_voie[i][j] = jLabel;
				constraints.gridx = i;
				constraints.gridy = j;
				panel_voie.add(tableau_voie[i][j],constraints);
			}
		}
	}

	public void addcar(String voie) {
		int numv=0;
		if(voie.equals("Voie1")) numv=0;
		else if(voie.equals("Voie2")) numv=1;
		else if(voie.equals("Voie3")) numv=2;
		(tableau_voie[numv][taille_table-1]).setIcon(image_car);
	}

	public void outcar(String voie) {
		int choix_voie=0;
		if(voie.equals("Voie1")) choix_voie=0;
		else if(voie.equals("Voie2")) choix_voie=1;
		else if(voie.equals("Voie3")) choix_voie=2;
		for(int j=0;j<taille_table;j++) {
			if((tableau_voie[choix_voie][j].getIcon()) != default_car) {
				tableau_voie[choix_voie][j].setIcon(default_car);
				raprocherCar(voie,j+1);
				break;
				//return i;
			}
		}
	}

	public void raprocherCar(String voie, int longueur)
	{
		int choix_voie = -1;
		if(voie.equals("Voie1")) choix_voie=0;
		else if(voie.equals("Voie2")) choix_voie=1;
		else if(voie.equals("Voie3")) choix_voie=2;
		for(int i=0;i<taille_table;i++) {
			for(int j=longueur;j>=0;j--) {
				if(!(tableau_voie[choix_voie][i].getIcon()).equals(default_car)) {
					if( (i-j)>=0 && (tableau_voie[choix_voie][i-j].getIcon() == default_car)) {
						(tableau_voie[choix_voie][i-j]).setIcon((tableau_voie[choix_voie][i]).getIcon());
						(tableau_voie[choix_voie][i]).setIcon(default_car);
					}
				}
			}
		}
	}

	/**
	 * Traitement des fichiers de la simulation
	 */

	public static void classer(File file) throws IOException{
		//the temporary file
		//file  = new File("classer.txt");
		BufferedReader br1 = new BufferedReader(new FileReader("Voie1.txt"));
		BufferedReader br2 = new BufferedReader(new FileReader("Voie2.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Voie3.txt"));
		BufferedReader br4 = new BufferedReader(new FileReader("Collecteur.txt"));

		String line1="";
		String line2="";
		String line3="";
		String line4="";
		FileWriter fileWriter = new FileWriter(file);
		//reading and writing of the file of the way 1 
		while((line1=br1.readLine())!=null && (line2=br2.readLine())!=null && (line3=br3.readLine())!=null && (line4=br4.readLine())!=null){
			//fileWriter.write(l1+"\r\n");
			fileWriter.write(line1+"\n");
			fileWriter.write(line2+"\n");
			fileWriter.write(line3+"\n");
			fileWriter.write(line4+"\n");
		}
		
		fileWriter.flush();
		fileWriter.close();
	}
	/*public void classer1(File file) throws IOException{
		//the temporary file
		//file  = new File("classer.txt");
		BufferedReader br1 = new BufferedReader(new FileReader("Voie1.txt"));
		BufferedReader br2 = new BufferedReader(new FileReader("Voie2.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Voie3.txt"));
		BufferedReader br4 = new BufferedReader(new FileReader("Collecteur.txt"));
		String line1="";
		String line2="";
		String line3="";
		String line4="";
		FileWriter fileWriter = new FileWriter(file);
		//reading and writing of the file of the way 1 
		//int[] tab;
		while((line1=br1.readLine())!=null && (line2=br2.readLine())!=null && (line3=br3.readLine())!=null && (line4=br4.readLine())!=null){

			fileWriter.write(line1+"\n");
			fileWriter.write(line2+"\n");
			fileWriter.write(line3+"\n");
			fileWriter.write(line4+"\n");
		}
		
		fileWriter.flush();
		fileWriter.close();
	}
*/
	public void fileToArray(File file) throws IOException
	{
		list = new ArrayList<>();
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line="";
		while((line = reader.readLine()) != null)
		{
			String[] s = line.split(":");
			if(s.length == 2) {
				list.add(new Tempo(s[0],s[1]));
			}

		}

	}

	public  void arrayToFile(File file) throws IOException
	{
		Tempo model;
		for(int i=0; i< list.size();i++) {
			Tempo model_i = list.get(i);
			for(int j=i+1 ;j <list.size();j++)
			{

				Tempo model_j = list.get(j);

				Double duree_i = Double.parseDouble(model_i.getTemps());
				Double duree_j = Double.parseDouble(model_j.getTemps());
				if(duree_i >= duree_j)
				{
					model = model_i;
					model_i = model_j;
					model_j = model;
					//list.get
					list.set(i,model_i);
					list.set(j,model_j);

				}
			}
		}

		FileWriter writer = new FileWriter(file);
		for(int i =0; i< list.size();i++) {
			Tempo model_1 = list.get(i);
			writer.write(model_1.getTemps()+ ":"+model_1.getAction()+"\r\n");
		}
		writer.flush();
		writer.close();

	}
	@Override
	public void run() {


		if (ready) {
			time = 0.0;
			double elapsed = 0.0;
			double time_voie = 0.0;
			double time_voie1 = 0.0;
			double time_voie2 = 0.0;
			double time_voie3 = 0.0;
			int size = infos_.size();
			System.out.println("la taille de infos_ =" + size);

			for (int i = 0; i < size-1; ++i) {
				while(pause) {
					synchronized (this) {
						try {
							this.wait();
						}catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (!ready) break;
				// read next event time
				time = Double.parseDouble(infos_.get(i)[0]);

				// get the next image(color) of the traffic light
				String string = infos_.get(i)[1];
				String[] spt = string.split(" ");

				System.out.println(string);
				/**
				 * les valeurs de elapsed
				 */
				if(spt.length > 1)
				{
					if(spt[2].equals("Voie1")) {
						elapsed = time - time_voie1;
						time_voie1 = time;		
					}else if(spt[2].equals("Voie2")) {
						elapsed = time - time_voie2;
						time_voie2 = time;		
					}else if(spt[2].equals("Voie3")) {
						elapsed = time - time_voie3;
						time_voie3 = time;		
					}
				}
				
				/*
				 * Entrer et Sortie Car
				 */
				
				if(spt.length == 3) {
					System.out.println("time =" +time);

					if(time == 0.0)
					{

						Random r=new Random();
						int indice=r.nextInt(taille_table);
						if(indice != 0)
						{
							if((spt[0].equals("add")) && (spt[2].equals("Voie1")))
							{
								if((tableau_voie[0][indice]).getIcon() == default_car)
								{
									System.out.println("oui add voie1");
									(tableau_voie[0][indice]).setIcon(image_car);
								}
							}else if((spt[0].equals("add")) && (spt[2].equals("Voie2")))
							{
								if((tableau_voie[1][indice]).getIcon() == default_car)
								{
									System.out.println("oui add voie2");
									(tableau_voie[1][indice]).setIcon(image_car);
								}
							} else if((spt[0].equals("add")) && (spt[2].equals("Voie3")))
							{
								if((tableau_voie[2][indice]).getIcon() == default_car)
								{
									System.out.println("oui add voie3");
									(tableau_voie[2][indice]).setIcon(image_car);
								}
							}
						}
					}else
					{
						if(spt[0].equals("add")) {
							raprocherCar(spt[2],(int)elapsed);
							panel_voie.repaint();
							addcar(spt[2]);
						}else if(spt[0].equals("outCar")) {
							outcar(spt[2]);
						}
					}
					//}
					panel_voie.repaint();
				}
				if(spt.length == 1) {
					if (string.compareTo("Vert")==0) {
						image_.setIcon(f_vert);
					} else if (string.compareTo("Jaune")==0) {
						image_.setIcon(f_jaune);
					} else if (string.compareTo("Rouge")==0) {
						image_.setIcon(f_rouge);
					} else if (string.compareTo("Panne")==0) {
						image_.setIcon(f_panne);
					}
					id_panel_feu_tricolor.repaint();
				}

				if (i < size - 1) {
					//time_voie = Math.max((Math.max(time_voie,time_voie2)),time_voie3);
					timeToSleep = (int) (time - time_voie)*1000;
					time_voie = Math.max((Math.max(time_voie,time_voie2)),time_voie3);

				}

				try {
					System.out.println("speed dalns run = "+speed);
					Thread.sleep(timeToSleep/speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
