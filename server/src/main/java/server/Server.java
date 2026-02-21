package server;

import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.json.JavalinGson;

import service.UserService;
import errorException.ErrorException;

import java.util.HashSet;

public class Server {

    private HashSet<UserService> users = new HashSet<UserService>();

    private final Javalin javalin;

    public Server() {
        Gson gson = new Gson();
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        // Register your endpoints and exception handlers here.

        // register new user
        javalin.post("/user", (ctx) -> {
            // input is {username, password, email}
            // returns 200, {username, authToken}

            String jsonString = ctx.body();
            UserService test = ctx.bodyAsClass(UserService.class);

            // verify username isn't already taken
            String currentUser = test.getUsername();
            for (UserService user : users) {
                if (user.getUsername().equals(currentUser)) {
                    // throw 403 error
                    ctx.status(403);
                    ctx.json(new ErrorException().error403());
                    return;
                }
            }
            test.makeAuth();
            ctx.json(test.getUser());
            ctx.status(200);
            users.add(test);
        });

        // login user
        javalin.post("/session", (ctx) -> {
            // input is {username, password}
            // returns 200, {username, authToken}

            String jsonString = ctx.body();
            UserService test = ctx.bodyAsClass(UserService.class);

            // verify username exists
            String currentUser = test.getUsername();
            for (UserService user : users) {
                if (user.getUsername().equals(currentUser)) {
                    // username exists, now check password.
                    if (user.isPassword(test.getPassword())) {
                        // correct password, return success
                        user.makeAuth();
                        ctx.json(user.getUser());
                        ctx.status(200);
                        return;
                    }
                }
            }

            // username or password is wrong, return unauthorized
            ctx.status(401);
            ctx.json(new ErrorException().error401());
        });

        // log user out
        javalin.delete("/session", (ctx) -> {
            // input is authToken
            // returns 200, {}
            String authToken = ctx.header("Authorization");

            // check for matching auth token, if exists clear it and return 200
            for (UserService user : users) {
                if (user.isAuth(authToken)) {
                    user.clearAuth();
                    ctx.result("{}");
                    ctx.status(200);
                    return;
                }
            }

            // auth token doesn't exist, return 401
            ctx.json(new ErrorException().error401());
            ctx.status(401);
        });

        // list games
        javalin.get("/game", (ctx) -> {
            // input is authToken
            // returns 200, {"games": [ // list of games]}

            String jsonString = ctx.body();
            UserService test = ctx.bodyAsClass(UserService.class);
            // get games somehow
            // return games
            ctx.result("{}");
            ctx.status(200);
        });

        // create game
        javalin.post("/game", (ctx) -> {
            // input is authToken, {gameName}
            // returns 200, {gameID}

            String jsonString = ctx.body();
            UserService test = ctx.bodyAsClass(UserService.class);
            ctx.json(test.makeGameID());
            ctx.status(200);
        });

        // join game
        javalin.put("/game", (ctx) -> {
            // input is authToken, {playerColor, gameID}
            // returns 200, {}

            String jsonString = ctx.body();
            UserService test = ctx.bodyAsClass(UserService.class);
            ctx.result("{}");
            ctx.status(200);
        });

        // clear database
        javalin.delete("/db", (ctx) -> {
            // Empty database

            // if successful, return 200, {}
            // if unsuccessful, return 500 and { "message": "Error: Database can't be deleted" }
            ctx.result("{}");
            ctx.status(200);
        });
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
