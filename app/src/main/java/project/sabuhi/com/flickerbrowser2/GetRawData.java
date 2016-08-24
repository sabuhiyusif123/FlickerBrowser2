package project.sabuhi.com.flickerbrowser2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by Cavid on 8/14/2016.
 */
enum DownloadStatus {IDLE,PROCESSING,NOT_INITIALISED,FAILED_OR_EMPTY,OK}
public class GetRawData {
    private String LOG_DATA=GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public void setmRawUrl(String mRawUrl) {        //
        this.mRawUrl = mRawUrl;
    }

    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }
    public void reset(){
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mData=null;
        this.mRawUrl=null;
    }


    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void execute(){
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);

    }
    public class DownloadRawData extends AsyncTask<String,Void,String> {

        protected  void onPostExecute(String webData){
            mData= webData;
            Log.v(LOG_DATA,"Data was returned"+mData);
            if(mData==null){
                if(mRawUrl==null){
                    mDownloadStatus=DownloadStatus.NOT_INITIALISED;
                }else{
                    mDownloadStatus= DownloadStatus.FAILED_OR_EMPTY;
                }
            }else {
                //Success
                mDownloadStatus = DownloadStatus.OK;
            }

        }

        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;
            if(strings == null){
                return null;
            }
            try{
                URL url =new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();  //creating connection
                urlConnection.setRequestMethod("GET");                      //default is also GET method we actually may not use this line
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }
                StringBuffer buffer = new StringBuffer();                           //buffer is a string that can store data
                reader = new BufferedReader(new InputStreamReader(inputStream));    //Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of
                // characters, arrays, and lines.
                String line;
                while((line=reader.readLine())!=null){                          // when there is not line to read , leave the loop
                    buffer.append(line+"\n");                                   //appending lines to buffer in order to store them safely
                }

                return buffer.toString();
            }catch (IOException e){
                Log.d(LOG_DATA,"Error Exception "+ e.getMessage());
            }finally {                                                          //making code more safer to disconnect from all connections
                if(urlConnection !=null){
                    urlConnection.disconnect();

                }if(reader !=null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.d(LOG_DATA,"Error closing reader "+e.getMessage());
                    }
                }

            }
            return null;
        }
    }
}
