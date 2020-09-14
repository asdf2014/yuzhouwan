package com.yuzhouwan.site.service.validation;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.yuzhouwan.site.api.validation.model.User;
import com.yuzhouwan.site.api.validation.model.UserModel;
import com.yuzhouwan.site.api.validation.service.First;
import com.yuzhouwan.site.api.validation.service.Group;
import com.yuzhouwan.site.api.validation.service.Second;
import com.yuzhouwan.site.api.validation.utils.ValidationUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Validator Controller
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
@RestController
@RequestMapping("/validator")
public class ValidatorController {

    @ApiOperation(
            value = "获得商品信息",
            notes = "获取商品信息(用于数据同步)",
            httpMethod = "POST",
            response = String.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "商品信息", response = String.class),
            @ApiResponse(code = 201, message = "ErrorType.errorCheckToken" + "(token验证失败)", response = String.class),
            @ApiResponse(code = 202, message = "ErrorType.error500" + "(系统错误)", response = String.class)})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ApiParam(required = true)
                       @Validated({Second.class})
                       /*@RequestBody*/ @ModelAttribute UserModel userModel, BindingResult result) {
        if (result.hasErrors()) {
            return ValidationUtils.getErrorInfos(result);
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Validated({First.class, Second.class}) UserModel userModel, BindingResult result) {
        if (result.hasErrors()) {
            return ValidationUtils.getErrorInfos(result);
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/update/order", method = RequestMethod.POST)
    public String register(@Validated(Group.class) User user, BindingResult result) {
        if (result.hasErrors()) {
            return ValidationUtils.getErrorInfos(result);
        }
        return "redirect:/success";
    }

    @RequestMapping("/multi")
    public String multi(@Valid @ModelAttribute("user") User user, BindingResult userErrors,
                        @Valid @ModelAttribute("userModel") UserModel userModel, BindingResult userModelErrors) {

        StringBuilder strBuilder = new StringBuilder();
        if (userErrors.hasErrors()) {
            strBuilder.append(ValidationUtils.getErrorInfos(userErrors)).append(" ");
        }
        if (userModelErrors.hasErrors()) {
            strBuilder.append(ValidationUtils.getErrorInfos(userModelErrors));
        }
        if (strBuilder.toString().length() > 0) {
            return strBuilder.toString();
        }
        return "redirect:/success";
    }
}
