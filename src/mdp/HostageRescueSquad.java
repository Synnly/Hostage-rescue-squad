package mdp;

import carte.AucuneCase;
import carte.Case;
import carte.Routine;
import coups.Coup;
import observable.Environnement;
import org.javatuples.Pair;
import personnages.*;

import java.sql.Array;
import java.util.*;

public class HostageRescueSquad implements MDP{
    private final Environnement env;
    private final Environnement envCopy;
    private Etat[] etats = null;

    private Map<Etat, Map<Pair<Coup, Direction>, Map<Etat, Double>>> transitions = new HashMap<>();
    private Map<Etat, Pair<Coup, Direction>[]> coupsEtat = new HashMap<>();
    private Map<Etat, Action[]> actionsEtat = new HashMap<>();
    private Map<Etat, Map<Coup, Map<Case, Etat>>> casesEtatsValides = new HashMap<>();
    private PolitiqueDistanceHostageRescueSquad politique;

    /**
     * Contructeur du MDP représentant le jeu Hostage Rescue Squad
     * @param env L'environnement
     */
    public HostageRescueSquad(Environnement env){
        this.env = env;
        this.envCopy = env.copy();
        politique = new PolitiqueDistanceHostageRescueSquad(this,this.envCopy);
    }

    @Override
    public Map<Etat, Pair<Coup, Direction>[]> getCoups() {
        if(!coupsEtat.isEmpty()){
            return coupsEtat;
        }

        Operateur op = env.getOperateurActif();
        Coup[] listeCoups = {op.getDeplacement(), op.getTir()};
        Map<Etat, Pair<Coup, Direction>[]> coups = new HashMap<>();

        Etat[] etats = getEtats();
        Etat restoreState = new EtatNormal(envCopy);

        for(Etat e : etats){
            envCopy.setEtat(e);
            Operateur opCopy = envCopy.getOperateurActif();
            ArrayList<Pair<Coup, Direction>> listePaires = new ArrayList<>();
            listePaires.add(new Pair<>(op.getFinTour(), Direction.AUCUN));

            for(Coup c : listeCoups){
                for(Case caseValide : c.getCasesValides(envCopy, opCopy)){

                    // Ajout de la direction
                    if(opCopy.getX() == -1 || opCopy.getY() == -1){
                        listePaires.add(new Pair<>(c, Direction.AUCUN));
                    } else if (opCopy.getX() > caseValide.x) {
                        listePaires.add(new Pair<>(c, Direction.GAUCHE));
                    } else if (opCopy.getX() < caseValide.x) {
                        listePaires.add(new Pair<>(c, Direction.DROITE));
                    } else if (opCopy.getY() > caseValide.y) {
                        listePaires.add(new Pair<>(c, Direction.HAUT));
                    } else {
                        listePaires.add(new Pair<>(c, Direction.BAS));
                    }
                }

                coups.put(e, listePaires.toArray(new Pair[0]));
            }
        }

        envCopy.setEtat(restoreState);
        if(coupsEtat.isEmpty()){
            coupsEtat.putAll(coups);
        }

        return coupsEtat;
    }


