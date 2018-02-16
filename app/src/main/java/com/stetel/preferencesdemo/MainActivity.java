package com.stetel.preferencesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.stetel.preferences.Preferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Preferences preferences = MyApplication.getPreferences(this);
        // set
        preferences.set("hello", "Hello world!", "bye", "Bye world!", "times", 2);

        Set<String> numberSet = new HashSet<>();
        numberSet.add("one");
        numberSet.add("two");
        numberSet.add("three");
        preferences.set("numbers", numberSet);

        List<String> rgbList = new ArrayList<>();
        rgbList.add("red");
        rgbList.add("green");
        rgbList.add("blue");
        preferences.set("rgbList", rgbList);

        preferences.set("child", new Person("Dave", 7));

        // need package 'com.google.code.gson:gson' to retrieve the var later
        Map<String, Person> parentsMap = new HashMap<>();
        parentsMap.put("Father", new Person("Jack", 40));
        parentsMap.put("Mother", new Person("Annie", 35));
        preferences.set("parentsMap", parentsMap);

        Wrapper<Person> wrapper = new Wrapper<>(new Person("grandfather", 65));
        preferences.set("wrapper", wrapper);
        // get and log
        Log.i(TAG, "Greetings: " + preferences.getString("hello") + ", " +
                preferences.getString("bye") + " x" + preferences.getInt("times", 0));

        Set<String> retrievedSet = preferences.getStringSet("numbers");
        for (String myString : retrievedSet) {
            Log.i(TAG, "Numbers item: " + myString);
        }
        List<String> retrievedList = preferences.getStringList("rgbList");
        for (String myString : retrievedList) {
            Log.i(TAG, "RGB list item: " + myString);
        }

        Person retrievedClass = preferences.get(Person.class, "myClass");
        Log.i(TAG, "Child name: " + retrievedClass.getName() + ", age: " +
                retrievedClass.getAge());

        // TypeToken is inside the package 'com.google.code.gson:gson'
        Type mapType = new TypeToken<Map<String, Person>>() {}.getType();
        Map<String, Person> retrievedMap = preferences.getMap(mapType, "parentsMap");
        for (Map.Entry<String, Person> entry: retrievedMap.entrySet()){
            Log.i(TAG, entry.getKey() + " name: " +
                    entry.getValue().getName() + ", age: " + entry.getValue().getAge());
        }

        Type type = new TypeToken<Wrapper<Person>>() {}.getType();
        Wrapper<Person> retrievedWrapper = preferences.get(type, "wrapper");
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
