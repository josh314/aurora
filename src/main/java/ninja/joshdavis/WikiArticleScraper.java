package ninja.joshdavis;

import java.util.Vector;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class WikiArticleScraper implements Scraper {
    // Constants and magic strings used in WP markup
    private static final String WP_DOMAIN = "https://en.wikipedia.org/wiki/";
    private static final String ARTICLE_CONTENT = "mw-content-text"; 
        
    public Vector<String> process(String html, String url) {
        Vector<String> urls = new Vector<String>();
        Document doc = Jsoup.parse(html, url);
        Element content = doc.getElementById("mw-content-text");
        Elements links = content.select("a[href]");
        for(Element link: links) {
            String link_url = link.attr("abs:href");
            if(link_url != null) {
                urls.add(link_url);
            }
        }
        return urls;
    }
}
