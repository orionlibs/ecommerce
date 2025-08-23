package de.hybris.platform.servicelayer.dto.converter;

public class GenericConverter<SOURCE, TARGET> implements Converter<SOURCE, TARGET>
{
    private Class<TARGET> destClass;


    public TARGET convert(SOURCE source) throws ConversionException
    {
        TARGET dest = createDestObject();
        return convert(source, dest);
    }


    public TARGET convert(SOURCE source, TARGET prototype) throws ConversionException
    {
        ConversionHelper.copyProperties(source, prototype);
        return prototype;
    }


    public void setDestClass(Class<TARGET> destClass)
    {
        this.destClass = destClass;
    }


    protected TARGET createDestObject() throws ConversionException
    {
        if(this.destClass == null)
        {
            throw new NullPointerException("No destClass set");
        }
        try
        {
            return this.destClass.newInstance();
        }
        catch(InstantiationException e)
        {
            throw new ConversionException(e.getMessage(), e);
        }
        catch(IllegalAccessException e)
        {
            throw new ConversionException(e.getMessage(), e);
        }
    }
}
