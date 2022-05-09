package types;

public class RedisArray extends RedisData {
    private final RedisData[] values;

    public RedisArray(RedisData[] values) {
        super();
        this.values = values;
    }

    @Override
    public RedisDataType getType() {
        return RedisDataType.ARRAY;
    }

    @Override
    public String getFormattedValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("*");
        sb.append(values.length);
        sb.append("\r\n");
        for (RedisData value : values) sb.append(value.getFormattedValue());
        return sb.toString();
    }

    @Override
    public RedisData[] getRawValue() {
        return values;
    }
}
