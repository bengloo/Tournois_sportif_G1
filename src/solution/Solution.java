package solution;

import instance.Instance;
import instance.modele.contrainte.*;
import operateur.OperateurInsertion;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** classe définissant Solution
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.2
 */
public class Solution {
    private static Instance instance;
    private Map<Integer, Journee> journees;
    private Map<String, Rencontre> rencontres;
    private Map<Integer, Equipe> equipes;
    private Integer coutTotal;
    private Map<Contrainte,Object> coefContraintes;


    //marge par rencontre(couple equipe) pour les journee viable
    private List<Integer>[][] margeJournees;

    //marge par journee pour les rencontre viable
    private List<Integer[]>[] margeRencontres;

    //marge par equipe pour les equipe viable
    private List<Integer>[] margeEquipe;

    //log du solveur ayant amené à cette solution
    private String log = "";

    public Solution(Instance instance) {
        this.instance = instance;

        //on pourra faire des operation par equipes
        this.equipes =new HashMap<>();
        for(int id=0;id<getNBEquipe();id++){
            addEquipe(id);
        }

        //on pourra faire des operation par journee
        this.journees =new HashMap<>();
        for(int id=0;id<getNbJournee();id++){
            addJournee(id);
        }

        //on pourra faire des operation par rencontres
        this.rencontres =new HashMap<>();
        for(int id=0;id<getNBEquipe();id++){
            for(int idAdverse=0;idAdverse<getNBEquipe();idAdverse++){
                if(id!=idAdverse){
                    addRencontre(this.equipes.get(id),this.equipes.get(idAdverse));
                }
            }
        }

        this.coutTotal=0;

        //le calcule du cout total par contrainte n'est par continus vis à vias des coef de la contrainte comme valc
        this.coefContraintes = new HashMap<>();
        for(TypeContrainte type:TypeContrainte.values()) {
            for (Contrainte contrainte : instance.getContraintes(type)){
                if(contrainte instanceof ContrainteEquite){
                    this.coefContraintes.put(contrainte,new HashMap<>());//Integer par default
                    for(int e:((ContrainteEquite) contrainte).getEquipes()){
                        ((HashMap<Integer,Integer>)this.coefContraintes.get(contrainte)).put(e,0);
                    }
                }else{
                    this.coefContraintes.put(contrainte,0);
                }
            }
        }
        margeJournees= new ArrayList[this.getNBEquipe()][this.getNBEquipe()];
        for (int i = 0; i < margeJournees.length; i++) {
            for (int j = 0; j < margeJournees[i].length; j++) {
                margeJournees[i][j] = new ArrayList<>();
                for(int k =0;k<this.getNbJournee();k++){
                    if(i!=j) {
                        margeJournees[i][j].add(k);
                    }
                }
            }
        }
        margeRencontres= new ArrayList[getNbJournee()];
        for(int i=0;i<margeRencontres.length;i++){
            margeRencontres[i]=new ArrayList<>();
            for(Rencontre r:this.rencontres.values()){
                margeRencontres[i].add(new Integer[]{r.getDomicile().getId(),r.getExterieur().getId()});
            }
        }

        margeEquipe=new ArrayList[getNbJournee()];
        for(int i=0;i<margeEquipe.length;i++){
            margeEquipe[i]=new ArrayList<>();
            for(int j=0;j<getNBEquipe();j++){
                margeEquipe[i].add(j);
            }
        }
    }


    public boolean isMeilleure(Solution sol){
        if(sol==null)return true;
        return sol.getCoutTotal()>this.getCoutTotal();
    }

    /**
     * Méthode permettant de récupérer l'instance de la solution
     * @return l'instance en question
     */
    public static Instance getInstance() {
        return instance;
    }

    /**
     * Méthode permettant de connaître la phase de la solution à partir d'une journée donnée
     * @param journee du championnat
     * @return 1 si on se situe dans la 1ère phase du championnat, 2 sinon
     */
    public Integer getPhase(Journee journee){
        if(journee==null)return null;
        return journee.getId()+1>getNbJournee()/2?2:1;
    }

