package com.yuzhouwan.hacker.json.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Jackson Bean
 *
 * @author Benedict Jin
 * @since 2020/7/5
 */
public class JacksonBean {

    private int id;
    private String name;
    private String blog;

    @JsonCreator
    public JacksonBean(@JsonProperty("id") int id,
                       @JsonProperty("name") String name,
                       @JsonProperty("theBlog") String blog) {
        this.id = id;
        this.name = name;
        this.blog = blog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }
}
