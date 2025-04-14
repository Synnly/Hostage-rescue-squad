package observable;

import carte.separation.Mur;
import carte.separation.Separation;
import coups.*;
import carte.cases.*;
import carte.Routine;
import mdp.*;
import mdp.etat.Etat;
import mdp.etat.EtatNormal;
import org.javatuples.Pair;
import personnages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * L'environnement d'une mission.
 */
public class Environnement extends Observable{

    private double probaCalmer;
    private int largeur;
    private int hauteur;
    private List<Case> cases;
    private List<Separation> separations;
    private Operateur operateur;
    private List<Terroriste> ennemis;
    private final int maxMenace = 7;
    private final int minMenace = 2;
    private int menace = minMenace;

    private boolean echec = false;
    private boolean missionFinie = false;
    private final double probaTirEnnemi = 0.3;
    private final double probaDeplacementEnnemi = 0.7;
    private final double probaSuccesDeplacement;
    private final double probaSuccesTir;
    private final double probaElimSil;
    private HostageRescueSquad mdp;
    private Pair<Coup, Direction> coupPredit = null;
    // Nombre de coups max calculés pour l'agent d'avertissement
    private final int nbCoupsMax = 10;
    // Nombre d'itérations max effectuées par l'agent d'avertissement
    private final int nbIters = 1000;
    // Proba d'échec à partir de laquelle on considère qu'un coup est dangereuse
    private double seuilAvertissement = 0.6;


    /**
     * Constructeur de l'environnement. Initialise le plateau de taille <code>largeur</code> x <code>hauteur</code>,
     * un opérateur et deux terroristes
     *
     * @param largeur Le nombre de cases en largeur du plateau. Doit être &gt;&nbsp;0
     * @param hauteur Le nombre de cases en hauteur du plateau. Doit être &gt;&nbsp;0
     * @param probaSuccesDeplacement La probabilité de succès des déplacements de l'opérateur. Doit être
     *                               0 &le;&nbsp;proba &le;&nbsp;1
     * @param probaSuccesTir La probabilité de succès des tirs de l'opérateur. Doit être
     *                       0 &le;&nbsp;proba &le;&nbsp;1
     */
    public Environnement(int largeur, int hauteur, double probaSuccesDeplacement, double probaSuccesTir, double probaElimSil, double probaCalmer) {
        assert largeur > 0 : "La largeur doit être > 0 (largeur =" + largeur + ")";
        assert hauteur > 0 : "La hauteur doit être > 0 (hauteur =" + hauteur + ")";
        assert probaSuccesDeplacement <= 1 && probaSuccesDeplacement >= 0 : "La probabilité de succès des déplacements de l'opérateur doit être 0 <= proba <= 1";
        assert probaSuccesTir <= 1 && probaSuccesTir >= 0 : "La probabilité de succès des tirs de l'opérateur doit être 0 <= proba <= 1";


        this.probaSuccesDeplacement = probaSuccesDeplacement;
        this.probaSuccesTir = probaSuccesTir;
        this.probaElimSil = probaElimSil;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.probaCalmer = probaCalmer;
        nouvellePartie();

        mdp = new HostageRescueSquad(this);
        System.out.println("L'ia se prépare ...");
        IterationValeur.iterationValeur(mdp);
        System.out.println("L'ia a fini");
        printPrediction();
    }

    public Environnement(Environnement env){
        this.largeur = env.largeur;
        this.hauteur = env.hauteur;
        this.operateur = env.operateur.copy();
        this.menace = env.menace;
        this.cases = new ArrayList<>();
        for(Case c:env.cases){
            cases.add(c.copy());
        }
        this.separations = new ArrayList<>();
        for(Separation sep : env.separations){
            separations.add(sep.copy());
        }
        this.ennemis = new ArrayList<>();
        for(Terroriste t:env.ennemis){
            ennemis.add(t.copy());
        }
        System.out.println("taille ennemis = "+ennemis.size());
        this.probaSuccesDeplacement = env.probaSuccesDeplacement;
        this.probaSuccesTir = env.probaSuccesTir;
        this.probaElimSil = env.probaElimSil;
        this.probaCalmer = env.probaCalmer;
        mdp = env.mdp;
    }


