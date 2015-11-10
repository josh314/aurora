package ninja.joshdavis;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ning.http.client.*;
import com.ning.http.client.extra.*;

public class Crawler {
    private AsyncHttpClient client;
    private ConcurrentLinkedQueue<String> queue;
    private HashSet<String> seen;
    private HashSet<Future<Response>> processing;
    private Scraper scraper;

    //Class containing the callbacks for processing responses and errors
    class ThrottledHandler extends AsyncCompletionHandler<Response>{
        String url;
        
        public ThrottledHandler(String _url) {
            url = _url;   
        }
        @Override
        public Response onCompleted(Response response) throws Exception {
            String html = response.getResponseBody();
            Iterable<String> links = scraper.process(html, url);
            for(String link_url: links) {
                if(link_url != null) {
                    addRequest(link_url);
                }
            }
            System.out.println("Request completed: " + url);
            return response;
        }                
        @Override
        public void onThrowable(Throwable t){
            System.out.println("Failed: " + url);
            System.out.println(t);
        }
    }
    
    public Crawler(Scraper _scraper) {
        //Create http client
        AsyncHttpClientConfig.Builder b = new AsyncHttpClientConfig.Builder().addRequestFilter(new ThrottleRequestFilter(100));
        client = new AsyncHttpClient(b.build());
        //Set scraper
        scraper = _scraper;        
        //Init book-keeping data structures
        queue = new ConcurrentLinkedQueue<String>();
        seen = new HashSet<String>();
        processing = new HashSet<Future<Response>>();
    }

    /**
     * Add a page to the <code>Crawler</code>'s request queue. Ignores urls previously seen by the <code>Crawler</code>. 
     * @param url absolute URL to be added. 
     */
    public void addRequest(String url) {
        if(!seen.contains(url)) {
            seen.add(url);
            queue.add(url);
        }
    }
    
    private void request(String url) {
        System.out.println("Requesting: "+url);
        Future<Response> f = client.prepareGet(url).execute(new ThrottledHandler(url));
        processing.add(f);
    }

    //Done when both queue and processing are empty 
    private boolean notDone() {
        return !(queue.isEmpty() & processing.isEmpty());
    }

    private void schedule_next_request() {
        String url = queue.peek();
        if(url != null) {
            request(url);
        }
        queue.poll();
    }

    // Removes resolved futures from the in-process list
    private void cleanup_finished_tasks() {
        HashSet<Future<Response>> done = new HashSet<Future<Response>>();
        for(Future<Response> f: processing) {
            if(f.isDone())
                done.add(f);
        }
        for(Future<Response> f: done) {
            processing.remove(f);
        }
    }
    
    public void crawl() {
        while(notDone()) {
            schedule_next_request();
            cleanup_finished_tasks();
            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        client.close();        
    }
    /** Print the current crawler status. */
    public void print_status()
    {
        System.out.println("In queue: " + queue.size());
        System.out.println("In process: " + processing.size());
        System.out.println("Have seen: " + seen.size());
    }
}
