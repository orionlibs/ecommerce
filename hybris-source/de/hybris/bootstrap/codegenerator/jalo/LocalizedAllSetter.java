package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YRelationEnd;

public class LocalizedAllSetter extends AbstractTypedAttributeMethod
{
    public LocalizedAllSetter(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, null, "setAll", managerMode);
        addParameter(forClass.getGenerator().getJavaClassName(desc.getType()), "value");
        if(isInitialOnly())
        {
            setVisibility(Visibility.PROTECTED);
        }
        if(desc.isRelationEndAttribute() && desc.getRelationEnd().getRelation().isAbstract())
        {
            throw new IllegalArgumentException("cannot create localized all-getter for 1-n relation end " + desc.getQualifier());
        }
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        MethodWriter writer = super.createNonCtxDelegateMethod();
        if(writer.getJavadoc() == null || writer.getJavadoc().length() < 1)
        {
            writer.setJavadoc(getJavadoc());
        }
        writer.addParameter(getParameterType("value"), "value");
        writer.setContentPlain(getName() + "( getSession().getSessionContext()" + getName() + ", value );");
        return writer;
    }


    protected void writeContent(JavaFile file)
    {
        YAttributeDescriptor desc = getAttribute();
        boolean isUsingOneToManyHandler = (desc.isRelationEndAttribute() && desc.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY && desc.getRelationEnd().getRelation().isAbstract());
        boolean usePOHandler = (desc.isPartOf() && !isUsingOneToManyHandler && getEnclosingClass().isGeneratePartOf());
        if(usePOHandler)
        {
            String valueType = getParameterType("value");
            file.add("new " + addRequiredImport("de.hybris.platform.util.PartOfHandler") + "<" + valueType + ">()");
            file.startBlock();
            file.add("@Override");
            String getterMethodName = "get" + ((desc.isLocalized() && desc.getType() instanceof de.hybris.bootstrap.typesystem.YMapType) ? "All" : "") + ClassWriter.firstLetterUpperCase(desc.getQualifier());
            file.add("protected " + valueType + " doGetValue(final SessionContext ctx)");
            file.startBlock();
            file.add("return " + getterMethodName + "( ctx" + (managerMode() ? ", item" : "") + " );");
            file.endBlock();
            file.add("@Override");
            file.add("protected void doSetValue(final SessionContext ctx, final " + valueType + " _value)");
            file.startBlock();
            file.add("final " + valueType + " value = _value;");
        }
        if(!desc.isWritable() && desc.isInitial())
        {
            file.add("if ( ctx == null) ");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "( \"ctx is null\", 0 );");
            file.endBlock();
            file.add("// initial-only attribute: make sure this attribute can be set during item creation only");
            file.add("if ( ctx.getAttribute( \"core.types.creation.initial\") != Boolean.TRUE )");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "( \"attribute '\"+" +
                            getQualifierConstant() + "+\"' is not changeable\", 0 );");
            file.endBlock();
        }
        if(desc.isRelationEndAttribute())
        {
            file.startBlock((managerMode() ? "item." : "") + "setAllLinkedItems( ");
            file.add("getAllValuesSessionContext(ctx),");
            file.add((desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false") + ",");
            if(getEnclosingClass().getConstantsClassName() != null)
            {
                file.add(addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName()) + ",");
            }
            else
            {
                file.add("\"" + desc.getRelationEnd().getRelationCode() + "\",");
            }
            file.add("value");
            file.endBlock(");");
        }
        else if(desc.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY)
        {
            file.add((
                            managerMode() ? "item." : "") + "setAllLocalizedProperties(ctx," + (managerMode() ? "item." : "") + ",value);");
        }
        else
        {
            throw new IllegalArgumentException("cannot generate getter for attribute " + desc + " - must be PROPERTY or relation end");
        }
        if(usePOHandler)
        {
            file.endBlock();
            file.endBlock("}.setValue( ctx, value );");
        }
    }
}
