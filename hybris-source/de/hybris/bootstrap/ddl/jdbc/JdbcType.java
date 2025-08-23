package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.LinkedList;
import java.util.List;

public class JdbcType
{
    private static final int UNKNOWN_SIZE = -1;
    private final int jdbcType;
    private final List<String> sizeParts = new LinkedList<>();
    private final int scale;
    private String defaultValue;


    public JdbcType(int jdbcType)
    {
        this(jdbcType, -1);
    }


    public JdbcType(int jdbcType, int size)
    {
        this(jdbcType, size, -1);
    }


    public JdbcType(int jdbcType, int size, int scale)
    {
        this(jdbcType, size, scale, null);
    }


    public JdbcType(int jdbcType, int size, int scale, String defaultValue)
    {
        this.jdbcType = jdbcType;
        if(size > -1)
        {
            this.sizeParts.add(String.valueOf(size));
        }
        this.scale = scale;
        this.defaultValue = defaultValue;
    }


    public int getJdbcType()
    {
        return this.jdbcType;
    }


    public String getSize()
    {
        return this.sizeParts.isEmpty() ? null : Joiner.on(" ").join(this.sizeParts);
    }


    public int getScale()
    {
        return this.scale;
    }


    public String getDefaultValue()
    {
        return this.defaultValue;
    }


    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public JdbcType withSizeModifier(String modifier)
    {
        Preconditions.checkNotNull(modifier, "modifier can't be null");
        Preconditions.checkState(!this.sizeParts.isEmpty(), "size must be specified");
        this.sizeParts.add(modifier);
        return this;
    }


    public String toString()
    {
        return "JdbcType{jdbcType=" + this.jdbcType + ", size=" + getSize() + ", scale=" + this.scale + ", defaultValue=" + this.defaultValue + "}";
    }


    public boolean hasComplexSize()
    {
        return (this.sizeParts.size() > 1);
    }
}
