package ninja.joshdavis;

public class TestCrawl
{
    public static void main(String[] args)
    {
        System.out.println("Starting TestCrawl.");
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
        
        Crawler crawlBaby = new Crawler(initial_queue);
        crawlBaby.crawl();
    }
}
