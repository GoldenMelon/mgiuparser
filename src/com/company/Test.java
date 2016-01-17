package com.company;

import com.sun.org.apache.xpath.internal.operations.Div;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Melon on 06.01.2016.
 */
public class Test {


    public static void main(String[] args) throws IOException {
        String login = "s267027";
        String password = "Fk247f3mbAu5";
        HashMap<String,LinkedList<String>> data = new HashMap<>();
        Connection.Response res = Jsoup.connect("http://elearn.sde.ru/login/index.php")
                .data("username", login, "password", password)
                .method(Connection.Method.POST)
                .execute();
        Document doc = res.parse();
        Map<String, String> loginCookies = res.cookies();

        //System.out.println(doc);
        Document doc2 = Jsoup.connect("http://elearn.sde.ru/mod/quiz/review.php?attempt=10528090")
                .cookies(loginCookies)
                .get();
        /**Elements el = doc2.getElementsByClass("qtext");
        String q[] = new String[el.size()];
        int counter = 1;
        for(Element e : el){
            String e1 = e.ownText();
            System.out.println(counter + " " + e1);
            q[counter-1]=e1;
            counter++;
        }

        Elements e2 = doc2.getElementsByClass("answer");
        counter = 1;
        for(Element e : e2){
            LinkedList<String> answers = new LinkedList<>();
            Elements td = e.getElementsByClass("text");
            for(Element t1 : td){
                //System.out.println(t1);
                //System.out.println(counter + " " + t1.text());
                String str = t1.text();
                answers.add(str);
            }
            data.put(q[counter-1],answers);
            System.out.println(answers);
            counter++;
        }
        //System.out.println(doc2);
        final File f = new File("filename.html");

        //System.out.println(answers.length);
        Iterator it = data.entrySet().iterator();
        Element e123 = new Document("");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Element afterarticle = e123.appendElement("article");
            afterarticle.appendElement("h3").text((String) pair.getKey());
            Element afterul = afterarticle.appendElement("ul");
            for(String s : (LinkedList<String>)pair.getValue()){
                afterul.appendElement("li").addClass("unknown").text(s);
            }

            //System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }*/
        System.out.println(doc2);
        FileUtils.writeStringToFile(new File("file01.html"),doc2.html(),"UTF-8");
    }
}
