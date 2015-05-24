package com.thunder.prefs;

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

import android.animation.FloatEvaluator;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SharedPrefsManager {

    private SharedPreferences sharedPreferences;

    public SharedPrefsManager(Context context, String name, int mode){
        sharedPreferences = context.getSharedPreferences(name, mode);
    }

    /**
     * Be aware that you can use only the following types for values: Integer, Long, String,
     * Boolean, Float and Set<String>
     * @param defValues the default values for the keys, e.g. {key3=true, key2=2, key1=value1}
     * @return the related values stored in the preferences
     */
    public Map<String,Object> getValues(Map<String,Object> defValues){
        Map<String,Object> values = new HashMap<String,Object>();
        for (String key: defValues.keySet()){
            values.put(key, getValueFor(key, defValues.get(key)));
        }
        return values;
    }

    /**
     *
     * @param key
     * @param defValue
     * @return the object stored into the preferences, null otherwise
     */
    private Object getValueFor(String key, Object defValue) {
        if (defValue instanceof Integer){
            return getInt(key, (Integer) defValue);
        }
        if (defValue instanceof Long){
            return getLong(key, (Long) defValue);
        }
        if (defValue instanceof String){
            return getString(key, (String) defValue);
        }
        if (defValue instanceof Boolean){
            return getBoolean(key, (Boolean) defValue);
        }
        if (defValue instanceof Float){
            return getFloat(key, (Float) defValue);
        }
        if (defValue instanceof Set){
            return getStringSet(key, (Set) defValue);
        }
        return null;
    }


    /**
     *
     * @param key
     * @param value
     */
    private void putValueAsyncFor(String key, Object value) {
        if (value instanceof Integer){
            putIntAsync(key, (Integer) value);
        }
        if (value instanceof Long){
            putLongAsync(key, (Long) value);
        }
        if (value instanceof String){
            putStringAsync(key, (String) value);
        }
        if (value instanceof Boolean){
            putBooleanAsync(key, (Boolean) value);
        }
        if (value instanceof Float){
            putFloatAsync(key, (Float) value);
        }
        if (value instanceof Set){
            putStringSetAsync(key, (Set) value);
        }
    }

    public void putValuesAsync(Map<String,Object> defValues){
        for (String key: defValues.keySet()){
            putValueAsyncFor(key, defValues.get(key));
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    private void putValueSyncFor(String key, Object value) {
        if (value instanceof Integer){
            putIntSync(key, (Integer) value);
        }
        if (value instanceof Long){
            putLongSync(key, (Long) value);
        }
        if (value instanceof String){
            putStringSync(key, (String) value);
        }
        if (value instanceof Boolean){
            putBooleanSync(key, (Boolean) value);
        }
        if (value instanceof Float){
            putFloatSync(key, (Float) value);
        }
        if (value instanceof Set){
            putStringSetSync(key, (Set) value);
        }
    }

    public void putValuesSync(Map<String,Object> defValues){
        for (String key: defValues.keySet()){
            putValueSyncFor(key, defValues.get(key));
        }
    }

    public Boolean contains(String key){
        return sharedPreferences.contains(key);
    }

    public void incrementSync(String key, int value, int step){
        int oldValue = getInt(key, value);
        sharedPreferences.edit().putInt(key, oldValue+step).commit();
    }

    public void decrementSync(String key, int value, int step){
        int oldValue = getInt(key, value);
        sharedPreferences.edit().putInt(key, oldValue-step).commit();
    }

    public int getInt(String key, int defValue){
        return sharedPreferences.getInt(key, defValue);
    }

    public void putIntSync(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public void putIntAsync(String key, int value){
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public long getLong(String key, long defValue){
        return sharedPreferences.getLong(key, defValue);
    }

    public void putLongSync(String key, long value){
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public void putLongAsync(String key, long value){
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public String getString(String key, String defValue){
        return sharedPreferences.getString(key, defValue);
    }

    public void putStringSync(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putStringAsync(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public float getFloat(String key, float defValue){
        return sharedPreferences.getFloat(key, defValue);
    }

    public void putFloatSync(String key, float value){
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    public void putFloatAsync(String key, float value){
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue){
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void putBooleanSync(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public void putBooleanAsync(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public Set<String> getStringSet(String key, Set<String> defValue){
        return sharedPreferences.getStringSet(key, defValue);
    }

    public void putStringSetSync(String key, Set<String> value){
        sharedPreferences.edit().putStringSet(key, value).commit();
    }

    public void putStringSetAsync(String key, Set<String> value){
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    public void clearSync(){
        sharedPreferences.edit().clear().commit();
    }

}
