package com.hybris.datahub.client.extension;

import java.io.IOException;
import java.io.InputStream;

public interface ExtensionSource
{
    InputStream inputStream() throws IOException;
}
