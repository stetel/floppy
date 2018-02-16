package com.stetel.preferences;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * Preferences improves the standard SharedPreferences: faster to use, lots of helpful methods,
 * save every kind of object.<br/>
 * <br/>
 * Usage:
 * <ul>
 *  <li>
 *      Use Dagger to inject the var as a singleton.<br/>
 *      Create an application module and provide the Shared Preference instance.<br/>
 *      You don't need to provide Preferences, because it is Dagger ready.
 *      <pre><code>
 * {@literal @}Singleton
 * {@literal @}Provides
 *  SharedPreferences provideSharedPreferences() {
 *    return application.getSharedPreferences(BuildConfig.APPLICATION_ID, 0);
 *  }
 *      </code></pre>
 *  </li>
 *  <li>
 *      Extend the Application class and instantiate Preferences, then add a static getter.
 *      <pre><code>
 * public class MyApplication extends Application {
 *   private Preferences preferences;
 *
 *  {@literal @}Override
 *   public void onCreate() {
 *     super.onCreate();
 *     this.preferences = new Preferences(getSharedPreferences(BuildConfig.APPLICATION_ID, 0));
 *   }
 *
 *   public static Preferences getPreferences(Context context) {
 *     return ((MyApplication)context.getApplicationContext()).preferences;
 *   }
 * }
 *      </code></pre>
 *  </li>
 *  <li>Generate a new instance every time you need it</li>
 * </ul>
 */
public class Preferences {
    private SharedPreferences sharedPreferences;
    private static final Gson gson = new Gson();
    private static final Type STRING_SET_TYPE = new TypeToken<Set<String>>(){}.getType();
    private static final Type INTEGER_SET_TYPE = new TypeToken<Set<Integer>>(){}.getType();
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type INTEGER_LIST_TYPE = new TypeToken<List<Integer>>(){}.getType();
    private static final Type STRING_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    private static final Type INTEGER_MAP_TYPE = new TypeToken<Map<String, Integer>>(){}.getType();

