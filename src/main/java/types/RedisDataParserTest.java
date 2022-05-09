package types;

import handler.RequestParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedisDataParserTest {

    @Test
    void parseEcho() throws IOException {
        InputStream inputStream = new StringBufferInputStream("*2\r\n$4\r\nECHO\r\n$3\r\nhey\r\n");
        RequestParser parser = new RequestParser(inputStream);
        RedisData data = parser.parseRequest();
        assertTrue(data instanceof RedisArray);
        RedisArray array = (RedisArray) data;
        RedisData[] rawValue = array.getRawValue();
        assertEquals(2, rawValue.length);
        assertTrue(rawValue[0] instanceof RedisBulkString);
        assertTrue(rawValue[1] instanceof RedisBulkString);
        RedisBulkString bulkString = (RedisBulkString) rawValue[0];
        assertEquals("ECHO", bulkString.getRawValue());
        bulkString = (RedisBulkString) rawValue[1];
        assertEquals("hey", bulkString.getRawValue());
    }

    @Test
    void parsePing() throws IOException {
        InputStream inputStream = new StringBufferInputStream("+PING\r\n");
        RequestParser parser = new RequestParser(inputStream);
        RedisData data = parser.parseRequest();
        assertTrue(data instanceof RedisSimpleString);
        RedisSimpleString simpleString = (RedisSimpleString) data;
        assertEquals("PING", simpleString.getRawValue());
    }

    @Test
    void testSet() throws IOException {
        InputStream inputStream = new StringBufferInputStream("*3\r\n$3\r\nSET\r\n$4\r\nkey\r\n$5\r\nvalue\r\n");
        RequestParser parser = new RequestParser(inputStream);
        RedisData data = parser.parseRequest();
        assertTrue(data instanceof RedisArray);
        RedisArray array = (RedisArray) data;
        RedisData[] rawValue = array.getRawValue();
        assertEquals(3, rawValue.length);
        assertTrue(rawValue[0] instanceof RedisBulkString);
        assertTrue(rawValue[1] instanceof RedisBulkString);
        assertTrue(rawValue[2] instanceof RedisBulkString);
        RedisBulkString bulkString = (RedisBulkString) rawValue[0];
        assertEquals("SET", bulkString.getRawValue());
        bulkString = (RedisBulkString) rawValue[1];
        assertEquals("key", bulkString.getRawValue());
        bulkString = (RedisBulkString) rawValue[2];
        assertEquals("value", bulkString.getRawValue());
    }
}