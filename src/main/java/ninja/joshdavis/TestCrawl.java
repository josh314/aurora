package ninja.joshdavis;


import java.util.*;
import java.io.*;
import java.nio.file.Paths;



public class TestCrawl
{
    public static void main(String[] args)
    {

        Vector<String> urls = new Vector<String>();
        // Open file, read line by line and put contents into Vector
        String filename = "initial_urls.txt";
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
        
        System.out.println("Starting test crawl.");
        //Vector<String> urls = new Vector<String>(Arrays.asList(initial_queue));
        Crawler crawlBaby = new Crawler(urls);
        crawlBaby.crawl();
    }
}
