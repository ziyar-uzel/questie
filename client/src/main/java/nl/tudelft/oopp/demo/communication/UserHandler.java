package nl.tudelft.oopp.demo.communication;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import nl.tudelft.oopp.demo.data.User;

public class UserHandler {

    /**
     Uses the user root Preferences to reconstruct the currentUser
     from the stored credentials (userId, authToken).

     @return the current user
     */
    static User getCurrentUser() {
        User currentUser = new User();
        currentUser.setId(Preferences.userRoot().get("userId", null));
        currentUser.setAuthToken(Preferences.userRoot().get("authToken", null));
        return currentUser;
    }

    /**
     * Parses the cookies that the HttpResponse requests to set, and
     * sets/updates the user root Preferences accordingly.
     *
     @param response the HttpResponse of the backend server
     */
    static void setCurrentUserFromResponse(HttpResponse<String> response) {
        List<String> cookies = response.headers().allValues("Set-Cookie");

        for (String cookie : cookies) {
            // cookie eg. "userId=1;Path =/;"
            System.out.println("Seting from cookie" + cookie);
            String[] cookieSplit = cookie.split(";")[0].split("=");
            System.out.println(Arrays.toString(cookieSplit));
            Preferences.userRoot().put(cookieSplit[0], cookieSplit[1]);
        }
    }
}
