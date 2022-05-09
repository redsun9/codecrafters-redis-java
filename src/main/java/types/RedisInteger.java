package types;

public class RedisInteger extends RedisData {
    private final long value;

    public RedisInteger(long value) {
        super();
        this.value = value;
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.INTEGER;
    }

    @Override
    public String getFormattedValue() {
        return ":" + value + "\r\n";
    }

    @Override
    public Long getRawValue() {
        return value;
    }
}
