package com.sayagodshala.dingdong.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.imageloader.ImageLoader;
import com.sayagodshala.dingdong.model.Order;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.util.Util;

import java.util.List;

public class MyOrderListAdapter extends BaseAdapter implements OnClickListener {

    public interface MyOrderListListener {
        public void onOrderSelected(Order order, int index);

    }


    ViewHolder mViewHolder = null, tempviewholder, lastViewHolder;
    private Context context;
    private List<Order> items;
    private LayoutInflater layoutInflater;
    private MyOrderListListener listener;
    private ImageLoader mImageLoader;
    private boolean isOpen = false;

    public MyOrderListAdapter(Context context, List<Order> items, MyOrderListListener listener) {
        this.context = context;
        this.items = items;
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
        if (items == null)
            return 0;
        else
            return items.size();
        // return 50;
    }

    @Override
    public Object getItem(int position) {
        Order item = null;
        try {
            item = items.get(position);
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

            cellView = layoutInflater.inflate(R.layout.list_item_order, null);

            holder = new ViewHolder();

            holder.linear_selector = Util.genericView(cellView, R.id.linear_selector);
            holder.text_schedule = Util.genericView(cellView, R.id.text_schedule);
            holder.text_order_value = Util.genericView(cellView, R.id.text_order_value);
            holder.text_orderid = Util.genericView(cellView, R.id.text_orderid);
            holder.view_top_spacer = Util.genericView(cellView, R.id.view_top_spacer);
            holder.container_placed = Util.genericView(cellView, R.id.container_placed);
            holder.container_dispatched = Util.genericView(cellView, R.id.container_dispatched);
            holder.container_delivered = Util.genericView(cellView, R.id.container_delivered);
            holder.container_products = Util.genericView(cellView, R.id.container_products);

            holder.view_bottom_spacer = Util.genericView(cellView, R.id.view_bottom_spacer);

            cellView.setTag(holder);
        }

        //FontUtils.setDefaultFont(context, cellView);

        holder = (ViewHolder) cellView.getTag();
        holder.linear_selector.setTag(position);

        holder.view_top_spacer.setVisibility(View.GONE);
        holder.text_schedule.setText("");
        holder.text_orderid.setText("");
        holder.text_order_value.setText("");
        holder.view_bottom_spacer.setVisibility(View.GONE);
        holder.container_placed.setAlpha(0.2f);
        holder.container_dispatched.setAlpha(0.2f);
        holder.container_delivered.setAlpha(0.2f);

        if (holder.container_products.getChildCount() > 0)
            holder.container_products.removeAllViews();


        //holder.linear_selector.setOnClickListener(this);

        Order item = items.get(position);

        if (item.getStatus().equalsIgnoreCase("placed")) {
            holder.container_placed.setAlpha(1);
        } else if (item.getStatus().equalsIgnoreCase("dispatched")) {
            holder.container_placed.setAlpha(1);
            holder.container_dispatched.setAlpha(1);
        } else if (item.getStatus().equalsIgnoreCase("delivered")) {
            holder.container_delivered.setAlpha(1);
        }

        drawProducts(item.getProducts(), holder);




        holder.text_schedule.setText("Date - " + item.getCreatedDate());
        holder.text_orderid.setText("Order Id - " + item.getOrderKey());

        holder.view_top_spacer.setVisibility(View.VISIBLE);

        if (position == items.size() - 1)
            holder.view_bottom_spacer.setVisibility(View.VISIBLE);

        defaultConfiguration(cellView, position);

        return cellView;
    }

    static class ViewHolder {
        LinearLayout linear_selector, container_placed, container_dispatched, container_delivered, container_products;
        TextView text_schedule, text_order_value, text_orderid, text_type, text_name, text_description, text_price, text_nos;
        View view_top_spacer, view_bottom_spacer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_selector:
                onOrderSelected(v);
                break;
        }
    }

    public void onOrderSelected(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        listener.onOrderSelected(items.get(position), position);
//        items.set(position, CartUtils.addItemInCart(context, items.get(position)));
//        notifyDataSetChanged();
    }

    private void drawProducts(List<Product> products, ViewHolder holder) {


        int sumTotal = 0;

        for (int i = 0; i < products.size(); i++) {


            Product product = products.get(i);

            View mainView = layoutInflater.inflate(R.layout.list_item_order_product, null);
            TextView text_type = Util.genericView(mainView, R.id.text_type);
            TextView text_name = Util.genericView(mainView, R.id.text_name);
            TextView text_price = Util.genericView(mainView, R.id.text_price);
            TextView text_quantity = Util.genericView(mainView, R.id.text_quantity);
            TextView text_subtotal = Util.genericView(mainView, R.id.text_subtotal);
            View spacer = Util.genericView(mainView, R.id.spacer);

            text_type.setText(product.getType());
            text_name.setText(product.getTitle());
            text_price.setText(product.getPrice());
            text_quantity.setText(product.getQuantity() + "");

            int total = product.getQuantity() * Integer.parseInt(product.getPrice());

            sumTotal += total;

            text_subtotal.setText(total + "");

            if (i == products.size() - 1) {
                spacer.setVisibility(View.GONE);
            }
            holder.container_products.addView(mainView);
        }


        holder.text_order_value.setText(sumTotal + "");


    }

}
