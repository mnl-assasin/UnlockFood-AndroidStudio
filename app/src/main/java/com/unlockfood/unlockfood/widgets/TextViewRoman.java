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
public class TextViewRoman extends TextView {

    public TextViewRoman(Context context) {
        super(context);
        setTypeface(FontStyle.helveticaRoman(context));
    }

    public TextViewRoman(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontStyle.helveticaRoman(context));
    }

    public TextViewRoman(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontStyle.helveticaRoman(context));
    }

}