import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main{

  public static void main (String [] args) throws IOException{
    Map<Integer, List<Integer>> listeAdjacence = new HashMap<>();
    String nomFichier = args[0];
    Map<Integer, Integer> couleurs = new HashMap<>();
    ColorationGraph colorationGraph = new ColorationGraph(listeAdjacence);

    listeAdjacence = colorationGraph.lireFichier(nomFichier);

    long lStartTime = System.nanoTime();
    listeAdjacence = colorationGraph.trierMap(listeAdjacence);
    couleurs = colorationGraph.assignerCouleur(listeAdjacence,nomFichier);

    long lEndTime = System.nanoTime();


    long output = lEndTime - lStartTime;

    System.out.println("Temps écoulé en millisecondes: " + output/1000000 + " ms");



    Boolean res = colorationGraph.checkColoring(couleurs, listeAdjacence, nomFichier);
    if(res){
      colorationGraph.ecrireGraphique(couleurs, listeAdjacence);
    }
    System.out.println("Ma liste d'adjacence : " + listeAdjacence);
    System.out.println("Mes couleurs" + couleurs);
    System.out.println("Résultat : " + res );
  }

}
