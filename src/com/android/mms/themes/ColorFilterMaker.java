/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.mms.themes;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ColorFilterMaker {
    /**
     * Makes a ColorFilter
     *
     * @param newColor new color of image when filter is applied.
     * @return
     */
    public static ColorFilter changeColor(int newColor, float intens) {
        ColorMatrix cm = new ColorMatrix();

        changeColor(cm, newColor, intens);

        return new ColorMatrixColorFilter(cm);
    }


    public static ColorFilter changeBWColor(int newColor, float intens) {
        ColorMatrix cm = new ColorMatrix();

        changeBWColor(cm, newColor, intens);

        return new ColorMatrixColorFilter(cm);
    }

    public static ColorFilter changeColorAlpha(int newColor, float intens, float alpha) {
        ColorMatrix cm = new ColorMatrix();

        changeColorAlpha(cm, newColor, intens, alpha);

        return new ColorMatrixColorFilter(cm);
    }

    public static ColorFilter changeHue(int newHue, int intens) {
        ColorMatrix cm = new ColorMatrix();

        changeHue(cm, newHue, intens);

        return new ColorMatrixColorFilter(cm);
    }


    // This works for a b/w or grayscale image

    private static void changeColor(ColorMatrix cm, int newColor, float itens) {
        float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;

        float[] mat = new float[] {
            R           ,0              ,0              ,0              ,0  // Red
            ,0          ,G              ,0      ,0              ,0  // Green
            ,0          ,0              ,B              ,0              ,0      // Blue
            ,0          ,0              ,0      ,1              ,0      // Alpha
        };


        float[] matrix = new float[] {
            itens   ,itens  ,itens  ,0              ,0    // Red
            ,itens       ,itens  ,itens  ,0              ,0    // Green
            ,itens       ,itens  ,itens  ,0              ,0    // Blue
            ,0           ,0              ,0              ,1              ,0    // Alpha
        };
        cm.setConcat(new ColorMatrix(mat), new ColorMatrix(matrix));
    }

    private static void changeBWColor(ColorMatrix cm, int newColor, float itens) {
        float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;

        float[] mat = new float[] {
            R           ,0              ,0              ,0              ,0      // Red
            ,0          ,G              ,0      ,0              ,0      // Green
            ,0          ,0              ,B              ,0              ,0              // Blue
            ,0          ,0              ,0      ,A              ,0      // Alpha
        };

        float[] matrix = new float[] {
            itens   ,itens  ,itens  ,0              ,0    // Red
            ,itens       ,itens  ,itens  ,0              ,0    // Green
            ,itens       ,itens  ,itens  ,0              ,0    // Blue
            ,0           ,0              ,0              ,A              ,0    // Alpha
        };
        cm.setConcat(new ColorMatrix(mat), new ColorMatrix(matrix));
    }

    private static void changeColorAlpha(ColorMatrix cm, int newColor, float itens, float alpha) {
        float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;

        float[] mat = new float[] {
            R           ,0              ,0              ,0              ,0      // Red
            ,0          ,G              ,0      ,0              ,0      // Green
            ,0          ,0              ,B              ,0              ,0              // Blue
            ,0          ,0              ,0      ,A              ,alpha  // Alpha
        };

        float[] matrix = new float[] {
            itens   ,itens  ,itens  ,0              ,0    // Red
            ,itens       ,itens  ,itens  ,0              ,0    // Green
            ,itens       ,itens  ,itens  ,0              ,0    // Blue
            ,0           ,0              ,0              ,A              ,0    // Alpha
        };
        cm.setConcat(new ColorMatrix(mat), new ColorMatrix(matrix));
    }


    private static void changeHue(ColorMatrix cm, int newHue, int intens) {
        float cosVal = (float) Math.cos(newHue);
        float sinVal = (float) Math.sin(newHue);
        float lumR = 0.212671f;
        float lumG = 0.715160f;
        float lumB = 0.072169f;

        float[] mat = new float[] {
            lumR + cosVal * (1 - lumR) + sinVal * (-lumR) + .5f         ,
            lumG + cosVal * (-lumG) + sinVal * (-lumG)            ,
            lumB + cosVal * (-lumB) + sinVal * (1 - lumB)         , 0             , intens,
            lumR + cosVal * (-lumR) + sinVal * (0.143f)                 ,
            lumG + cosVal * (1 - lumG) + sinVal * (0.140f) + .5f  ,
            lumB + cosVal * (-lumB) + sinVal * (-0.283f)          , 0             , intens,
            lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR))    ,
            lumG + cosVal * (-lumG) + sinVal * (lumG)                     ,
            lumB + cosVal * (1 - lumB) + sinVal * (lumB)  + .5f   , 0             , intens,
            0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f
        };
        cm.postConcat(new ColorMatrix(mat));
    }





}
