package de.hybris.platform.media.storage;

public interface ConfigValueConverter<T>
{
    T convert(String paramString);
}
