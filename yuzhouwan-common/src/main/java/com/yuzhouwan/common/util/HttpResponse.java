package com.yuzhouwan.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: HttpResponse
 *
 * @author Benedict Jin
 * @since 2016/3/21 0030
 */
public class HttpResponse implements Serializable {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public HttpResponse() {
    }

    public HttpResponse(int code, byte[] bytes) {
        this.bytes = bytes;
        this.code = code;
    }

    private byte[] bytes;
    private int code;
    private String contentType;

    public String getCharset() {
        if (contentType != null && contentType.lastIndexOf("=") != -1) {
            String charset = contentType.substring(contentType.lastIndexOf("=") + 1);
            try {
                Charset.forName(charset);
            } catch (Exception e) {
                return DEFAULT_CHARSET;
            }
        }
        return DEFAULT_CHARSET;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getText() {
        return this.getText(this.getCharset());
    }

    public String getText(String charset) {
        return new String(bytes, Charset.forName(charset));
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(bytes);
    }

    public boolean isError() {
        if (code >= 400) {
            return true;
        }
        return false;
    }
}