    /**
     * Creates an instance passing the SharedPreferences.
     *
     * @param sharedPreferences Android SharedPreferences
     */
    @Inject
    public Preferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @return Saved boolean value or false if not present
     */
    public boolean getBoolean(String name) {
        return sharedPreferences.getBoolean(name, false);
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved boolean value or defValue if not present
     */
    public boolean getBoolean(String name, boolean defValue) {
        return sharedPreferences.getBoolean(name, defValue);
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @return Saved integer value or 0 if not present
     */
    public int getInt(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved integer value or defValue if not present
     */
    public int getInt(String name, int defValue) {
        return sharedPreferences.getInt(name, defValue);
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @return Saved boolean value or 0 if not present
     */
    public float getFloat(String name) {
        return sharedPreferences.getFloat(name, 0);
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved float value or defValue if not present
     */
    public float getFloat(String name, float defValue) {
        return sharedPreferences.getFloat(name, defValue);
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @return Saved long value or 0 if not present
     */
    public long getLong(String name) {
        return sharedPreferences.getLong(name, 0);
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved long value or defValue if not present
     */
    public long getLong(String name, long defValue) {
        return sharedPreferences.getLong(name, defValue);
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @return Saved String value or null if not present
     */
    public String getString(String name) {
        return sharedPreferences.getString(name, null);
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved String value or defValue if not present
     */
    public String getString(String name, String defValue) {
        return sharedPreferences.getString(name, defValue);
    }

    /**
     * Get a String Set.
     *
     * @param name name of the var
     * @return Saved String Set or null if not present
     */
    public Set<String> getStringSet(String name) {
        return getSet(STRING_SET_TYPE, name);
    }

    /**
     * Get an Integer Set.
     *
     * @param name name of the var
     * @return Saved Integer Set or null if not present
     */
    public Set<Integer> getIntegerSet(String name) {
        return getSet(INTEGER_SET_TYPE, name);
    }

    /**
     * Get a generic Set.<br/>
     * <br/>
     * You must include the Google GSON library to use this method (com.google.code.gson:gson)<br/>
     * Then create a new TypeToken, e.g.
     * <pre><code>{@literal Type type = new TypeToken<Set<MyClass>>(){}.getType();}</pre></code>
     *
     * @param type generic type
     * @param name name of the var
     * @return Saved generic Set or null if not present
     */
    public <E> Set<E> getSet(Type type, String name) {
        String setString = sharedPreferences.getString(name, null);
        if (setString == null) {
            return null;
        }
        if (setString.isEmpty()) {
            return new HashSet<>();
        }
        return gson.fromJson(setString, type);
    }

    /**
     * Get a String List.
     *
     * @param name name of the var
     * @return Saved String List or null if not present
     */
    public List<String> getStringList(String name) {
        return getList(STRING_LIST_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer List or null if not present
     */
    public List<Integer> getIntegerList(String name) {
        return getList(INTEGER_LIST_TYPE, name);
    }

    /**
     * Get a generic List.<br/>
     * <br/>
     * You must include the Google GSON library to use this method (com.google.code.gson:gson)<br/>
     * Then create a new TypeToken, e.g.
     * <pre><code>{@literal Type type = new TypeToken<List<MyClass>>(){}.getType();}</pre></code>
     *
     * @param type generic type
     * @param name name of the var
     * @return Saved generic List or null if not present
     */
    public <E> List<E> getList(Type type, String name) {
        String listString = sharedPreferences.getString(name, null);
        if (listString == null) {
            return null;
        }
        if (listString.isEmpty()) {
            return new ArrayList<>();
        }
        return gson.fromJson(listString, type);
    }

    /**
     * Get a String Map.
     *
     * @param name name of the var
     * @return Saved String Map or null if not present
     */
    public Map<String, String> getStringMap(String name) {
        return getMap(STRING_MAP_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer List or null if not present
     */
    public Map<String, Integer> getIntegerMap(String name) {
        return getMap(INTEGER_MAP_TYPE, name);
    }

    /**
     * Get a generic Map.<br/>
     * <br/>
     * You must include the Google GSON library to use this method (com.google.code.gson:gson)<br/>
     * Then create a new TypeToken, e.g.
     * <pre><code>{@literal Type type = new TypeToken<Map<String, MyClass>>(){}.getType();}</pre></code>
     *
     * @param type generic type
     * @param name name of the var
     * @return Saved generic Map or null if not present
     */
    public <K, V> Map<K, V> getMap(Type type, String name) {
        String mapString = sharedPreferences.getString(name, null);
        if (mapString == null) {
            return null;
        }
        if (mapString.isEmpty()) {
            return new HashMap<>();
        }
        return gson.fromJson(mapString, type);
    }

    /**
     * Get an Enum value.
     *
     * @param enumType enum class
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved Enum value or defValue if not present
     */
    public <T extends Enum<T>> T getEnum(Class<T> enumType, String name, T defValue) {
        String enumString = sharedPreferences.getString(name, defValue.name());
        return Enum.valueOf(enumType, enumString);
    }

    /**
     * Get a custom object.
     *
     * @param cls Object class
     * @param name name of the var
     * @return Saved custom object or null if not present
     */
    public <T> T get(Class<T> cls, String name) {
        String objString = sharedPreferences.getString(name, null);
        if (objString == null) {
            return null;
        }
        if (objString.isEmpty()) {
            try {
                return cls.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return gson.fromJson(objString, cls);
    }

    /**
     * Get a custom object with a generic type.<br/>
     * <br/>
     * You must include the Google GSON library to use this method (com.google.code.gson:gson)<br/>
     * Then create a new TypeToken, e.g.
     * <pre><code>{@literal Type type = new TypeToken<CustomClass<String>>(){}.getType();}</pre></code>
     *
     * @param name name of the var
     * @return Saved custom object or null if not present
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Type type, String name) {
        String objString = sharedPreferences.getString(name, null);
        if (objString == null) {
            return null;
        }
        if (objString.isEmpty()) {
            try {
                TypeToken<T> typeToken = (TypeToken<T>) TypeToken.get(type);
                return (T) typeToken.getRawType().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return gson.fromJson(objString, type);
    }

    /**
     * Set a var containing any value
     *
     * @param name name of the var
     * @param value Any primitive or object
     */
    public void set(String name, Object value) {
        set(Collections.singletonMap(name, value));
    }

    /**
     * Set multiple vars containing any value.<br/>
     * The arguments must be passed in name/value pairs where the name is always a String and value
     * can be any object.<br/>
     * Using this method is more efficient in case you need to save multiple vars.
     *
     * @param namesValues array of names and values
     */
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

    /**
     * Set multiple vars containing any value.<br/>
     * Using this method is more efficient in case you need to save multiple vars.
     *
     * @param namesValues map of names and values
     */
    public void set(Map<String, Object> namesValues) {
        if (namesValues != null && namesValues.size() > 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
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

    /**
     * Increment an integer value.
     *
     * @param name name of the var
     * @param defValue default value to use before the increment if the var is not present
     * @return Saved integer value after the increment
     */
    public int increment(String name, int defValue) {
        int val = getInt(name, defValue);
        if (val < Integer.MAX_VALUE) {
            val++;
        }
        set(name, val);
        return val;
    }

    /**
     * Decrement an integer value.
     *
     * @param name name of the var
     * @param defValue default value to use before the decrement if the var is not present
     * @return Saved integer value after the decrement
     */
    public int decrement(String name, int defValue) {
        int val = getInt(name, defValue);
        if (val > Integer.MIN_VALUE) {
            val--;
        }
        set(name, val);
        return val;
    }

    /**
     * Remove vars.
     *
     * @param names array of vars to remove
     */
    public void remove(String... names) {
        for (String name : names) {
            set(name, null);
        }
    }

    /**
     * Remove all the vars.
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
