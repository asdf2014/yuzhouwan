package com.yuzhouwan.hacker.javaProhibited.serializable.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Serializable with No Serializable Attribution
 *
 * @author Benedict Jin
 * @since 2015/8/4
 */
public class SerializableWithNoSAttribution implements Serializable {

    private String id;
    private transient String name;
    private int age;

    private Infos infos;

    public SerializableWithNoSAttribution() {
    }

    public SerializableWithNoSAttribution(String id, String name, int age, Infos infos) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.infos = infos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Infos getInfos() {
        return infos;
    }

    public void setInfos(Infos infos) {
        this.infos = infos;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeUTF(this.id);
        out.writeUTF(this.name);
        out.writeInt(this.age);
        out.writeUTF(this.infos.getTel());
        out.writeUTF(this.infos.getBlog());
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.id = in.readUTF();
        this.name = in.readUTF();
        this.age = in.readInt();
        this.infos = new Infos(in.readUTF(), in.readUTF());
    }


    @Override
    public String toString() {
        return "SerializableWithNoSAttribution{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", infos=" + infos +
                '}';
    }
}
