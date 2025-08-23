package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;

public abstract class AbstractTypedAttributeMethod extends AbstractAttributeMethodWriter
{
    private final String qualifierContant;


    public AbstractTypedAttributeMethod(JaloClassWriter forClass, YAttributeDescriptor desc, String returnTypeFull, String prefix, boolean managerMode)
    {
        super(forClass, desc, returnTypeFull, ((prefix != null) ? prefix : "") + ((prefix != null) ? prefix : "") + (
                                        (!desc.isLocalized() && desc.getType() instanceof YMapType) ? "All" : ""),
                        managerMode);
        if(desc.getPersistenceType() == YAttributeDescriptor.PersistenceType.JALO && !desc.isRelationEndAttribute())
        {
            setAbstract(true);
        }
        addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        if(managerMode())
        {
            addParameter(desc.getEnclosingType().getJavaClassName(), "item");
        }
        this
                        .qualifierContant = (getConstantsClassNameShort() != null) ? (ConstantsWriter.requiresAttributeConstants(desc.getEnclosingType(), forClass.getExtension()) ? (getConstantsClassNameShort() + ".Attributes." + getConstantsClassNameShort() + "." + desc.getEnclosingType()
                        .getCode()) : desc.getQualifier().toUpperCase()) : ("\"" + desc.getQualifier() + "\".intern()");
    }


    protected static String calculateReturnType(ClassWriter forClass, YAttributeDescriptor desc)
    {
        YType type;
        if(desc.isRedeclaredOneToManyRelationEnd())
        {
            type = desc.getDeclaringAttribute().getType();
        }
        else
        {
            type = !desc.isLocalized() ? desc.getType() : ((YMapType)desc.getType()).getReturnType();
        }
        return forClass.getGenerator().getJavaClassName(type);
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        AttributeMethodDelegate delegate = new AttributeMethodDelegate(this);
        if(managerMode())
        {
            delegate.addParameter(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType()), "item");
        }
        return (MethodWriter)delegate;
    }


    protected String getQualifierConstant()
    {
        return this.qualifierContant;
    }
}
