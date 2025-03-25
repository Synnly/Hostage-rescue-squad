package mdp;

import carte.Case;
import carte.Routine;
import coups.Coup;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.*;

public class HostageRescueSquad implements MDP{
    private final Environnement env;
    private Etat[] etats = null;

    public HostageRescueSquad(Environnement env){
        this.env = env;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }


    @Override
    public Etat[] getEtats() {
        ArrayList<Etat> listeEtats = new ArrayList<>();
        int nbOps = 1;                                              // A changer quand plusieurs operateurs
        Routine routine = env.getEnnemis().get(0).getRoutine();     // A changer quand plusieurs routines
        int[] indCasesOp = new int[nbOps];
        int nbCases = env.getLargeur() * env.getHauteur();
        int nbTerr = env.getEnnemis().size();

        // Initialisation positions terroristes
        int[] indCasesTerr = new int[nbTerr];
        for(int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr ++){
            Case caseTerr = env.getCase(env.getEnnemis().get(indTerr).getX(), env.getEnnemis().get(indTerr).getY());
            indCasesTerr[indTerr] = env.getPlateau().indexOf(caseTerr);
        }

        for (int indCase = 0; indCase < Math.pow(nbCases, nbOps); indCase++) {
            // Positions operateurs
            for (int indOp = 0; indOp < nbOps; indOp++) {
                indCasesOp[indOp] = ((int) (indCase / Math.pow(nbCases, indOp))) % nbCases;
            }

            // Positions terroristes
            for(int indCaseRoutine = 0; indCaseRoutine < routine.taille(); indCaseRoutine ++) {
                for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
                    indCasesTerr[indTerr] = routine.nextIndex(indCasesTerr[indTerr]);
                }

                 // Quels operateurs ont l'objectif
                boolean[] aObjectif = new boolean[nbOps];
                for (int i = 0; i < Math.pow(2, nbOps); i++) {
                    for (int j = 0; j < nbOps; j++) {
                        aObjectif[j] = ((int) (i / Math.pow(2, j))) == 1;
                    }

                    // Niveau de menace
                    for (int menace = env.getMinMenace(); menace < env.getMaxMenace(); menace++) {
                        EtatNormal e = new EtatNormal(indCasesOp, aObjectif, indCasesTerr, menace);

                        if(etatEstValideEtNonTerminal(e)) {
                            listeEtats.add(e);
                        }
                    }
                }
            }
        }
        return listeEtats.toArray(new Etat[0]);
    }

    /**
     * Calcule tous les états accessibles en fonction de si l'action échoue ou non et leur probabilité correspondante
     * @param etatDepart L'etat de départ
     * @param action L'action effectuée
     * @return Le dictionnaire ayant pour clé l'état et en valeur la probabilité d'y arriver
     */
    @Override
    public Map<Etat, Double> transition(Etat etatDepart, Action action){
        Map<Etat, Double> etats = new HashMap<>();
        etats.put(etatDepart, 1.);

        // parcours des coups composant l'action
        for(int i = 0; i < action.coups().size(); i++){
            Map<Etat, Double> etatsTemp = new HashMap<>(2 * etats.size());

            // action des coups sur les etats
            for(Etat e : etats.keySet()){
                if(!e.estTerminal()) {
                    if(action.coups().get(i).probaSucces != 1 && action.coups().get(i).probaSucces != 0) {
                        Etat etatSucces = simuler(env, e, action.coups().get(i), action.personnage(), action.cibles().get(i), true);
                        Etat etatEchec = simuler(env, e, action.coups().get(i), action.personnage(), action.cibles().get(i), false);

                        etatsTemp.put(etatSucces, etats.get(e) * action.coups().get(i).probaSucces);
                        etatsTemp.put(etatEchec, etats.get(e) * (1 - action.coups().get(i).probaSucces));
                    }
                    else{
                        Etat etat = simuler(env, e, action.coups().get(i), action.personnage(), action.cibles().get(i), action.coups().get(i).probaSucces == 1);
                        etatsTemp.put(etat, etats.get(e) * action.coups().get(i).probaSucces);
                    }
                }
                else{
                    etatsTemp.put(e, etats.get(e));
                }
            }
            etats = etatsTemp;
        }
        return etats;
    }

    @Override
    public double recompense(Etat s, Action a, Etat sPrime){
        double recomp = 0;

        // actions
        for(Coup coup : a.coups()){
            if (coup.estDeplacement()) {
                recomp +=  valeurDeplacement;
            }
            else if (coup.estTir()) {
                recomp += Math.min(1, s.indCaseTerroristes.length- sPrime.indCaseTerroristes.length) * valeurTuerEnnemi;
            }
            else if (coup.estFinTour()) {
                recomp += valeurDeplacement;
            }
        }

        // echec
        if(sPrime.estTerminal() && !sPrime.estReussite()){
            recomp += valeurEchec;
        }

        // changement de niveau de menace
        recomp += (s.menace - sPrime.menace) * valeurDeltaMenace;

        // recuperation de l'objectif
        for (int i = 0; i < s.aObjectif.length; i++) {
            if(sPrime.aObjectif[i] && !s.aObjectif[i]){
                recomp += valeurObjectif;
            }
        }

        // reussite de la mission
        if(sPrime.estTerminal() && sPrime.estReussite()){
            recomp += valeurReussite;
        }

        return recomp;
    }

    public boolean etatEstValideEtNonTerminal(Etat e){
        // Terroriste et operateur sur la meme case
        Routine routine = env.getEnnemis().get(0).getRoutine();
        for(int indOp = 0; indOp < e.indCaseOperateurs.length; indOp ++) {
            for (int indTerr = 0; indTerr < e.indCaseTerroristes.length; indTerr++) {
                int indCasePlateauTerr = env.getPlateau().indexOf(routine.getCase(e.indCaseTerroristes[indTerr]));
                if(e.indCaseOperateurs[indOp] == indCasePlateauTerr){
                    return false;
                }
            }
        }

        // Plusieurs objectifs récupérés
        boolean aObj = false;
        for(boolean opAObjectif : e.aObjectif){
            if(opAObjectif && !aObj){
                aObj = true;
            }
            if(opAObjectif && aObj){
                return false;
            }
        }

        // Tous les ennemis morts et menace != minMenace
        boolean ennemisTousMorts = true;
        for (int indTerr = 0; indTerr < e.indCaseTerroristes.length; indTerr++) {
            if(e.indCaseTerroristes[indTerr] != -1){
                ennemisTousMorts = false;
                break;
            }
        }
        if (ennemisTousMorts && e.menace != env.getMinMenace()){
            return false;
        }

        // Operateur a l'objectif et est sur la ligne du bas
        for(int indOp = 0; indOp < e.indCaseOperateurs.length; indOp ++) {
            if(e.indCaseOperateurs[indOp]/env.getLargeur() == env.getHauteur()-1 && e.aObjectif[indOp]){
                return false;
            }
        }

        return true;
    }

    /**
     * Calcule tous les arrangements de coups possibles, compte tenu de la limite de points d'actions de l'opérateur et
     * de la fin de tour
     * @param listeCoups La liste de tous les coups possibles
     * @param maxNbCoups Le nombre maximal de coups en un tour
     * @param op L'opérateur effectuant les coups
     * @return L'ensemble des suites de coups possibles
     */
    private Set<List<Coup>> getAllSuitesCoupsPossibleOperateur(Coup[] listeCoups, int maxNbCoups, Operateur op){
        Set<List<Coup>> actionsPossibles = new HashSet<>();
        for (int i = 0; i < Math.pow(listeCoups.length, maxNbCoups); i++) {
            List<Coup> actionComplete = new ArrayList<>();
            int idAction = i; // Identifiant de l'action, ie une sequence d'indice de coups

            int k = Math.max(1, (int) (Math.log(i) / Math.log(listeCoups.length)));
            int sumPA = 0;

            do {
                // Tour fini, donc pas d'actions en plus possible
                if(actionComplete.contains(op.getFinTour())){
                    sumPA = op.getPointsAction();
                }
                else {
                    int indiceCoup = (int) (idAction % Math.pow(listeCoups.length, k));
                    // si assez de PA pour faire l'action
                    if(sumPA + listeCoups[indiceCoup].cout <= op.getPointsAction()) {
                        actionComplete.add(listeCoups[indiceCoup]);
                        sumPA += listeCoups[indiceCoup].cout;
                    }
                    else{
                        actionComplete.add(op.getFinTour());
                        sumPA = op.getPointsAction();
                    }
                }
                idAction = idAction / listeCoups.length;
            } while (sumPA < op.getPointsAction());

            actionsPossibles.add(actionComplete);
        }
        return actionsPossibles;
    }

    /**
     * Calcule tous les arrangements de coups possibles pour les terroristes
     * @param listeCoups La liste de tous les coups possibles
     * @param maxNbCoups Le nombre maximal de coups en un tour
     * @return L'ensemble des suites de coups possibles
     */
    private Set<List<Coup>> getAllSuitesCoupsPossibleTerroristes(Coup[] listeCoups, int maxNbCoups){
        Set<List<Coup>> actionsPossibles = new HashSet<>();

        for (int i = 0; i < Math.pow(listeCoups.length, maxNbCoups); i++) {
            List<Coup> actionComplete = new ArrayList<>();
            int idAction = i; // Identifiant de l'action, ie une sequence d'indice de coups

            for (int k = maxNbCoups-1; k >= 0; k--) {
                int indiceCoup = (int) (idAction / Math.pow(listeCoups.length, k));
                actionComplete.add(listeCoups[indiceCoup]);
                idAction = (int) (idAction % Math.pow(listeCoups.length, k));
            }
            actionsPossibles.add(actionComplete);
        }
        return actionsPossibles;
    }

    /**
     * Calcule toutes les actions possibles pour le joueur en fonction des suites de coups possibles
     * @param env L'environnement du joueur
     * @return La liste des actions valides
     */
    public Set<Action> getAllActionsPossibleOperateur(Environnement env){
        Operateur op = env.getOperateurActif();
        Coup[] listeCoups = {op.getDeplacement(), op.getTir(), op.getFinTour()};

        // Nombre maximum de coups par opérateur
        int maxNbCoups = 0;
        for (Coup c : listeCoups) {
            maxNbCoups = Math.max(maxNbCoups, op.getPointsAction() / c.cout);
        }

        Set<List<Coup>> suiteCoupsPossibles = getAllSuitesCoupsPossibleOperateur(listeCoups, maxNbCoups, op);
        Set<Action> actionPossibles = new HashSet<>();

        for(List<Coup> suiteCoup : suiteCoupsPossibles) {   // Parcours de toutes les suites de coups
            // Liste des cases des couches. La case i de la couche j correspond à l'etat i de la case j
            List<List<Case>> casesCouches = new ArrayList<>();
            casesCouches.add(new ArrayList<>());
            casesCouches.get(0).add(env.getCase(env.getOperateurActif().getX(), env.getOperateurActif().getY()));

            // Liste des etats des couches. L'etat i de la couche j correspond à la case i de la case j
            List<List<Etat>> etatsCouches = new ArrayList<>();
            etatsCouches.add(new ArrayList<>());
            etatsCouches.get(0).add(new Etat(env.copy()));

            while (!casesCouches.isEmpty()) {
                while (!casesCouches.isEmpty() && casesCouches.size() < suiteCoup.size() + 1){     // création des nouvelles couches
                    int indiceCouche = etatsCouches.size()-1;
                    Map<Case, Etat> coucheTemp = getCasesValidesEtEtatsOperateur(env, etatsCouches.get(indiceCouche).get(0), suiteCoup.get(indiceCouche));

                    // Transformation de la map en listes
                    List<Case> casesTemp = new ArrayList<>(coucheTemp.size());
                    List<Etat> etatsTemp = new ArrayList<>(coucheTemp.size());
                    for(Case c : new ArrayList<>(coucheTemp.keySet())){
                        casesTemp.add(c.copy());
                        etatsTemp.add(coucheTemp.get(c));
                    }
                    casesCouches.add(casesTemp);
                    etatsCouches.add(etatsTemp);

                    // Si la derniere couche est vide ici, alors le coup n'as pas de cases valides, donc il faut
                    // supprimer le noeud
                    while(!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()){
                        casesCouches.remove(casesCouches.size() - 1);
                        etatsCouches.remove(etatsCouches.size() - 1);

                        if(!casesCouches.isEmpty()){
                            casesCouches.get(casesCouches.size() - 1).remove(0);
                            etatsCouches.get(etatsCouches.size() - 1).remove(0);
                        }
                    }
                }
                if(casesCouches.isEmpty()){
                    break;
                }

                List<Case> casesCible = new ArrayList<>();
                for(List<Case> couche : casesCouches){
                    casesCible.add(couche.get(0));
                }
                casesCible.remove(0);   // Retrait de la case de départ
                actionPossibles.add(new Action(env.getOperateurActif(), suiteCoup, casesCible));

                // Suppression de la premiere case de la derniere couche
                casesCouches.get(casesCouches.size() - 1).remove(0);
                etatsCouches.get(etatsCouches.size() - 1).remove(0);

                // Suppression des couches vides et des premiers noeuds précédent une couche vide
                while(!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()){
                    casesCouches.remove(casesCouches.size() - 1);
                    etatsCouches.remove(etatsCouches.size() - 1);

                    if(!casesCouches.isEmpty()){
                        casesCouches.get(casesCouches.size() - 1).remove(0);
                        etatsCouches.get(etatsCouches.size() - 1).remove(0);
                    }
                }
            }
        }
        return actionPossibles;
    }

    /**
     * Calcule toutes les cases et états correspondants d'arrivée apres que l'opérateur ait effectué le coup
     * @param env L'environnement de l'opérateur
     * @param etat L'etat de départ
     * @param coup Le coup à effectuer
     * @return Le dictionnaire de toutes les cases et états correspondants d'arrivée
     */
    private Map<Case, Etat> getCasesValidesEtEtatsOperateur(Environnement env, Etat etat, Coup coup){
        Map<Case, Etat> caseEtat = new HashMap<>();
        Coup coupCopy = coup.copy();
        coupCopy.probaSucces = 1;

        Environnement envCopy = env.copy();
        envCopy.setEtat(etat);
        Personnage persoCopy = envCopy.getOperateurActif();
        List<Case> casesValides = new ArrayList<>(coup.getCasesValides(envCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY())));

        for(Case c : casesValides) {
            envCopy = env.copy();
            coupCopy.effectuer(envCopy, envCopy.getOperateurActif(), c);
            caseEtat.put(c, new Etat(envCopy));
        }
        return caseEtat;
    }

    /**
     * Calcule l'utilité de l'état si le tour actuel est celui du joueur
     * @param env L'environnement
     * @param etatDepart L'état de départ
     * @param tour Le numéro du tour
     * @return La somme des utilités pondéré par leurs probabilités des états accessibles si <code>tour</code> < <code>IterationValeur.maxTour</code>, <code>MDP.valeurEchec</code> sinon
     */
    private double utiliteEtatTourJoueur(Environnement env, Etat etatDepart, int tour){
        // Si mission pas réussie au bout de maxTour alors échec
        if(tour > maxTour){
            return 0;
        }

        Set<Action> actions = getAllActionsPossibleOperateur(env);
        double sumUtil = 0;

        for(Action action : actions){
            Map<Etat, Double> probaEtats = MDP.transition(env.copy(), etatDepart, action);

            // Ajout des utilités des états d'arrivée pondéré par proba d'y arriver
            for(Etat etatArrivee : probaEtats.keySet()){
                Environnement envCopy = env.copy();
                envCopy.setEtat(etatArrivee);
                envCopy = envCopy.copy();

                sumUtil += probaEtats.get(etatArrivee) * (MDP.recompense(etatDepart, action, etatArrivee) + gamma * utiliteEtatTourEnnemi(envCopy, etatArrivee, tour));
            }
        }
        return sumUtil/actions.size();
    }

    /**
     * Calcule l'utilité de l'état si le tour actuel est celui du terroriste
     * @param env L'environnement
     * @param etatDepart L'état de départ
     * @param tour Le numéro du tour
     * @return La somme des utilités pondéré par leurs probabilités des états accessibles si <code>tour</code> < <code>IterationValeur.maxTour</code>, <code>MDP.valeurEchec</code> sinon
     */
    private double utiliteEtatTourEnnemi(Environnement env, Etat etatDepart, int tour){
        // Si mission pas réussie au bout de maxTour alors échec
        if(tour >= maxTour){
            return MDP.valeurEchec;
        }
        if(env.getEnnemis().isEmpty()){     // Aucun ennemi, donc pas de changement
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatDepart);
            envCopy = envCopy.copy();
            return utiliteEtatTourJoueur(envCopy, etatDepart, tour + 1);
        }

        Terroriste terro = env.getEnnemis().get(0);
        Set<List<Coup>> suitesCoupsPossible = getAllSuitesCoupsPossibleTerroristes(new coups.Coup[]{terro.getDeplacement(), terro.getTir()}, env.getMenace());
        double sumUtil = 0;

        Map<Etat, Double> probaEtats = new HashMap<>();
        for(List<Coup> suiteCoups : suitesCoupsPossible) {
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatDepart);
            envCopy = envCopy.copy();
            envCopy.effectuerCoupsTerroristes(suiteCoups);

            // Proba de la suite de coups ennemi
            double proba = 1;
            for(Coup c : suiteCoups){
                proba *= env.getProbaCoupEnnemi(c);
            }

            Etat etatTemp = new Etat(envCopy);
            etatTemp.operateur().resetPointsAction();   // On redonne les PA à l'opérateur
            probaEtats.put(etatTemp, proba);
        }

        // Ajout des utilités des états d'arrivée pondéré par proba d'y arriver
        for(Etat etatArrivee : probaEtats.keySet()){
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatArrivee);
            envCopy = envCopy.copy();
            sumUtil += probaEtats.get(etatArrivee) * (gamma * utiliteEtatTourJoueur(envCopy, etatArrivee, tour + 1));
        }

        return sumUtil;
    }

    private Etat simuler(Environnement env, Etat etatDepart, Coup coup, Personnage perso, Case cible, boolean succes){
        // Copie profonde de l'environnement dans l'etat de depart
        Environnement envCopy = env.copy();
        Coup coupCopy = coup.copy();
        envCopy.setEtat(etatDepart);
        envCopy = envCopy.copy(); // ligne necessaire sinon instance de perso en copie de surface
        coupCopy.probaSucces = succes ? 1 : 0;

        coupCopy.effectuer(envCopy, envCopy.getPersonnage(perso), envCopy.getCase(cible));

        return new Etat(envCopy);
    }
}
