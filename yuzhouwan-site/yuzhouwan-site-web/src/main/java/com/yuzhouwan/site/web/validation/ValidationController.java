package com.yuzhouwan.site.web.validation;

import com.yuzhouwan.site.api.validation.model.User;
import com.yuzhouwan.site.api.validation.model.UserModel;
import com.yuzhouwan.site.api.validation.service.First;
import com.yuzhouwan.site.api.validation.service.Group;
import com.yuzhouwan.site.api.validation.service.Second;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.site.web.validation
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
@RestController
@RequestMapping("/validator")
public class ValidationController {

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Validated({Second.class}) UserModel userModel, BindingResult result) {
        if (result.hasErrors()) {
            return "validate/error";
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Validated({First.class, Second.class}) UserModel user, BindingResult result) {
        if (result.hasErrors()) {
            return "validate/error";
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/update/order", method = RequestMethod.POST)
    public String register(@Validated(Group.class) User user, BindingResult result) {
        if (result.hasErrors()) {
            return "validate/error";
        }
        return "redirect:/success";
    }

    @RequestMapping("/validate/multi")
    public String multi(@Valid @ModelAttribute("user") User user, BindingResult userErrors,
                        @Valid @ModelAttribute("userModel") UserModel userModel, BindingResult userModelErrors) {

        if (userErrors.hasErrors()) { //如果user模型对象验证失败
            return "validate/error";
        }
        if (userModelErrors.hasErrors()) { //如果userModel模型对象验证失败
            return "validate/error";
        }
        return "redirect:/success";
    }
}
