package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import static hexlet.code.repository.UrlRepository.save;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UrlsController {

    public static void addUrl(Context ctx) throws SQLException {
        String urlName  = ctx.formParam("url");
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
    }

    public static void listOfUrls(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        ctx.attribute("urls", urls);
        ctx.render("urls/urls.html");
    }

    public static void showUrl(Context ctx) throws SQLException {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        Url url = UrlRepository.find(id);

        if (url == null) {
            throw new NotFoundResponse();
        }

        List<UrlCheck> urlChecks = UrlCheckRepository.getEntitiesByUrlId(id);
        ctx.attribute("url", url);
        ctx.attribute("checks", urlChecks);
        ctx.render("urls/show.html");
    }

    public static void checks(Context ctx) throws SQLException {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);
        Url url = UrlRepository.find(id);
        if (url == null) {
            throw new NotFoundResponse();
        }
        UrlCheckRepository.save(getCheck(url));
        ctx.attribute("url", url);
        ctx.redirect("/urls/" + id);
    }

    public static UrlCheck getCheck(Url url) {
        String checkedUrlName = url.getName();

        HttpResponse<String> urlResponse = Unirest
                .get(checkedUrlName)
                .asString();

        int urlStatusCode = urlResponse.getStatus();

        Document urlDoc = Jsoup.parse(urlResponse.getBody());
        String urlH1Value = "";

        if (urlDoc.select("h1").first() != null) {
            urlH1Value = urlDoc.select("h1").first().text();
        }

        String urlTitle = "";

        if (urlDoc.select("title").first() != null) {
            urlTitle = urlDoc.select("title").first().text();
        }

        String urlDescription = "";

        if (!urlDoc.select("meta[name=description]").isEmpty()) {
            urlDescription = urlDoc.select("meta[name=description]")
                    .get(0)
                    .attr("content");
        }
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        return new UrlCheck(
                urlStatusCode,
                urlTitle,
                urlH1Value,
                urlDescription,
                (int) url.getId(),
                createdAt);
    }
}