    /**
     * Réinitialise les paramètres de départ d'une nouc-velle partie
     */
    public void nouvellePartie(){
        cases = new ArrayList<>();
        separations = new ArrayList<>();
        initPlateau(largeur, hauteur);

        // Creation des opérateurs
        Deplacement deplacementOp = new Deplacement(1, probaSuccesDeplacement);
        Tir tirOp = new Tir(1, probaSuccesTir);
        EliminationSilencieuse elimSil = new EliminationSilencieuse(1,probaElimSil);
        Calmer calmer = new Calmer(2,probaCalmer);
        operateur = new Operateur(this, largeur/2 + 1, hauteur-1, 2, deplacementOp, tirOp,elimSil,calmer);
        operateur.setActionActive(deplacementOp);

        // Création de la routine
        Routine routine = creerRoutine();

        // Création des ennemis
        Deplacement deplacementTer = new Deplacement(0, 1);
        Tir tirTer = new Tir(0, 1);
        ennemis = new ArrayList<>(3);

        List<CaseReapparitionEnnemis> reapparitions = routine.getReapparitionEnnemis();
        Terroriste ennemi = new Terroriste(this, reapparitions.get(0).getX(), reapparitions.get(0).getY(), 0, deplacementTer, tirTer);
        Terroriste ennemi2 = new Terroriste(this,reapparitions.get(1).getX(), reapparitions.get(1).getY(), 0, deplacementTer, tirTer);

        ennemi.setRoutine(routine);
        ennemi2.setRoutine(routine);
        ennemis.add(ennemi);
        ennemis.add(ennemi2);

        for(int i = 0; i<3; i++){
            Terroriste terr = new Terroriste(this, -1, -1, 0, deplacementTer, tirTer );
            terr.setRoutine(routine);
            System.out.println("new terr = "+terr.getId());
            ennemis.add(terr);

        }









        missionFinie = false;
        menace = minMenace;
        if(mdp != null) coupPredit = IterationValeur.predict(mdp, new EtatNormal(this));
        System.out.println("NOUVELLE PARTIE");
    }

    /**
     * Relance une partie
     */
    public void finDePartie(){
        this.nouvellePartie();
        this.notifyObservers();
    }

    /**
     * Fonction temporaire de creation de routine
     *
     * @return La routine
     */
    public Routine creerRoutine(){
        Case pred = (getCase(largeur/2, 0));
//        Case pred = getCase(2, 0);
        Case next;
        Routine routine = new Routine(pred);

        next = new CaseReapparitionEnnemis(getCase(pred.x+1, pred.y)); routine.ajouterReapparitionEnnemis(pred, (CaseReapparitionEnnemis) next); pred = next;
        next = getCase(pred.x+1, pred.y); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = new CaseReapparitionEnnemis(getCase(pred.x-1, pred.y)); routine.ajouterReapparitionEnnemis(pred, (CaseReapparitionEnnemis)next); pred = next;
        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x+1, pred.y); routine.ajouterCase(pred, next);

