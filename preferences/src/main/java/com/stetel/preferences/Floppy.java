package com.stetel.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Floppy improves the standard SharedPreferences: faster to use, lots of helpful methods,
 * save every kind of object, keep tracks of app versions changes.<br/>
 * <br/>
 * To start using it retrieve an instance via the insert() method.
 */
public class Floppy implements Serializable {
    private static final String __APP_VERSION_CODE = "__APP_VERSION_CODE";
    private static final Gson gson = new Gson();
    private static final Type STRING_SET_TYPE = new TypeToken<Set<String>>(){}.getType();
    private static final Type INTEGER_SET_TYPE = new TypeToken<Set<Integer>>(){}.getType();
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type INTEGER_LIST_TYPE = new TypeToken<List<Integer>>(){}.getType();
    private static final Type STRING_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    private static final Type INTEGER_MAP_TYPE = new TypeToken<Map<String, Integer>>(){}.getType();
    private static volatile Floppy instance;
    private SharedPreferences sharedPreferences;
    private Versions versions;

    /**
     * Retrieve a Floppy instance.
     *
     * @param context Context
     * @return An instance of this class
     */
    public static Floppy insert(Context context) {
        if (instance == null) {
            synchronized (Floppy.class) {
                if (instance == null) {
                    instance = new Floppy(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * This method avoid the serialization of this object.
     *
     * @return
     */
    protected Floppy readResolve() {
        throw new RuntimeException("Floppy class is not serializable");
    }

    /**
     * Cloning is not supported.
     *
     * @return Never returns
     * @throws CloneNotSupportedException This is always thrown
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Floppy class is a singleton");
    }

    /**
     * Private constructor which gets the SharedPreferences with the file name equals to the
     * package name and save the app version info.
     *
     * @param appContext App context
     */
    private Floppy(Context appContext) {
        if (instance != null) {
            throw new RuntimeException("Use insert() to get an instance of the Floppy class");
        }
        this.sharedPreferences = appContext.getSharedPreferences(appContext.getPackageName(), 0);
        int previousVersion = readInt(__APP_VERSION_CODE, -1);
        try {
            int currentVersion = appContext.getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0).versionCode;
            if (previousVersion != currentVersion) {
                write(__APP_VERSION_CODE, currentVersion);
            }
            if (previousVersion < 0) {
                this.versions = new Versions(currentVersion, currentVersion);
            } else {
                this.versions = new Versions(previousVersion, currentVersion);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // shouldn't happen
        }
    }

    /**
     * Allows to know if the app was updated since the last run.<br/>
     * <br/>
     * For example if you changed a var name in the new version of the app, you can use the
     * returned value to know what was the previous app version and copy the old var.<br/>
     * This is inspired to the SQLiteOpenHelper onUpgrade() method without the burden to specify
     * a separated Shared Floppy version.<br/>
     * <br/>
     * <b>Important:</b> you will get the versions information only the first time you invoke this method.
     *  From the second time and on, the information of the previous version is lost. It is
     *  recommended to call this method as early as possible (e.g. extended Application class) and
     *  proceed to update the data.
     *
     * @return Versions information
     */
    public Versions checkUpdate() {
        Versions tempVersions = versions.clone();
        versions = new Versions(tempVersions.getCurrent(), tempVersions.getCurrent());
        return tempVersions;
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @return Saved boolean value or false if not present
     */
    public boolean readBoolean(String name) {
        return sharedPreferences.getBoolean(name, false);
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved boolean value or defValue if not present
     */
    public boolean readBoolean(String name, boolean defValue) {
        return sharedPreferences.getBoolean(name, defValue);
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @return Saved integer value or 0 if not present
     */
    public int readInt(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved integer value or defValue if not present
     */
    public int readInt(String name, int defValue) {
        return sharedPreferences.getInt(name, defValue);
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @return Saved boolean value or 0 if not present
     */
    public float readFloat(String name) {
        return sharedPreferences.getFloat(name, 0);
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved float value or defValue if not present
     */
    public float readFloat(String name, float defValue) {
        return sharedPreferences.getFloat(name, defValue);
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @return Saved long value or 0 if not present
     */
    public long readLong(String name) {
        return sharedPreferences.getLong(name, 0);
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved long value or defValue if not present
     */
    public long readLong(String name, long defValue) {
        return sharedPreferences.getLong(name, defValue);
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @return Saved String value or null if not present
     */
    public String readString(String name) {
        return sharedPreferences.getString(name, null);
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved String value or defValue if not present
     */
    public String readString(String name, String defValue) {
        return sharedPreferences.getString(name, defValue);
    }

    /**
     * Get a String Set.
     *
     * @param name name of the var
     * @return Saved String Set or null if not present
     */
    public Set<String> readStringSet(String name) {
        return readSet(STRING_SET_TYPE, name);
    }

    /**
     * Get an Integer Set.
     *
     * @param name name of the var
     * @return Saved Integer Set or null if not present
     */
    public Set<Integer> readIntegerSet(String name) {
        return readSet(INTEGER_SET_TYPE, name);
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
    public <E> Set<E> readSet(Type type, String name) {
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
    public List<String> readStringList(String name) {
        return readList(STRING_LIST_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer List or null if not present
     */
    public List<Integer> readIntegerList(String name) {
        return readList(INTEGER_LIST_TYPE, name);
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
    public <E> List<E> readList(Type type, String name) {
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
    public Map<String, String> readStringMap(String name) {
        return readMap(STRING_MAP_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer List or null if not present
     */
    public Map<String, Integer> readIntegerMap(String name) {
        return readMap(INTEGER_MAP_TYPE, name);
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
    public <K, V> Map<K, V> readMap(Type type, String name) {
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
    public <T extends Enum<T>> T readEnum(Class<T> enumType, String name, T defValue) {
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
    public <T> T read(Class<T> cls, String name) {
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
    public <T> T read(Type type, String name) {
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
    public void write(String name, Object value) {
        write(Collections.singletonMap(name, value));
    }

    /**
     * Set multiple vars containing any value.<br/>
     * The arguments must be passed in name/value pairs where the name is always a String and value
     * can be any object.<br/>
     * Using this method is more efficient in case you need to save multiple vars.
     *
     * @param namesValues array of names and values
     */
    public void write(Object... namesValues) {
        if (namesValues != null && namesValues.length > 0) {
            if (namesValues.length % 2 != 0) {
                throw new IllegalArgumentException("namesValues must be a name/value argument list");
            }
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < namesValues.length; i += 2) {
                map.put((String) namesValues[i], namesValues[i+1]);
            }
            write(map);
        }
    }

    /**
     * Set multiple vars containing any value.<br/>
     * Using this method is more efficient in case you need to save multiple vars.
     *
     * @param namesValues map of names and values
     */
    public void write(Map<String, Object> namesValues) {
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
        int val = readInt(name, defValue);
        if (val < Integer.MAX_VALUE) {
            val++;
        }
        write(name, val);
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
        int val = readInt(name, defValue);
        if (val > Integer.MIN_VALUE) {
            val--;
        }
        write(name, val);
        return val;
    }

    /**
     * Remove vars.
     *
     * @param names array of vars to remove
     */
    public void delete(String... names) {
        for (String name : names) {
            write(name, null);
        }
    }

    /**
     * Remove all the vars.
     */
    public void eject() {
        sharedPreferences.edit().clear().apply();
    }
}
