package ninja.joshdavis;

import java.util.*;
import java.io.*;

public class DemoCrawl {        
    public static void main(String[] args) {
        Crawler crawlBaby = new Crawler(new DummyScraper());
        for(String filename: args) {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String url;
                while ((url = br.readLine()) != null) {
                    crawlBaby.addRequest(url);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File:" + filename + " does not exist.");
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println("Starting test crawl.");
        crawlBaby.crawl();
    }
}
