package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YExtension;

public abstract class AbstractAttributeMethodWriter extends MethodWriter
{
    protected static final String PRIMITIVE_POSTFIX = "AsPrimitive";
    private final JaloClassWriter forClass;
    private final YAttributeDescriptor attributeDesc;
    private final String constantsClassNameShort;
    private final boolean managerModeVariable;


    public AbstractAttributeMethodWriter(JaloClassWriter forClass, YAttributeDescriptor desc, String returnTypeFull, String methodName, boolean managerMode)
    {
        super(desc.isPrivate() ? Visibility.PACKAGE_PROTECTED : Visibility.PUBLIC, returnTypeFull, methodName);
        this.forClass = forClass;
        if(forClass.isJaloLogicFree())
        {
            this.constantsClassNameShort = null;
        }
        else
        {
            this
                            .constantsClassNameShort = (forClass.getConstantsClassName() == null) ? null : addRequiredImport(forClass.getConstantsClassName());
        }
        this.attributeDesc = desc;
        this.managerModeVariable = managerMode;
        if(desc.isRelationEndAttribute() && (
                        !desc.getNamespace().equals(desc.getRelationEnd().getRelation().getNamespace()) ||
                                        !desc.getRelationEnd().getRelation().getNamespace().equals(forClass.getExtension())))
        {
            System.err.println("===========================================");
            System.err.println("current class:" + forClass.getClassName());
            System.err.println("current extension:" + forClass.getExtension().getExtensionName());
            System.err.println("attribute extension:" + ((YExtension)desc.getNamespace()).getExtensionName());
            System.err.println("relation end extension:" + ((YExtension)desc
                            .getRelationEnd().getNamespace()).getExtensionName());
            System.err.println("relation extension:" + ((YExtension)desc
                            .getRelationEnd().getRelation().getNamespace()).getExtensionName());
            System.err.println("===========================================");
        }
    }


    protected boolean managerMode()
    {
        return this.managerModeVariable;
    }


    public YAttributeDescriptor getAttribute()
    {
        return this.attributeDesc;
    }


    protected String getConstantsClassNameShort()
    {
        return this.constantsClassNameShort;
    }


    protected JaloClassWriter getEnclosingClass()
    {
        return this.forClass;
    }


    protected boolean isInitialOnly()
    {
        return (getAttribute().isInitial() && !getAttribute().isWritable());
    }
}
