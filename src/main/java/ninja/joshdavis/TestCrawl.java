package ninja.joshdavis;

public class TestCrawl
{
    public static void main(String[] args)
    {
        System.out.println("Starting TestCrawl.");
        String[] initial_queue = {
            "http://www.google.com/",
            "http://www.bfttyfgfhjfghjgjkghg.com/",
            "http://www.cnn.com/",
            "http://www.wikipedia.org/wiki/Barack_Obama",
            "http://www.arxiv.org",
            "http://www.amazon.com",
            "http://www.hockeybuzz.com"
        };
        
        Crawler crawlBaby = new Crawler(initial_queue);
        crawlBaby.crawl();
    }
}
