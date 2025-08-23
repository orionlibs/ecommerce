package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Locale;

public abstract class AbstractItemModel implements Serializable
{
    public static final String MODEL_CONTEXT_FACTORY = "model.context.factory.class";
    private final ItemModelInternalContext ctx;
    public static final String LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER = "enable.language.fallback.serviceLayer";


    private static NewModelContextFactory bootstrapFromSystemProperty(String prop)
    {
        NewModelContextFactory tmp = null;
        String className = readClassNameFromProperty(prop, "de.hybris.platform.servicelayer.model.DefaultNewModelContextFactory");
        try
        {
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            tmp = (NewModelContextFactory)((ctxLoader == null) ? Class.forName(className) : Class.forName(className, true, ctxLoader)).newInstance();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("the 'new model' context factory '" + className + "' is illegal", e);
        }
        return tmp;
    }


    private static String readClassNameFromProperty(String property, String defaultClassName)
    {
        String ret = System.getProperty(property);
        return (ret == null) ? defaultClassName : ret;
    }


    public AbstractItemModel()
    {
        this.ctx = EntityContextFactoryHolder.factory.createNew(getClass());
    }


    protected AbstractItemModel(ItemModelContext ctx)
    {
        if(ctx == null)
        {
            throw new IllegalArgumentException("context must not be null");
        }
        if(ctx instanceof ItemModelInternalContext)
        {
            this.ctx = (ItemModelInternalContext)ctx;
        }
        else
        {
            throw new IllegalArgumentException("wrong type of context");
        }
    }


    public ItemModelContext getItemModelContext()
    {
        return (ItemModelContext)getPersistenceContext();
    }


    protected ItemModelInternalContext getPersistenceContext()
    {
        return this.ctx;
    }


    public String getTenantId()
    {
        return getItemModelContext().getTenantId();
    }


    public PK getPk()
    {
        return getItemModelContext().getPK();
    }


    public String getItemtype()
    {
        return getItemModelContext().getItemType();
    }


    public <T> T getProperty(String name)
    {
        ItemModelInternalContext persistenceCtx = getPersistenceContext();
        if(persistenceCtx.isDynamicAttribute(name))
        {
            return (T)persistenceCtx.getDynamicValue(this, name);
        }
        return (T)persistenceCtx.getPropertyValue(name);
    }


    public void setProperty(String name, Object value)
    {
        ItemModelInternalContext persistenceCtx = getPersistenceContext();
        if(persistenceCtx.isDynamicAttribute(name))
        {
            persistenceCtx.setDynamicValue(this, name, value);
        }
        else
        {
            persistenceCtx.setPropertyValue(name, value);
        }
    }


    public <T> T getProperty(String name, Locale locale)
    {
        ItemModelInternalContext persistenceCtx = getPersistenceContext();
        if(persistenceCtx.isDynamicAttribute(name))
        {
            return (T)persistenceCtx.getLocalizedDynamicValue(this, name, locale);
        }
        return (T)persistenceCtx.getLocalizedValue(name, locale);
    }


    public void setProperty(String name, Locale locale, Object value)
    {
        ItemModelInternalContext persistenceCtx = getPersistenceContext();
        if(persistenceCtx.isDynamicAttribute(name))
        {
            persistenceCtx.setLocalizedDynamicValue(this, name, locale, value);
        }
        else
        {
            persistenceCtx.setLocalizedValue(name, locale, value);
        }
    }


    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            return true;
        }
        if(!(obj instanceof AbstractItemModel))
        {
            return false;
        }
        return getItemModelContext().equals(((AbstractItemModel)obj).getItemModelContext());
    }


    public int hashCode()
    {
        return getPersistenceContext().hashCode(this);
    }


    public String toString()
    {
        if(getPk() == null)
        {
            return getClass().getSimpleName() + " (<unsaved>)";
        }
        return getClass().getSimpleName() + " (" + getClass().getSimpleName() + "@" + getPk().toString() + ")";
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return getPersistenceContext().writeReplace(this);
    }


    public Object readResolve() throws ObjectStreamException
    {
        return this;
    }


    protected static boolean toPrimitive(Boolean value)
    {
        return Boolean.TRUE.equals(value);
    }


    protected static int toPrimitive(Integer value)
    {
        return (value != null) ? value.intValue() : 0;
    }


    protected static double toPrimitive(Double value)
    {
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    protected static float toPrimitive(Float value)
    {
        return (value != null) ? value.floatValue() : 0.0F;
    }


    protected static byte toPrimitive(Byte value)
    {
        return (value != null) ? value.byteValue() : 0;
    }


    protected static long toPrimitive(Long value)
    {
        return (value != null) ? value.longValue() : 0L;
    }


    protected static short toPrimitive(Short value)
    {
        return (value != null) ? value.shortValue() : 0;
    }


    protected static char toPrimitive(Character value)
    {
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    protected static Boolean toObject(boolean value)
    {
        return Boolean.valueOf(value);
    }


    protected static Integer toObject(int value)
    {
        return Integer.valueOf(value);
    }


    protected static Double toObject(double value)
    {
        return Double.valueOf(value);
    }


    protected static Float toObject(float value)
    {
        return Float.valueOf(value);
    }


    protected static Byte toObject(byte value)
    {
        return Byte.valueOf(value);
    }


    protected static Long toObject(long value)
    {
        return Long.valueOf(value);
    }


    protected static Short toObject(short value)
    {
        return Short.valueOf(value);
    }


    protected static Character toObject(char value)
    {
        return Character.valueOf(value);
    }
}
