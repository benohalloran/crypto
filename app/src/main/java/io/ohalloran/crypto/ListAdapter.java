package io.ohalloran.crypto;

import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 4/3/2015.
 */
public abstract class ListAdapter<T> extends BaseAdapter {
    protected List<T> data;

    public ListAdapter(List<T> data) {
        this.data = data;
    }

    @SafeVarargs
    public ListAdapter(T... data) {
        this(Arrays.asList(data));
    }

    public void updateData(List<T> people) {
        data = people;
        notifyDataSetChanged();
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
