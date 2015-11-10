package ninja.joshdavis;

import java.util.*;

public class ConstantScraper implements Scraper {
    public List<String> process(String html, String url) {
        return Arrays.asList("http://www.cnn.com","http://www.hockeybuzz.com");
    }
}
