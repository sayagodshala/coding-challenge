package com.sayagodshala.dingdong.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

public class ChooseAddressListAdapter extends BaseAdapter implements OnClickListener {

    public interface CustomerChooseAddressListAdapterListener {
        public void onAddressChoosed(Address address);

        public void onAddressEdited(Address address);

        public void onAddressDeleted(Address address);
    }

    ViewHolder mViewHolder = null;
    private Context context;
    private List<Address> addresses;
    private LayoutInflater layoutInflater;
    private CustomerChooseAddressListAdapterListener listener;

    public ChooseAddressListAdapter(Context context, List<Address> products, CustomerChooseAddressListAdapterListener listener) {
        this.context = context;
        this.addresses = products;


        this.listener = listener;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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

            cellView = layoutInflater.inflate(R.layout.list_item_customer_choose_address, null);

            holder = new ViewHolder();

            holder.relative_selector = Util.genericView(cellView, R.id.relative_selector);
            holder.text_title = Util.genericView(cellView, R.id.text_title);
            holder.relative_delete = Util.genericView(cellView, R.id.relative_delete);
            holder.button_checkbox = Util.genericView(cellView, R.id.button_checkbox);
            holder.text_subtitle = Util.genericView(cellView, R.id.text_subtitle);

            holder.relative_delete.setTag(R.id.TAG_ID, holder);
            holder.button_checkbox.setTag(R.id.TAG_ID, holder);
            cellView.setTag(holder);
        }


        holder = (ViewHolder) cellView.getTag();
        holder.relative_selector.setTag(position);
        holder.button_checkbox.setTag(position);
        holder.relative_delete.setTag(position);

        holder.text_title.setText("");
        Address address = addresses.get(position);

        holder.relative_selector.setClickable(true);
        holder.relative_selector.setOnClickListener(this);
        holder.relative_delete.setOnClickListener(this);
        holder.button_checkbox.setBackgroundResource(R.drawable.button_checkbox_blue);

        holder.button_checkbox.setSelected(false);

        if (address.isSelected()) {
            holder.button_checkbox.setSelected(true);
            //Log.v("TAG", position + " is selected");
        }


        if (address.getAddress() != null) {
            holder.text_title.setText(address.getAddress());
        } else {
            holder.text_title.setText(address.getLandmark());
        }

        holder.text_subtitle.setText(address.getLandmark());

        defaultConfiguration(cellView, position);

        return cellView;
    }


    static class ViewHolder {
        RelativeLayout relative_selector, relative_delete;
        Button button_checkbox;
        TextView text_title, text_subtitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_selector:
                onChoose(v);
                break;
            case R.id.relative_delete: {
                Log.v("TAG", " Delete Clicked");
                onDelete(v);
            }
            break;

        }
    }

    public void onDelete(View v) {
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

//        YesNoAlertDialog yesNoAlertDialog = new YesNoAlertDialog(context, " Would you like to delete this address ?", "No", "Yes", new YesNoAlertDialog.YesNoAlertDialogListener() {
//            @Override
//            public void onPositiveAction() {
//
//                ((CustomerChooseAddressListActivity) context).onAddressDeleted(addresses.get(position));
//            }
//
//            @Override
//            public void onNegativeAction() {
//
//            }
//        });
//
//        yesNoAlertDialog.show();


    }


    public void onChoose(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Address address = addresses.get(position);

//        if (!address.isIn_delivery_area()) {
//            Toast.makeText(context, " This address does not fall within the permitted delivery zone of the selected outlet", Toast.LENGTH_SHORT).show();
//
//        } else {
//            for (Address ads : addresses)
//                ads.setIsSelected(false);
//
//            Log.v("TAG", " Selected address " + address.getId());
//            address.setIsSelected(true);
//            notifyDataSetChanged();
//
//            listener.onAddressChoosed(addresses.get(position));
//        }

        for (Address ads : addresses)
            ads.setSelected(false);

        Log.v("TAG", " Selected address " + address.getAddressId());
        address.setSelected(true);
        listener.onAddressChoosed(address);
        notifyDataSetChanged();

    }

}