    public Integer getPhase(Integer idJournee){
        if(idJournee==null)return null;
        return idJournee+1>getNbJournee()/2?2:1;
    }

    /**
     * Méthode permettant de récupérer le nombre de journées qui ont lieu lors du championnat
     * @return l'entier symbolisant le nombre de journées
     */
    public int getNbJournee(){
        return getNBEquipe()*2-2;
    }

    /**
     * Méthode permettant de récupérer le nombre de rencontres qui ont lieu lors du championnat
     * @return l'entier symbolisant le nombre de rencontres
     */
    public int getNBRencontreJournee(){
        return getNBEquipe()/2;
    }

    /**
     * Méthode permettant de récupérer le nombre d'équipes jouant lors du championnat
     * @return l'entier symbolisant le nombre d'équipes
     */
    public int getNBEquipe(){
        return getInstance().getNbEquipes();
    }

    /**
     * Méthode permettant de récupérer l'ensemble des journées de compétition
     * @return le tableau de l'ensemble des journées
     */
    public Map<Integer, Journee> getJournees() {
        return this.journees;
    }

    /**
     * Méthode permettant de récupérer l'ensemble des rencontres de la compétition
     * @return le tableau de l'ensemble des rencontres
     */
    public Map<String, Rencontre> getRencontres() {
        return this.rencontres;
    }

    /**
     * Méthode permettant de récupérer l'ensemble des équipes de la compétition
     * @return le tableau de l'ensemble des équipes
     */
    public Map<Integer, Equipe> getEquipes() {
        return this.equipes;
    }

    /**
     * Méthode permettant de récupérer le coût total de la solution
     * @return l'entier symbolisant le coût
     */
    public Integer getCoutTotal() {
        int ctt=0;
        for(Contrainte c:coefContraintes.keySet()){
            ctt+=c.getCoutTotal(this);
        }
        return ctt;
    }

    /**
     * Méthode permettant de récupérer le coeff des contraintes de la solution
     * @return le tableau de l'ensemble des coeffs de contraintes
     */
    public Map<Contrainte, Object> getCoefContraintes() {
        return this.coefContraintes;
    }

    /**
     * Récupère la liste des contraintes d'un type donné de la solution
     * @param type des contraintes à récupérer
     * @return la liste chaînée des contraintes
     */
    public LinkedList<? extends Contrainte> getContraintes(TypeContrainte type){
        return instance.getContraintes(type);
    }

    /**
     * Récupère la liste de toutes les contraintes de la solution
     * @return la liste chaînée des contraintes
     */
    public LinkedList<Contrainte> getContraintes(){
        return instance.getContraintes();
    }

    /**
     * Indique à la fois l'ID de l'équipe jouant à domicile suivi de celui de l'équipe jouant en extérieur
     * @param IDequipeDomicile l'ID de l'équipe jouant à domicile
     * @param IDequipeExterne l'ID de l'équipe jouant en extérieur
     * @return la chaîne de caractères correspondante
     */
    public String getIDRencontre(Integer IDequipeDomicile,Integer IDequipeExterne){
        return IDequipeDomicile+"-"+IDequipeExterne;
    }

    public String getIDRencontre(Equipe eDomicile,Equipe eExterne){
        return eDomicile.getId()+"-"+eExterne.getId();
    }
    public Equipe getEquipeByID(int id){
        return this.equipes.get(id);
    }
    public Journee getJourneeByID(int id){
        return this.journees.get(id);
    }
    public Rencontre getRencontreByID(String id){
        return this.rencontres.get(id);
    }
    public Rencontre getRencontreByEquipes(Equipe eDomicile,Equipe eExterne){
        return this.rencontres.get(getIDRencontre(eDomicile,eExterne));
    }
    public Rencontre getRencontreByEquipes(int eDomicile,int eExterne){
        return this.rencontres.get(getIDRencontre(eDomicile,eExterne));
    }
    public Journee getJourneeRencontreByEquipes(int eDomicile,int eExterne){
        return this.rencontres.get(getIDRencontre(eDomicile,eExterne)).getJournee();
    }
    public boolean isRJPresent(int jId, Rencontre r){
        if(!this.journees.containsKey(jId))return false;
        return this.journees.get(jId).isPresent(r);
    }

