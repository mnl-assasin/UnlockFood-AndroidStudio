package com.unlockfood.unlockfood.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.unlockfood.unlockfood.utils.FontStyle;

/**
 * Created by mykelneds on 15/01/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewMyriad extends TextView {

    public TextViewMyriad(Context context) {
        super(context);
        setTypeface(FontStyle.myriad(context));
    }

    public TextViewMyriad(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontStyle.myriad(context));
    }

    public TextViewMyriad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontStyle.myriad(context));
    }

}