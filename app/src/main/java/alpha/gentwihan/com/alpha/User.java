package alpha.gentwihan.com.alpha;

/**
 * Created by se780 on 2017-08-05.
 */

public class User {
    private String username;
    private String first_name;
    private String token;
    private String id;

    public User(String username, String first_name) {
        this.username = username;
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
