package com.sayagodshala.dingdong.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayagodshala.dingdong.model.Product;
import com.sayagodshala.dingdong.settings.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class CartUtils {

    public static List<Product> getListOfItemsInCart(Context context) {
        List<Product> products = new ArrayList<Product>();
        String raw = AppSettings.getValue(context, AppSettings.PREF_CART, "");
        if (!raw.equalsIgnoreCase("")) {
            products = new Gson().fromJson(raw, new TypeToken<List<Product>>() {
            }.getType());
        }
        return products;
    }

    public static Product addItemInCart(Context context, Product product) {
        List<Product> products = getListOfItemsInCart(context);
        product.setQuantity(1);
        products.add(product);
        Log.d("products in cart", new Gson().toJson(products));
        AppSettings.setValue(context, AppSettings.PREF_CART, new Gson().toJson(products));
        return product;
    }

    public static Product incrementProductIncart(Context context, Product product) {
        List<Product> products = getListOfItemsInCart(context);
        int size = products.size();
        for (int i = 0; i < size; i++) {
            if (products.get(i).getProductId().equalsIgnoreCase(product.getProductId())) {
                int quantity = product.getQuantity();
                quantity += 1;
                product.setQuantity(quantity);
                products.set(i, product);
                break;
            }
        }
        Log.d("products in cart", new Gson().toJson(products));
        AppSettings.setValue(context, AppSettings.PREF_CART, new Gson().toJson(products));
        return product;
    }

    public static Product decrementProductIncart(Context context, Product product) {
        List<Product> products = getListOfItemsInCart(context);
        int size = products.size();

        int quantity = 0;

        for (int i = 0; i < size; i++) {
            if (products.get(i).getProductId().equalsIgnoreCase(product.getProductId())) {
                quantity = product.getQuantity();
                quantity -= 1;
                if (quantity <= 0) {
                    quantity = 0;
                    product.setQuantity(quantity);
                    products.remove(i);
                    break;
                } else {
                    product.setQuantity(quantity);
                    products.set(i, product);
                    break;
                }
            }
        }
        Log.d("products in cart", new Gson().toJson(products));
        AppSettings.setValue(context, AppSettings.PREF_CART, new Gson().toJson(products));
        return product;
    }


}
