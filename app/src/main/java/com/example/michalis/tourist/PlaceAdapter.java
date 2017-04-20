package com.example.michalis.tourist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;
import com.google.android.gms.location.places.Place;

/**
 * Created by michalis on 3/15/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterViewHolder> {
    private Cursor placeCursor;
    private LongListItemClickListener longlistItemClickListerer;

    PlaceAdapter(LongListItemClickListener longlistItemClickListerer) {
        this.longlistItemClickListerer = longlistItemClickListerer;
    }

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

        if (placeCursor.getInt(placeCursor.getColumnIndex(PlaceEntry.PLACE_HOME))==1)
            holder.tvPlaceName.setText("HOME");
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

    public Cursor getPlaceCursor() {
        return placeCursor;
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

    public interface LongListItemClickListener {
        void onLongListItemClickListener(Long id, String name);
    }

    public class PlaceAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener{
        private TextView tvPlaceName;

        public PlaceAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
        }

        @Override
        public boolean onLongClick(View v) {
            placeCursor.moveToPosition(getAdapterPosition());
            String name = placeCursor.getString(placeCursor.getColumnIndex(PlaceEntry.PLACE_NAME));
            Long id = placeCursor.getLong(placeCursor.getColumnIndex(PlaceEntry._ID));
            longlistItemClickListerer.onLongListItemClickListener(id, name);
            return true;
        }
    }
}
