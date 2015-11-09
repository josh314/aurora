package ninja.joshdavis;

import java.util.*;

public class DummyScraper implements Scraper {
    public Vector<String> process(String html, String url) {
        return new Vector<String>();
    }
}
