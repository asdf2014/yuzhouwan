package com.yuzhouwan.site.api.validation.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuzhouwan.site.api.validation.service.First;
import com.yuzhouwan.site.api.validation.service.Second;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: User Model
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
@ApiModel(value = "UserModel")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,         // Were binding by providing a name
        include = JsonTypeInfo.As.PROPERTY, // The name is provided in a property
        property = "type",                  // Property name is type
        visible = true                      // Retain the value of type after deserialization
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserModel {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "{id.empty}", groups = {First.class, Second.class})
    private int id;

    @ApiModelProperty(value = "userName")
    @NotNull(message = "{username.empty}", groups = {First.class, Second.class})
    @Pattern(regexp = ".*•.*", message = "userName's format error", groups = {Second.class})
    private String userName;

    @ApiModelProperty(value = "content")
    @NotNull(message = "{content.empty}", groups = {First.class, Second.class})
    private String content;

    @ApiModelProperty(value = "account")
    @DecimalMax(value = "100", message = "The max value of account is 100000000", groups = {Second.class})
    private int account;

    @Email(message = "Email address is invalid", groups = {Second.class})
    private String email;

    @Future(message = "Date must be future", groups = {Second.class})
    private Date future;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFuture() {
        return future;
    }

    public void setFuture(Date future) {
        this.future = future;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

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
