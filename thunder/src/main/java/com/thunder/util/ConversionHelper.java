package com.thunder.util;

/*
 * Copyright (C) 2015 Michele Paparella
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.TypedValue;

/**
 * a class that provides useful methods for converting stuff
 */
public class ConversionHelper {

    /**
     *
     * @param context
     * @param dp value in dp
     * @return the pixel value
     */
    public static float dpToPixel(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
