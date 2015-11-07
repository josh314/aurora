package ninja.joshdavis;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ning.http.client.*;
import com.ning.http.client.extra.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Crawler {
    private AsyncHttpClient client;
    private ConcurrentLinkedQueue<String> queue;
    private HashSet<String> seen;
    private HashSet<Future<Response>> processing;
    
    class ThrottledHandler extends AsyncCompletionHandler<Response>{
        String url;
        public ThrottledHandler(String _url) {
            url = _url;   
        }
        @Override
        public Response onCompleted(Response response) throws Exception{
            String html = response.getResponseBody();
            Document doc = Jsoup.parse(html, url);
            Elements links = doc.select("a[href]");
            for(Element link: links) {
                String link_url = link.attr("abs:href");
                if(link_url != null) {
                    enqueue_request(link_url);
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
    
    public Crawler(Collection<String> urls) {
        //Create the client
        AsyncHttpClientConfig.Builder b = new AsyncHttpClientConfig.Builder().addRequestFilter(new ThrottleRequestFilter(100));
        client = new AsyncHttpClient(b.build());

        //Init book-keeping data structures
        queue = new ConcurrentLinkedQueue<String>();
        seen = new HashSet<String>(urls.size());
        processing = new HashSet<Future<Response>>(urls.size());

        //Fill the to-be-scheduled queue with initial inputs
        for(String url: urls) {
            enqueue_request(url);
        }
    }

    private void enqueue_request(String url) {
        if(!seen.contains(url)) {
            seen.add(url);
            queue.add(url);
        }
    }
    
    private void request(String url) {
        System.out.println("Requesting: "+url);
        Future<Response> f = this.client.prepareGet(url).execute(new ThrottledHandler(url));
        processing.add(f);
    }

    private boolean notDone() {//Done when both queue and processing are empty 
        return !(queue.isEmpty() & processing.isEmpty());
    }

    private void schedule_next_request() {
        String url = queue.poll();
        if(url != null) {
            request(url);
        }
    }

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
        }
        client.close();        
    }

    public void print_status()
    {
        System.out.println("In queue: " + queue.size());
        System.out.println("In process: " + processing.size());
        System.out.println("Have seen: " + seen.size());
    }
}
