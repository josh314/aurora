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

    public Crawler(String[] _queue) {
        this.queue = new HashSet<String>(_queue.length);
        for(int i=0; i < _queue.length; ++i) {
            queue.add(_queue[i]);
        }
        this.client = new AsyncHttpClient();
        this.processing = new HashSet<Future<Response>>(_queue.length);
    }
    public Crawler(HashSet<String> _queue) {
        queue = new HashSet<String>(_queue);
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

    private boolean notDone() {
        boolean res = true;
        if(this.queue.isEmpty()) {
            for(Future<Response> f: this.processing) {
                res &= f.isDone();
            }
        }
        return res;
    }
    
    public void crawl() {
        for(String url : this.queue) {
            this.request(url);
        }
        this.queue.clear();
        while(this.notDone()) {}
        this.client.close();
    }

    public void print()
    {
        System.out.println("Test Crawler class.");
    }
}
