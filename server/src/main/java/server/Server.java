package server;

import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.json.JavalinGson;

import user.User;

public class Server {

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
            User test = ctx.bodyAsClass(User.class);
            test.makeAuth();
            ctx.json(test.getUser());
            ctx.status(200);
        });

        // login user
        javalin.post("/session", (ctx) -> {
            // input is {username, password}
            // returns 200, {username, authToken}

            String jsonString = ctx.body();
            User test = ctx.bodyAsClass(User.class);
            test.makeAuth();
            ctx.json(test.getUser());
            ctx.status(200);
        });

        // log user out
        javalin.delete("/session", (ctx) -> {
            // input is authToken
            // returns 200, {}

            ctx.result("{}");
            ctx.status(200);
        });

        // list games
        javalin.get("/game", (ctx) -> {
            // input is authToken
            // returns 200, {"games": [ // list of games]}

            String jsonString = ctx.body();
            User test = ctx.bodyAsClass(User.class);
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
            User test = ctx.bodyAsClass(User.class);
            ctx.json(test.makeGameID());
            ctx.status(200);
        });

        // join game
        javalin.put("/game", (ctx) -> {
            // input is authToken, {playerColor, gameID}
            // returns 200, {}

            String jsonString = ctx.body();
            User test = ctx.bodyAsClass(User.class);
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

    public String addName (String name) {
        return name;
    }
}
