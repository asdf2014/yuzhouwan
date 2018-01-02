package com.yuzhouwan.hacker.avro;

import com.yuzhouwan.common.dir.DirUtils;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;

import static groovy.util.GroovyTestCase.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Avro User Test
 *
 * @author Benedict Jin
 * @since 2017/1/13
 */
public class AvroUserTest {

    @Test
    public void createUserTest() throws Exception {

        // 1. Creating Users
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder().setName("Charlie").setFavoriteColor("blue").setFavoriteNumber(null).build();

        // 2. Serializing
        // Serialize user1, user2 and user3 to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        String avroDir = DirUtils.RESOURCES_PATH.concat("/avro");
        DirUtils.makeSureExist(avroDir, false);
        File file = new File(avroDir.concat("/users.avro"));
        dataFileWriter.create(user1.getSchema(), file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        // There should have more user object, then will get more performance
        dataFileWriter.close();

        // 3. Deserializing
        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(file, userDatumReader);
        User user = null;
        String userStr;
        int count = 0;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with many items.
            user = dataFileReader.next(user);
            if ("{\"name\": \"Alyssa\", \"favorite_number\": 256, \"favorite_color\": null}".equals(userStr = user.toString()) ||
                    "{\"name\": \"Ben\", \"favorite_number\": 7, \"favorite_color\": \"red\"}".equals(userStr) ||
                    "{\"name\": \"Charlie\", \"favorite_number\": null, \"favorite_color\": \"blue\"}".equals(userStr))
                count++;
        }
        assertEquals(3, count);
    }
}
