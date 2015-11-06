package ninja.joshdavis;

import java.util.Vector;
import java.util.Arrays;

public class TestCrawl
{
    public static void main(String[] args)
    {

        String[] initial_queue = {
            "http://www.google.com/",
            "http://www.cnn.com/",
            "http://www.wikipedia.org/wiki/Barack_Obama",
            "http://www.wikipedia.org/wiki/Mathematics",
            "http://www.wikipedia.org/wiki/Daredevil",
            "http://www.wikipedia.org/wiki/Star",
            "http://www.wikipedia.org/wiki/Java",
            "http://www.wikipedia.org/",
            "http://www.arxiv.org",
            "http://www.amazon.com",
            "http://www.hockeybuzz.com"
        };
        
        System.out.println("Starting test crawl.");
        Vector<String> urls = new Vector<String>(Arrays.asList(initial_queue));
        Crawler crawlBaby = new Crawler(urls);
        crawlBaby.crawl();
    }
}
