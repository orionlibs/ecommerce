package de.hybris.platform.servicelayer.web.session;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.serializer.Deserializer;

public class HybrisDeserializer implements Deserializer<Object>
{
    private final ClassLoader classLoader;


    public static PersistedSession deserialize(byte[] serializedSession, Deserializer deSerializer)
    {
        PersistedSession session = null;
        if(ArrayUtils.isNotEmpty(serializedSession))
        {
            try
            {
                InputStream byteArrayInputStream = new ByteArrayInputStream(serializedSession);
                try
                {
                    session = (PersistedSession)deSerializer.deserialize(byteArrayInputStream);
                    byteArrayInputStream.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        byteArrayInputStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            catch(IOException ex)
            {
                throw new SystemException("Failed to deserialize object type", ex);
            }
        }
        return session;
    }


    public HybrisDeserializer(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }


    public Object deserialize(InputStream inputStream) throws IOException
    {
        ConfigurableObjectInputStream configurableObjectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
        try
        {
            return configurableObjectInputStream.readObject();
        }
        catch(ClassNotFoundException ex)
        {
            throw new SystemException("Failed to deserialize object type", ex);
        }
    }
}
