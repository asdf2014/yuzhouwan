package com.yuzhouwan.hacker.javaProhibited.serializable;

import com.yuzhouwan.common.util.FileUtils;
import com.yuzhouwan.hacker.javaProhibited.serializable.bean.Country;
import com.yuzhouwan.hacker.javaProhibited.serializable.bean.Infos;
import com.yuzhouwan.hacker.javaProhibited.serializable.bean.SerializableWithNoSAttribution;
import com.yuzhouwan.hacker.javaProhibited.serializable.converter.SerializationConverter;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Serializable Test
 *
 * @author Benedict Jin
 * @since 2015/8/4
 */
public class SerializableTest {

    private static final String SERIALIZE_FILE_PATH = "serializable.obj";

    /*
     * Serialization of Object SerializableWithNoSAttribution{id='ASDF2014', name='asdf', age=20, infos=Infos{tel='123456'}} completed.
     * Deserialization of Object SerializableWithNoSAttribution{id='ASDF2014', name='asdf', age=20, infos=Infos{tel='123456'}} is completed.
     */
    @Test
    public void testSerializeWithUnSerializeVariable() {
        Infos infos = new Infos("123456", "yuzhouwan.com");
        SerializableWithNoSAttribution s = new SerializableWithNoSAttribution("ASDF2014", "asdf", 20, infos);
        assertEquals("SerializableWithNoSAttribution{id='ASDF2014', name='asdf', age=20, infos=Infos{tel='123456', blog='yuzhouwan.com'}}", s.toString());

        SerializationConverter.serialize(s, "asdf");
        s = SerializationConverter.deserialize("asdf", SerializableWithNoSAttribution.class);
        assertEquals("SerializableWithNoSAttribution{id='ASDF2014', name='asdf', age=20, infos=Infos{tel='123456', blog='yuzhouwan.com'}}", s.toString());
    }

    @Test
    public void testTransient() throws Exception {
        try {
            Country country = new Country("China", "+08:00");
            assertEquals("Country{name='China', timezone='+08:00'}", country.toString());
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZE_FILE_PATH))) {
                oos.writeObject(country);
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZE_FILE_PATH))) {
                country = (Country) ois.readObject();
                assertEquals("Country{name='China', timezone='null'}", country.toString());
            }
        } finally {
            FileUtils.retryDelete(new File(SERIALIZE_FILE_PATH), 3, 100);
        }
    }
}
