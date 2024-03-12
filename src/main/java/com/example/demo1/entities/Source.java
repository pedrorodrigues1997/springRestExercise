package com.example.demo1.entities;

import java.util.Objects;

public class Source {

    private int id;
    private String name;


    public static Source createSource() {
        return new Source();
    }

    public int getId() {
        return id;
    }

    public Source setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Source setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return id == source.id && Objects.equals(name, source.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
