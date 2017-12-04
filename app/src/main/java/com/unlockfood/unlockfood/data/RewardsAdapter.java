package com.unlockfood.unlockfood.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.unlockfood.unlockfood.R;

import java.util.List;

/**
 * Created by mykelneds on 21/09/2017.
 */

public class RewardsAdapter extends BaseAdapter {

    Context ctx;
    List<String> items;
    int level;

    public RewardsAdapter(Context ctx, List<String> items, int level) {
        this.ctx = ctx;
        this.items = items;
        this.level = level;
    }

    @Override
    public int getCount() {
        if (items.size() > 10)
            return items.size();
        else return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(ctx);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, GridView.AUTO_FIT));
            imageView.setAdjustViewBounds(true);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) view;
        }
        if (level > i)
            if (i < items.size())
                if (items.get(i).equals(""))
                    imageView.setImageResource(mThumbIds[i]);
                else
                    Picasso.with(ctx).load(items.get(i)).error(R.drawable.img_level0).into(imageView);
            else imageView.setImageResource(mThumbIds[i]);
        else
            imageView.setImageResource(R.drawable.img_level0);
        return imageView;
    }

    private Integer[] mThumbIds = {
            R.drawable.img_level1, R.drawable.img_level2,
            R.drawable.img_level3, R.drawable.img_level4,
            R.drawable.img_level5, R.drawable.img_level6,
            R.drawable.img_level7, R.drawable.img_level8,
            R.drawable.img_level9, R.drawable.img_level10,
    };
}
