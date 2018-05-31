package com.stetel.floppydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.stetel.floppy.Floppy;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Floppy floppy = Floppy.insert(this);
        // set
        floppy.write("hello", "Hello world!", "bye", "Bye world!", "times", 2);

        Set<String> numberSet = new HashSet<>();
        numberSet.add("one");
        numberSet.add("two");
        numberSet.add("three");
        floppy.write("numbers", numberSet);

        List<String> rgbList = new ArrayList<>();
        rgbList.add("red");
        rgbList.add("green");
        rgbList.add("blue");
        floppy.write("rgbList", rgbList);

        floppy.write("child", new Person("Dave", 7));

        // need package 'com.google.code.gson:gson' to retrieve the var later
        Map<String, Person> parentsMap = new HashMap<>();
        parentsMap.put("Father", new Person("Jack", 40));
        parentsMap.put("Mother", new Person("Annie", 35));
        floppy.write("parentsMap", parentsMap);

        Wrapper<Person> wrapper = new Wrapper<>(new Person("grandfather", 65));
        floppy.write("wrapper", wrapper);
        // get and log
        Log.i(TAG, "onUpgrade setup: " + floppy.readString("setup"));

        Log.i(TAG, "Update info: " + floppy.readString("updatedInfo"));

        Log.i(TAG, "Contains hello: " + floppy.contains("hello"));
        Log.i(TAG, "Contains seeya: " + floppy.contains("seeya"));
        Log.i(TAG, "Contains both hello & seeya: " + floppy.contains("hello", "seeya"));

        Log.i(TAG, "Greetings: " + floppy.readString("hello") + ", " +
                floppy.readString("bye") + " x" + floppy.readInt("times", 0));

        Set<String> retrievedSet = floppy.readStringSet("numbers");
        for (String myString : retrievedSet) {
            Log.i(TAG, "Numbers item: " + myString);
        }
        List<String> retrievedList = floppy.readStringList("rgbList");
        for (String myString : retrievedList) {
            Log.i(TAG, "RGB list item: " + myString);
        }

        Person retrievedClass = floppy.read(Person.class, "child");
        Log.i(TAG, "Child name: " + retrievedClass.getName() + ", age: " +
                retrievedClass.getAge());

        // TypeToken is inside the package 'com.google.code.gson:gson'
        Type mapType = new TypeToken<Map<String, Person>>() {}.getType();
        Map<String, Person> retrievedMap = floppy.read(mapType, "parentsMap");
        for (Map.Entry<String, Person> entry: retrievedMap.entrySet()){
            Log.i(TAG, entry.getKey() + " name: " +
                    entry.getValue().getName() + ", age: " + entry.getValue().getAge());
        }

        Type type = new TypeToken<Wrapper<Person>>() {}.getType();
        Wrapper<Person> retrievedWrapper = floppy.read(type, "wrapper");
        Log.i(TAG, "Wrapper grandfather name: " + retrievedWrapper.toString());
    }

    static class Person {
        private String name;
        private int age;

        Person() {
            this.name = "Nobody";
            this.age = -1;
        }

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    static class Wrapper<K> {
        K object;

        Wrapper() {

        }

        Wrapper(K object) {
            this.object = object;
        }

        @Override
        public String toString() {
            return object == null ? "No object" : object.toString();
        }
    }
}
