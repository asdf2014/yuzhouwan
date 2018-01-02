package com.yuzhouwan.site.api.validation.model;

import com.yuzhouwan.site.api.validation.enums.NotEmpty;
import com.yuzhouwan.site.api.validation.service.GroupA;
import com.yuzhouwan.site.api.validation.service.GroupB;
import org.hibernate.validator.constraints.Length;

import javax.validation.groups.Default;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: User
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class User {

    @NotEmpty(message = "{firstName.may.be.empty}")
    private String firstName;

    @NotEmpty(message = "{middleName.may.be.empty}", groups = Default.class)
    @Length(min = 1, max = 20)
    private String middleName;

    @NotEmpty(message = "{lastName.may.be.empty}", groups = GroupA.class)
    private String lastName;

    @NotEmpty(message = "{country.may.be.empty}", groups = GroupB.class)
    private String country;

    @NotEmpty(field = "password", message = "{password.empty.error}")
    private String password;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
