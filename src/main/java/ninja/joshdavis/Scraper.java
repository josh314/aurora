package ninja.joshdavis;

public interface Scraper {
    /** 
        This method takes in two <code>String</code>s: an absolute <code>url</code> and the <code>html</code> obtained from crawling that page. It returns an Iterable of absolute URLs (as Strings) to be scheduled for future crawling. An empty Iterable no further URLs to crawl. Returning a  ull reference will throw an error.
        Note that hrefs appearing in <code>html</code> may be relative and have to be resolved using <code>url</code>.
     */
    Iterable<String> process(String html, String url);
}
