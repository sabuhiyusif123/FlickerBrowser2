package project.sabuhi.com.flickerbrowser2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG="MainActivity";
    private List<Photo> mListPhotos= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) );


        ProcessPhotos processPhotos = new ProcessPhotos("android,lollipop",true);
        processPhotos.execute();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        GetRawData getRawData = new GetRawData("https://api.flickr.com/services/feeds/photos_public.gne?tags=Android,Lollipop&format=json&nojsoncallback=1");
        GetFlickrJsonData jsonData = new GetFlickrJsonData("Android,lollipop",true); //We need to override this method again in order to give the view to the Adapter to show the stuffs on the screen

        jsonData.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class ProcessPhotos extends GetFlickrJsonData{
        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);

        }
        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }
        public class ProcessData extends DownloadJsonData{
                 protected void onPostExecute(String webdata){
                     super.onPostExecute(webdata);
                     flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(getMPhotos(),MainActivity.this);
                     mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
                 }

        }

    }

}
