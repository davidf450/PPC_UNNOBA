package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

public class User {
    private String username;
    private String password;

    public User(){}
    public User (String username, String password){
        this.username = username.toLowerCase();
        this.password = password.toLowerCase();
    }
//<editor-fold desc="Getters and setters">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int usernameLenght(){
        return username.length();
    }
//</editor-fold>
}
