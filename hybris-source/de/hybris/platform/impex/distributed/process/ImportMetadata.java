package de.hybris.platform.impex.distributed.process;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class ImportMetadata
{
    private static final char[] FORBIDDEN_CHARS = new char[] {'&', '='};
    private static final Joiner.MapJoiner JOINER = Joiner.on('&').withKeyValueSeparator("=");
    private static final Splitter.MapSplitter SPLITTER = Splitter.on('&').withKeyValueSeparator("=");
    private final Map<String, String> metadata;


    public static ImportMetadata fromMetadata(String metaData)
    {
        if(Strings.isNullOrEmpty(metaData))
        {
            return empty();
        }
        return new ImportMetadata(SPLITTER.split(metaData));
    }


    public static ImportMetadata empty()
    {
        return new ImportMetadata(new HashMap<>());
    }


    public int size()
    {
        return this.metadata.size();
    }


    public String get(String key)
    {
        if(key == null || !this.metadata.containsKey(key))
        {
            return null;
        }
        return this.metadata.get(key);
    }


    public String set(String key, String value)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key mustn't be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(value), "value mustn't be blank");
        Preconditions.checkArgument(!StringUtils.containsAny(key, FORBIDDEN_CHARS), "key mustn't contain '&' or '=' character");
        Preconditions.checkArgument(!StringUtils.containsAny(value, FORBIDDEN_CHARS), "value mustn't contain '&' or '=' character");
        return this.metadata.put(key, value);
    }


    private ImportMetadata(Map<String, String> metadata)
    {
        this.metadata = new HashMap<>(metadata);
    }


    public String getStringRepresentation()
    {
        return JOINER.join(this.metadata);
    }
}
