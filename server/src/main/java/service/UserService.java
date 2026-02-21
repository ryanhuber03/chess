package service;

import java.util.UUID;

public class UserService {
    private String username;
    private String password;
    private String email;
    private String authToken;
    private String message;
    private String games;
    private String gameID;
    private String playerColor;
    private String gameName;
    // private Game[] games;

    public UserResponse getUser () {
        return new UserResponse(this.username, this.authToken);
    }

    public void makeAuth () {
        authToken = UUID.randomUUID().toString();
    }

    public GameID makeGameID () {
        gameID = UUID.randomUUID().toString();
        return new GameID(gameID);
    }

    public String getUsername () {
        return username;
    }

    public boolean isPassword (String password) {
        if (password == null) {
            return false;
        }
        return password.equals(this.password);
    }

    public String getPassword () {
        return password;
    }

    public boolean isAuth (String auth) {
        if (authToken == null) {
            return false;
        }
        return auth.equals(authToken);
    }

    public void clearAuth () {
        authToken = null;
    }

    public record UserResponse (String username, String authToken) {}

    public record GameID (String gameID) {}

    // public record Games (Game[] games) {}
}