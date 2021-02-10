# Introduction
Floppy improves the standard SharedPreferences: faster to use, lots of helpful methods, save every kind of object, keep tracks of app versions changes.

Insert the disk to read and write data.

Note: Floppy is not thread safe to ensure maximum performances. You should take care of synchronization only when you read/write the same var from different threads.

# Importing
The library is available at MavenCentral  and can be imported by adding a dependency in the Gradle build file.
```
implementation 'com.stetel:floppy:1.1.0'
```

Check if you have the correct repositories in the root (not app) gradle file.
``` 
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
``` 

# Usage
**Retrieve an instance of Floppy**
``` 
Floppy floppy = Floppy.insert(context);
```
You can now use _floppy_ to read and write any kind of objects inside the Shared Preferences.

**Simple**
```
floppy.write("greeting", "Hello world!");

String greeting = floppy.readString("greeting");
```

**Multiple vars**
```
// Array of arguments (name/value pairs)...
floppy.write("greeting", "Hello world!", "count", 3);

// ... or map
Map<String, Object> vars = new HashMap<>();
vars.put("greeting", "Hello world!");
vars.put("count", 3);
floppy.write(vars);
// get the vars
String greeting = floppy.readString("greeting");
int count = floppy.readInt("count");
```
It is recommended to use these instead of calling _write_ multiple times in the same place.

**Collections**
```
// Set
Set<String> numbersSet = new HashSet<>();
numberSet.add("one");
numberSet.add("two");
numberSet.add("three");
floppy.write("numbersSet", numbersSet);

Set<String> retrievedNumbersSet = floppy.readStringSet("numbersSet");

// List
List<String> rgbList = new ArrayList<>();
rgbList.add("red");
rgbList.add("green");
rgbList.add("blue");
floppy.write("rgbList", rgbList);

List<String> retrievedRgbList = floppy.readStringList("rgbList");

// Map
Map<String, Integer> numbersMap = new HashMap<>();
parentsMap.put("one", 1);
parentsMap.put("two", 2);
floppy.write("numbersMap", numbersMap);

Map<String, Integer> retrievedNumbersMap = floppy.readIntegerMap("numbersMap");
```

**Custom classes and generics**
```
// Custom object
floppy.write("jack", new Person("jack", 40));

Person jack = floppy.read(Person.class, "jack");

// List with custom object
List<Person> peopleList = new ArrayList<>();
peopleList.add(new Person("jack", 40));
peopleList.add(new Person("molly", 35));
floppy.write("people", peopleList);

Type peopleListType = new TypeToken<List<Person>() {}.getType();
List<Person> retrievedPeopleList = floppy.read(peopleListType, "people");
```
Add the dependency for TypeToken which is inside the package _'com.google.code.gson:gson'_.

**Check vars**
```
boolean exists = floppy.contains("var");
boolean bothExists = floppy.contains("var1", "var2"); // all the vars must exists
```

**Delete vars**
```
floppy.delete("var"); // single
floppy.delete("var1", "var2", "var3"); // multi
floppy.format(); // delete everything
```

# Advanced
Floppy can keep track of the version of your variables' list and you can use this information to make changes when you publish a new update.
This is inspired to the SQLiteOpenHelper onUpgrade() method.

E.g.: You start implementing Floppy and define the version as 1, then you save a var called "setup" with the value _true_.
You decide to add new setup steps, so you want to change the setup var to contain strings instead of booleans.
So you change to version 2 where the old `setup = true` corresponds to `setup = "account"` and `setup = false` to `setup = "none"`.

As soon as your app starts in the extended Application class, use the Floppy.driveUpgrade() method to see if your app is just updated and you need to perform additional operations.
```
public class MyApplication extends Application {
    private static final int FLOPPY_DRIVE_VERSION = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        Floppy.driveUpgrade(this, FLOPPY_DRIVE_VERSION, new Loader() {
            @Override
            public void onUpgrade(Floppy floppy, int previousVersion, int currentVersion) {
                if (previousVersion < 1) {
                    floppy.format();
                }
                if (previousVersion < 2) {
                    boolean setupBool = floppy.readBoolean("setup");
                    floppy.write("setup", setupBool ? "account" : "none");
                }
            }
        });
    }
}
```
It is recommended to use _cascading if statements_ based on _previousVersion_ to correctly handle all the upgrade scenarios.

Important: this method will set the new version and call the Loader.onUpgrade() if the version is different from the previous one.
From the second time and on, the information of the previous version is lost because it was overwritten by the first invocation of the method.

# Proguard
Floppy uses Google GSON lib to handle object saving, thus needs to add the rules for excluding certain classes from Proguards.
These rules are added automatically when you import Floppy via Gradle, so you don't have to worry about it.

# Authors
- Lorenzo Lombardo - _Stetel Srl_ - www.stetel.com

# License
Printer is available under the [The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)

# History

### 1.1.0 
- Added method to check if a var exists
- Added better exception explanation due to using a read() method with an incorrect type
- Changed: Improved javadocs, explaining some behavior of the write() methods
- Fixed: driveUpgrade listener is not called anymore if it is the first time starting the app

### 1.0.0 
- First release