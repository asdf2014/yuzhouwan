package com.yuzhouwan.hacker.json.fastjson;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Map;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Realtime Dimension Table Result
 *
 * @author Benedict Jin
 * @since 2018/7/30
 */
public class Result {

    private String statusCode;
    private String msg;
    private List<Map<String, Object>> data;
    private String error;
    private Long total;

    public Result() {
    }

    public Result(String statusCode, String msg, List<Map<String, Object>> data, String error, Long total) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
        this.error = error;
        this.total = total;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Result)) {
            return false;
        }

        Result result = (Result) o;

        return new EqualsBuilder()
                .append(getStatusCode(), result.getStatusCode())
                .append(getMsg(), result.getMsg())
                .append(getData(), result.getData())
                .append(getError(), result.getError())
                .append(getTotal(), result.getTotal())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getStatusCode())
                .append(getMsg())
                .append(getData())
                .append(getError())
                .append(getTotal())
                .toHashCode();
    }
}
