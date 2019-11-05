package com.example.android.done;

import android.util.Log;
import android.view.View;

public class ViewAnimFactory {


    private View view;
    
    public void setWidth(int width) {
        view.getLayoutParams().width = width;
        view.requestLayout();

        Log.e(ViewAnimFactory.class.getName(), "setWidth: " + width);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setHeight(int height) {
        view.getLayoutParams().height = height;
    }
}
