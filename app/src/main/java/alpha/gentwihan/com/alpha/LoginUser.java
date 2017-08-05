package alpha.gentwihan.com.alpha;

/**
 * Created by se780 on 2017-08-06.
 */

public class LoginUser {
    private String username;
    private String first_name;

    public LoginUser(String username, String first_name) {
        this.username = username;
        this.first_name = first_name;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getUsername() {
        return username;
    }
}
