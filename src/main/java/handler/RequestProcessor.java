package handler;

import storage.InMemoryStorage;
import types.*;

@SuppressWarnings("DuplicatedCode")
public class RequestProcessor {
    public static RedisData processRequest(RedisData data) {
        String command = getCommand(data);

        switch (command.toUpperCase()) {
            case "PING":
                return processPing(data);
            case "ECHO":
                return processEcho(data);
            case "GET":
                return processGet(data);
            case "SET":
                return processSet(data);
            default:
                return new RedisError("unknown command");
        }
    }

    private static RedisData processPing(RedisData data) {
        return new RedisSimpleString("PONG");
    }

    private static RedisData processEcho(RedisData data) {
        if (data.getType() == RedisDataType.ARRAY && ((RedisArray) data).getRawValue().length == 2) {
            return ((RedisArray) data).getRawValue()[1];
        } else return new RedisError("wrong number of arguments for command");
    }

    private static RedisData processGet(RedisData data) {
        if (data.getType() == RedisDataType.ARRAY && ((RedisArray) data).getRawValue().length == 2) {
            data = ((RedisArray) data).getRawValue()[1];
            if (data.getType() == RedisDataType.SIMPLE_STRING) {
                String val = InMemoryStorage.get(((RedisSimpleString) data).getRawValue());
                return new RedisBulkString(val);
            } else if (data.getType() == RedisDataType.BULK_STRING) {
                String val = InMemoryStorage.get(((RedisBulkString) data).getRawValue());
                return new RedisBulkString(val);
            }
        }
        return new RedisError("wrong number of arguments for command");
    }

    private static RedisData processSet(RedisData data) {
        if (data.getType() == RedisDataType.ARRAY && ((RedisArray) data).getRawValue().length == 3) {
            RedisData keyData = ((RedisArray) data).getRawValue()[1];
            RedisData valueData = ((RedisArray) data).getRawValue()[2];
            String key = null;
            String value = null;
            if (keyData.getType() == RedisDataType.SIMPLE_STRING) key = ((RedisSimpleString) keyData).getRawValue();
            else if (keyData.getType() == RedisDataType.BULK_STRING) key = ((RedisBulkString) keyData).getRawValue();
            if (key == null) return new RedisError("wrong number of arguments for command");

            if (valueData.getType() == RedisDataType.SIMPLE_STRING)
                value = ((RedisSimpleString) valueData).getRawValue();
            else if (valueData.getType() == RedisDataType.BULK_STRING)
                value = ((RedisBulkString) valueData).getRawValue();
            if (value == null) return new RedisError("wrong number of arguments for command");

            String previousValue = InMemoryStorage.put(key, value);
            if (previousValue == null) return new RedisSimpleString("OK");
            else return new RedisBulkString(previousValue);
        }
        return new RedisError("wrong number of arguments for command");
    }

    private static String getCommand(RedisData data) {
        if (data == null) return null;
        if (data.getType() == RedisDataType.ARRAY) {
            RedisArray array = (RedisArray) data;
            RedisData[] values = array.getRawValue();
            if (values.length == 0) return null;
            if (values[0].getType() == RedisDataType.BULK_STRING) {
                return ((RedisBulkString) values[0]).getRawValue();
            } else if (values[0].getType() == RedisDataType.SIMPLE_STRING) {
                return ((RedisSimpleString) values[0]).getRawValue();
            }
        } else if (data.getType() == RedisDataType.BULK_STRING) {
            return ((RedisBulkString) data).getRawValue();
        } else if (data.getType() == RedisDataType.SIMPLE_STRING) {
            return ((RedisSimpleString) data).getRawValue();
        }
        return null;
    }
}