    /*public boolean isRJPresent(int jId,String rID){
        return  this.journees.get(jId).isPresent(rID);
    }*/




    /**
     * Indique toutes les rencontres jouées par l'équipe passée en paramètre
     * @param IDequipe l'ID de l'équipe en question
     * @return la liste chaînée des rencontres
     */
    public LinkedList<Rencontre> getRencontresByEquipe(Integer IDequipe){
        LinkedList<Rencontre> rencontresEquipe = new LinkedList<Rencontre>();

        // Parcours de toutes les rencontres du championnat
        for (Rencontre r : this.rencontres.values()) {
            if (r.getDomicile().equals(IDequipe) || r.getExterieur().equals(IDequipe)) {
                rencontresEquipe.add(r);
            }
        }
        return rencontresEquipe;
    }

    /**
     * Ajoute une équipe à la solution finale (championnat)
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addEquipe(int id){
        if(id < 0 ||this.equipes.containsKey(id))return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }

    /**
     * Ajoute une rencontre entre deux équipes à la solution finale (championnat)
     * @param equipe l'ID de l'équipe à ajouter jouant à domicile lors de la rencontre
     * @param equipeAdverse l'ID de l'équipe adverse à ajouter jouant en extérieur lors de la rencontre
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addRencontre(Equipe equipe,Equipe equipeAdverse){
        if(equipe==null||equipeAdverse==null)return false;
        String id= getIDRencontre(equipe.getId(),equipeAdverse.getId());
        if(this.rencontres.containsKey(id))return false;
        this.rencontres.put(id, new Rencontre(equipeAdverse,equipe));
        return true;
    }

    /**
     * Ajoute une journée de compétition à la solution finale (championnat)
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addJournee(int id){
        if(id < 0 ||this.journees.containsKey(id))return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    /**
     * Met à jour le coût total de la solution
     * @param cout à ajouter à la solution
     */
    public void addCoutTotal(Integer cout) {
        this.coutTotal += cout;
    }

    /**
     * Méthode permettant de récupérer la rencontre retour en connaissance de la rencontre du match aller
     * @param r la rencontre ciblée
     * @return la rencontre du match retour
     */
    public Rencontre getMatchRetour(Rencontre r){
        return this.rencontres.get(r.getLabelRetour());
    }

    /**
     * Méthode permettant d'ajouter le coeff du coût de la contrainte au coût total de la solution
     * @param c la contrainte en question
     * @param deltaCoef le delta du coeff
     * @param deltaCout le coût associé
     */
    public void addCoefCoutContrainte(Contrainte c,Object deltaCoef,Integer deltaCout){
        //update du coef contrainte
        //System.out.println("Deltacoef pour cette insert"+deltaCoef);
        if(c instanceof ContrainteEquite){
            for(Integer e:((HashMap<Integer,Integer>)deltaCoef).keySet()){
                ((HashMap<Integer,Integer>)coefContraintes.get(c)).put(e,((HashMap<Integer,Integer>)coefContraintes.get(c)).get(e)+((HashMap<Integer,Integer>)deltaCoef).get(e));
            }
        }else {
            coefContraintes.put(c, (Integer)coefContraintes.get(c) + (Integer)deltaCoef);
        }
        //update du cout totale
        coutTotal+=deltaCout;
    }

    public Integer getCoefEquite(Contrainte c,Integer equipe){
        return ( ((HashMap<Integer,Integer>)coefContraintes.get(c)).get(equipe));
    }

    public void addCoefEquite(Contrainte c,Integer equipe,Integer deltatCoef){
        ((HashMap<Integer,Integer>)coefContraintes.get(c)).put(equipe,((HashMap<Integer,Integer>)coefContraintes.get(c)).get(equipe)+deltatCoef);
    }

