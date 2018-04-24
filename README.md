# Introduction
Floppy improves the standard SharedPreferences: faster to use, lots of helpful methods, save every kind of object, keep tracks of app versions changes.

Insert the disk to read and write data.

Note: Floppy is not thread safe to ensure maximum performances. You should take care of synchronization only when you read/write the same var from different threads.

# Importing
The library can be imported by adding a dependency in the Gradle build file.
```
implementation 'com.stetel:floppy:1.0.0'
```
Please check what is the latest version.
See [Stetel Maven repository website](https://maven.stetel.com/help.jsp) for more information.

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

# Advanced
Floppy keeps track of the version code of your app, you can use this information to make changes when you publish a new update.

E.g.: In version 1.0 (code 10) of your app, you have a var saved as "setup" with the value _true_.
You decide to add new setup steps, so you want to change the setup var to contain strings instead of booleans.
So in version 1.1 (code 11) the old `setup = true` corresponds to `setup = "account"` and `setup = false` to `setup = "none"`.

As soon as your app starts in the extended Application class, use the checkUpdate() method to see if the app was just updated and perform the change.
```
Floppy floppy = Floppy.insert(this);
Versions versions = floppy.checkUpdate();
if (versions.isUpdated()) {
    if (versions.getPrevious() < 11) {
        boolean setupBool = floppy.getBoolean("setup");
        floppy.write("setup", setupBool ? "account" : "none");
    }
}
```
This is inspired to the SQLiteOpenHelper onUpgrade() method without the burden to specify a separated version.