public class Main {
    private static final int HAUT = 0;
    private static final int BAS = 1;
    private static final int GAUCHE = 2;
    private static final int DROITE = 3;

    private static int tailleX = 4;
    private static int tailleY = 3;

    private static boolean[][] cases = {
            {true, true, true},
            {true, false, true},
            {true, true, true},
            {true, true, true}};

    private static double[][] vals = {
            {-0.04, -0.04, -0.04},
            {-0.04, 0, -0.04},
            {-0.04, -0.04, -0.04},
            {-0.04, -1, 1}};

    private static double[][] sol = null;

    public static void main(String[] args) {
        printActions(iterationValeur(0.99999, 0.0001));
    }

    public static double qValeur(int i, int j, int a, double[][] util){
        double sum = 0;
        switch (a){
            case GAUCHE:
                if(i-1 >= 0 && cases[i-1][j]){sum += 0.8 * util[i-1][j] + vals[i-1][j];} // Haut
                else{sum += 0.8 * util[i][j];}

                if(j-1 >= 0 && cases[i][j-1]){sum += 0.1 * util[i][j-1] + vals[i][j-1];} // Gauche
                else{sum += 0.1 * util[i][j];}

                if(j+1 < tailleY && cases[i][j+1]){sum += 0.1 * util[i][j+1] + vals[i][j+1];} // Droite
                else{sum += 0.1 * util[i][j];}
                break;

            case DROITE:
                if(i+1 < tailleX && cases[i+1][j]){sum += 0.8 * util[i+1][j] + vals[i+1][j];} // Bas
                else{sum += 0.8 * util[i][j] + vals[i][j];}

                if(j-1 >= 0 && cases[i][j-1]){sum += 0.1 * util[i][j-1] + vals[i][j-1];} // Gauche
                else{sum += 0.1 * util[i][j] + vals[i][j];}

                if(j+1 < tailleY && cases[i][j+1]){sum += 0.1 * util[i][j+1] + vals[i][j+1];} // Droite
                else{sum += 0.1 * util[i][j] + vals[i][j];}
                break;

            case BAS:
                if(j-1 >= 0 && cases[i][j-1]){sum += 0.8 * util[i][j-1] + vals[i][j-1];} // Gauche
                else{sum += 0.8 * util[i][j] + vals[i][j];}

                if(i-1 >= 0 && cases[i-1][j]){sum += 0.1 * util[i-1][j] + vals[i-1][j];} // Haut
                else{sum += 0.1 * util[i][j] + vals[i][j];}

                if(i+1 < tailleX && cases[i+1][j]){sum += 0.1 * util[i+1][j] + vals[i+1][j];} // Bas
                else{sum += 0.1 * util[i][j] + vals[i][j];}
                break;

            case HAUT:
                if(j+1 < tailleY && cases[i][j+1]){sum += 0.8 * util[i][j+1] + vals[i][j+1];} // Droite
                else{sum += 0.8 * util[i][j] + vals[i][j];}

                if(i-1 >= 0 && cases[i-1][j]){sum += 0.1 * util[i-1][j] + vals[i-1][j];} // Haut
                else{sum += 0.1 * util[i][j] + vals[i][j];}

                if(i+1 < tailleX && cases[i+1][j]){sum += 0.1 * util[i+1][j] + vals[i+1][j];} // Bas
                else{sum += 0.1 * util[i][j] + vals[i][j];}
                break;
        }
        return sum;
    }

    public static int[][] iterationValeur(double gamma, double epsilon){
        double[][] util = vals.clone();
        int[][] actionsSol = new int[tailleX][tailleY];

        // Initialisation de la sol et des actions
        for(int x = 0; x < tailleX; x++) {
            for (int y = 0; y < tailleY; y++) {
                actionsSol[x][y] = 0;
            }
        }

        double delta;
        do {
            delta = 0;
            double[][] utilClone = new double[tailleX][tailleY];

            for(int x = 0; x < tailleX; x++) {
                for (int y = 0; y < tailleY; y++) {
                    double max = 0;
                    int maxAction = 0;

                    for(int action = 0; action < 4; action ++){
                        double qval = qValeur(x, y, action, util) * gamma;

                        if(qval > max){
                            max = qval;
                            maxAction = action;
                        }
                    }

                    utilClone[x][y] = max;
                    actionsSol[x][y] = maxAction;

                    delta = Math.max(delta, Math.abs(utilClone[x][y] - util[x][y]));
                }
            }

            util = utilClone;

        }
        while(delta > epsilon * (1-gamma)/gamma);

        return actionsSol;
    }

    public static void printActions(int[][] actions){
        StringBuilder sb = new StringBuilder();
        for(int[] ligne : actions){
            for(int action: ligne){
                sb.append(intToActionString(action)).append(" | ");
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

    public static void printValeurs(double[][] valeurs){
        StringBuilder sb = new StringBuilder();
        for(double[] ligne : valeurs){
            for(double action: ligne){
                sb.append(action).append(" | ");
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

    public static String intToActionString(int action){
        return switch (action) {
            case HAUT -> ">";
            case BAS -> "<";
            case GAUCHE -> "^";
            case DROITE -> "v";
            default -> "Not an action";
        };
    }

}

/*
* recompenses additives devaluees : comment prendre en compte les gammas recompenses futures ? bfs ?
*/