        return routine;
    }

    /**
     * Renvoie la largeur du plateau en nombre de cases
     *
     * @return La largeur
     */
    public int getLargeur() {return largeur;}

    /**
     * Renvoie la hauteur du plateau en nombre de cases
     *
     * @return La hauteur
     */
    public int getHauteur() {return hauteur;}

    /**
     * Renvoie la liste des cases constituant le plateau.&nbsp;Les cases sont ordonnés dans l'ordre de la largeur puis hauteur,
     * donc la case d'indice <code>i</code> est de coordonnées <code>(i % largeur, i // largeur)</code>
     *
     * @return Le plateau
     */
    public List<Case> getPlateau(){return cases;}

    /**
     * Retourne la case aux coordonnées (<code>x</code>, <code>y</code>)
     *
     * @param x La coordonnée en largeur de la case cible. Doit être 0 &le;&nbsp;<code>x</code>&lt;&nbsp;<code>largeur</code>
     * @param y La coordonnée en hauteur de la case cible. Doit être 0 &le;&nbsp;<code>y</code>&lt;&nbsp;<code>hauteur</code>
     * @return La case
     */
    public Case getCase(int x, int y){
        if(x == -1 && y == -1){
            return AucuneCase.instance;
        }

        assert x >= 0 && x < largeur : "x doit être 0 <= x < largeur (x = "+ x + ")";
        assert y >= 0 && y < hauteur : "y doit être 0 <= y < hauteur (y = "+ y + ")";

        return cases.get(y * largeur + x);
    }

    /**
     * Renvoie l'opérateur actif, c'est-à-dire le dernier opérateur sélectionné pour
     * une action ou l'opérateur suivant dans la liste après que l'ancien opérateur
     * a fini son tour
     *
     * @return L'opérateur actif
     */
    public Operateur getOperateurActif() {
        return operateur;
    }

    /**
     * Renvoie la liste des ennemis en vie.
     *
     * @return La liste des ennemis
     */
    public List<Terroriste> getEnnemis(){
        return ennemis;
    }

    /**
     * Recrée le plateau de taille <code>largeur</code> x <code>hauteur</code>
     *
     * @param largeur Le nombre de cases en largeur du plateau. Doit être &gt;&nbsp;0
     * @param hauteur Le nombre de cases en hauteur du plateau. Doit être &gt;&nbsp;0
     */
    public void initPlateau(int largeur, int hauteur){
        assert largeur > 0 : "La largeur doit être > 0 (largeur =" + largeur + ")";
        assert hauteur > 0 : "La hauteur doit être > 0 (hauteur =" + hauteur + ")";

        // Cases
        cases.clear();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                cases.add(new CaseNormale(this, x, y));
            }
        }
        int x = (largeur/2);
        int y = (hauteur/4);
        cases.set(y * largeur + x, new Objectif(this, x, y));

        // Couvertures (temporaire)
        cases.set(1, new Couverture(this, 1, 0));
        cases.set(4, new Couverture(this, 4, 0));
        cases.set(9, new Couverture(this, 2, 1));
        cases.set(19, new Couverture(this, 5, 2));
//        cases.set(31, new Couverture(this, 3, 4));
        cases.set(29, new Couverture(this, 1, 4));
        cases.set(32, new Couverture(this, 4, 4));

