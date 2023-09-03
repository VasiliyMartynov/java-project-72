package hexlet.code.controllers;

import io.javalin.http.Handler;
public class RootController {
    public static Handler welcome = ctx -> ctx.render("urls/index.html");

//    public static Handler getWelcome() {
//        return welcome;
//    }
}
