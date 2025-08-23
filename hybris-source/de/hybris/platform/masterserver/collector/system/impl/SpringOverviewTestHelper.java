package de.hybris.platform.masterserver.collector.system.impl;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

final class SpringOverviewTestHelper
{
    static byte[] base64Decode(String str)
    {
        return Base64.getDecoder().decode(str);
    }


    static String decompress(byte[] compressedBytes, Charset encoding)
    {
        Inflater inflater = new Inflater();
        byte[] buffer = new byte[compressedBytes.length * 20];
        inflater.setInput(compressedBytes);
        try
        {
            int size = inflater.inflate(buffer);
            return new String(buffer, 0, size, encoding);
        }
        catch(DataFormatException e)
        {
            throw new RuntimeException(e);
        }
    }
}
