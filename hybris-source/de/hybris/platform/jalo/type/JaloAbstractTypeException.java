package de.hybris.platform.jalo.type;

public class JaloAbstractTypeException extends JaloTypeException
{
    public JaloAbstractTypeException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }


    public JaloAbstractTypeException(ComposedType type)
    {
        super("type " + ((type != null) ? type.getCode() : "null") + " is abstract", 1234);
    }
}