    /**
     * Méthode permettant de vérifier la faisabilité de l'entièreté de la solution (avec les contraintes + planning)
     * @return true si la solution est réalisable, false sinon
     */
    public boolean check(){
        return check(true);
    }

    public boolean check(boolean verbose){
        if(!checkIntegriteeChampionat(verbose))return false;
        return checkAllContrainte(verbose);
    }

    /**
     * Méthode permettant de vérifier la faisabilité de la solution vis-à-vis du planning du nombre de rencontres sur chaque journée et des phases
     * @return true si ces paramètres sont réalisables, false sinon
     */
    public boolean checkIntegriteeChampionat(boolean verbose){
        int compt;

        for(Journee j : this.journees.values()){
            // Vérification du nombre de rencontres sur une journée
            if(j.getRencontres().size()>getNBRencontreJournee()){
                if(verbose)System.err.println("La journée "+j.getId()+" a un nombre de rencontres égal à: "+j.getRencontres().toString());
                return false;
            }
            // Vérification que le nombre de rencontres d'une équipe sur une journée soit égal à 1
            for (Equipe e : this.equipes.values()) {
                compt = 0;
                for (Rencontre r : this.rencontres.values()) {
                    if ( j.isPresent(r) && (r.getDomicile().equals(e) || r.getExterieur().equals(e))) {
                        compt++;
                    }
                }
                if (compt != 1) {
                    if(verbose)System.err.println("L'équipe "+e+" a un nombre de rencontres égal à: "+compt+" sur la journée "+j);
                    return false;
                }
            }
        }
        // Vérification que les matchs aller et retour ne sont pas dans la même phase
        for(Rencontre r : this.rencontres.values()){
            if(getPhase(r.getJournee())==getPhase(this.rencontres.get(r.getLabelRetour()).getJournee())){
                //int a=getPhase(r.getJournee());
                //int b= getPhase(this.rencontres.get(r.getLabelRetour()).getJournee());
                //Rencontre ra=this.rencontres.get(r.getLabelRetour());
                if(verbose)System.err.println("La rencontre "+r.toString()+" a son match retour dans la même phase");
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode permettant de vérifier la faisabilité de la solution vis-à-vis des contraintes
     * @return true si les contraintes sont réalisables, false sinon
     */
    public boolean checkAllContrainte(boolean verbose){
        boolean res=true;
        for(Contrainte c:getContraintes()){
            if(!c.checkContrainte(this)){
                if(verbose)System.err.println("Contrainte non respectée: "+c.toString());
                if(c instanceof ContraintePauseEquipe || c instanceof ContraintePauseGlobale){
                    if(verbose)System.err.println("Coef"+((Integer)coefContraintes.get(c)));
                }else{
                    //System.err.println("Coef"+coefContraintes.get(c).toString());
                }

                res= false;
            }
        }

        return res;
    }

    /**
     * Méthode permettant de récupérer la meilleure insertion d'une rencontre dans le championnat (coût le plus faible)
     * @param r la rencontre à insérer
     * @return le meilleur opérateur d'insertion
     */
    public OperateurInsertion getMeilleureInsertionRencontre(Rencontre r) {
        OperateurInsertion bestInsertion = new OperateurInsertion();
        List<OperateurInsertion> list = new ArrayList<OperateurInsertion>(this.getInsertRencontreViable(r));
        long seed = System.currentTimeMillis();
        Collections.shuffle(list,new Random(seed));
        for(OperateurInsertion o:list){
            if(o != null ) {
                if(o.isMouvementRealisable()){
                    if(o.isMeilleur(bestInsertion)){
                        bestInsertion = o;
                    }
                }else{
                    //this.margeJournees[r.getDomicile().getId()][r.getExterieur().getId()].remove(o.getJournee().getId());
                    //this.margeRencontres[o.getJournee().getId()].removeIf(n->(n[0]==r.getDomicile().getId()&&n[1]==r.getExterieur().getId()));
                }

            }
        }
        return bestInsertion;
    }

    public OperateurInsertion getMeilleureInsertion(ArrayList<Rencontre> rTarget) {
        OperateurInsertion bestInsertion = new OperateurInsertion();
        List<Rencontre> list = new ArrayList<Rencontre>(rTarget);
        long seed = System.currentTimeMillis();
        Collections.shuffle(list,new Random(seed));
        for(Rencontre r:list){
            OperateurInsertion o= getMeilleureInsertionRencontre(r);
            if(o != null && o.isMeilleur(bestInsertion)) {
                if(o.isMouvementRealisable() ){
                    bestInsertion = o;
                }
            }
        }
        return bestInsertion;
    }

    public OperateurInsertion getMeilleureInsertionV2(ArrayList<OperateurInsertion> oTarget) {
        OperateurInsertion bestInsertion = new OperateurInsertion();
        long seed = System.currentTimeMillis();
        Collections.shuffle(oTarget,new Random(seed));
        for(OperateurInsertion o:oTarget){
            if(o != null ) {
                if(o.isMouvementRealisable()){
                    if(o.isMeilleur(bestInsertion)){
                        bestInsertion = o;
                    }
                }else{
                    this.margeJournees[o.getRencontre().getDomicile().getId()][o.getRencontre().getExterieur().getId()].remove(o.getJournee().getId());
                    this.margeRencontres[o.getJournee().getId()].removeIf(n->(n[0]==o.getRencontre().getDomicile().getId()&&n[1]==o.getRencontre().getExterieur().getId()));
                }

            }
        }
        return bestInsertion;
    }



    /**
     * Méthode permettant de récupérer la première tentative d'insertion d'une rencontre dans le championnat
     * @param r la rencontre ciblée
     * @return le premier opérateur d'insertion valide, null sinon
     */
    public OperateurInsertion getPremiereInsertion(Rencontre r) {
        for(Journee j: this.journees.values()) {
            OperateurInsertion o = new OperateurInsertion(this,j,r);
            if(o != null && o.isMouvementRealisable()) {
                return o;
            }
        }
        System.err.println("Il n'y a plus d'insertion valide");
        return null;
    }

    public void writeSolution(String repSolveur){
        File folder = new File("resultats/"+repSolveur);
        //on crer un dossier propre au solveur si besoin
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String aux;
            if (os.contains("win")) {
                //System.out.println("Système d'exploitation : Windows");
                aux=("resultats\\\\" + repSolveur + "\\\\" + (instance.getChemin().split("\\\\")[instance.getChemin().split("\\\\").length-1]).replace(".txt","_sol.txt"));
            } else if (os.contains("mac")) {
                //System.out.println("Système d'exploitation : macOS");
                aux= ("resultats/" + repSolveur + "/" + (instance.getChemin().split("/")[instance.getChemin().split("/").length-1]).replace(".txt","_sol.txt"));

            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                //System.out.println("Système d'exploitation : Unix/Linux");
                aux=("resultats/" + repSolveur + "/" + (instance.getChemin().split("/")[instance.getChemin().split("/").length-1]).replace(".txt","_sol.txt"));
            } else {
                System.out.println("Système d'exploitation inconnu : " + os);
                aux=("resultats/" + repSolveur + "/" + (instance.getChemin().split("/")[instance.getChemin().split("/").length-1]).replace(".txt","_sol.txt"));
            }
            File file = new File(aux);
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);


            bw.write("// Solution");
            bw.newLine();
            bw.write("// Instance "+instance.getNom());
            bw.newLine();
            for (Journee j : this.journees.values()) {
                bw.newLine();
                bw.write("// Journee "+j.getId().toString());
                bw.newLine();
                for (Rencontre r : j.getRencontres().values()) {
                    bw.write(r.getDomicile().getId().toString()+"   "+r.getExterieur().getId().toString());
                    bw.newLine();
                }
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeSolutionChekerProf(String repSolveur){

        //on vide le dossier
        File dossier = new File("checkerProf");

        // Vérifier si le chemin spécifié correspond à un dossier existant
        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            // Parcourir tous les fichiers du dossier
            for (File fichier : fichiers) {
                // Vérifier si le fichier est un fichier ".txt"
                if (fichier.isFile() && fichier.getName().endsWith(".txt")) {
                    // Supprimer le fichier
                    fichier.delete();
                }
            }
        } else {
            System.out.println("Le chemin spécifié ne correspond pas à un dossier existant.");
        }
        //on copie l'intsance
        try {
            File fichierSource = new File(instance.getChemin());
            File dossierDestination = new File("checkerProf/");

            // Vérifier si le fichier source existe
            if (fichierSource.exists()) {
                // Vérifier si le dossier de destination existe
                if (!dossierDestination.exists()) {
                    // Créer le dossier de destination s'il n'existe pas
                    dossierDestination.mkdirs();
                }

                // Lire le contenu du fichier source
                FileInputStream fileInputStream = new FileInputStream(fichierSource);
                byte[] buffer = new byte[(int) fichierSource.length()];
                fileInputStream.read(buffer);
                fileInputStream.close();

                // Créer le fichier de destination
                File fichierDestination = new File(dossierDestination, "fichierInstance.txt");
                FileOutputStream fileOutputStream = new FileOutputStream(fichierDestination);
                fileOutputStream.write(buffer);
                fileOutputStream.close();

                //System.out.println("Le fichier a été copié avec succès vers : " + fichierDestination.getAbsolutePath());
            } else {
                System.out.println("Le fichier source n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //on write la solution
        try {
            File file = new File("checkerProf/fichierInstance_sol.txt");
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);


            bw.write("// Solution");
            bw.newLine();
            bw.write("// Instance "+instance.getNom());
            bw.newLine();
            for (Journee j : this.journees.values()) {
                bw.newLine();
                bw.write("// Journee "+j.getId().toString());
                bw.newLine();
                for (Rencontre r : j.getRencontres().values()) {
                    bw.write(r.getDomicile().getId().toString()+"   "+r.getExterieur().getId().toString());
                    bw.newLine();
                }
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<OperateurInsertion> updateMages(OperateurInsertion o){

        if(o!=null) {
            //Update marge des contraintes inerantes si une operation à été apliqué-----------------------------------------------------------------
            //equip domicile
            int d = o.getRencontre().getDomicile().getId();
            //equipe exterieur
            int e = o.getRencontre().getExterieur().getId();
            //journee insert
            int j = o.getJournee().getId();
            //les rencontre impliquant les même equipe ne peuvent plus étre inséré sur la même journee
            for (int i = 0; i < getNBEquipe(); i++) {
                margeJournees[d][i].removeIf(n -> (n == j));
                margeJournees[e][i].removeIf(n -> n == j);
                margeJournees[i][e].removeIf(n -> n == j);
                margeJournees[i][d].removeIf(n -> n == j);
            }
            //le match retour ne peux plus étre afecté sur la même phase
            for (int j2 = 0; j2 < getNbJournee(); j2++) {
                if (getPhase(j2) == getPhase(j)) {
                    int jaux=j2;
                    margeJournees[e][d].removeIf(n -> n == jaux);
                }
            }
            //aprés insert la rencontre inseré n'a par convention plus aucune journee de marge
            margeJournees[d][e].clear();

            //la journee ne peux pas recevoir d'autre rencontre impliquant les même equipes
            margeRencontres[j].removeIf(n -> ((n[0] == d) || (n[0] == e) || (n[1] == d) || (n[1] == e)));

            for (int j2 = 0; j2 < getNbJournee(); j2++) {
                if (getPhase(j2) == getPhase(j)) {
                    //les journee de la même phase ne peuvent pas recevoir le match retour
                    margeRencontres[j2].removeIf(n -> (n[0] == e && n[1] == d));
                }
                //les autres jounee ne peuvent plus accepter la rencontre inseré
                margeRencontres[j2].removeIf(n -> (n[0] == d && n[1] == e));
            }
            //aprés insert la journe d'insertion n'a par convention plus aucune rencontre de marge si elle est pleine
            if (o.getJournee().getRencontres().size() == getNBRencontreJournee()) {
                margeRencontres[j].clear();
            }
            margeEquipe[j].removeIf(n -> (n == d || n == e));
        }

        //update marge contrainte --------------------------------------------------------------------------------
        List<OperateurInsertion> listInserViable =new ArrayList<>();
        //pour tous les insert possible restant on verifie qu'il ne declanche pas de contrainte dure
        for(int di=0;di<getNBEquipe();di++){
            for(int ei=0;ei<getNBEquipe();ei++){
                if(di!=ei) {
                    for(int ji:margeJournees[di][ei]){
                        OperateurInsertion oi = new OperateurInsertion(this, this.getJourneeByID(ji), this.getRencontreByEquipes(di, ei));
                        if (!oi.isMouvementRealisable()) {
                            //margeJournees[di][ei].removeIf(n -> (n == ji));
                            System.out.println(nbMargineString());
                            int daux = di;
                            int eaux = ei;
                            margeRencontres[ji].removeIf(n -> (n[0] == daux && n[1] == eaux));
                        } else {
                            listInserViable.add(oi);
                        }
                    }
                }
            }
        }

    return listInserViable;
    }

    public  int getNbMargeRJ(Journee j){
        return  margeRencontres[j.getId()].size();
    }
    public int getNbMargeJR(Rencontre r){
        return margeJournees[r.getDomicile().getId()][r.getExterieur().getId()].size();
    }
    public int getNbMargeJE(Journee j){
        return margeEquipe[j.getId()].size()/2;
    }


    public int getNbMargeGlobal(OperateurInsertion o){
        int nbMJR=getNbMargeJR(o.getRencontre());
        int nbMRJ=getNbMargeRJ(o.getJournee());
        int nbMJE=getNbMargeJE(o.getJournee());

        //priorité absolus les operation critique
        if(nbMRJ==1||nbMJR==1||nbMJE==1)return 1;
        if(nbMRJ==2||nbMJR==2)return 2;
        return nbMJR;
    }
    /**
     * Methode retournant les rencontre qui n'ont pas encore été inséré et qui on le moins de journee
     * de marge pour étré inséré
     */
    public ArrayList<Rencontre> getRencontresMinMarge(){
        ArrayList<Rencontre> res= new ArrayList<>();
        int sizeMin=Integer.MAX_VALUE;
        for(Rencontre r:rencontres.values()){
            //si il existe un ensemble d'insert ayant moins de marge
            if(getNbMargeJR(r)!=0&&getNbMargeJR(r)<sizeMin){
                res.clear();
                sizeMin=getNbMargeJR(r);
            }
            //completer l'ensemble
            if(getNbMargeJR(r)==sizeMin){
                res.add(r);
            }
        }
        return res;
    }

    public ArrayList<OperateurInsertion> getInsertionMinMarge(List<OperateurInsertion> listinsert){
        ArrayList<OperateurInsertion> res=new ArrayList<>();
        int margeMin=Integer.MAX_VALUE;
        for(OperateurInsertion o:listinsert) {
            if (getNbMargeGlobal(o) != 0 && getNbMargeGlobal(o) < margeMin) {
                res.clear();
                margeMin = getNbMargeGlobal(o);
            }
            if (getNbMargeGlobal(o) == margeMin) {
                res.add(o);
            }
        }
        return res;
    }



    public ArrayList<OperateurInsertion>getInsertRencontreViable(Rencontre r){
        ArrayList<OperateurInsertion> res= new ArrayList<>();
        for(int j:margeJournees[r.getDomicile().getId()][r.getExterieur().getId()]){
            res.add(new OperateurInsertion(this,this.getJourneeByID(j),r));
        }
        return res;
    }

    public List<String> getRencontreSansJournee(){
        List<String> res=new ArrayList<>();
        for(Rencontre r:this.getRencontres().values()){
            if(r.getJournee()==null){
                res.add(r.getLabel());
            }
        }
        return res;
    }

    public String getLog() {
        return log;
    }

    public String getLog(int i) {
        //System.out.println(log);
        //System.out.println((log.split("\\|"))[i]);
        return log.split("\\|")[i];
    }

    public void addLog(String log) {
        this.log = this.log+log;
    }

    public void restLog() {
        this.log = "";
    }

    public String nbMargineString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<getNBEquipe();i++){
            for(int j=0;j<getNBEquipe();j++){
                sb.append(String.format("%2d", margeJournees[i][j].size())+";");
            }
            sb.append("\n");
        }
        sb.append("\n");
        for(int i=0;i<getNbJournee();i++){
            sb.append(String.format("%2d", (margeRencontres[i]).size())+";");
        }
        sb.append("\n");
        for(int i=0;i<getNbJournee();i++){
            sb.append(String.format("%2d", (margeEquipe[i]).size())+";");
        }
        sb.append("\n");
        return sb.toString();
    }

    public void logCheckProf(){
        String cheminJar = "CheckerChampionnat.jar";
        String motifContraintes = "\t\t (\\d+) contraintes non satisfaites";
        String motifDeviation = "\t\t (\\d+) de deviation";
        String motifCout = "\t\t cout de la solution : (\\d+)";
        String motifCoutBis =".*cout de la solution: (\\d+). *";
        Pattern patternContraintes = Pattern.compile(motifContraintes);
        Pattern patternDeviation = Pattern.compile(motifDeviation);
        Pattern patternCout = Pattern.compile(motifCout);
        Pattern patternCoutbis = Pattern.compile(motifCoutBis);
        int contraintes = 0;
        int deviation = 0;
        int cout = 0;
        try {
            ProcessBuilder processBuilder;
            if(getLog(7).equals("bengloo")){
                processBuilder = new ProcessBuilder("../../openjdk-20.0.1/bin/java", "-jar", cheminJar);
            }else {
                processBuilder = new ProcessBuilder("java", "-jar", cheminJar);
            }
            processBuilder.directory(new File("checkerProf/"));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capturer la sortie du processus exécutant le fichier JAR
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcherContraintes = patternContraintes.matcher(line);
                Matcher matcherDeviation = patternDeviation.matcher(line);
                Matcher matcherCout = patternCout.matcher(line);
                Matcher matcherCoutbis = patternCoutbis.matcher(line);

                if (matcherContraintes.find()) {
                    contraintes = Integer.parseInt(matcherContraintes.group(1));
                } else if (matcherDeviation.find()) {
                    deviation = Integer.parseInt(matcherDeviation.group(1));
                } else if (matcherCout.find()) {
                    cout = Integer.parseInt(matcherCout.group(1));
                }else if (matcherCoutbis.find()) {
                    cout = Integer.parseInt(matcherCoutbis.group(1));
                }
                //System.out.println(line);
            }
            addLog("|"+cout+"|"+deviation+"|"+contraintes);
            // Attendre la fin de l'exécution du processus
            int exitCode = 0;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Championnat{\n");
        sb.append("\tInstance=").append(getInstance().getNom()).append("\n");
        sb.append("\tequipes=").append(equipes.values()).append("\n");
        sb.append("\trencontres=").append(rencontres.values()).append("\n");
        sb.append("\tjournees=[").append("\n");
        for(Journee j:journees.values()){
            sb.append("\t\t").append(j.toString()).append("\n");
        }
        sb.append("\t]").append("\n");
        sb.append("\tcoutTotal=").append(coutTotal).append("\n");
        sb.append("\tcoefContraintes=[").append("\n");
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes= getInstance().getContraintes(type);
            sb.append("\t\t"+type+"\n");
            for(Contrainte c:contraintes){
                sb.append("\t\t\tcoef="+this.coefContraintes.get(c)+" <= ").append(c.toString()).append("\n");
            }
        }
        sb.append("\t]").append("\n");
        sb.append("}");
        return sb.toString();
    }
    public String toStringSimple(){
        StringBuilder sb = new StringBuilder();
        for(Journee j:journees.values()){
            sb.append("J"+j.getId()+":");
            for(Rencontre r:j.getRencontres().values()){
                sb.append(r.getLabel()+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}

