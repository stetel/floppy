package com.stetel.preferences;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Preferences {
    private SharedPreferences preferences;
    private static final Gson gson = new Gson();
    private static final Type STRING_SET_TYPE = new TypeToken<Set<String>>(){}.getType();
    private static final Type INTEGER_SET_TYPE = new TypeToken<Set<Integer>>(){}.getType();
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type INTEGER_LIST_TYPE = new TypeToken<List<Integer>>(){}.getType();
    private static final Type STRING_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    private static final Type INTEGER_MAP_TYPE = new TypeToken<Map<String, Integer>>(){}.getType();

    public Preferences(SharedPreferences preferencesInstance) {
        this.preferences = preferencesInstance;
    }

    public boolean getBoolean(String name) {
        return preferences.getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defValue) {
        return preferences.getBoolean(name, defValue);
    }

    public int getInt(String name) {
        return preferences.getInt(name, 0);
    }

    public int getInt(String name, int defValue) {
        return preferences.getInt(name, defValue);
    }

    public float getFloat(String name) {
        return preferences.getFloat(name, 0);
    }

    public float getFloat(String name, float defValue) {
        return preferences.getFloat(name, defValue);
    }

    public long getLong(String name) {
        return preferences.getLong(name, 0);
    }

    public long getLong(String name, long defValue) {
        return preferences.getLong(name, defValue);
    }

    public String getString(String name) {
        return preferences.getString(name, null);
    }

    public String getString(String name, String defValue) {
        return preferences.getString(name, defValue);
    }

    public Set<String> getStringSet(String name) {
        return getSet(STRING_SET_TYPE, name);
    }

    public Set<Integer> getIntegerSet(String name) {
        return getSet(INTEGER_SET_TYPE, name);
    }

    public <E> Set<E> getSet(Type type, String name) {
        String setString = preferences.getString(name, null);
        if (TextUtils.isEmpty(setString)) {
            return null;
        }
        return gson.fromJson(setString, type);
    }

    public List<String> getStringList(String name) {
        return getList(STRING_LIST_TYPE, name);
    }

    public List<Integer> getIntegerList(String name) {
        return getList(INTEGER_LIST_TYPE, name);
    }

    public <E> List<E> getList(Type type, String name) {
        String listString = preferences.getString(name, null);
        if (TextUtils.isEmpty(listString)) {
            return null;
        }
        return gson.fromJson(listString, type);
    }

    public Map<String, String> getStringMap(String name) {
        return getMap(STRING_MAP_TYPE, name);
    }

    public Map<String, Integer> getIntegerMap(String name) {
        return getMap(INTEGER_MAP_TYPE, name);
    }

    public <K, V> Map<K, V> getMap(Type type, String name) {
        String mapString = preferences.getString(name, null);
        if (TextUtils.isEmpty(mapString)) {
            return null;
        }
        return gson.fromJson(mapString, type);
    }

    public <T extends Enum<T>> T getEnum(Class<T> enumType, String name, T defValue) {
        String enumString = preferences.getString(name, defValue.name());
        return Enum.valueOf(enumType, enumString);
    }

    public <T> T get(Class<T> cls, String name) {
        String objString = preferences.getString(name, null);
        if (TextUtils.isEmpty(objString)) {
            return null;
        }
        return gson.fromJson(objString, cls);
    }

    public void set(String name, Object value) {
        set(Collections.singletonMap(name, value));
    }

    public void set(Object... namesValues) {
        if (namesValues != null && namesValues.length > 0) {
            if (namesValues.length % 2 != 0) {
                throw new IllegalArgumentException("namesValues must be a name/value argument list");
            }
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < namesValues.length; i += 2) {
                map.put((String) namesValues[i], namesValues[i+1]);
            }
            set(map);
        }
    }

    public void set(Map<String, Object> namesValues) {
        if (namesValues != null && namesValues.size() > 0) {
            SharedPreferences.Editor editor = preferences.edit();
            for (Map.Entry<String, Object> nameValuePair : namesValues.entrySet()) {
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
                } else if (value instanceof Enum) {
                    editor.putString(name, ((Enum) value).name());
                } else {
                    editor.putString(name, gson.toJson(value));
                }
            }
            editor.apply();
        }
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
