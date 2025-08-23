package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;

public abstract class AbstractPrimitiveAttributeMethod extends AbstractAttributeMethodWriter
{
    protected static final String PRIMITIVE_POSTFIX = "AsPrimitive";
    private final AbstractTypedAttributeMethod realOne;


    public AbstractPrimitiveAttributeMethod(AbstractTypedAttributeMethod realOne, String returnType, String postfix)
    {
        super(realOne.getEnclosingClass(), realOne.getAttribute(), returnType, (postfix != null) ? (realOne.getName() + realOne.getName()) :
                        realOne.getName(), realOne.managerMode());
        this.realOne = realOne;
        addParameter(addRequiredImport("de.hybris.platform.jalo.SessionContext"), "ctx");
        if(managerMode())
        {
            addParameter(
                            addRequiredImport(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType())), "item");
        }
    }


    public static boolean isPrimitive(YAttributeDescriptor desc)
    {
        YType type = !desc.isLocalized() ? desc.getType() : ((YMapType)desc.getType()).getReturnType();
        return (type instanceof YAtomicType && ((YAtomicType)type).getPrimitiveJavaClass() != null);
    }


    protected static Class getPrimitiveType(YAttributeDescriptor desc)
    {
        YType type = !desc.isLocalized() ? desc.getType() : ((YMapType)desc.getType()).getReturnType();
        if(!(type instanceof YAtomicType))
        {
            throw new IllegalArgumentException("attribute " + desc + " doesnt hold primitve values (type is " + type + ")");
        }
        Class ret = ((YAtomicType)type).getPrimitiveJavaClass();
        if(ret == null)
        {
            throw new IllegalArgumentException("attribute " + desc + " doesnt hold supported primitve values (type is " + type + ")");
        }
        return ret;
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        AttributeMethodDelegate delegate = new AttributeMethodDelegate(this);
        if(managerMode())
        {
            delegate.addParameter(
                            addRequiredImport(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType())), "item");
        }
        return (MethodWriter)delegate;
    }


    protected String getPrimitveDefaultValueString(Class primitve)
    {
        if(int.class.equals(primitve) || short.class.equals(primitve) || byte.class.equals(primitve) || long.class
                        .equals(primitve))
        {
            return "0";
        }
        if(double.class.equals(primitve))
        {
            return "0.0d";
        }
        if(float.class.equals(primitve))
        {
            return "0.0f";
        }
        if(boolean.class.equals(primitve))
        {
            return "false";
        }
        if(char.class.equals(primitve))
        {
            return "0";
        }
        throw new IllegalArgumentException("unknown primitve class '" + primitve + "'");
    }


    public AbstractTypedAttributeMethod getRealOne()
    {
        return this.realOne;
    }
}
