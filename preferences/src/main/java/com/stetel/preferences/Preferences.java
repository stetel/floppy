package com.stetel.preferences;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Preferences {
    private Gson gson = new Gson();
    private SharedPreferences preferences;
    private Type stringListType = new TypeToken<List<String>>(){}.getType();
    private Type integerListType = new TypeToken<List<Integer>>(){}.getType();
    private Type stringMapType = new TypeToken<Map<String, String>>(){}.getType();
    private Type integerMapType = new TypeToken<Map<String, Integer>>(){}.getType();

    public Preferences(SharedPreferences preferencesInstance) {
        this.preferences = preferencesInstance;
    }

    public boolean getBoolean(String name) {
        return preferences.getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defValue) {
        return preferences.getBoolean(name, defValue);
    }

    public int getInt(String name, int defValue) {
        return preferences.getInt(name, defValue);
    }

    public float getFloat(String name, float defValue) {
        return preferences.getFloat(name, defValue);
    }

    public long getLong(String name, long defValue) {
        return preferences.getLong(name, defValue);
    }

    public String getString(String name) {
        return preferences.getString(name, null);
    }

    public Set<String> getStringSet(String name) {
        Set<String> set = preferences.getStringSet(name, null);
        if (set == null) {
            return null;
        } else {
            return new HashSet<>(set);
        }
    }

    public <T extends Enum<T>> T getEnum(Class<T> type, String name, T defValue) {
        String enumString = preferences.getString(name, defValue.name());
        return Enum.valueOf(type, enumString);
    }

    public List<String> getStringList(String name) {
        return getList(stringListType, name);
    }

    public List<String> getIntegerList(String name) {
        return getList(integerListType, name);
    }

    public <E> List<E> getList(Type type, String name) {
        String mapString = preferences.getString(name, null);
        return gson.fromJson(mapString, type);
    }

    public Map<String, String> getStringMap(String name) {
        return getMap(stringMapType, name);
    }

    public Map<String, Integer> getIntegerMap(String name) {
        return getMap(integerMapType, name);
    }

    public <K, V> Map<K, V> getMap(Type type, String name) {
        String mapString = preferences.getString(name, null);
        return gson.fromJson(mapString, type);
    }

    public <T> T get(Class<T> cls, String name) {
        String mapString = preferences.getString(name, null);
        return gson.fromJson(mapString, cls);
    }

    public void set(String name, Object value) {
        set(Collections.singletonMap(name, value));
    }

    public void set(Map<String, Object> nameValuePairs) {
        SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<String, Object> nameValuePair : nameValuePairs.entrySet()) {
            String name = nameValuePair.getKey();
            Object value = nameValuePair.getValue();
            if (value == null) {
                editor.remove(name);
            } else if (value instanceof Boolean) {
                editor.putBoolean(name, (boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(name, (int) value);
            } else if (value instanceof Float) {
                editor.putFloat(name, (float) value);
            } else if (value instanceof Long) {
                editor.putLong(name, (long) value);
            } else if (value instanceof String) {
                editor.putString(name, (String) value);
            } else if (value instanceof Set) {
                editor.putStringSet(name, (Set) value);
            } else if (value instanceof Enum) {
                editor.putString(name, ((Enum) value).name());
            } else {
                editor.putString(name, gson.toJson(value));
            }
        }
        editor.apply();
    }

    public int increment(String name, int defValue) {
        int val = getInt(name, defValue);
        if (val < Integer.MAX_VALUE) {
            val++;
        }
        set(name, val);
        return val;
    }

    public int decrement(String name, int defValue) {
        int val = getInt(name, defValue);
        if (val > Integer.MIN_VALUE) {
            val--;
        }
        set(name, val);
        return val;
    }

    public void remove(String... names) {
        for (String name : names) {
            set(name, null);
        }
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
