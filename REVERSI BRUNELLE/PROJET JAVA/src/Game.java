import java.util.Scanner;

public class Game {	
	private TaskGame gui; // Pour l'interface pour dessiner
	
	private static Scanner input = new Scanner(System.in);
	
	public Game(TaskGame gui){
		this.gui = gui;
	}
	
	public void launch() {
		
		System.out.println("Debut de la methode launch de Game");
		
		boolean decision=true;
		
		do{
			
			int compteur_joueur=1, compteur_coup=0, x_grid=0, y_grid=0, milieu;
			boolean jeu = true, valeur=false, testCoupValide=true;
			String couleur = "";
			
			int[][] grille = null; 
			
			while (valeur==false){
				
				System.out.println("Quelle taille de plateau souhaitez vous utiliser ?");
				do{
					System.out.println("Un carré de 8 x 8 minimum s'il vous plait !");
					x_grid=input.nextInt();
					y_grid=input.nextInt();
				}while(x_grid<2||y_grid <2);
				
				grille=Board.constructionTableau(x_grid,y_grid); // Construction du tableau et initialisation des cases à 0
				
				if (x_grid==y_grid){
					
					System.out.println("Plateau en cours de création...");
					drawGrid(x_grid, y_grid); // Dessine le plateau
					
					milieu=(x_grid-1)/2; // Trouve le milieu du plateau
					
					drawBlueSquare(milieu, milieu);
					Board.valeurCase(grille,milieu,milieu,2); // On affecte une valeur à une case du tableau
					System.out.println("On dessine un carre bleu en (" +milieu+ ", " +milieu+")");
					
					drawRedSquare(milieu+1, milieu);
					Board.valeurCase(grille,milieu+1,milieu,1);
					System.out.println("On dessine un carre rouge en (" +(milieu+1)+ ", " +milieu+")");
					
					drawRedSquare(milieu, milieu+1);
					Board.valeurCase(grille,milieu,milieu+1,1);
					System.out.println("On dessine un carre rouge en (" +milieu+ ", " +(milieu+1)+")");
					
					drawBlueSquare(milieu+1, milieu+1);
					Board.valeurCase(grille,milieu+1,milieu+1,2);
					System.out.println("On dessine un carre bleu en (" +(milieu+1)+ ", " +(milieu+1)+")");
					
					
					System.out.println("Création du plateau terminé");
					valeur=true;
					
					Board.affichageTableau(grille);
				}
				else{
					valeur=false;
					System.out.println("Entrez une valeur permettant de former une grille carrée !");
				}
		
			}
			
			
			while (jeu)
			{
				int x, y;
				if (compteur_joueur%2 != 0)
				{
					couleur="ROUGE";
					System.out.println("Tour " +compteur_joueur+ " : A toi de jouer joueur " +couleur); 
											
					do{
						waitForMouseClick();
						 x=getX();
						 y=getY();
						
						if(caseRemplie(grille,x,y))
						{
							testCoupValide=true; // Le joueur passe son tour
							compteur_coup--;
						}
						else
						{
							testCoupValide=Board.coupValide(grille, x, y,2);
						}
						
					}while(testCoupValide==false);
					
					dessineBoardCoupParCoup(grille);
					//remplissage(compteur_joueur,x,y); // Méthode qui change une case vide dans la couleur du joueur
					//Board.valeurCase(grille, x, y, 1); // Ajoute la case bleue dans le tableau (ajoute "1")
					compteur_joueur++;
					compteur_coup++;
				}
				else if (compteur_joueur%2 == 0)
				{
					couleur="BLEU";
					System.out.println("Tour " +compteur_joueur+ " : A toi de jouer joueur " +couleur);
					
					do{
						waitForMouseClick();
						 x=getX();
						 y=getY();
						
						if(caseRemplie(grille,x,y))
						{
							testCoupValide=true;// Le joueur passe son tour
							compteur_coup--;
						}
						else
						{
							testCoupValide=Board.coupValide(grille, x, y,1);	
						}
						
					}while(testCoupValide==false);
						
					
					//remplissage(compteur_joueur,x,y); // Méthode qui change une case vide dans la couleur du joueur
					//Board.valeurCase(grille, x, y, 2); // Ajoute la case bleue dans le tableau (ajoute "2")
					//Board.compteurDirection(grille,x,y,);
					dessineBoardCoupParCoup(grille);
					compteur_joueur++;
					compteur_coup++;
				};
				Board.affichageTableau(grille);
				if(compteur_coup==(x_grid*y_grid)-4){
					jeu=false;
				}
				
			};
			
			afficheGagnant(grille);
			
			int relance;
			
			do{
				System.out.println("Voulez-vous refaire une partie ?");
				System.out.println("1) Oui  /  2) Non");
				relance=input.nextInt();
				if(relance==1){
					decision=true;
				}
				else if(relance==2){
					decision=false;
				}
				else{
					relance=3;
				}
			}while(relance==3);
		}while(decision);
		
	};		
	
