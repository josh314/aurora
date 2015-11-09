package ninja.joshdavis;

import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class BasicCrawlScraper implements Scraper {
    public Vector<String> process(String html, String url) {
        Vector<String> urls = new Vector<String>();
        Document doc = Jsoup.parse(html, url);
        Elements links = doc.select("a[href]");
        for(Element link: links) {
            String link_url = link.attr("abs:href");
            if(link_url != null) {
                urls.add(link_url);
            }
        }
        return urls;
    }
}
