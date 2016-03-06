package dartmouth.edu.dartreminder.backend.data;

public class UserAccount {
    public static final String DATASTORE_PARENT_ENTITY_NAME = "DatastoreParent";
    public static final String DATASTORE_PARENT_KEY_NAME = "DatastoreParent";

    public static final String USER_ENTRY_ENTITY_NAME = "UserAccountEntry";
    public static final String FIELD_NAME_USER_NAME = "user_name";
    public static final String FIELD_NAME_PASSWORD = "password";

    private String username;
    private String password;

    public UserAccount(String username, String password){
        this.username = username;
        this.password = password;
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