package ninja.joshdavis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import com.ning.http.client.*;

public class Crawler {
    private ArrayList<String> queue;
    private AsyncHttpClient client;

    public Crawler(String[] _queue) {
        this.queue = new ArrayList<String>(_queue.length);
        for(int i=0; i < _queue.length; ++i) {
            queue.add(_queue[i]);
        }
        this.client = new AsyncHttpClient();
    }
    public Crawler(ArrayList<String> _queue) {
        queue = _queue;//Copy it?
    }

    private void request(String url) {
        this.client.prepareGet(url).execute(new AsyncCompletionHandler<Response>(){ 
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
    }
    
    public void crawl() {
        for(String url : this.queue) {
            this.request(url);
        }
    }

    public void print()
    {
        System.out.println("Test Crawler class.");
    }
}
