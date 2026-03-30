import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class analyse {

    static void main() throws IOException, DatabindException {

        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("ressource/old/dataset.posts&users.31.json");
        JsonNode parentNode = objectMapper.readTree(file);
        JsonNode n = parentNode.findValue("posts");

        //load Posts
        List<Post> posts = objectMapper.readValue(n.traverse(), new TypeReference<List<Post>>(){});

        //load list of bot
        File botFile = new File("ressource/old/dataset.bots.31.txt");
        BufferedReader br = new BufferedReader(new FileReader(botFile));
        String line;
        List<String> botName = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            botName.add(line);
        }


        List<User> userList = new ArrayList<User>();
        List<User> botList = new ArrayList<User>();

        GlobalStats usersStats = new GlobalStats();
        GlobalStats botsStats = new GlobalStats();

        for (Post p : posts){
            Stats stats = new Stats(p.text);

            boolean nouvUtil = true;
            boolean bot = false;
            for (String s : botName){
                if (s.equals(p.author_id)) {
                    bot = true;
                    break;
                }
            }

            if (bot){
                for (User u : botList){
                    if (u.id.equals(p.author_id)){
                        u.userStat.addStats(stats);
                        u.userStat.addMots(stats.tabMots);
                        nouvUtil = false;
                    }
                }
            }else {
                for (User u : userList){
                    if (u.id.equals(p.author_id)){
                        u.userStat.addStats(stats);
                        u.userStat.addMots(stats.tabMots);
                        nouvUtil = false;
                    }
                }
            }

            //Création nouvelle utilisateur
            if(nouvUtil){
                if(!bot){
                    User newUser = new User(p.author_id);
                    newUser.userStat.addStats(stats);
                    newUser.userStat.addMots(stats.tabMots);
                    userList.add(newUser);
                }else{
                    User newUser = new User(p.author_id);
                    newUser.userStat.addStats(stats);
                    newUser.userStat.addMots(stats.tabMots);
                    botList.add(newUser);
                }

            }

            if (bot){
                botsStats.addStats(stats);
                botsStats.addMots(stats.tabMots);
            }else {
                usersStats.addStats(stats);
                usersStats.addMots(stats.tabMots);
            }

        }

        /*
        System.out.println("Utilisateur : ");
        System.out.println("   moy posts:" + usersStats.nb_posts/userList.size());
        System.out.println("   ecart-type mots: " + usersStats.getEcartTypeMots());
        System.out.println("   fréquence cap lock : " + usersStats.getFrequenceCapLock());
        System.out.println("   fréquence ponctuation : " + usersStats.getFrequencePonctuation());


        System.out.println("bot : ");
        System.out.println("   moy posts:" + botsStats.nb_posts/botList.size());
        System.out.println("   ecart-type mots: " + botsStats.getEcartTypeMots());
        System.out.println("   fréquence cap lock : " + botsStats.getFrequenceCapLock());
        System.out.println("   fréquence ponctuation : " + botsStats.getFrequencePonctuation());
         */
        List<String> motUser = new ArrayList<String>();
        int[] nbUser = new int[2000];

        for (User u : userList){
            for (int i = 0; i< u.userStat.elemMots; i++){
                double f = (double) u.userStat.frequenceMots[i] / (double) u.userStat.nb_posts;
                if ( f > 0.1 && f < 0.9){
                    int index = 0;
                    boolean trouve = false;
                    for (String m : motUser){
                        if (m.equals(u.userStat.tabMots[i])){
                            nbUser[index]++;
                            trouve = true;
                        }
                        index++;

                    }
                    if(!trouve){
                        motUser.add(u.userStat.tabMots[i]);
                        nbUser[index] = 1;
                    }
                }
            }
        }

        int index = 0;
        for (String s: motUser){
            if (nbUser[index] > 3) {
                System.out.print(s);
                System.out.println("   " + nbUser[index]);
            }
            index++;
        }
    }
}
