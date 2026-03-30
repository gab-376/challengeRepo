public class User {
    public String id;
    public GlobalStats userStat;

    public User(String id){
        this.id = id;
        userStat = new GlobalStats();
    }
}
