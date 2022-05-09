package types;

public class RedisBulkString extends RedisData {
    private final String value;

    public RedisBulkString(String value) {
        super();
        this.value = value;
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.BULK_STRING;
    }

    @Override
    public String getFormattedValue() {
        if (value == null) return "$-1\r\n";
        return "$" + value.length() + "\r\n" + value + "\r\n";
    }

    @Override
    public String getRawValue() {
        return value;
    }
}
