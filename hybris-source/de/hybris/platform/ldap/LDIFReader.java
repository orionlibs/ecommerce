package de.hybris.platform.ldap;

import de.hybris.platform.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

public class LDIFReader
{
    public static final String DEFAULT_ENCODING = "UTF-8";
    private final BufferedReader reader;


    public LDIFReader(BufferedReader reader)
    {
        this.reader = reader;
    }


    private void add(Attributes entry, String attribute, String value, int type) throws IOException
    {
        Attribute vals = entry.get(attribute);
        if(vals == null)
        {
            vals = new BasicAttribute(attribute);
        }
        if(type == 2)
        {
            vals.add(Base64.decode(value));
        }
        else if(type == 1)
        {
            vals.add(value);
        }
        else if(type == 3)
        {
            URL url = null;
            try
            {
                url = new URL(value);
            }
            catch(MalformedURLException ex)
            {
                throw new IOException("" + ex + ": Cannot construct url " + ex);
            }
            if(!url.getProtocol().equalsIgnoreCase("file"))
            {
                throw new IOException("Protocol not supported: " + url.getProtocol());
            }
            File file = new File(url.getFile());
            byte[] byteArray = new byte[(int)file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            try
            {
                fileInputStream.read(byteArray);
                fileInputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fileInputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            vals.add(byteArray);
        }
        else
        {
            throw new IOException("Unknown type.");
        }
        entry.put(vals);
    }


    public Attributes getNext() throws IOException
    {
        int type = 1;
        Attributes entry = null;
        int linenr = 0;
        StringBuilder value = null;
        String attribute = null, line = attribute;
        while((line = this.reader.readLine()) != null)
        {
            int len = line.length();
            linenr++;
            if(len <= 0 || line.charAt(0) != '#')
            {
                if(len > 0 && line.charAt(0) == ' ')
                {
                    if(value == null)
                    {
                        throw new IOException("Parse Error (line " + linenr + "): Value continued without an attribute.");
                    }
                    value.append(line.substring(1));
                    continue;
                }
                if(len == 0)
                {
                    if(attribute != null && value != null)
                    {
                        if(entry == null)
                        {
                            entry = new BasicAttributes(true);
                        }
                        add(entry, attribute, value.toString(), type);
                        attribute = null;
                        value = null;
                    }
                    if(entry != null)
                    {
                        return entry;
                    }
                    continue;
                }
                int pos = line.indexOf(':');
                if(pos == -1)
                {
                    throw new IOException("Parse Error (line " + linenr + "): Cannot parse input line. Missing ':' character !!!");
                }
                if(attribute != null && value != null)
                {
                    if(entry == null)
                    {
                        entry = new BasicAttributes(true);
                    }
                    add(entry, attribute, value.toString(), type);
                }
                int posTo = pos;
                int posFrom = pos + 1;
                if(posFrom < len)
                {
                    if(line.charAt(posFrom) == ':')
                    {
                        type = 2;
                        posFrom++;
                    }
                    else if(line.charAt(posFrom) == '<')
                    {
                        type = 3;
                        posFrom++;
                    }
                    else
                    {
                        type = 1;
                    }
                    if(line.charAt(posFrom) == ' ')
                    {
                        posFrom++;
                    }
                }
                attribute = line.substring(0, posTo);
                value = new StringBuilder(line.substring(posFrom));
            }
        }
        return entry;
    }
}
