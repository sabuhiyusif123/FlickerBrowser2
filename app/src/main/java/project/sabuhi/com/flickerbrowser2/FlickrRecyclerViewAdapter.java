package project.sabuhi.com.flickerbrowser2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cavid on 8/20/2016.
 */

public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder> {
    private List<Photo> mListPhotos;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> mListPhotos, Context mContext) {
        this.mListPhotos = mListPhotos;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return (null!=mListPhotos ? mListPhotos.size(): 0);
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,null);
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);

        return flickrImageViewHolder;

    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        Photo photoItem= mListPhotos.get(position);
        Picasso.with(mContext).load(photoItem.getmImage())
                .error(R.drawable.download)
                .placeholder(R.drawable.download)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getmTitle());
    }
}
