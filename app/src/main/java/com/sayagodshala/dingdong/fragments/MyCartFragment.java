package com.sayagodshala.dingdong.fragments;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sayagodshala.dingdong.BaseFragment;
import com.sayagodshala.dingdong.R;
import com.sayagodshala.dingdong.activities.ChooseAddressActivity_;
import com.sayagodshala.dingdong.adapters.ProductCartListAdapter;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.util.CartUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_my_cart)
public class MyCartFragment extends BaseFragment implements ProductCartListAdapter.ProductCartListListener {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.text_total_price)
    TextView text_total_price;

    @ViewById(R.id.container_cart)
    RelativeLayout container_cart;

    @ViewById(R.id.container_shopnow)
    LinearLayout container_shopnow;

    @ViewById(R.id.list_items)
    ListView list_items;
    ProductCartListAdapter productListAdapter;
    List<Product> listItems;

    @AfterViews
    void init() {
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        listItems = new ArrayList<>();
        text_total_price.setText("");
        container_cart.setVisibility(View.GONE);
        container_shopnow.setVisibility(View.GONE);
        listItems = CartUtils.getListOfItemsInCart(getActivity());
        drawListItems();
        updateCartView();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment", "onpause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment", "onresume");
    }

    @Override
    public void onProductAdd(Product product, int index) {
        listItems.set(index, CartUtils.addItemInCart(getActivity(), listItems.get(index)));
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    @Override
    public void onProductIncrement(Product product, int index) {
        listItems.set(index, CartUtils.incrementProductIncart(getActivity(), listItems.get(index)));
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    @Override
    public void onProductDecrement(Product product, int index) {
        Product product1 = CartUtils.decrementProductIncart(getActivity(), listItems.get(index));
        if (product1.getQuantity() > 0) {
            listItems.set(index, product1);
        } else {
            listItems.remove(index);
        }
        productListAdapter.notifyDataSetChanged();
        updateCartView();
    }

    private void drawListItems() {
        productListAdapter = new ProductCartListAdapter(getActivity(), listItems, this);
        list_items.setAdapter(productListAdapter);
    }

    private void updateCartView() {
        List<Product> cartProducts = CartUtils.getListOfItemsInCart(getActivity());
        if (cartProducts.size() > 0) {
            container_cart.setVisibility(View.VISIBLE);
            container_shopnow.setVisibility(View.GONE);
        } else {
            container_cart.setVisibility(View.GONE);
            container_shopnow.setVisibility(View.VISIBLE);
        }
        int totalQuantity = 0;
        int topPrice = 0;
        for (int i = 0; i < cartProducts.size(); i++) {
            Product cartProduct = cartProducts.get(i);
            int quantity = cartProduct.getQuantity();
            totalQuantity += quantity;
            int price = Integer.parseInt(cartProduct.getPrice());
            int productPrice = quantity * price;
            topPrice += productPrice;
        }

        if (totalQuantity > 0)
            title.setText("My Cart (" + totalQuantity + ")");
        else
            title.setText("My Cart");

        text_total_price.setText(topPrice + "");
    }

    @Click(R.id.button_shopnow)
    void onShopNowClick() {
        getActivity().finish();
    }

    @Click(R.id.container_cart)
    void onCartClick() {
        ChooseAddressActivity_.intent(this).start();
    }

}