//        cases.set(15,new Couverture(this,1,2));
//        cases.set(22,new Couverture(this,1,3));
//        cases.set(36,new Couverture(this,1,5));
//        cases.set(43,new Couverture(this,1,6));
//        cases.set(50,new Couverture(this,1,7));
//        cases.set(57,new Couverture(this,1,8));
//        cases.set(64,new Couverture(this,1,9));
//
//        cases.set(26, new Couverture(this, 5, 3));
//        cases.set(33, new Couverture(this, 5, 4));
//        cases.set(40, new Couverture(this, 5, 5));
//        cases.set(47, new Couverture(this, 5, 6));
//        cases.set(54, new Couverture(this, 5, 7));
//        cases.set(61, new Couverture(this, 5, 8));
//        cases.set(68, new Couverture(this, 5, 9));
//
//        cases.set(16, new Couverture(this, 2, 2));
//        cases.set(18, new Couverture(this, 4, 2));

        // Murs
        separations.clear();
        separations.add(new Mur(getCase(3, 0), getCase(3, 1)));
        separations.add(new Mur(getCase(4, 0), getCase(4, 1)));
        separations.add(new Mur(getCase(4, 2), getCase(5, 2)));
        separations.add(new Mur(getCase(4, 3), getCase(5, 3)));
        separations.add(new Mur(getCase(2, 3), getCase(2, 4)));
        separations.add(new Mur(getCase(3, 3), getCase(3, 4)));
        separations.add(new Mur(getCase(1, 1), getCase(2, 1)));
        separations.add(new Mur(getCase(1, 2), getCase(2, 2)));
        separations.add(new Mur(getCase(4, 3), getCase(4, 4)));
    }

    /**
     * Effectue le tour des ennemis.&nbsp;Remet l'action choisie par les opérateurs au déplacement
     */
    public void tourEnnemi(){
        List<Double> nombres = getNombresAleatoires(menace);
        for(int i = 0; i < menace; i++) {
            if(nombres.get(i) < probaTirEnnemi){   // Tir
                for (Terroriste ennemi : ennemis) {
                    ennemi.getTir().effectuer(this, ennemi, getCase(operateur.getX(), operateur.getY()));
                }
            }
            else {
                for (Terroriste ennemi : ennemis) {     // Deplacement
                    Case posEnnemi = getCase(ennemi.getX(), ennemi.getY());
                    ennemi.getDeplacement().effectuer(this, ennemi, ennemi.getRoutine().prochaineCase(posEnnemi));
                }
            }
        }

        operateur.setActionActive(operateur.getDeplacement());

        if(!isMissionFinie()) {
            operateur.resetPointsAction();
        }
    }

    /**
     * Définit la case ciblée par l'action active de l'opérateur actif
     *
     * @param x La coordonnée en largeur de la case cible. Doit être 0 &le;&nbsp;<code>x</code>&lt;&nbsp;<code>largeur</code>
     * @param y La coordonnée en hauteur de la case cible. Doit être 0 &le;&nbsp;<code>y</code>&lt;&nbsp;<code>hauteur</code>
     */
    public void choisirCase(int x, int y){
        assert x >= -1 && x < largeur : "x doit être 0 <= x < " + largeur + "(x = " + x + ")";
        assert y >= -1 && y < hauteur : "y doit être 0 <= y < " + hauteur + "(y = " + y + ")";

        Case c = getCase(x, y);

        // Effectuer l'action
        Operateur op = getOperateurActif();
        op.getActionActive().effectuer(this, op, c);

        // Tour ennemi
        if(op.getPointsAction() == 0){
            tourEnnemi();
        }

        if(missionFinie) nouvellePartie();

        Coup[] listeCoups = new Coup[]{op.getDeplacement(), op.getTir(), op.getFinTour(), op.getEliminationSilencieuse()};
        for(Coup coup : listeCoups){
            for(Case caseValide : coup.getCasesValides(this, op)){
                Direction dir;
                if(caseValide.x == -1 || caseValide.y == -1) dir = Direction.AUCUN;
                else if(caseValide.x > op.getX()) dir = Direction.DROITE;
                else if(caseValide.x < op.getX()) dir = Direction.GAUCHE;
                else if(caseValide.y > op.getY()) dir = Direction.BAS;
                else dir = Direction.HAUT;

                double probaEchec = ExplorationProba.probaEchec(this, mdp, new EtatNormal(this), coup, dir, nbCoupsMax, nbIters, listeCoups);
                if(probaEchec >= seuilAvertissement) {
                    System.out.println("ATTENTION : fin de partie dans " + (probaEchec * 100.) + " % des cas après " + coup + " vers " + dir + " en moins de " + nbCoupsMax + " coups");
                }
            }
        }

        printPrediction();
        notifyObservers();
    }

    /**
     * Fait récupérer l'objectif à l'opérateur.&nbsp;L'objectif devient une case normale après l'appel de cette fonction
     *
     * @param obj L'objectif récupéré.&nbsp;Ne peut être <code>null</code>.
     * @param op  L'opérateur récupérant l'objectif.&nbsp;Ne peut être <code>null</code>.&nbsp;Doit être sur la même case que l'objectif.
     */
    public void recupereObjectif(Objectif obj, Operateur op){
        assert obj != null : "L'objectif ne peut être null";
        assert op != null : "L'opérateur ne peut être null";

        assert obj.x == op.getX() && obj.y == op.getY() : "L'opérateur doit être sur l'objectif";

        int index = -1;
        for(int i = 0; i < cases.size(); i++){
            if (cases.get(i).id == obj.id){
                index = i;
            }
        }
        assert index != -1: "Objectif non trouvé";

//        cases.get(index).estObjectif = false;
        op.setPossedeObjectif(true);
    }

    /**
     * Définit l'action de déplacement comme action active pour l'opérateur actif
     */
    public void setDeplacementActionActive(){
        operateur.setActionActive(operateur.getDeplacement());
        notifyObservers();
    }

    /**
     * Définit l'action de tir comme action active pour l'opérateur actif
     */
    public void setTirActionActive(){
        operateur.setActionActive(operateur.getTir());
        notifyObservers();
    }

    /**
     * Définit l'action de fin de tour comme action active pour l'opérateur actif
     */
    public void setFinTourActionActive(){
        operateur.setActionActive(operateur.getDeplacement());
        tourEnnemi();

        if(missionFinie) nouvellePartie();

        printPrediction();
        notifyObservers();
    }

    /**
     * Définit l'action d'élimination silencieuse comme active pour l'opérateur actif
     */
    public void setElimSilActionActive(){
        operateur.setActionActive(operateur.getEliminationSilencieuse());
        notifyObservers();
    }

    /**
     * Tue tous les ennemis présents sur la case <code>arr</code>.&nbsp;Si aucun ennemi n'est dans la case, ne fait
     * rien.
     *
     * @param arr La case sur laquelle se situent les ennemis à tuer.&nbsp;Ne peut être <code>null</code>
     */
    public void tuerEnnemis(Case arr){
        assert arr != null : "La case ne peut être null";
        for(Terroriste ennemi : ennemis) {
            if (ennemi.getX() == arr.x && ennemi.getY() == arr.y) {
                ennemi.setX(-1);
                ennemi.setY(-1);
            }
        }
    }

    /**
     * Indique si au moins un ennemi est présent sur la case <code>c</code>
     *
     * @param c La case.&nbsp;Ne peut être <code>null</code>
     * @return <code>true</code> si au moins un ennemi est présent dessus, <code>false</code> sinon
     */
    public boolean aEnnemisSurCase(Case c){
        assert c != null : "La case ne peut être null";
        for(Terroriste ennemi : ennemis){
            if(ennemi.getX() == c.x && ennemi.getY() == c.y){
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie une liste de réels aléatoires entre 1 et 0
     * @param nb Le nombre de réels à générer. Doit être &gt;&nbsp;0
     * @return La liste des nombres
     */
    public List<Double> getNombresAleatoires(int nb){
        assert nb > 0 : "Le nombre de réels à générer doit être strictement positif";
        Random random = new Random();
        List<Double> nombres = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            nombres.add(random.nextDouble());
        }
        return nombres;
    }

    /**
     * Renvoie le niveau de menace. Est toujours contenu en minMenace et maxMenace
     */
    public int getMenace() {
        return menace;
    }

    /**
     * Affiche dans le terminal la meilleure action prédite par l'IA
     */
    public void printPrediction(){
        coupPredit = IterationValeur.predict(mdp, new EtatNormal(this));

        System.out.println(coupPreditToString());
    }

    /**
     * Retourne la chaîne de caractère correspondant au conseil de l'IA
     * @return le coup prédit (String)
     */
    public String coupPreditToString(){
        StringBuilder sb = new StringBuilder("L'ia vous conseille de ");
        if (coupPredit.getValue0().estFinTour()) {
            sb.append("terminer le tour");
        } else {
            sb.append(coupPredit.getValue0());
            if(coupPredit.getValue1() != Direction.AUCUN){
                sb.append(" vers ");

            }
            switch (coupPredit.getValue1()) {
                case HAUT -> sb.append("le haut");
                case BAS -> sb.append("le bas");
                case GAUCHE -> sb.append("la gauche");
                case DROITE -> sb.append("la droite");
                default -> {
                }
            }
        }
        return sb.toString();
    }

    /**
     * Renvoie une instance en copie profonde de cet objet. Tous les champs de cette instance sont aussi des copies
     * profondes
     * @return La copie
     */
    public Environnement copy(){
        return new Environnement(this);
    }

    /**
     * Modifie l'état de l'environnement, ie son état au début du tour joueur. Redonne les PA aux opérateurs
     * @param e Le nouvel état
     */
    public void setEtat(Etat e){
        Case caseOp = e.indCaseOperateurs[0] == -1 ? AucuneCase.instance : cases.get(e.indCaseOperateurs[0]);
        operateur.setX(caseOp.getX());
        operateur.setY(caseOp.getY());
        operateur.setPointsAction(e.nbPAOperateurs[0]);

        operateur.setPossedeObjectif(e.aObjectif[0]);
        missionFinie = e.estTerminal();
        echec = !e.estReussite();

        Routine routine = ennemis.get(0).getRoutine();
        for (int indTerr = 0; indTerr < e.indCaseTerroristes.length; indTerr++) {
            Case caseTerr = routine.getCase(e.indCaseTerroristes[indTerr]);
            ennemis.get(indTerr).setX(caseTerr.getX());
            ennemis.get(indTerr).setY(caseTerr.getY());
        }

        menace = e.menace;
    }

    /**
     * Indique si la mission est un échec
     * @return true si la mission est un échec, false sinon
     */
    public boolean isEchec() {
        return echec;
    }

    /**
     * Indique si la mission est finie, ie un échec ou une réussite
     * @return true si la mission est finie, false sinon
     */
    public boolean isMissionFinie() {
        return missionFinie;
    }

    /**
     * Fait effectuer les coups à tous les terroristes
     * @param coups La liste des coups
     */
    public void effectuerCoupsTerroristes(List<Coup> coups){
        for(Terroriste t : ennemis){
            if(t.getX() == -1 && t.getY() == -1){
                continue;
            }

            for(Coup c : coups) {
                if (c.estTir()) {
                    c.effectuer(this, t, getCase(operateur.getX(), operateur.getY()));
                } else if (c.estDeplacement()) {
                    c.effectuer(this, t, t.getRoutine().prochaineCase(getCase(t.getX(), t.getY())));
                }
            }
        }
    }

    /**
     * Calcule la probabilité que les ennemis effectuent le coup fourni
     * @param c Le coup
     * @return La probabilité
     */
    public double getProbaCoupEnnemi(Coup c){
        if(c.estTir()) {
            return probaTirEnnemi;
        }
        else if(c.estDeplacement()) {
            return probaDeplacementEnnemi;
        }
        else return 0;
    }

    /**
     * Renvoie le personnage de l'environnement (généralement copié quand cette fonction est utilisée) qui correspond au
     * personnage fourni
     * @param perso Le personnage contre qui comparer
     * @return Le personnage qui correspond
     */
    public Personnage getPersonnage(Personnage perso){
        if(operateur.getId() == perso.getId()){
            return operateur;
        }

        for(Terroriste t:ennemis){
            if(t.getId() == perso.getId()){
                return t;
            }
        }
        return null;
    }

    /**
     * Renvoie la case de même identifiant dans le plateau
     * @param c La case
     * @return La case ayant le même identifiant
     */
    public Case getCase(Case c){
        for(Case c1:cases){
            if(c.id == c1.id){
                return c1;
            }
        }
        return null;
    }

    /**
     * Termine la mission
     * @param succes Vrai si la mission est un succes, faux sinon
     */
    public void terminerMission(boolean succes){
        if(!succes){
            operateur.setX(-1);
            operateur.setY(-1);
        }
        missionFinie = true;
        echec = !succes;
    }

    /**
     * Augmente la menace si la menace < maxMenace, sinon ne fait rien.
     */
    public void augmenterMenace(){
        menace ++;
        if (menace > maxMenace){
            menace = maxMenace;
        }
    }
    /**
     * Diminue la menace si la menace > minMenace, sinon ne fait rien.
     */
    public void diminuerMenace(){
        menace --;
        if (menace < minMenace){
            menace = minMenace;
        }
    }

    /**
     * Réinitialise la menace à minMenace
     */
    public void resetMenace(){
        menace = minMenace;
    }

    /**
     * Renvoie le niveau de menace minimum
     */
    public int getMinMenace() {
        return minMenace;
    }

    /**
     * Renvoie le niveau de menace maximum
     */
    public int getMaxMenace() {
        return maxMenace;
    }

    public List<Separation> getSeparations(){
        return separations;
    }
    public boolean tousTerrsMorts() {
        boolean tousTerrsMorts = true;
        for (Terroriste t : getEnnemis()) {
            if (t.getX() != -1 || t.getY() != -1) {
                tousTerrsMorts = false;
                break;
            }
        }
        return tousTerrsMorts;
    }



    /**
     * Exécute l'action recomandée par l'IA
     */
    public void executerActionRecommandee(){

        operateur.setActionActive(coupPredit.getValue0());
        switch (coupPredit.getValue1()){
            case HAUT -> operateur.getActionActive().effectuer(this, operateur, this.getCase(operateur.getX(), operateur.getY() - 1));
            case BAS -> operateur.getActionActive().effectuer(this, operateur, this.getCase(operateur.getX(), operateur.getY() + 1));
            case GAUCHE -> operateur.getActionActive().effectuer(this, operateur, this.getCase(operateur.getX() - 1, operateur.getY()));
            case DROITE -> operateur.getActionActive().effectuer(this, operateur, this.getCase(operateur.getX() + 1, operateur.getY()));
            case AUCUN -> operateur.getActionActive().effectuer(this, operateur, AucuneCase.instance);
        }
        if(operateur.getPointsAction() == 0){
            tourEnnemi();
        }

        if(missionFinie) nouvellePartie();

        printPrediction();
        notifyObservers();
    }

    public void setCalmerActionActive() {
        operateur.setActionActive(operateur.getCalmer());
        choisirCase(-1,-1);
    }
}
