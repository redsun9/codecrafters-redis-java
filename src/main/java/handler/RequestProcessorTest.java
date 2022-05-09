package handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import storage.InMemoryStorage;
import types.RedisArray;
import types.RedisData;
import types.RedisSimpleString;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("DuplicatedCode")
class RequestProcessorTest {

    @AfterEach
    void tearDown() {
        InMemoryStorage.clear();
    }

    @Test
    void testProcessPing() {
        RedisSimpleString request = new RedisSimpleString("PING");
        String expected = "PONG";
        RedisData actual = RequestProcessor.processRequest(request);
        assertEquals(expected, actual.getRawValue());
        assertEquals("+PONG\r\n", actual.getFormattedValue());
    }

    @Test
    void testProcessSet() {
        RedisData[] request = new RedisData[3];
        request[0] = new RedisSimpleString("SET");
        request[1] = new RedisSimpleString("key");
        request[2] = new RedisSimpleString("value");

        RedisData array = new RedisArray(request);

        String expected = "OK";
        RedisData actual = RequestProcessor.processRequest(array);
        assertEquals(expected, actual.getRawValue());
        assertEquals("+OK\r\n", actual.getFormattedValue());

        request[2] = new RedisSimpleString("value2");
        array = new RedisArray(request);
        expected = "$5\r\nvalue\r\n";

        actual = RequestProcessor.processRequest(array);
        assertEquals(expected, actual.getFormattedValue());
    }

    @Test
    void testProcessGet() {
        RedisData[] getRequestData = new RedisData[2];
        getRequestData[0] = new RedisSimpleString("GET");
        getRequestData[1] = new RedisSimpleString("key");
        RedisData getRequest = new RedisArray(getRequestData);

        RedisData getResponse = RequestProcessor.processRequest(getRequest);
        assertEquals("$-1\r\n", getResponse.getFormattedValue());

        RedisData[] setRequestData = new RedisData[3];
        setRequestData[0] = new RedisSimpleString("SET");
        setRequestData[1] = new RedisSimpleString("key");
        setRequestData[2] = new RedisSimpleString("value");
        RedisData setRequest = new RedisArray(setRequestData);
        RequestProcessor.processRequest(setRequest);

        getResponse = RequestProcessor.processRequest(getRequest);
        assertEquals("$5\r\nvalue\r\n", getResponse.getFormattedValue());
    }

    @Test
    void testExpire() throws InterruptedException {
        RedisData[] setRequestData = new RedisData[5];
        setRequestData[0] = new RedisSimpleString("SET");
        setRequestData[1] = new RedisSimpleString("key");
        setRequestData[2] = new RedisSimpleString("value");
        setRequestData[3] = new RedisSimpleString("px");
        setRequestData[4] = new RedisSimpleString("100");

        RedisData setRequest = new RedisArray(setRequestData);
        RequestProcessor.processRequest(setRequest);


        RedisData[] getRequestData = new RedisData[2];
        getRequestData[0] = new RedisSimpleString("GET");
        getRequestData[1] = new RedisSimpleString("key");
        RedisData getRequest = new RedisArray(getRequestData);
        RedisData getResponse = RequestProcessor.processRequest(getRequest);
        assertEquals("$5\r\nvalue\r\n", getResponse.getFormattedValue());

        Thread.sleep(100);
        getResponse = RequestProcessor.processRequest(getRequest);
        assertEquals("$-1\r\n", getResponse.getFormattedValue());
    }
}