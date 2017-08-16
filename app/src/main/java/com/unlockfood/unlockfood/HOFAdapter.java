package com.unlockfood.unlockfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unlockfood.unlockfood.api.HallOfFameData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mykelneds on 14/05/2017.
 */

public class HOFAdapter extends BaseAdapter {

    Context ctx;
    List<HallOfFameData> items;

    LayoutInflater inflater;

    public HOFAdapter(Context ctx, List<HallOfFameData> items) {
        this.ctx = ctx;
        this.items = items;

        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;

        if (view != null)
            holder = (ViewHolder) view.getTag();
        else {
            view = inflater.inflate(R.layout.item_hof, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        HallOfFameData item = items.get(i);
        holder.tvName.setText(item.getFirstName() + " " + item.getLastName());
        holder.tvPeopleFed.setText(String.valueOf(item.getPeopleFed()));
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.civProfile)
        CircleImageView civProfile;
        @Bind(R.id.tvName)
        TextView tvName;
        @Bind(R.id.tvPeopleFed)
        TextView tvPeopleFed;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
