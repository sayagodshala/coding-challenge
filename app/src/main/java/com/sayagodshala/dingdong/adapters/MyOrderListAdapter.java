package com.sayagodshala.dingdong.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        public void onOrderCanceled(Order order, int index);

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
            holder.text_payable = Util.genericView(cellView, R.id.text_payable);
            holder.text_payable_cart = Util.genericView(cellView, R.id.text_payable_cart);
            holder.text_payable_discount = Util.genericView(cellView, R.id.text_payable_discount);
            holder.text_orderid = Util.genericView(cellView, R.id.text_orderid);
            holder.view_top_spacer = Util.genericView(cellView, R.id.view_top_spacer);
            holder.container_order_status = Util.genericView(cellView, R.id.container_order_status);
            holder.container_order_cancel = Util.genericView(cellView, R.id.container_order_cancel);
            holder.container_discount_payable = Util.genericView(cellView, R.id.container_discount_payable);
            holder.container_cart_payable = Util.genericView(cellView, R.id.container_cart_payable);
            holder.text_order_status = Util.genericView(cellView, R.id.text_order_status);
            holder.image_order_status = Util.genericView(cellView, R.id.image_order_status);
            holder.container_products = Util.genericView(cellView, R.id.container_products);

            holder.view_bottom_spacer = Util.genericView(cellView, R.id.view_bottom_spacer);

            cellView.setTag(holder);
        }

        //FontUtils.setDefaultFont(context, cellView);

        holder = (ViewHolder) cellView.getTag();
        holder.linear_selector.setTag(position);
        holder.container_order_cancel.setTag(position);

        holder.view_top_spacer.setVisibility(View.GONE);
        holder.text_schedule.setText("");
        holder.text_orderid.setText("");
        holder.text_payable.setText("");
        holder.text_payable_discount.setText("");
        holder.text_payable_cart.setText("");
        holder.text_order_status.setText("");
        holder.view_bottom_spacer.setVisibility(View.GONE);
        holder.container_discount_payable.setVisibility(View.GONE);
        holder.container_cart_payable.setVisibility(View.GONE);
        holder.image_order_status.setImageDrawable(null);
        holder.container_order_cancel.setVisibility(View.GONE);

        if (holder.container_products.getChildCount() > 0)
            holder.container_products.removeAllViews();


        //holder.linear_selector.setOnClickListener(this);

        Order item = items.get(position);

        if (!item.getDiscount().equalsIgnoreCase("0")) {
            holder.container_discount_payable.setVisibility(View.VISIBLE);
            holder.container_cart_payable.setVisibility(View.VISIBLE);
        }

        if (item.getStatus().equalsIgnoreCase("placed")) {
            holder.image_order_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_order_placed));
            holder.text_order_status.setText("Order Placed");
            holder.container_order_cancel.setVisibility(View.VISIBLE);
        } else if (item.getStatus().equalsIgnoreCase("dispatched")) {
            holder.image_order_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_order_dispatched));
            holder.text_order_status.setText("Order Dispatched");
            holder.container_order_cancel.setVisibility(View.GONE);
        } else if (item.getStatus().equalsIgnoreCase("delivered")) {
            holder.image_order_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_order_delivered));
            holder.text_order_status.setText("Order Delivered");
            holder.container_order_cancel.setVisibility(View.GONE);
        } else {
            holder.image_order_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_order_canceled));
            holder.text_order_status.setText("Order Canceled");
            holder.container_order_cancel.setVisibility(View.GONE);
        }

        drawProducts(item, holder);

        holder.text_schedule.setText("Date - " + Util.parseDate(item.getCreatedDate()));
        holder.text_orderid.setText("Order Id - " + item.getOrderKey());

        holder.view_top_spacer.setVisibility(View.VISIBLE);

        holder.container_order_cancel.setOnClickListener(this);

        if (position == items.size() - 1)
            holder.view_bottom_spacer.setVisibility(View.VISIBLE);

        defaultConfiguration(cellView, position);

        return cellView;
    }

    static class ViewHolder {
        RelativeLayout container_discount_payable, container_cart_payable;
        LinearLayout linear_selector, container_order_status, container_order_cancel, container_products;
        TextView text_schedule, text_order_status, text_payable_cart, text_payable_discount, text_payable, text_orderid, text_type, text_name, text_description, text_price, text_nos;
        View view_top_spacer, view_bottom_spacer;
        ImageView image_order_status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_selector:
                onOrderSelected(v);
                break;
            case R.id.container_order_cancel:
                onOrderCanceled(v);
                break;
        }
    }

    public void onOrderSelected(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        listener.onOrderSelected(items.get(position), position);
//        items.set(position, CartUtils.addItemInCart(context, items.get(position)));
//        notifyDataSetChanged();
    }

    public void onOrderCanceled(View v) {
        final int position = Integer.parseInt(v.getTag().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to cancel this order?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOrderCanceled(items.get(position), position);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void drawProducts(Order order, ViewHolder holder) {


        List<Product> products = order.getProducts();

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


        if (!order.getDiscount().equalsIgnoreCase("0")) {
            int payableAmount = sumTotal - Integer.parseInt(order.getDiscount());
            holder.text_payable_discount.setText(order.getDiscount());
            holder.text_payable_cart.setText(sumTotal + "");
            holder.text_payable.setText(payableAmount + "");
        } else {
            holder.text_payable.setText(sumTotal + "");
        }


    }

}
