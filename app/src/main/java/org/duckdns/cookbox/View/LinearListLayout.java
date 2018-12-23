package org.duckdns.cookbox.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/*
    LinearListLayout's addView method accepts an int resource,
    and takes the job of inflating it.
 */
public class LinearListLayout extends LinearLayout {

    public LinearListLayout(Context context) {
        super(context);
    }

    public LinearListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinearListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public View addListItem(int resource) {
        View view = inflate(getContext(), resource, this);
        this.addView(view);
        return view;
    }

    public View addListItem(int resource, int position) {
        View view = inflate(getContext(), resource, this);
        this.addView(view, position);
        return view;
    }

    public View addListItem(int resource, ViewGroup.LayoutParams params) {
        View view = inflate(getContext(), resource, null);
        this.addView(view, params);
        return view;
    }

    public View addListItem(int resource, int position, ViewGroup.LayoutParams params) {
        View view = inflate(getContext(), resource, this);
        this.addView(view, position, params);
        return view;
    }
}
