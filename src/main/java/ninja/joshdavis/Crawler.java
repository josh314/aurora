package ninja.joshdavis;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import org.jsoup.*;

public class Crawler {
    private ArrayList<URL> queue;

    public Crawler(String[] _queue) {
        this.queue = new ArrayList<URL>(_queue.length);
        try {
            for(int i=0; i < _queue.length; ++i) {
                queue.add(new URL(_queue[i]));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
        }        
    }
    public Crawler(ArrayList<URL> _queue) {
        queue = _queue;
    }


    private void request(URL url) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } 
        catch (IOException e) {   
            System.out.println("Could not find url: " + url);
        }
    }
    
    public void crawl() {
        for(URL url : this.queue) {
            this.request(url);
        }
    }

    public void print()
    {
        System.out.println("Test Crawler class.");
    }
}
