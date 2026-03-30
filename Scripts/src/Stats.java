import java.util.ArrayList;
import java.util.List;

public class Stats {
    public int nb_mots;
    public int nb_chars;
    public int nb_apo;
    public String[] tabMots;

    public Stats(String text){
        this.nb_chars = text.length();
        this.tabMots = text.trim().split("\\s+");
        this.nb_mots = tabMots.length;
        this.nb_apo = countChar(text, '\'');

    }


    public int nbMotsCapLock(){
        int nb = 0;

        for(String s : tabMots){
            boolean cap = true;
            for(char c: s.toCharArray()){
                if (Character.isLowerCase(c)){
                    cap = false;
                    break;
                }
            }
            if (cap){
                nb++;
            }
        }
        return nb;
    }

    public List<String> listMotsCapLock(){
        List<String> list = new ArrayList<String>();

        for(String s : tabMots){
            boolean cap = true;
            for(char c: s.toCharArray()){
                if (!Character.isUpperCase(c)){
                    cap = false;
                    break;
                }
            }
            if (cap){
                list.add(s);
            }
        }
        return list;
    }



    public int nbPonctuation(){
        int nb = 0;
        for(String s: tabMots){
            if(s.matches("^[A-Z].*|\\.$|!$|\\?$|,$")){
                nb++;
            }
        }
        return nb;
    }

    public static int countChar(String str, char c){
        return (int) str.chars() // Gets an IntStream of character ASCII values
                .filter(ch -> ch == c) // Filters for the target character
                .count(); // Counts the filtered elements
    }



}
