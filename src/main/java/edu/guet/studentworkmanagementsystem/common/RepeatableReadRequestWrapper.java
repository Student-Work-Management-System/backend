package edu.guet.studentworkmanagementsystem.common;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RepeatableReadRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    public RepeatableReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void setReadListener(ReadListener readListener) {}
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }
}
