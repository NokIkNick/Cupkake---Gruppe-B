package app.entities;

public class User {

    private int userID;
    private String email;
    private String password;
    private int balance;
    private boolean admin;

    /**
     *
     * @param userID
     * @param email
     * @param password
     * @param balance
     * @param admin
     */

    public User(int userID, String email, String password, int balance, boolean admin) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.admin = admin;
    }
    public User(){

    }
    public int getUserID() {
        return userID;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isAdmin() {
        return admin;
    }
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", admin=" + admin +
                '}';
    }
}
