package project.sabuhi.com.flickerbrowser2;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cavid on 8/17/2016.
 */

public class GetFlickrJsonData extends GetRawData{
    private String LOG_TAG=GetFlickrJsonData.class.getSimpleName();
    private Uri mDestinationUri;
    private List<Photo> mPhotos;

    public GetFlickrJsonData(String searchCriteria, boolean matchAll){
        super(null);
        createAndUptadeUri(searchCriteria,matchAll);
        mPhotos=new ArrayList<Photo>();
    }
    public void execute(){
        super.setmRawUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG,"Build UrI "+mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

    }

    public List<Photo> getMPhotos() {
        return mPhotos;
    }

    public boolean createAndUptadeUri(String searchCriteria, boolean matchAll){                 //Created the proper URL and pass it to mDestinationUri
        final String FLICKR_API_BASE_URL="https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM="tags";
        final String TAGMODE_PARAM="tagmode";
        final String FORMAT_PARAM="format";
        final String NO_JSON_CALLBACK_PARAM="nojsoncallback";

        mDestinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM,searchCriteria)
                .appendQueryParameter(TAGMODE_PARAM,matchAll ? "ALL":"ANY")
                .appendQueryParameter(FORMAT_PARAM,"json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM,"1")
                .build();

        return mDestinationUri != null;
    }
    public class DownloadJsonData extends DownloadRawData{

       public void processResult(){
           if(getmDownloadStatus()!= DownloadStatus.OK){
               return;
           }

           final String FLICKR_ITEMS="items";
           final String FLICKR_TITLE="title";
           final String FLICKR_LINK="link";
           final String FLICKR_MEDIA="media";
           final String FLICKR_M="m";
           final String FLICKR_AUTHOR="author";
           final String FLICKR_AUTHOR_ID="author_id";
           final String FLICKR_TAGS="tags";


           try{
               JSONObject jsonData = new JSONObject(getmData());                                //Creating jsonObject
               JSONArray itemsArray = jsonData.getJSONArray(FLICKR_ITEMS);                      //creating jsonArray
               for(int i=0;i<itemsArray.length();i++){                                          //loop through all the elements of Array
                   JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                   String title = jsonPhoto.getString(FLICKR_TITLE);
                   String tags= jsonPhoto.getString(FLICKR_TAGS);
                   String link = jsonPhoto.getString(FLICKR_LINK);
                   String author = jsonPhoto.getString(FLICKR_AUTHOR);
                   String author_id= jsonPhoto.getString(FLICKR_AUTHOR_ID);
                   JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                   String photoUrl = jsonMedia.getString(FLICKR_M);

                   Photo photoObject = new Photo(title,author,author_id,link,tags,photoUrl);
                   mPhotos.add(photoObject);
               }
           }catch (JSONException e){
               Log.e(LOG_TAG,"Error downloading raw data Json "+e.getMessage());
           }
           for(Photo singlePhoto:mPhotos){                              //loops all Photos and displays with the help of Log.v
               Log.v(LOG_TAG,"*****"+singlePhoto.toString());
           }
       }


        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] par={mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }
}
