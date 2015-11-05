package ninja.joshdavis;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import com.ning.http.client.*;

public class Crawler {
    private HashSet<String> queue;
    private AsyncHttpClient client;
    private HashSet<Future<Response>> processing;

    public Crawler(String[] _queue, int max_conn) {
        this.queue = new HashSet<String>(_queue.length);
        for(int i=0; i < _queue.length; ++i) {
            queue.add(_queue[i]);
        }

        this.client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setMaxConnections(max_conn).build());

        this.processing = new HashSet<Future<Response>>(_queue.length);
    }

    public Crawler(String[] _queue) {
        this(_queue, 30);
    }
    
    private void request(String url) {
        Future<Response> f = this.client.prepareGet(url).execute(new AsyncCompletionHandler<Response>(){ 
                @Override
                public Response onCompleted(Response response) throws Exception{
                    // Do something with the Response
                    // ...
                    System.out.println("Request completed: " + response.getUri());
                    return response;
                }
                
                @Override
                public void onThrowable(Throwable t){
                    // Something wrong happened.
                    System.out.println(t);
                }
            });
        //        this.queue.remove(url);
        this.processing.add(f);
    }

    private boolean notDone() {//Done when both queue and processing are empty 
        return !(this.queue.isEmpty() & this.processing.isEmpty());
    }
    
    public void crawl() {
        for(String url : this.queue) {
            this.request(url);
        }
        this.queue.clear();
        while(this.notDone()) {
            HashSet<Future<Response>> done = new HashSet<Future<Response>>();//TODO:Initial size?
            for(Future<Response> f: this.processing) {
                if(f.isDone())
                    done.add(f);
            }
            for(Future<Response> f: done) {
                this.processing.remove(f);
            }
        }
        this.client.close();        
    }

    public void print()
    {
        System.out.println("Test Crawler class.");
    }
}
