package ninja.joshdavis;

public class TestCrawl
{
    public static void main(String[] args)
    {
        System.out.println("Starting TestCrawl.");
        String[] initial_queue = {"http://bfttyfgfhjfghjgjkghg.com/"};
        Crawler crawlBaby = new Crawler(initial_queue);
        crawlBaby.crawl();
    }
}
