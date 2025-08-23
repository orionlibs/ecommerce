package de.hybris.platform.persistence.property.internal;

import de.hybris.platform.core.HybrisEnumValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.WriteAbortedException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectInputStreamWithOverriddenUUID extends ObjectInputStream
{
    public static final String SUID_FIELD_NAME = "suid";
    private static final Logger LOG = LoggerFactory.getLogger(ObjectInputStreamWithOverriddenUUID.class.getName());


    public ObjectInputStreamWithOverriddenUUID(InputStream inputStream) throws IOException
    {
        super(inputStream);
    }


    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException
    {
        ObjectStreamClass descriptor = super.readClassDescriptor();
        LOG.debug("Read descriptor: {}", descriptor);
        try
        {
            Class<?> clazz = Class.forName(descriptor.getName());
            if(isHybrisEnumValueClass(clazz))
            {
                long serialVersionUID = ObjectStreamClass.lookup(clazz).getSerialVersionUID();
                if(descriptor.getSerialVersionUID() != 0L)
                {
                    FieldUtils.writeField(descriptor, "suid", Long.valueOf(serialVersionUID), true);
                }
            }
        }
        catch(IllegalAccessException e)
        {
            throw new WriteAbortedException(e.getMessage(), e);
        }
        return descriptor;
    }


    private static boolean isHybrisEnumValueClass(Class<?> clazz)
    {
        return (HybrisEnumValue.class.isAssignableFrom(clazz) && !Enum.class.isAssignableFrom(clazz));
    }
}
