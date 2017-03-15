package com.example.michalis.tourist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;

import org.w3c.dom.Text;

/**
 * Created by michalis on 3/15/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterViewHolder> {
    private Cursor placeCursor;

    @Override
    public PlaceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.place_list_item, parent, false);
        view.setFocusable(true);
        return new PlaceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceAdapterViewHolder holder, int position) {
        if (!placeCursor.moveToPosition(position)){
            return;
        }
        holder.tvPlaceName.setText(placeCursor.getString(placeCursor.getColumnIndex(PlaceEntry.PLACE_NAME)));
        holder.itemView.setTag(placeCursor.getString(placeCursor.getColumnIndex(PlaceEntry.PLACE_ID)));
    }

    public void swapCursor(Cursor cursor) {
        if (placeCursor != null) {
            placeCursor.close();
        }
        placeCursor = cursor;
        if (placeCursor != null){
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (placeCursor == null) return 0;
        return placeCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (!placeCursor.moveToPosition(position)){
            return 0;
        }
        return (long) placeCursor.getInt(placeCursor.getColumnIndex(PlaceEntry._ID));
    }

    public class PlaceAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlaceName;

        public PlaceAdapterViewHolder(View itemView) {
            super(itemView);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
        }
    }
}
