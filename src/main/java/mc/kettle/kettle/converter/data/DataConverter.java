package mc.kettle.kettle.converter.data;

public interface DataConverter<T> {
    byte getDataValue(T target);
    void setDataValue(T target, byte dataValue);
}