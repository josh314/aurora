package ninja.joshdavis;

import java.io.IOException;
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
    
    class ThrottledHandler extends AsyncCompletionHandler<Response>{
        @Override
        public Response onCompleted(Response response) throws Exception{
            // Do something with the Response
            // ...
            System.out.println("Request completed: " + response.getUri());
            return response;
        }
                
        @Override
        public void onThrowable(Throwable t){
            System.out.println(t);
        }
    }
    
    public Crawler(String[] urls) {
        //Create the client

        AsyncHttpClientConfig.Builder b = new AsyncHttpClientConfig.Builder().addRequestFilter(new ThrottleRequestFilter(100));
        client = new AsyncHttpClient(b.build());

        //Init book-keeping data structures
        queue = new ConcurrentLinkedQueue<String>();
        seen = new HashSet<String>(urls.length);
        processing = new HashSet<Future<Response>>(urls.length);

        //Fill the to-be-scheduled queue with initial inputs
        for(int i=0; i < urls.length; ++i) {
            enqueue_request(urls[i]);
        }
    }

    private void enqueue_request(String url) {
        if(!seen.contains(url)) {
            seen.add(url);
            queue.add(url);
        }
    }
    
    private void request(String url) {        
        Future<Response> f = this.client.prepareGet(url).execute(new ThrottledHandler());
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
        HashSet<Future<Response>> done = new HashSet<Future<Response>>();//TODO:Initial size?
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
            //print_status();
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
