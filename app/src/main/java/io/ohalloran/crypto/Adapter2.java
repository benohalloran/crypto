package io.ohalloran.crypto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ben on 4/5/2015.
 */
public class Adapter2<T> extends ListAdapter<T> {
    int layoutID;
    int vID;
    Context c;

    public Adapter2(Context c, List<T> data, int layoutID, int viewID) {
        super(data);
        this.c = c;
        this.layoutID = layoutID;
        vID = viewID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root = view != null ? view :
                LayoutInflater.from(c).inflate(layoutID, viewGroup, false);
        TextView tv = (TextView) root.findViewById(vID);
        tv.setText(getItem(i).toString());
        return tv;
    }
}
