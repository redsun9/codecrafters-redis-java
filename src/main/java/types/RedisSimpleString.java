package types;

public class RedisSimpleString extends RedisData {
    private final String value;

    public RedisSimpleString(String value) {
        this.value = value;
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.SIMPLE_STRING;
    }

    @Override
    public String getFormattedValue() {
        return "+" + value + "\r\n";
    }

    @Override
    public String getRawValue() {
        return value;
    }
}