    @Override
    public Etat[] getEtats() {
        // Si états déjà calculés
        if (etats != null){
            return etats;
        }

        Set<Etat> listeEtats = new HashSet<>();
        int nbOps = 1;                                              // A changer quand plusieurs operateurs
        Routine routine = env.getEnnemis().get(0).getRoutine();     // A changer quand plusieurs routines
        int[] indCasesOp = new int[nbOps];
        int nbCases = env.getLargeur() * env.getHauteur();
        int nbTerr = env.getEnnemis().size();
        int maxPA = env.getOperateurActif().getMaxPointsAction();

        // Quels operateurs en vie
        for(int idCombOpsEnVie = 0; idCombOpsEnVie < Math.pow(2, nbOps); idCombOpsEnVie++) {
            for (int indCase = 0; indCase < Math.pow(nbCases, nbOps); indCase++) {

                // Positions operateurs
                for (int indOp = 0; indOp < nbOps; indOp++) {
                    if(((int) (idCombOpsEnVie / Math.pow(2, indOp))) % 2 == 0){    // Si op mort, pos = -1
                        indCasesOp[indOp] = -1;
                    }
                    else {
                        indCasesOp[indOp] = ((int) (indCase / Math.pow(nbCases, indOp))) % nbCases; //calcul qui retire bit droite et gauche ( ne garde que l'indice de l'opérateur)
                    }
                }

                // Quels terr en vie
                for (int idCombTerrVie = 0; idCombTerrVie < Math.pow(2, nbTerr); idCombTerrVie++) {

                    // Initialisation positions terroristes si en vie
                    int[] indCasesTerr = new int[nbTerr];
                    for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
                        if (((int) (idCombTerrVie / Math.pow(2, indTerr))) % 2 == 0) {    // Si terr mort, pos = -1
                            indCasesTerr[indTerr] = -1;
                        } else {
                            Case caseTerr = env.getCase(env.getEnnemis().get(indTerr).getX(), env.getEnnemis().get(indTerr).getY());
                            indCasesTerr[indTerr] = routine.indexOf(caseTerr);
                        }
                    }

                    // Positions terroristes
                    for (int indCaseRoutine = -1; indCaseRoutine < routine.taille()-1; indCaseRoutine++) {
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
                            for (int menace = env.getMinMenace(); menace <= env.getMaxMenace(); menace++) {

                                // Nombre de PA
                                for(int idNbPa = 0; idNbPa < Math.pow(maxPA+1, nbOps); idNbPa ++){

                                    int[] listePaOps = new int[nbOps];
                                    for (int j = 0; j < nbOps; j++) {
                                        listePaOps[j] = ((int) (idNbPa / Math.pow(maxPA, j)));
                                    }

                                    Etat e = creerEtat(indCasesOp, listePaOps, aObjectif, indCasesTerr, menace);
                                    if (etatEstValide(e)) {
                                        listeEtats.add(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Conversion
        etats = new Etat[listeEtats.size()];
        etats = listeEtats.toArray(etats);
        return etats;
    }

    @Override
    public Map<Etat, Double> transition(Etat etatDepart, Coup coup, Direction direction){
        // Si transition déjà calculée
        if(transitions.get(etatDepart) != null){
            if(transitions.get(etatDepart).get(new Pair<>(coup, direction)) != null){
                return transitions.get(etatDepart).get(new Pair<>(coup, direction));
            }
        }

        Map<Etat, Double> distribution = new HashMap<>();
        Etat restoreState = new EtatNormal(envCopy);

        // Simulation
        if(coup.probaSucces != 1 && coup.probaSucces != 0) {    // Succès ou échec non garanti
            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, true), coup.probaSucces);

            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, false), 1-coup.probaSucces);
        }
        else{   // Succès ou échec garanti
            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, coup.probaSucces == 1), coup.probaSucces);
        }

        envCopy.setEtat(restoreState);

        // Transition des ennemis
        distribution = transitionEnnemis(distribution);

        // Sauvegarde
        if(transitions.get(etatDepart) == null){
            Map<Pair<Coup, Direction>, Map<Etat, Double>> coupEtat = new HashMap<>();
            coupEtat.put(new Pair<>(coup, direction), distribution);
            transitions.put(etatDepart, coupEtat);
        } else {
            transitions.get(etatDepart).put(new Pair<>(coup, direction), distribution);
        }

        return distribution;
    }




    @Override
    public double recompense(Etat s, Coup coup, Etat sPrime){
        double recomp = 0;

        // coup
        if (coup.estDeplacement()) {
            recomp +=  valeurDeplacement;
        }
        else if (coup.estTir()) {
            recomp += Math.max(1, s.indCaseTerroristes.length- sPrime.indCaseTerroristes.length) * valeurTuerEnnemi;
        }
        else if (coup.estFinTour()) {
            recomp += valeurDeplacement;
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



    /**
     * Indique si l'état est valide
     * @param e L'état
     * @return Vrai si e est valide, faux sinon
     */
    public boolean etatEstValide(Etat e){
        // Terroriste et operateur sur la meme case
        Routine routine = env.getEnnemis().get(0).getRoutine();
        for(int indOp = 0; indOp < e.indCaseOperateurs.length; indOp ++) {
            for (int indTerr = 0; indTerr < e.indCaseTerroristes.length; indTerr++) {
                int indCasePlateauTerr = env.getPlateau().indexOf(routine.getCase(e.indCaseTerroristes[indTerr]));
                if(e.indCaseOperateurs[indOp] != -1 && e.indCaseOperateurs[indOp] == indCasePlateauTerr){
                    return false;
                }
            }
        }

        // Plusieurs objectifs récupérés
        // À changer si plusieurs objectifs
        boolean aObj = false;
        for(boolean opAObjectif : e.aObjectif){
            if(opAObjectif && !aObj){
                aObj = true;
            }
            else if(opAObjectif && aObj){
                return false;
            }
        }

        // Tous les ennemis morts et menace != minMenace
        int nbEnnemisMorts = env.getEnnemis().size();
        for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
            if(e.indCaseTerroristes[indTerr] != -1){
                nbEnnemisMorts --;
            }
        }
        if (nbEnnemisMorts == env.getEnnemis().size() && e.menace != env.getMinMenace()){
            return false;
        }

        // A enlever quand respawn d'ennemis
        // Niveau de menace != minMenace + nbEnnemisMorts quand tous ennemis pas morts
        if(nbEnnemisMorts != env.getEnnemis().size() && e.menace != nbEnnemisMorts + env.getMinMenace()){
            return false;
        }

        // Nombre de PA cohérent
        int maxPA = env.getOperateurActif().getMaxPointsAction();
        boolean opEnJeu = false;

        for(int nbPa : e.nbPAOperateurs){
            if (nbPa != 0 || nbPa != maxPA){
                if(opEnJeu){
                    return false;
                }
                opEnJeu = true;
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
     * Calcule toutes les cases et états correspondants d'arrivée apres que l'opérateur ait effectué le coup
     * @param etat L'etat de départ
     * @param coup Le coup à effectuer
     * @return Le dictionnaire de toutes les cases et états correspondants d'arrivée
     */
    private Map<Case, Etat> getCasesValidesEtEtatsOperateur(Etat etat, Coup coup){
        // Si cases valides déjà calculés
        if(casesEtatsValides.get(etat) != null && casesEtatsValides.get(etat).get(coup) != null){
            return casesEtatsValides.get(etat).get(coup);
        }

        // Sauvegarde et modification
        Map<Case, Etat> caseEtat = new HashMap<>();
        Etat restoreState = new EtatNormal(envCopy);
        Coup coupCopy = coup.copy();
        coupCopy.probaSucces = 1;

        envCopy.setEtat(etat);
        Personnage persoCopy = envCopy.getOperateurActif();
        List<Case> casesValides = new ArrayList<>(coup.getCasesValides(envCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY())));

        // Simulation
        for(Case c : casesValides) {
            envCopy.setEtat(etat);
            coupCopy.effectuer(envCopy, envCopy.getOperateurActif(), c);
            caseEtat.put(c, new EtatNormal(envCopy));
        }
        envCopy.setEtat(restoreState);

        // Sauvegarde
        if(casesEtatsValides.get(etat) == null){
            Map<Coup, Map<Case, Etat>> map = new HashMap<>();
            map.put(coup, caseEtat);
            casesEtatsValides.put(etat, map);
        }
        else{
            casesEtatsValides.get(etat).put(coup, caseEtat);
        }
        return caseEtat;
    }

    /**
     * Calcule l'état après le coup effectué
     * @param etatDepart L'état de départ
     * @param coup Le coup à effectuer
     * @param perso Le personnage effectuant le coup
     * @param direction La direction dans lequel le coup est effectué
     * @param succes Vrai si le coup réussi, faux sinon
     * @return L'état d'arrivée
     */
    private Etat simuler(Etat etatDepart, Coup coup, Personnage perso, Direction direction, boolean succes){
        // Copie profonde de l'environnement dans l'etat de depart
        Coup coupCopy = coup.copy();
        Etat restoreState = creerEtat(envCopy);
        envCopy.setEtat(etatDepart);
        Personnage persoCopy = envCopy.getPersonnage(perso);
        coupCopy.probaSucces = succes ? 1 : 0;

        // Simulation
        switch (direction){
            case HAUT -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY() - 1));
            case BAS -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY() + 1));
            case GAUCHE -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX() - 1, persoCopy.getY()));
            case DROITE -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX() + 1, persoCopy.getY()));
            case AUCUN -> coupCopy.effectuer(envCopy, persoCopy, AucuneCase.instance);
        }

        Etat etat = creerEtat(envCopy);
        envCopy.setEtat(restoreState);

        return etat;
    }

    /**
     * Crée l'état correspondant en fonction de si la mission est finie et si elle est une réussite ou non
     * @param env L'environnement
     * @return L'état de l'environnement
     */
    public Etat creerEtat(Environnement env){
        if(env.isMissionFinie()){
            if(env.isEchec()){
                return new EtatEchec(env);
            }
            return new EtatReussite(env);
        }
        else{
            return new EtatNormal(env);
        }
    }

    /**
     * Crée l'état correspondant.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param nbPaOps La liste des quantités de points d'action de chaque opérateur
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     * @return L'état avec le type correpondant (normal, échec ou réussite)
     */
    public Etat creerEtat(int[] indCaseOperateurs, int[] nbPaOps, boolean[] aObjectif, int[] indCaseTerroristes, int menace){
        // Un des opérateurs morts ?
        for(int indCase : indCaseOperateurs){
            if(indCase == -1){
                return new EtatEchec(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
            }
        }

        // Tous les objectifs récupérés ?
        boolean tousObjRecup = true;
        for(boolean opAObj : aObjectif){
            if(!opAObj){
                tousObjRecup = false;
                break;
            }
        }

        if(tousObjRecup) {
            // ET tous les opérateurs sur la ligne du bas ?
            for (int indCase : indCaseOperateurs) {
                if (indCase < (env.getHauteur() - 1) * env.getLargeur()) {
                    return new EtatNormal(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
                }
            }
            return new EtatReussite(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
        }
        return new EtatNormal(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
    }

    /**
     * Calcule les états et leur distribution après que les ennemis ont effectué leurs actions
     * @param distribution La distribution des états du tour joueur
     * @return La distribution à la fin du tour ennemi
     */
    private Map<Etat, Double> transitionEnnemis(Map<Etat, Double> distribution){
        Map<Etat, Double> distributionEnnemis = new HashMap<>();
        int nbTerrs = env.getEnnemis().size();
        Coup[] listeCoups = {env.getEnnemis().get(0).getDeplacement(), env.getEnnemis().get(0).getTir()};

        Etat restoreState = creerEtat(envCopy);
        for(Etat e : distribution.keySet()) {
            if(e.estTerminal() || Arrays.stream(e.nbPAOperateurs).sum() != 0){    // Etat terminal ou pas le tour de l'ennemi donc rien à faire
                distributionEnnemis.put(e, distribution.get(e));
                continue;
            }
            for (int idSuiteCoups = 0; idSuiteCoups < Math.pow(listeCoups.length, nbTerrs); idSuiteCoups++) {
                // Suite de coups
                List<Coup> suiteCoup = new ArrayList<>(e.menace);
                double proba = 1;
                for (int i = 0; i < e.menace; i++) {
                    Coup c = listeCoups[((int) (idSuiteCoups / Math.pow(listeCoups.length, i))) % listeCoups.length];
                    suiteCoup.add(c);
                    proba *= envCopy.getProbaCoupEnnemi(c);
                }

                // Simulation
                envCopy.setEtat(e);
                envCopy.effectuerCoupsTerroristes(suiteCoup);

                Etat etatSimu = creerEtat(envCopy);
                etatSimu.nbPAOperateurs[0] = env.getOperateurActif().getMaxPointsAction();
                distributionEnnemis.put(etatSimu, proba * distribution.get(e));
                envCopy.setEtat(restoreState);
            }
        }

        return distributionEnnemis;
    }


    /*  Fonctions ajoutée pour RTDP */
    @Override
    public List<Action> getActionsEtat(Etat e) {
        Operateur op = env.getOperateurActif();
        Coup[] listeCoups = {op.getDeplacement(), op.getTir(), op.getFinTour()};

        // Nombre maximum de coups par opérateur
        int maxNbCoups = 0;
        for (Coup c : listeCoups) {
            maxNbCoups = Math.max(maxNbCoups, op.getPointsAction() / c.cout);
        }

        Set<List<Coup>> suiteCoupsPossibles = getAllSuitesCoupsPossibleOperateur(listeCoups, maxNbCoups, op);
        ArrayList<Action> actionPossibles = new ArrayList<>();

        envCopy.setEtat(e);
        List<Action> actions = new ArrayList<>();
        for (List<Coup> suiteCoup : suiteCoupsPossibles) {   // Parcours de toutes les suites de coups

            // Liste des couches de cases. La couche i est composée de toutes les cases accessibles en partant du
            // premier état de la couche i-1 et en effectuant le ie coup de la suite de coups sur toutes les cases valides.
            List<List<Case>> casesCouches = new ArrayList<>();
            casesCouches.add(new ArrayList<>());
            casesCouches.get(0).add(envCopy.getCase(envCopy.getOperateurActif().getX(), envCopy.getOperateurActif().getY()));

            // Liste des couches d'états. La couche i est composée de tous les états accessibles en partant du
            // premier état de la couche i-1 et en effectuant le ie coup de la suite de coups sur toutes les cases valides.
            List<List<Etat>> etatsCouches = new ArrayList<>();
            etatsCouches.add(new ArrayList<>());
            etatsCouches.get(0).add(new EtatNormal(envCopy));

            while (!casesCouches.isEmpty()) {
                while (!casesCouches.isEmpty() && casesCouches.size() < suiteCoup.size() + 1) {     // création des nouvelles couches
                    int indiceCouche = etatsCouches.size() - 1;
                    Map<Case, Etat> coucheTemp = getCasesValidesEtEtatsOperateur(etatsCouches.get(indiceCouche).get(0), suiteCoup.get(indiceCouche));

                    // Transformation de la map en listes
                    List<Case> casesTemp = new ArrayList<>(coucheTemp.size());
                    List<Etat> etatsTemp = new ArrayList<>(coucheTemp.size());
                    for (Case c : new ArrayList<>(coucheTemp.keySet())) {
                        casesTemp.add(c.copy());
                        etatsTemp.add(coucheTemp.get(c));
                    }
                    casesCouches.add(casesTemp);
                    etatsCouches.add(etatsTemp);

                    // Si la derniere couche est vide ici, alors le coup n'as pas de cases valides, donc il faut
                    // supprimer le noeud
                    while (!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()) {
                        casesCouches.remove(casesCouches.size() - 1);
                        etatsCouches.remove(etatsCouches.size() - 1);

                        if (!casesCouches.isEmpty()) {
                            casesCouches.get(casesCouches.size() - 1).remove(0);
                            etatsCouches.get(etatsCouches.size() - 1).remove(0);
                        }
                    }
                }
                if (casesCouches.isEmpty()) {
                    break;
                }

                List<Integer> directions = new ArrayList<>();
                for (int indCouche = 1; indCouche < casesCouches.size(); indCouche++) {
                    Case ancienneCase = casesCouches.get(indCouche - 1).get(0);
                    Case nouvelleCase = casesCouches.get(indCouche).get(0);

                    // Ajout de la direction
                    if (nouvelleCase.x == -1 || nouvelleCase.y == -1) {
                        directions.add(Action.AUCUN);
                    } else if (nouvelleCase.x > ancienneCase.x) {
                        directions.add(Action.DROITE);
                    } else if (nouvelleCase.x < ancienneCase.x) {
                        directions.add(Action.GAUCHE);
                    } else if (nouvelleCase.y > ancienneCase.y) {
                        directions.add(Action.BAS);
                    } else {
                        directions.add(Action.HAUT);
                    }
                }
                actions.add(new Action(env.getOperateurActif(), suiteCoup, directions));

                // Suppression de la premiere case de la derniere couche
                casesCouches.get(casesCouches.size() - 1).remove(0);
                etatsCouches.get(etatsCouches.size() - 1).remove(0);

                // Suppression des couches vides et des premiers noeuds précédent une couche vide
                while (!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()) {
                    casesCouches.remove(casesCouches.size() - 1);
                    etatsCouches.remove(etatsCouches.size() - 1);

                    if (!casesCouches.isEmpty()) {
                        casesCouches.get(casesCouches.size() - 1).remove(0);
                        etatsCouches.get(etatsCouches.size() - 1).remove(0);
                    }
                }
            }
        }
        //actionPossibles.put(e, actions.toArray(new Action[0]));

        // Sauvegarde
        //actionsEtat = actionPossibles;
        return actions;
    }

    @Override
    public Double recompenseRTDP(Etat s, Action a, Etat sPrime) {
        Pair actionEtat = this.politique.P(this,s,sPrime,a);
        //System.out.println("Clé: " + actionEtat.getKey() + ", Valeur: " + actionEtat.getValue());
        return (double) actionEtat.getValue();
    }

    /**
     * Parmis toutes les actions possibles, on choisi celle qui nous rapproche le plus de l'objectif, sans nous tuer
     * @param e ETAT
     * @return l'action gloutonne
     */
    @Override
    public Action getActionGloutonne(Etat e) {
        ArrayList<Action> actions = (ArrayList<Action>) getActionsEtat(e);
        Action bestAction = politique.P(this,e);

        return bestAction;
    }
    @Override
    public Etat etatSuivant(Etat s, Action a) {
        Etat suivant = simulerAction(a,s);
        while(suivant.estEchec()){
            suivant = simulerAction(a,suivant);
        }
        System.out.println(a+" : "+s+" et "+suivant);
        return suivant;
    }

    /**
     * Retourne l'état d'arrivé à partir d'une action et d'un état de départ en simulant l'action
     * @param action Action
     * @param etatDepart Etat
     * @return Etat d'arrivé
     */
    public Etat simulerAction(Action action,Etat etatDepart){
        Etat etatArrivee = etatDepart;
        for(int i = 0; i < action.coups().size(); i++){
            etatArrivee = simuler(etatArrivee, action.coups().get(i), action.personnage(), action.directions().get(i), action.coups().get(i).probaSucces == 1);
        }
        return etatArrivee;
    }

}
