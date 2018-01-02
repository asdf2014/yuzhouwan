package com.yuzhouwan.common.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: HttpResponse
 *
 * @author Benedict Jin
 * @since 2016/3/21
 */
public class HttpResponse implements Serializable {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int HTTP_ERROR_CODE = 400;

    private byte[] bytes;
    private int code;
    private String contentType;

    public HttpResponse() {
    }

    public HttpResponse(int code, byte[] bytes) {
        this.bytes = bytes;
        this.code = code;
    }

    public String getCharset() {
        if (contentType != null && contentType.lastIndexOf("=") != -1)
            try {
                Charset.forName(contentType.substring(contentType.lastIndexOf("=") + 1));
            } catch (Exception e) {
                return DEFAULT_CHARSET;
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
        return code >= HTTP_ERROR_CODE;
    }
}
