package handler;

import types.*;

import java.io.IOException;
import java.io.InputStream;

public class RequestParser {
    private final InputStream inputStream;

    public RequestParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public RedisData parseRequest() throws IOException {
        char c = readChar();
        if (c == '+') {
            return parseSimpleString();
        } else if (c == '-') {
            return parseError();
        } else if (c == ':') {
            return parseInteger();
        } else if (c == '$') {
            return parseBulkString();
        } else if (c == '*') {
            return parseArray();
        } else {
            return null;
        }
    }

    private RedisData parseArray() throws IOException {
        int length = (int) readInteger();
        RedisData[] values = new RedisData[length];
        for (int i = 0; i < length; i++) values[i] = parseRequest();
        return new RedisArray(values);
    }

    private RedisData parseBulkString() throws IOException {
        int length = (int) readInteger();
        if (length < 0) {
            skipNBytes(2);
            return new RedisBulkString(null);
        } else {
            return new RedisBulkString(readString());
        }
    }

    private RedisData parseInteger() throws IOException {
        return new RedisInteger(readInteger());
    }

    private RedisData parseError() throws IOException {
        return new RedisError(readString());
    }

    private RedisData parseSimpleString() throws IOException {
        return new RedisSimpleString(readString());
    }

    private long readInteger() throws IOException {
        long value = 0;
        boolean isNegative = false;
        char c = readChar();
        while (c != '\r') {
            if (c == '-') isNegative = true;
            else value = value * 10 + c - '0';
            c = readChar();
        }
        skipNBytes(1);
        if (isNegative) return -value;
        else return value;
    }

    private String readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        char c = readChar();
        while (c != '\r') {
            sb.append(c);
            c = readChar();
        }
        skipNBytes(1);
        return sb.toString();
    }

    private char readChar() throws IOException {
        int c = -1;
        while (c == -1) c = inputStream.read();
        return (char) c;
    }

    private void skipNBytes(int n) throws IOException {
        while (n != 0) n -= inputStream.skip(n);
    }
}
