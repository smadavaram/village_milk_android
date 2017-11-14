package com.villagemilk.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by sagaryarnalkar on 29/04/15.
 */
public class Fonts {

    public static final String FONT_ROBOTO_CONDENSED = "fonts/Roboto-Condensed.ttf";
    public static final String FONT_ROBOTO_BOLD_CONDENSED = "fonts/Roboto-BoldCondensed.ttf";
    public static final String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    public static final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";

    public static Typeface getTypeface(Context context, String fontPath) {

        Typeface tf = null;

        try {

            tf = Typeface.createFromAsset(context.getAssets(), fontPath);

        } catch (Exception e) {

        }

        return tf;
    }

}
