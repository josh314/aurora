package ninja.joshdavis;

import java.util.*;
import java.io.*;
import java.nio.file.Paths;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class TestCrawl
{
    static class DummyScraper implements Scraper {
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
        
    public static void add_urls_from_file(Vector<String> urls, String filename) {
        // Open file, read line by line and put contents into Vector
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                urls.add(line);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File:" + filename + " does not exist.");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args)
    {
        Vector<String> urls = new Vector<String>();
        
        for(String file: args) {
            add_urls_from_file(urls, file);
        }
        
        System.out.println("Starting test crawl.");
        Crawler crawlBaby = new Crawler(urls, new DummyScraper());
        crawlBaby.crawl();
    }
}
