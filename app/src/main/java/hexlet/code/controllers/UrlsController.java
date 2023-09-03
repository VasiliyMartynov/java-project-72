package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import static hexlet.code.repository.UrlRepository.save;

import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;

public class UrlsController {

    public static Handler addUrl = ctx -> {

        String urlName  = ctx.formParam("url");
        System.out.println("----------------");
        System.out.println(urlName);
        System.out.println("----------------");
        URL url;

        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");
            return;
        }

        String normalizedUrl = url.getProtocol() + "://" + url.getAuthority();

        Url urlFromDb = UrlRepository.findByName(normalizedUrl);

        if (urlFromDb != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "info");
            ctx.redirect("/urls");
            return;
        }
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Url urlForSave = new Url(normalizedUrl, createdAt);
        save(urlForSave);
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect("/urls");

    };

    public static Handler listOfUrls = ctx -> {

        List<Url> urls = UrlRepository.getEntities();
        ctx.attribute("urls", urls);
        ctx.render("urls/urls.html");
    };

    public static Handler showUrl = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        Url url = UrlRepository.find(id);

        if (url == null) {
            throw new NotFoundResponse();
        }

        List<UrlCheck> urlChecks = UrlCheckRepository.getEntitiesByUrlId(id);
        ctx.attribute("url", url);
        ctx.attribute("checks", urlChecks);
        ctx.render("urls/show.html");

    };

    public static Handler checks = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        Url url = UrlRepository.find(id);

        if (url == null) {
            throw new NotFoundResponse();
        }

        UrlCheckRepository.save(getCheck(url));

        ctx.attribute("url", url);
        ctx.redirect("/urls/" + id);
    };

    public static UrlCheck getCheck(Url url) throws IOException {

        String checkedUrlName = url.getName();

        HttpResponse<String> urlResponse = Unirest.get(checkedUrlName).asString();
        int urlStatusCode = urlResponse.getStatus();

        Document urlDoc = Jsoup.connect(checkedUrlName).get();

        String urlH1Value = "";
        if (urlDoc.select("h1").first() != null) {
            urlH1Value = urlDoc.select("h1").first().text();
        }

        String urlTitle = "";
        if (urlDoc.select("title").first() != null) {
            urlH1Value = urlDoc.select("title").first().text();
        }

        String urlDescription = "";

        if (!urlDoc.select("meta[name=description]").isEmpty()) {
            urlDescription = urlDoc.select("meta[name=description]")
                    .get(0)
                    .attr("content");
        }

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        return new UrlCheck(urlStatusCode, urlTitle, urlH1Value, urlDescription, (int) url.getId(), createdAt);
    }

//    public static Handler getAddUrl() {
//        return addUrl;
//    }
//
//    public static Handler getListOfUrls() {
//        return addUrl;
//    }
//
//    public static Handler getShowUrl() {
//        return addUrl;
//    }
//
//    public static Handler getChecks() {
//        return addUrl;
//    }
}
