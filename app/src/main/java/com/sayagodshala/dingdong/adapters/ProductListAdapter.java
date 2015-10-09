package com.sayagodshala.dingdong.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.imageloader.ImageLoader;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.util.Constants;
import com.sayagodshala.dingdong.util.Util;

import java.util.List;

public class ProductListAdapter extends BaseAdapter implements OnClickListener {

    public interface ProductListListener {
        public void onProductAdd(Product product, int index);

        public void onProductIncrement(Product product, int index);

        public void onProductDecrement(Product product, int index);
    }


    ViewHolder mViewHolder = null, tempviewholder, lastViewHolder;
    private Context context;
    private List<Product> products;
    private LayoutInflater layoutInflater;
    private ProductListListener listener;
    private ImageLoader mImageLoader;
    private boolean isOpen = false;

    public ProductListAdapter(Context context, List<Product> products, ProductListListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
        mImageLoader = new ImageLoader(context);
        isOpen = Util.isServiceOpen(context);
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
        Product item = null;
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

            cellView = layoutInflater.inflate(R.layout.list_item_product, null);

            holder = new ViewHolder();

            holder.image_dummy = Util.genericView(cellView, R.id.image_dummy);
            holder.linear_selector = Util.genericView(cellView, R.id.linear_selector);
            holder.linear_add = Util.genericView(cellView, R.id.linear_add);
            holder.text_name = Util.genericView(cellView, R.id.text_name);
            holder.text_description = Util.genericView(cellView, R.id.text_description);
            holder.text_price = Util.genericView(cellView, R.id.text_price);
            holder.text_type = Util.genericView(cellView, R.id.text_type);
            holder.view_top_spacer = Util.genericView(cellView, R.id.view_top_spacer);
            holder.txtadd = Util.genericView(cellView, R.id.txtadd);
            holder.layout_price = Util.genericView(cellView, R.id.layout_price);
            holder.txtnocater = Util.genericView(cellView, R.id.textnotcater);
            holder.linear_buttons = Util.genericView(cellView, R.id.l1_buttons);
            holder.button_increment = Util.genericView(cellView, R.id.button_increment);
            holder.button_decrement = Util.genericView(cellView, R.id.button_decrement);
            holder.text_nos = Util.genericView(cellView, R.id.text_nos);

            holder.button_decrement.setTag(R.id.TAG_ID, holder);
            holder.button_increment.setTag(R.id.TAG_ID, holder);
            holder.linear_add.setTag(R.id.TAG_ID, holder);

            holder.view_bottom_spacer = Util.genericView(cellView, R.id.view_bottom_spacer);

            cellView.setTag(holder);
        }

        //FontUtils.setDefaultFont(context, cellView);

        holder = (ViewHolder) cellView.getTag();
        holder.linear_selector.setTag(position);
        holder.linear_add.setTag(position);
        holder.button_decrement.setTag(position);
        holder.button_increment.setTag(position);

        holder.view_top_spacer.setVisibility(View.GONE);
        holder.text_description.setText("");
        holder.text_name.setText("");
        holder.text_price.setText("");
        holder.text_type.setText("");
        holder.view_bottom_spacer.setVisibility(View.GONE);

        //holder.linear_selector.setOnClickListener(this);
        holder.button_increment.setOnClickListener(this);
        holder.button_decrement.setOnClickListener(this);
        holder.linear_add.setOnClickListener(this);

        Util.setTouchDeligate(holder.button_decrement, cellView, 100, 20, 20, 20);
        Util.setTouchDeligate(holder.button_increment, cellView, 20, 20, 20, 20);

        holder.linear_add.setOnClickListener(this);

        holder.linear_add.setVisibility(View.GONE);
        holder.linear_buttons.setVisibility(View.GONE);

        holder.linear_add.setAlpha(0.9f);

        holder.image_dummy.setBackgroundResource(R.drawable.dummy_product_image);

        Product product = products.get(position);
        String url = product.getImage();

        holder.text_type.setText(product.getType());

        if (product.getQuantity() > 0) {
            holder.linear_buttons.setVisibility(View.VISIBLE);
        } else {
            holder.linear_add.setVisibility(View.VISIBLE);
        }

        if (product.getQuantity() < 10)
            holder.text_nos.setText("0" + product.getQuantity());
        else
            holder.text_nos.setText(product.getQuantity() + "");


        if (url != null || !url.equalsIgnoreCase("")) {
            mImageLoader.DisplayImage(Constants.BASE_URL + url, holder.image_dummy);
        }

        holder.view_top_spacer.setVisibility(View.VISIBLE);

        if (position == products.size() - 1)
            holder.view_bottom_spacer.setVisibility(View.VISIBLE);

        holder.linear_add.setEnabled(true);
        holder.layout_price.setVisibility(View.VISIBLE);
        holder.txtnocater.setVisibility(View.GONE);


        holder.text_price.setText(product.getPrice());

        Log.d("isServiceOpen", isOpen + "");

        if (!isOpen) {
            holder.linear_add.setEnabled(false);
            holder.linear_add.setVisibility(View.VISIBLE);
            holder.linear_buttons.setVisibility(View.GONE);
            holder.linear_add.setAlpha(0.3f);
        }

        holder.text_description.setText(product.getDescription().trim());

        if (product.getDescription().equalsIgnoreCase("")) {
            holder.text_description.setVisibility(View.GONE);
        }

        holder.text_name.setText(product.getTitle().trim());

        defaultConfiguration(cellView, position);

        return cellView;
    }

    static class ViewHolder {
        ImageView image_dummy, image_product1;
        Button button_increment, button_decrement;
        LinearLayout linear_selector, linear_add, layout_price, linear_buttons;
        TextView text_type, text_name, text_description, text_price, txtadd, txtnocater, text_nos;
        View view_top_spacer, view_bottom_spacer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_selector:
//                clickView(v);
                break;
            case R.id.linear_add:
                addProduct(v);
                break;
            case R.id.button_increment:
                increment(v);
                break;
            case R.id.button_decrement:
                decrement(v);
                break;
        }
    }

    public void addProduct(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        listener.onProductAdd(products.get(position), position);
//        products.set(position, CartUtils.addItemInCart(context, products.get(position)));
//        notifyDataSetChanged();
    }

    public void increment(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        listener.onProductIncrement(products.get(position), position);
//        products.set(position, CartUtils.incrementProductIncart(context, products.get(position)));
//        notifyDataSetChanged();
    }

    public void decrement(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        listener.onProductDecrement(products.get(position), position);
//        products.set(position, CartUtils.decrementProductIncart(context, products.get(position)));
//        notifyDataSetChanged();
    }

}
