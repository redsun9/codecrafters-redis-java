package handler;

import types.*;

public class RequestProcessor {
    public static RedisData processRequest(RedisData data) {
        if (data.getType() == RedisDataType.SIMPLE_STRING) {
            RedisSimpleString simpleString = (RedisSimpleString) data;
            if (simpleString.getRawValue().equalsIgnoreCase("PING")) {
                return new RedisSimpleString("PONG");
            }
        }
        if (data.getType() == RedisDataType.BULK_STRING) {
            RedisBulkString bulkString = (RedisBulkString) data;
            if (bulkString.getRawValue().equalsIgnoreCase("PING")) {
                return new RedisSimpleString("PONG");
            }
        }
        if (data.getType() == RedisDataType.ARRAY) {
            RedisArray array = (RedisArray) data;
            RedisData[] values = array.getRawValue();
            if (values.length != 0 && values[0].getType() == RedisDataType.SIMPLE_STRING) {
                String command = ((RedisSimpleString) values[0]).getRawValue();
                if (command.equalsIgnoreCase("PING")) return new RedisSimpleString("PONG");
                if (command.equalsIgnoreCase("ECHO")) return values[1];
            } else if (values.length != 0 && values[0].getType() == RedisDataType.BULK_STRING) {
                String command = ((RedisBulkString) values[0]).getRawValue();
                if (command.equalsIgnoreCase("PING")) return new RedisSimpleString("PONG");
                if (command.equalsIgnoreCase("ECHO")) return values[1];
            }
        }
        return new RedisError("UNKNOWN_COMMAND");
    }
}
