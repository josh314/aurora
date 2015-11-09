package ninja.joshdavis;

import java.util.*;
import java.io.*;

public class TestCrawl {        
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
    public static void main(String[] args) {
        Vector<String> urls = new Vector<String>();
        
        for(String file: args) {
            add_urls_from_file(urls, file);
        }
        
        System.out.println("Starting test crawl.");
        Crawler crawlBaby = new Crawler(urls, new DummyScraper());
        crawlBaby.crawl();
    }
}
