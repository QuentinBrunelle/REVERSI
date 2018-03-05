
public class Board {

	public static int[][] constructionTableau(int x, int y){
		int[][] grille = new int[x][y];
		
		for(int i = 0; i<x ; i++){
			for(int j = 0; j<y ; j++){
				grille[i][j]=0;
			}
		}
		return grille;
	}
	
	// Attribue une valeur (0 = vide ; 1 = rouge ; 2 = bleu) à une case dans un tableau
	
	public static void valeurCase(int[][] grille,int x, int y, int valeur){
		grille[x][y]=valeur;
	}
	
	// Vérifie si une des cases adjacentes à la case choisie par le joueur est dans le board
	
	public static boolean retourneValeurCase(int[][] grille, int x, int y, int valeur){
		if (x<0 || x>grille.length-1 || y<0 || y>grille.length-1){
			return false;
		}
		else if (grille[x][y]==valeur){
			return true;
		}
		return false;
	}
	
	public static boolean coupValide(int[][] grille, int x, int y, int valeur){
		
		int val=0;
		boolean test=false;
		
		if(valeur==1){
			val=2;
		}
		else if (valeur==2){
			val=1;
		}
		
		if (retourneValeurCase(grille,x-1,y-1,valeur)){ // Renvoi vrai si la case de la diagonale gauche est de la couleur opposée
			if (testDirection(grille,x-1,y-1,-1,-1,val)){
				valeurCase(grille, x, y, val);
				test= true;
			}
		}
		if (retourneValeurCase(grille,x,y-1,valeur)){
			if (testDirection(grille,x,y-1,0,-1,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x+1,y-1,valeur)){
			if (testDirection(grille,x+1,y-1,1,-1,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x+1,y,valeur)){
			if (testDirection(grille,x+1,y,1,0,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x+1,y+1,valeur)){
			if (testDirection(grille,x+1,y+1,1,1,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x,y+1,valeur)){
			if (testDirection(grille,x,y+1,0,1,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x-1,y+1,valeur)){
			if (testDirection(grille,x-1,y+1,-1,1,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		if (retourneValeurCase(grille,x-1,y,valeur)){
			if (testDirection(grille,x-1,y,-1,0,val)){
				valeurCase(grille, x, y, val);
				test=true;
			}
		}
		
		if (test==false){
			System.out.println("Coup impossible, veuillez réessayer !");
		}
		
		return test;
			
	}
	
	public static boolean testDirection(int[][] grille, int x, int y, int directionX, int directionY, int val){
		int compteur=0;
		while(x>=0 && x<=(grille.length-1) && y>=0 && y<=(grille.length-1) && grille[x][y]!=0){
			compteur++;
			if(grille[x][y]==val){
				for(int i=compteur;i>=0;i--){
					valeurCase(grille,x,y,val);
					x-=directionX;
					y-=directionY;
				}
				return true;
			}
			x+=directionX;
			y+=directionY;
		}
		
		return false;
	}
	
	
	/*public static void affichageTableau (int[][] grille){ // ANCIENNE METHODE D'AFFICHAGE DU TABLEAU
		
	
		for (int i = 0; i < (grille.length); i++)
		{
		    for (int j = 0; j < (grille.length); j++){
		    	//System.out.println("A l'emplacement (" +i+", " +j+ ") du tableau nous avons = " + grille[i][j]);
		    	System.out.println(grille[i][j]);
		    }
			
		}
	}
	*/
	public static void affichageTableau (int[][] grille) {
		
		int i=0,j=0;
		while(j<(grille.length)) {
			while(i<(grille.length)) {
				System.out.print(grille[i][j] +" ");
				i++;
			}
			i=0;
			System.out.println("");
			j++;
		}
	}
}
