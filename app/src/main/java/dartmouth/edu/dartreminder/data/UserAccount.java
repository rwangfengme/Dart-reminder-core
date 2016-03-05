package dartmouth.edu.dartreminder.data;

/**
 * Created by gejing on 3/2/16.
 */
public class UserAccount {
    private Long userId;
    private String username;
    private String password;

    public UserAccount(long userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }
}