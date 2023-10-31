package app.entities;

public class User {

    private final int userID;
    private final String email;
    private final String password;
    private final int balance;
    private final boolean admin;

    /**
     *
     * @param userID
     *  ID
     * @param email
     *  login
     * @param password
     *  private password
     * @param balance
     *  balance
     * @param admin
     *  are you an Admin?
     */

    public User(int userID, String email, String password, int balance, boolean admin) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.admin = admin;
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
