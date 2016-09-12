package com.yuzhouwan.site.api.validation.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuzhouwan.site.api.validation.service.First;
import com.yuzhouwan.site.api.validation.service.Second;

import javax.validation.constraints.NotNull;

@ApiModel(value = "UserModel")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Were binding by providing a name
        include = JsonTypeInfo.As.PROPERTY, // The name is provided in a property
        property = "type", // Property name is type
        visible = true // Retain the value of type after deserialisation
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserModel {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "{id.empty}", groups = {First.class})
    private int id;

    @ApiModelProperty(value = "userName")
    @NotNull(message = "{username.empty}", groups = {First.class, Second.class})
    private String userName;

    @ApiModelProperty(value = "content")
    @NotNull(message = "{content.empty}", groups = {First.class, Second.class})
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

