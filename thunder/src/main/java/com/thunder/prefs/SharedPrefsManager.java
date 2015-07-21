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

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * a layer that allows a developer to call useful methods to deal with SharedPreferences
 */
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

    /**
     * async method
     * @param defValues
     */
    public void putValuesAsync(Map<String,Object> defValues){
        for (String key: defValues.keySet()){
            putValueAsyncFor(key, defValues.get(key));
        }
    }

    /**
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

    /**
     * sync method
     * @param defValues
     */
    public void putValuesSync(Map<String,Object> defValues){
        for (String key: defValues.keySet()){
            putValueSyncFor(key, defValues.get(key));
        }
    }

    /**
     *
     * @param key
     * @return true if the key is in the SharedPreferences file, false otherwise
     */
    public Boolean contains(String key){
        return sharedPreferences.contains(key);
    }

    /**
     *
     * @param key
     * @param defValue the default value if the key is not in the SharedPreferencesFile
     * @param step the value of the increment
     */
    public void incrementSync(String key, int defValue, int step){
        int oldValue = getInt(key, defValue);
        sharedPreferences.edit().putInt(key, oldValue+step).commit();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue){
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putIntSync(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putIntAsync(String key, int value){
        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue){
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putLongSync(String key, long value){
        sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putLongAsync(String key, long value){
        sharedPreferences.edit().putLong(key, value).apply();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue){
        return sharedPreferences.getString(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putStringSync(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putStringAsync(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public float getFloat(String key, float defValue){
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putFloatSync(String key, float value){
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putFloatAsync(String key, float value){
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue){
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putBooleanSync(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putBooleanAsync(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public Set<String> getStringSet(String key, Set<String> defValue){
        return sharedPreferences.getStringSet(key, defValue);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putStringSetSync(String key, Set<String> value){
        sharedPreferences.edit().putStringSet(key, value).commit();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putStringSetAsync(String key, Set<String> value){
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    /**
     *
     */
    public void clearSync(){
        sharedPreferences.edit().clear().commit();
    }

}
