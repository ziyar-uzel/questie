package nl.tudelft.oopp.demo.data;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Objects;



public class User {
    private String id;
    private String authToken;
    private String username;

    public User() {
    }

    public Boolean exists() {
        return authToken != null && authToken.length() > 0;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Accepts a URI, and adds userId, authToken and username
     * of the User to it as HTTP params.
     * !FIXME currently url must not have preexisting http parameters, this will break
     * !FIXME unsafe, encrypt parameters before sending them
     *
     * @return the user `signed` URI
     */
    public URI sign(URI url) {
        // FIXME hardcoded implementation

        String username = this.getUsername();

        if (username == null) {
            username = "default";
        }

        String params = (String.format("userId=%s&authToken=%s&username=%s",
                this.getId(),
                this.getAuthToken(),
                username.replaceAll(" ","_")));

        try {
            return URI.create(url.toString() + "?" + params);
        } catch (Exception e) {
            return URI.create(url.toString());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Equals method for the User class.
     * @param o the object being compared
     * @return true if users are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getId() == user.getId()
                && Objects.equals(getAuthToken(), user.getAuthToken())
                && Objects.equals(getUsername(), user.getUsername());
    }
}
