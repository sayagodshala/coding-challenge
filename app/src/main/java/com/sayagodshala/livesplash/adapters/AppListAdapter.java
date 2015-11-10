package com.sayagodshala.livesplash.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayagodshala.livesplash.R;
import com.sayagodshala.livesplash.imageloader.ImageLoader;
import com.sayagodshala.livesplash.model.Entry;
import com.sayagodshala.livesplash.util.Constants;
import com.sayagodshala.livesplash.util.Util;

import java.util.List;

public class AppListAdapter extends BaseAdapter implements OnClickListener {


    ViewHolder mViewHolder = null;
    private Context context;
    private List<Entry> products;
    private LayoutInflater layoutInflater;
    private ImageLoader mImageLoader;

    public AppListAdapter(Context context, List<Entry> products) {
        this.context = context;
        this.products = products;
        mImageLoader = new ImageLoader(context);
        try {
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        if (products == null)
            return 0;
        else
            return products.size();
        // return 50;
    }

    @Override
    public Object getItem(int position) {
        Entry item = null;
        try {
            item = products.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }


    private void defaultConfiguration(View cellView, int position) {
        mViewHolder = (ViewHolder) cellView.getTag();
        // if (position % 2 == 0)
        // mViewHolder.relative_container.setBackgroundResource(R.color.white);
        // else
        // mViewHolder.relative_container.setBackgroundResource(R.color.grey1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cellView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;


        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (cellView == null) {

            cellView = layoutInflater.inflate(R.layout.list_item_app, null);

            holder = new ViewHolder();

            holder.image_dummy = Util.genericView(cellView, R.id.image_app);
            holder.title = Util.genericView(cellView, R.id.title);
            holder.subtitle = Util.genericView(cellView, R.id.subtitle);

            cellView.setTag(holder);
        }

        //FontUtils.setDefaultFont(context, cellView);

        holder = (ViewHolder) cellView.getTag();

        holder.title.setText("");
        holder.subtitle.setText("");


        holder.image_dummy.setBackgroundResource(R.drawable.img);

        Entry product = products.get(position);

        if (product.getImage().size() > 0) {
            String url = product.getImage().get(0).getLabel();
            if (url != null || !url.equalsIgnoreCase("")) {
                Log.d("ImagePath", Constants.BASE_URL + Constants.BASE_PATH + url);
                mImageLoader.DisplayImage(url, holder.image_dummy);
            }

        }


        holder.title.setText(product.getName().getLabel());
        holder.subtitle.setText("Application");


        defaultConfiguration(cellView, position);

        return cellView;
    }

    static class ViewHolder {
        ImageView image_dummy;
        LinearLayout linear_selector, linear_add, layout_price, linear_buttons;
        TextView title, subtitle;
    }

    @Override
    public void onClick(View v) {
    }

}