	/* Remplis la case de la couleur correspondante au joueur
	
	private void remplissage(int joueur, int x, int y){
		if (joueur%2 != 0){
			drawRedSquare(x,y);
		}
		else if (joueur%2 == 0){
			drawBlueSquare(x,y);
		}
	}*/
	
	// Recherche si une case est remplie 
	
	private boolean caseRemplie(int[][] grille,int x, int y){
		if (grille[x][y]==1 || grille[x][y]==2){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	// Regarde l'intégralité du tableau et dessine le board en fonction de la valeur des cases
	
	private void dessineBoardCoupParCoup(int[][] grille){
		int i=0, j=0, rouge=1, bleu=2;
		while(j<(grille.length)) {
			while(i<(grille.length)) {
				if(grille[i][j]==rouge){
					drawRedSquare(i,j);
				}
				else if(grille[i][j]==bleu){
					drawBlueSquare(i,j);
				}
				else{};
				i++;
			}
			i=0;
			j++;
		}	
	}
	
	// Affiche le gagnant et le nombre de points
	
	private void afficheGagnant(int[][] grille){
		int i=0, j=0, rouge=1, compteur_rouge=0, bleu=2, compteur_bleu=0;
		while(j<(grille.length)) {
			while(i<(grille.length)) {
				if(grille[i][j]==rouge){
					compteur_rouge++;
				}
				else if(grille[i][j]==bleu){
					compteur_bleu++;
				}
				else{};
				i++;
			}
			i=0;
			j++;
		}
		if(compteur_rouge<compteur_bleu){
			System.out.println("VICTOIRE DU JOUEUR BLEU ! \\o/");
			System.out.println("Points joueur bleu : " +compteur_bleu);
			System.out.println("Points joueur rouge : " +compteur_rouge);
		}
		else if(compteur_rouge>compteur_bleu){
			System.out.println("VICTOIRE DU JOUEUR 	ROUGE ! \\o/");
			System.out.println("Points joueur rouge : " +compteur_rouge);
			System.out.println("Points joueur bleu : " +compteur_bleu);
		}
		else{
			System.out.println("~o~ EGALITE ~o~");
			System.out.println("Points joueur bleu : " +compteur_bleu);
			System.out.println("Points joueur rouge : " +compteur_rouge);
		}
	}
	
	// Attend qu'il y ait un click de souris sur la grille
	private void waitForMouseClick() {
		gui.waitForMouseClick();
	}
	
	// Abscisse du clic
	private int getX() {
		return gui.getX();
	}
	
	// Ordonnee du clic
	private int getY() {
		return gui.getY();
	}
	
	// Dessine une grille vide de taille sizeAbs x sizeOrd
	private void drawGrid(int sizeAbs, int sizeOrd) {
		gui.drawGrid(sizeAbs, sizeOrd);
	}

	// Dessine un carre rouge en (x, y)
	private void drawRedSquare(int x, int y) {
		gui.drawRedSquare(x, y);
	}

	// Dessine un carre bleu en (x, y)
	private void drawBlueSquare(int x, int y) {
		gui.drawBlueSquare(x, y);
	}
	
	private void drawGreenSquare(int x, int y) {
		gui.drawGreenSquare(x, y);
	}
	
	private void drawWhiteSquare(int x, int y) {
		gui.drawWhiteSquare(x, y);
	}
	
}
