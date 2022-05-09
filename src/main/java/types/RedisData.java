package types;

public abstract class RedisData {
    abstract public RedisDataType getType();

    abstract public String getFormattedValue();

    abstract public Object getRawValue();

    public String toString() {
        return getType() + "[" + getFormattedValue() + "]";
    }
}
