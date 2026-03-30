import java.util.ArrayList;
import java.util.List;

public class GlobalStats {

    public int nb_posts = 0;

    public int total_mots = 0;
    public int total_chars = 0;
    public int total_apo = 0;

    //tableaux pour les statistiques
    private Stats[] tabStats;
    private int nbStats = 0;
    private int maxStats = 10;

    public String[] tabMots;
    public int[] frequenceMots;
    public int elemMots = 0;
    private int maxMots = 10;

    public GlobalStats(){
        tabStats = new Stats[maxStats];
        tabMots = new String[maxMots];
        frequenceMots = new int[maxMots];
    }

    public double getMoyenneMots(){
        return (double) total_mots / (double) nb_posts;
    }

    public double getMoyenneChars(){
        return (double) total_chars / (double) nb_posts;
    }

    public double getMoyenneApo(){
        return (double) total_apo / (double) nb_posts;
    }

    public void addStats(Stats st){
        if(nbStats< maxStats){
            tabStats[nbStats] = st;
        }else {
            maxStats = maxStats *2;
            Stats[] newTab = new Stats[maxStats];
            System.arraycopy(tabStats,0, newTab , 0, tabStats.length);
            tabStats = newTab;
            tabStats[nbStats] = st;
        }
        nbStats++;
        nb_posts++;
        total_mots += st.nb_mots;
        total_chars += st.nb_chars;
        total_apo += st.nb_apo;
    }

    public void addMots(String[] mots){
        boolean exist = false;
        for (String s: mots){
            for (int i = 0; i < elemMots; i++){
                if (s.equals(tabMots[i])){
                    exist = true;
                    frequenceMots[i]++;
                }
            }
            if (!exist){
                if(elemMots >= maxMots){
                    maxMots = maxMots*2;
                    String[] newTabMots = new String[maxMots];
                    int[] newTabFreq = new int[maxMots];
                    System.arraycopy(tabMots, 0, newTabMots, 0, tabMots.length);
                    System.arraycopy(frequenceMots, 0, newTabFreq, 0, frequenceMots.length);
                    tabMots = newTabMots;
                    frequenceMots = newTabFreq;

                }
                frequenceMots[elemMots] = 1;
                tabMots[elemMots++] = s;
            }
        }
    }

    public double getEcartTypeMots(){
        double moy = this.getMoyenneMots();
        double carreEcart = 0;
        for (int i = 0; i < nbStats; i++){
            Stats s = tabStats[i];
            double ecart = s.nb_mots - moy;
            carreEcart += ecart * ecart;
        }
        return Math.sqrt(carreEcart / (tabStats.length - 1) );
    }

    public double getFrequenceCapLock(){
        int count = 0;
        for (int i = 0 ; i < nbStats; i++){
            count += tabStats[i].nbMotsCapLock();
        }
        return (double) count / (double) nbStats;
    }

    public List<String> getListCapLock(){
        List<String> list = new ArrayList<String>();
        for (int i = 0 ; i < nbStats; i++){
            list.addAll(tabStats[i].listMotsCapLock());
        }
        return list;
    }

    public double getFrequencePonctuation(){
        int count = 0;
        for (int i = 0 ; i < nbStats; i++){
            count += tabStats[i].nbPonctuation();
        }
        return (double) count / (double) nbStats;
    }


}
