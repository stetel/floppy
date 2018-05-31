package com.stetel.floppy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
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
    private static final String __FLOPPY_DRIVE_VERSION_ = "__FLOPPY_DRIVE_VERSION_";
    private static final Gson gson = new Gson();
    private static final Type STRING_SET_TYPE = new TypeToken<Set<String>>(){}.getType();
    private static final Type INTEGER_SET_TYPE = new TypeToken<Set<Integer>>(){}.getType();
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>(){}.getType();
    private static final Type INTEGER_LIST_TYPE = new TypeToken<List<Integer>>(){}.getType();
    private static final Type STRING_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
    private static final Type INTEGER_MAP_TYPE = new TypeToken<Map<String, Integer>>(){}.getType();
    private static volatile Floppy instance;
    private SharedPreferences sharedPreferences;

    /**
     * Use this method to define the current version of your SharedPreference's variables (alias Floppy Drive)<br/>
     * You should change this number every time you make some big changes to the structure of your variables.<br/>
     * This is inspired to the SQLiteOpenHelper onUpgrade() method.<br/>
     * <br/>
     * E.g.: You start implementing Floppy and define the version as 1, then you save a var called "setup" with the value _true_.
     * You decide to add new setup steps, so you want to change the setup var to contain strings instead of booleans.
     * So you change to version 2 where the old `setup = true` corresponds to `setup = "account"` and `setup = false` to `setup = "none"`.<br/>
     * <br/>
     * <b>Important:</b> this method will set the new version and call the Loader.onUpgrade() if the version is different from the previous one.
     *  From the second time and on, the information of the previous version is lost because it was overwritten by the first invocation of the method.
     *
     * @param context Context
     * @param version Current version
     * @param loader Interface which is called if and only if the previous version is different to the current one
     */
    public static void driveUpgrade(Context context, int version, Loader loader) {
        Floppy floppy = Floppy.insert(context);
        int previousVersion = floppy.readInt(__FLOPPY_DRIVE_VERSION_, -1);
        if (previousVersion != version) {
            floppy.write(__FLOPPY_DRIVE_VERSION_, version);
        }
        if (previousVersion >= 0) {
            loader.onUpgrade(floppy, previousVersion, version);
        }
    }

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
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @return Saved boolean value or false if not present
     */
    public boolean readBoolean(String name) {
        return readBoolean(name, false);
    }

    /**
     * Get a boolean value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved boolean value or defValue if not present
     */
    public boolean readBoolean(String name, boolean defValue) {
        try {
            return sharedPreferences.getBoolean(name, defValue);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not 'boolean'");
        }
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @return Saved integer value or 0 if not present
     */
    public int readInt(String name) {
        return readInt(name, 0);
    }

    /**
     * Get an integer value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved integer value or defValue if not present
     */
    public int readInt(String name, int defValue) {
        try {
            return sharedPreferences.getInt(name, defValue);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not 'int'");
        }
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @return Saved boolean value or 0 if not present
     */
    public float readFloat(String name) {
        return readFloat(name, 0);
    }

    /**
     * Get a float value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved float value or defValue if not present
     */
    public float readFloat(String name, float defValue) {
        try {
            return sharedPreferences.getFloat(name, defValue);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not 'float'");
        }
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @return Saved long value or 0 if not present
     */
    public long readLong(String name) {
        return readLong(name, 0);
    }

    /**
     * Get a long value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved long value or defValue if not present
     */
    public long readLong(String name, long defValue) {
        try {
            return sharedPreferences.getLong(name, defValue);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not 'long'");
        }
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @return Saved String value or null if not present
     */
    public String readString(String name) {
        return readString(name, null);
    }

    /**
     * Get a String value.
     *
     * @param name name of the var
     * @param defValue default value if the var is not present
     * @return Saved String value or defValue if not present
     */
    public String readString(String name, String defValue) {
        try {
            return sharedPreferences.getString(name, defValue);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not 'String'");
        }
    }

    /**
     * Get a String Set.
     *
     * @param name name of the var
     * @return Saved String Set or null if not present
     */
    public Set<String> readStringSet(String name) {
        return read(STRING_SET_TYPE, name);
    }

    /**
     * Get an Integer Set.
     *
     * @param name name of the var
     * @return Saved Integer Set or null if not present
     */
    public Set<Integer> readIntegerSet(String name) {
        return read(INTEGER_SET_TYPE, name);
    }

    /**
     * Get a String List.
     *
     * @param name name of the var
     * @return Saved String List or null if not present
     */
    public List<String> readStringList(String name) {
        return read(STRING_LIST_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer List or null if not present
     */
    public List<Integer> readIntegerList(String name) {
        return read(INTEGER_LIST_TYPE, name);
    }

    /**
     * Get a String Map.
     *
     * @param name name of the var
     * @return Saved String Map or null if not present
     */
    public Map<String, String> readStringMap(String name) {
        return read(STRING_MAP_TYPE, name);
    }

    /**
     * Get an Integer List.
     *
     * @param name name of the var
     * @return Saved Integer Map or null if not present
     */
    public Map<String, Integer> readIntegerMap(String name) {
        return read(INTEGER_MAP_TYPE, name);
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
        try {
            String enumString = sharedPreferences.getString(name, defValue.name());
            return Enum.valueOf(enumType, enumString);
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not " + enumType.toString());
        }
    }

    /**
     * Get a custom object.
     *
     * @param cls Object class
     * @param name name of the var
     * @return Saved custom object or null if not present
     */
    public <T> T read(Class<T> cls, String name) {
        try {
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
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not '" + cls.toString() + "'");
        }
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
        try {
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
        } catch (ClassCastException e) {
            throw new RuntimeException("The type of the requested var is not '" + type.toString() + "'");
        }
    }

    /**
     * Set a var containing any value<br/>
     * <br/>
     * <i>Note: Objects representing primitives are converted to primitive for better efficiency.
     * Saving an Integer require to use readInt(...) and not read(Integer.class,...)</i>
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
     * Using this method is more efficient in case you need to save multiple vars.<br/>
     * <br/>
     * <i>Note: Objects representing primitives are converted to primitive for better efficiency.
     * Saving an Integer require to use readInt(...) and not read(Integer.class,...)</i>
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
     * Using this method is more efficient in case you need to save multiple vars.<br/>
     * <br/>
     * <i>Note: Objects representing primitives are converted to primitive for better efficiency.
     * Saving an Integer require to use readInt(...) and not read(Integer.class,...)</i>
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
    public int writeIncrement(String name, int defValue) {
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
    public int writeDecrement(String name, int defValue) {
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
    public void format() {
        int version = readInt(__FLOPPY_DRIVE_VERSION_, -1);
        sharedPreferences.edit().clear().apply();
        write(__FLOPPY_DRIVE_VERSION_, version);
    }
}
