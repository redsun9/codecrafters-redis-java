package types;

public class RedisError extends RedisData {
    private final String value;

    public RedisError(String value) {
        super();
        this.value = value;
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.ERROR;
    }

    @Override
    public String getFormattedValue() {
        return "-" + value + "\r\n";
    }

    @Override
    public String getRawValue() {
        return value;
    }
}
