package de.hybris.platform.cmscockpit.url.impl;

public interface FrontendUrlDecoder<T>
{
    T decode(String paramString);
}
