package com.example.comparewithmvvmrecyclerviewcrud;

public class People {
    String name;
    String age;

    public People(String name ,String age){
        this.name=name;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
