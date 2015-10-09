package com.sayagodshala.dingdong.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.model.GooglePrediction;
import com.sayagodshala.dingdong.util.Util;

import java.util.List;
import java.util.Random;

/**
 * Created by sayagodshala on 5/26/2015.
 */
public class LocationSearchListAdapter extends BaseAdapter implements View.OnClickListener {

    public interface LocationSearchListAdapterEventListener {
        public void onLocationSearchedClicked(GooglePrediction item);
    }

    private Context context;
    private List<GooglePrediction> mData;
    private LayoutInflater layoutInflater;
    private Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private LocationSearchListAdapterEventListener mListener;

    public LocationSearchListAdapter(Context context, List<GooglePrediction> data, LocationSearchListAdapterEventListener listener) {
        this.context = context;
        this.mData = data;
        mListener = listener;

        mRandom = new Random();

        try {
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        else
            return mData.size();
    }

    @Override
    public Object getItem(int position) {
        GooglePrediction item = null;
        item = mData.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cellView, ViewGroup parent) {

        ViewHolder holder;

        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (cellView == null) {
            cellView = layoutInflater.inflate(R.layout.list_item_searched_location, null);
            holder = new ViewHolder();
            holder.text_title = Util.genericView(cellView, R.id.text_title);
            holder.text_subtitle = Util.genericView(cellView, R.id.text_subtitle);
            holder.linear_selector = Util.genericView(cellView, R.id.linear_selector);
            holder.relative_icons = Util.genericView(cellView, R.id.relative_icons);
            holder.image_time = Util.genericView(cellView, R.id.image_time);
            holder.image_location = Util.genericView(cellView, R.id.image_location);
            holder.image_search = Util.genericView(cellView, R.id.image_location_search);
            holder.top_spacer = Util.genericView(cellView, R.id.view_top_spacer);

            holder.linear_selector.setTag(position);

            cellView.setTag(holder);
        }

        holder = (ViewHolder) cellView.getTag();
        holder.image_location.setVisibility(View.GONE);
        holder.image_time.setVisibility(View.GONE);
        holder.image_search.setVisibility(View.GONE);
        holder.top_spacer.setVisibility(View.GONE);


        if (position == 0) {
            holder.top_spacer.setVisibility(View.VISIBLE);
        }
        holder.linear_selector.setTag(position);

        GooglePrediction item = null;

        try {

            item = mData.get(position);
            Log.d("GooglePrediction", new Gson().toJson(item));
            if (item != null) {
                if (item.getDescription().equalsIgnoreCase("use my location")) {

                    holder.image_location.setVisibility(View.VISIBLE);
                    holder.text_title.setText(item.getDescription());
                    holder.text_title.setTextColor(Color.parseColor("#6b6b6b"));
                    holder.text_subtitle.setVisibility(View.GONE);

                } else {
                    if (item.getType() != null) {
                        if (item.getType().equalsIgnoreCase("saved")) {
                            holder.image_time.setVisibility(View.VISIBLE);
                        }

                    } else {
                        holder.text_title.setTextColor(Color.parseColor("#adadad"));
                        holder.image_search.setVisibility(View.VISIBLE);
                    }

                    String[] address = item.getDescription().split(",");
                    holder.text_title.setText(address[0]);
                    if (address.length > 1) {
                        String moreAddress = "";
                        for (int i = 1; i < address.length; i++) {
                            if (i == 1)
                                moreAddress = address[i];
                            else
                                moreAddress += ", " + address[i];
                        }
                        holder.text_subtitle.setText(moreAddress.trim());
                    } else {
                        holder.text_subtitle.setVisibility(View.GONE);
                    }
                }
            }
            holder.linear_selector.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cellView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_selector:
                clickView(v);
                break;
        }
    }


    private void clickView(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        try {
            mListener.onLocationSearchedClicked(mData.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        TextView text_title, text_subtitle;
        LinearLayout linear_selector;
        RelativeLayout relative_icons;
        View top_spacer;
        ImageView image_time, image_location, image_search;
    }

}
