package com.company;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dima on 19.06.2016.
 */
enum Subject {
    proekti_info_system_pt1("http://elearn.sde.ru/mod/quiz/review.php?attempt=11110720"),
    proekti_info_system_pt2("http://elearn.sde.ru/mod/quiz/review.php?attempt=11111050"),
    proektny_praktikum("http://elearn.sde.ru/mod/quiz/review.php?q=142401&attempt=11110846"),
    teoriya_ekonom_pt1("http://elearn.sde.ru/mod/quiz/review.php?attempt=11111299"),
    teoriya_ekonom_pt2("http://elearn.sde.ru/mod/quiz/review.php?attempt=11110573"),
    teoriya_ekonom_pt3("http://elearn.sde.ru/mod/quiz/review.php?attempt=11111235");
    private String url;

    private Subject(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

public class SecondSelect {
    public static void main(String[] args) throws IOException {
        String login = "s267027";
        String password = "Fk247f3mbAu5";
        Subject s = Subject.teoriya_ekonom_pt1;
//        String sec = "http://elearn.sde.ru/mod/quiz/attempt.php?id=477989"; //практика
        Connection.Response res = Jsoup.connect("http://elearn.sde.ru/login/index.php")
                .data("username", login, "password", password)
                .method(Connection.Method.POST)
                .execute();
        Map<String, String> loginCookies = res.cookies();

        //System.out.println(doc);
        Document doc2 = Jsoup.connect(s.getUrl())
                .cookies(loginCookies)
                .get();
        Elements elements = doc2.select("div.content");
        Map<String, Map<String, String>> m = new HashMap<>();
        for (Element el : elements) {
            Map<String, String> answersMap = new HashMap<>();
            String question = el.select(".qtext").text();
            for (Element answers : el.select(".answer .text")) {
                String img = null;
                if (!answers.select("img").isEmpty()) {
                    img = answers.select("img").first().attr("alt").toLowerCase();
                }
                if (img == null) {
                    img = "хз";
                }
                answersMap.put(answers.text(), img);
            }
            m.put(question, answersMap);
        }
        final File f = new File(s.name() + ".html");

        Document doc = Jsoup.parse(f, null, "http://example.com/");
        for (Map.Entry<String, Map<String, String>> entry : m.entrySet()) {
            boolean hasAlready = false;
            for (Element item : doc.select("article")) {
                if (item.select("h3").text().contains(entry.getKey())) {
                    hasAlready = true;
                    for (Map.Entry<String, String> subentry : entry.getValue().entrySet()) {
                        if (subentry.getValue().contains("верно") || subentry.getValue().contains("неверно")) {
                            for (Element el : item.select("li")) {
                                String o = el.text().replaceAll("^\\D. ", "");
                                String b = subentry.getKey().replaceAll("^\\D. ", "");
                                if (o.contains(b)) {
                                    el.removeClass(el.className());
                                    el.addClass(subentry.getValue());
                                }
                            }
                        }
                    }
                }
            }
            if (!hasAlready) {
                Element afterarticle = doc.appendElement("article");
                afterarticle.appendElement("h3").text(entry.getKey());
                Element afterul = afterarticle.appendElement("ul");
                for (Map.Entry<String, String> subentry : entry.getValue().entrySet()) {
                    afterul.appendElement("li").addClass(subentry.getValue()).text(subentry.getKey());
                }
            }
        }

        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
    }
}
