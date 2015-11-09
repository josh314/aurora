package ninja.joshdavis;

import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class ConstantScraper implements Scraper {
    public List<String> process(String html, String url) {
        return Arrays.asList("http://www.cnn.com","http://www.hockeybuzz.com");
    }
}
