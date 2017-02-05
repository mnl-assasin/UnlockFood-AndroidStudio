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
public class TextViewMed extends TextView {

    public TextViewMed(Context context) {
        super(context);
        setTypeface(FontStyle.helveticaMed(context));
    }

    public TextViewMed(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontStyle.helveticaMed(context));
    }

    public TextViewMed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontStyle.helveticaMed(context));
    }

}