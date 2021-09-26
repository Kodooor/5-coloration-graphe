import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class ColorationGraph{

  public Map<Integer, List<Integer>> listeAdjacence;

  public ColorationGraph(Map<Integer, List<Integer>> listeAdjacence){
    this.listeAdjacence = listeAdjacence;
  }

  // Fonction qui récupère les données du fichier texte passé en paramètre  pour les insérer
  // dans la liste d'adjacence en paramètre de la classe - on travaille ensuite à partir de ça
  public Map<Integer, List<Integer>> lireFichier(String nomFichier) throws IOException{
      BufferedReader in;
      // On teste l'ouverture du fichier
      try{
			     in = new BufferedReader(new FileReader(nomFichier));
         }
      catch(IOException ie){
           System.out.println("Erreur lors de la lecture du fichier");
           throw new IOException();
      }
      // line contient la ligne du fichier
			String line;
      // nouvLine est la ligne après tous les replaces()
      String nouvLine;
      // secAttribut contient la liste d'adjacence
      String sommet = "";
      List<Integer> secAttribut = new ArrayList<>();

  		while ((line = in.readLine()) != null){
        // Si ce n'est pas le nombre de sommets dans le fichier
        if(!line.equals("10") || line.equals("12")){
          // On purge notre ligne de tous les caractères qui ne sont pas le sommet ou les voisins
          nouvLine = line.replace("[", "").replace("]", "").replace(" ","");
          sommet = nouvLine.split(":", 0)[0];

          String[] arrOfStr = nouvLine.split(":");
          String[] arrOfStrFinal;
          Integer indice = 0;
          for (String a : arrOfStr) {
              if(indice != 0){
                arrOfStrFinal = a.split(",");
                for(String s : arrOfStrFinal){
                  secAttribut.add(Integer.parseInt(s));
                  indice = 0;
                }
              }
              // On ajoute les voisins a notre list de voisins pour le sommet i
              indice++;
          }
        }
        // On insère à notre liste globale les valeurs
        if(secAttribut.size() != 0){
          this.listeAdjacence.put(Integer.parseInt(sommet), secAttribut);
        }
        // On réinitialise secAttribut pour le prochain indice
        secAttribut = new ArrayList<>();
  		}
      // On ferme le "curseur"
      in.close();
      // On retourne la liste
      return this.listeAdjacence;
  }

// Fonction qui retourne la map triée selon la longueur de la value de la map
public LinkedHashMap<Integer, List<Integer>> trierMap(Map<Integer, List<Integer>> listeAdjacence) throws IOException{
  LinkedHashMap<Integer, List<Integer>>listeRetour = new LinkedHashMap<>();
  Integer max = 0;
  Integer sommet = 0;
  Integer tailleMap = listeAdjacence.size();
  List<Integer> listeValeur = new ArrayList<>();
  Integer i = 0;

  while (i<=tailleMap){
    for(Map.Entry<Integer, List<Integer>> listePrincipale : listeAdjacence.entrySet()) {
      Integer tailleListe = listePrincipale.getValue().size();
      if(tailleListe >= max){
        max = tailleListe;
        sommet = listePrincipale.getKey();
        listeValeur = listePrincipale.getValue();
      }
    }
    listeRetour.put(sommet, listeValeur);
    listeAdjacence.remove(sommet);
    max = 0;
    i++;

  }

  return listeRetour;
}

// Fonction de coloration de graphe : atribue un integer à chaque sommet, ce qui représente sa couleur
public Map<Integer,Integer> assignerCouleur(Map<Integer, List<Integer>> listeAdjacence, String _nomFichier) throws IOException{
    Map<Integer, Integer> listeResultat = new HashMap<>();
    List<Integer> listeVoisinsPourSommet = new ArrayList<>();
    List<Integer> listeVoisinsPremierIteration = new ArrayList<>();
    List<Integer> listeCouleursUtilisees = new ArrayList<>();
    List<Integer> listeSommetsTraites = new ArrayList<>();

    Integer sommetCourant;
    Integer sommetPremierIteration = 0;
    Integer indice = 0;
    Integer couleurSommet = 1;

    Boolean sommetTraites = false;
    Boolean notConnected = true;

    listeAdjacence = lireFichier(_nomFichier);
    listeAdjacence = trierMap(listeAdjacence);
    while(listeResultat.size() != listeAdjacence.size()){
      for(Map.Entry<Integer, List<Integer>> listePrincipale : listeAdjacence.entrySet()) {

          sommetCourant = listePrincipale.getKey();
          listeVoisinsPourSommet = listePrincipale.getValue();

          for(int a=0; a<listeSommetsTraites.size(); ++a){
            if(listeSommetsTraites.get(a) == sommetCourant){
              sommetTraites = true;
            }
          }

          if(sommetTraites == false){
            if(indice == 0){
              sommetPremierIteration = listePrincipale.getKey();
              listeVoisinsPremierIteration = listePrincipale.getValue();
              listeResultat.put(sommetPremierIteration, couleurSommet);
              listeCouleursUtilisees.add(couleurSommet);
              listeSommetsTraites.add(sommetPremierIteration);

              indice = 99999;
            }

            for(int i=0; i<listeVoisinsPremierIteration.size(); ++i){
              if(listeVoisinsPremierIteration.get(i) != sommetCourant){
                notConnected = true;
              }
              if(listeVoisinsPremierIteration.get(i) == sommetCourant){
                notConnected = false;
                break;
              }
            }
            if(notConnected == true){
              listeResultat.put(sommetCourant, couleurSommet);
              listeCouleursUtilisees.add(couleurSommet);
              listeSommetsTraites.add(sommetCourant);
              for(int j=0; j<20; ++j){
                try{
                  listeVoisinsPremierIteration.add(listeVoisinsPourSommet.get(j));
                }
                catch (Exception e){}
              }
            }
            notConnected = false;
          }
          sommetTraites = false;
      }
      indice = 0;
      couleurSommet++;

    }

    ecrireFichierTexte(listeResultat);
    System.out.println(listeResultat);
    return listeResultat;
}

// Fonction qui vérifie si la coloration est correcte ou non
public boolean checkColoring(Map<Integer, Integer> listeCouleurs, Map<Integer, List<Integer>> listeAdjacence, String _nomFichier) throws IOException{

  listeCouleurs = assignerCouleur(listeAdjacence,_nomFichier);

  HashMap<Integer, List<Integer>> mapRes = new HashMap<>();

  List<Integer> voisinsCourants;
  Integer couleurSommetCourant = 0;

  for(Map.Entry<Integer, List<Integer>> liste : listeAdjacence.entrySet()){

    voisinsCourants = new ArrayList<>(liste.getValue());
    couleurSommetCourant = listeCouleurs.get(liste.getKey()); // 6//1

    for(int a=0; a<voisinsCourants.size(); ++a){
      voisinsCourants.set(a,listeCouleurs.get(voisinsCourants.get(a)));
      if(voisinsCourants.get(a) > 5 || couleurSommetCourant > 5 || voisinsCourants.get(a) == couleurSommetCourant ){
        return false;
      }
    }
    mapRes.put(liste.getKey(), voisinsCourants);
  }
  return true;
}


// Fonction qui écrit dans un fichier texte le résultat de l'execution
public void ecrireFichierTexte(Map<Integer, Integer> listeResultat) throws IOException{
    ArrayList<String> listeCouleurs = new ArrayList<>();
    listeCouleurs.add("purple");
    listeCouleurs.add("green");
    listeCouleurs.add("blue");
    listeCouleurs.add("red");
    listeCouleurs.add("black");
    File file = new File("resultat.colors");
    file.createNewFile();

    PrintWriter writer = new PrintWriter(file);

    for(Map.Entry<Integer, Integer> liste : listeResultat.entrySet()){
      if(liste.getValue()-1 <= 4){
        writer.println(liste.getKey() + " : " + listeCouleurs.get(liste.getValue()-1));
      }
    }
    writer.flush();
    writer.close();
}

// Fonction qui permet de générer le rendu graphique du graphe, après assignation de la coloration
public void ecrireGraphique(Map<Integer, Integer> listeResultat, Map<Integer, List<Integer>> listeAdjacence) throws IOException{
    File file = new File("graphe.dot");
    file.createNewFile();
    ArrayList<String> listeCouleurs = new ArrayList<>();
    listeCouleurs.add("purple");
    listeCouleurs.add("green");
    listeCouleurs.add("blue");
    listeCouleurs.add("red");
    listeCouleurs.add("black");
    PrintWriter writer = new PrintWriter(file);

    writer.println("digraph D{");
    for(Map.Entry<Integer, Integer> liste : listeResultat.entrySet()){
      writer.println(liste.getKey() + " [fontcolor = "+ listeCouleurs.get(liste.getValue()-1) + " color= " + listeCouleurs.get(liste.getValue()-1) + "]" );
    }
    for(Map.Entry<Integer, List<Integer>> listeDeux : listeAdjacence.entrySet()){

      for(int a=0; a<listeDeux.getValue().size(); ++a){
        writer.println(listeDeux.getKey() + " -> " + listeDeux.getValue().get(a));
      }

    }
    writer.println("}");
    writer.flush();
    writer.close();
}
}
