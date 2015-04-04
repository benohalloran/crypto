package io.ohalloran.crypto.utils;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/3/2015.
 */
public abstract class ListAdapter<T> extends BaseAdapter {
    protected List<T> data;

    public ListAdapter(List<T> data) {
        this.data = data;
    }

    public ListAdapter() {
        this(new ArrayList<T>());
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).hashCode();
    }
}
