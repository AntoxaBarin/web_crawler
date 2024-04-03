package org.crawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {
    public static void main(String[] args) {
        String url = "https://en.cppreference.com/w/cpp/language/functions";
        crawl(1, url, new ArrayList<String>());
    }

    private static final int Max_depth = 5;
    private static void crawl(int level, String url, ArrayList<String> visited_pages) {
        if (level <= Max_depth) {
            Document document = request(url, visited_pages);

            if (document != null) {
                for (Element link : document.select("a[href]")) {
                    String next_url = link.absUrl("href");
                    if (!visited_pages.contains(next_url)) {
                        crawl(level + 1, next_url, visited_pages);
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> visited) {
        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            if (connection.response().statusCode() == 200) {
                System.out.println("Link: " + url);
                System.out.println(document.title());
                visited.add(url);

                return document;
            }
            return null;
        }
        catch (IOException e) {
            return null;
        }
    }
}