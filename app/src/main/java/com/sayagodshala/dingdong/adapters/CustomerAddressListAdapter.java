package com.sayagodshala.dingdong.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.model.Address;
import com.sayagodshala.dingdong.util.Util;

import java.util.List;

public class CustomerAddressListAdapter extends BaseAdapter implements OnClickListener {

    public interface CustomerAddressListAdapterListener {
        public void onAddressDeleted(Address address);

        public void onAddressEdited(Address address);
    }

    ViewHolder mViewHolder = null;
    private Context context;
    private List<Address> addresses;
    private LayoutInflater layoutInflater;
    private CustomerAddressListAdapterListener listener;

    public CustomerAddressListAdapter(Context context, List<Address> products, CustomerAddressListAdapterListener listener) {
        this.context = context;
        this.addresses = products;
        this.listener = listener;
        try {
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (addresses == null)
            return 0;
        else
            return addresses.size();
        // return 50;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        Address item = null;
        try {
            item = addresses.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    private void defaultConfiguration(View cellView, int position) {
        mViewHolder = (ViewHolder) cellView.getTag();
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

            cellView = layoutInflater.inflate(R.layout.list_item_customer_address, null);

            holder = new ViewHolder();

            holder.relative_selector = Util.genericView(cellView, R.id.relative_selector);
            holder.text_title = Util.genericView(cellView, R.id.text_title);
            holder.text_subtitle = Util.genericView(cellView, R.id.text_subtitle);
            holder.button_edit = Util.genericView(cellView, R.id.button_edit);
            holder.button_delete = Util.genericView(cellView, R.id.button_delete);

            holder.button_edit.setTag(R.id.TAG_ID, holder);
            holder.button_delete.setTag(R.id.TAG_ID, holder);

            cellView.setTag(holder);
        }

        holder = (ViewHolder) cellView.getTag();
        holder.relative_selector.setTag(position);
        holder.button_delete.setTag(position);
        holder.button_edit.setTag(position);

        holder.text_title.setText("");
        holder.text_subtitle.setText("");
        holder.relative_selector.setOnClickListener(this);
        holder.button_delete.setOnClickListener(this);
        holder.button_edit.setOnClickListener(this);

        Address address = addresses.get(position);

        if (address.getAddress() != null) {
            holder.text_title.setText(address.getAddress());
        } else {
            holder.text_title.setText(address.getLandmark());
            holder.text_subtitle.setVisibility(View.GONE);
        }

        holder.text_subtitle.setText(address.getLandmark());

        defaultConfiguration(cellView, position);

        return cellView;
    }


    static class ViewHolder {
        RelativeLayout relative_selector;
        Button button_edit, button_delete;
        TextView text_title, text_subtitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_selector:
                clickView(v);
                break;
            case R.id.button_edit:
                onEdit(v);
                break;
            case R.id.button_delete:
                onAddressDeleted(v);
                break;
        }
    }

    public void clickView(View v) {
        int position = Integer.parseInt(v.getTag().toString());
    }

    public void onEdit(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.TAG_ID);
        listener.onAddressEdited(addresses.get(position));
    }


    public void onAddressDeleted(View v) {
        final int position = Integer.parseInt(v.getTag().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Would you like to delete this address?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onAddressDeleted(addresses.get(position));
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}
