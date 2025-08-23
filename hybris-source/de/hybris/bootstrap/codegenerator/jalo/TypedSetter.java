package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YType;
import java.util.List;
import java.util.Map;

public class TypedSetter extends AbstractTypedAttributeMethod
{
    private static final String CONSISTENCY_CHECK_EXCEPTION = "de.hybris.platform.jalo.ConsistencyCheckException";
    private static final String JALO_BUSINESS_EXCEPTION = "de.hybris.platform.jalo.JaloBusinessException";
    private final YAttributeDescriptor descriptor;
    private final Map<String, List<String>> customSettersSignatureInfoMap;


    public TypedSetter(JaloClassWriter forClass, YAttributeDescriptor descriptor, boolean managerMode, Map<String, List<String>> customSettersSignatureInfoMap)
    {
        super(forClass, descriptor, null, "set", managerMode);
        this.customSettersSignatureInfoMap = customSettersSignatureInfoMap;
        this.descriptor = descriptor;
        addThrownExceptionIfRequired((MethodWriter)this);
        addParameter(calculateReturnType((ClassWriter)forClass, descriptor), "value");
        if(isInitialOnly())
        {
            setVisibility(Visibility.PROTECTED);
        }
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        MethodWriter writer = super.createNonCtxDelegateMethod();
        addThrownExceptionIfRequired(writer);
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
            String getterMethodName = "get" + ((!desc.isLocalized() && desc.getType() instanceof YMapType) ? "All" : "") + ClassWriter.firstLetterUpperCase(desc.getQualifier());
            file.add("protected " + valueType + " doGetValue(final SessionContext ctx)");
            file.startBlock();
            file.add("return " + getterMethodName + "( ctx" + (managerMode() ? ", item" : "") + " );");
            file.endBlock();
            file.add("@Override");
            file.add("protected void doSetValue(final SessionContext ctx, final " + valueType + " _value)");
            file.startBlock();
            file.add("final " + valueType + " value = _value;");
        }
        if((!desc.isWritable() && desc.isInitial()) || desc.isLocalized())
        {
            file.add("if ( ctx == null) ");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "( \"ctx is null\", 0 );");
            file.endBlock();
        }
        if(!desc.isWritable() && desc.isInitial())
        {
            file.add("// initial-only attribute: make sure this attribute can be set during item creation only");
            file.add("if ( ctx.getAttribute( \"core.types.creation.initial\") != Boolean.TRUE )");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "( \"attribute '\"+" +
                            getQualifierConstant() + "+\"' is not changeable\", 0 );");
            file.endBlock();
        }
        if(desc.isLocalized())
        {
            file.add("if( ctx.getLanguage() == null )");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "(\"" +
                            getEnclosingClass().getClassName() + "." + getName() + " requires a session language\", 0 );");
            file.endBlock();
        }
        if(desc.isRelationEndAttribute() && desc.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY)
        {
            if(desc.getRelationEnd().getRelation().isAbstract())
            {
                String handlerQualifier = ((ItemTypeWriter)getEnclosingClass()).getRelationHandlerMemberName(desc);
                file.add(handlerQualifier + ".setValues( ctx, " + handlerQualifier + ", value );");
            }
            else
            {
                String srcOrderedExpr, tgtOrderedExpr;
                if(desc.getRelationEnd().getRelation().getTargetEnd().isOrdered())
                {
                    String str = addRequiredImport("de.hybris.platform.util.Utilities");
                    srcOrderedExpr = str + ".getRelationOrderingOverride(" + str + "_SRC_ORDERED, true)";
                }
                else
                {
                    srcOrderedExpr = "false";
                }
                if(desc.getRelationEnd().getRelation().getSourceEnd().isOrdered())
                {
                    String str = addRequiredImport("de.hybris.platform.util.Utilities");
                    tgtOrderedExpr = str + ".getRelationOrderingOverride(" + str + "_TGT_ORDERED, true)";
                }
                else
                {
                    tgtOrderedExpr = "false";
                }
                String util = addRequiredImport("de.hybris.platform.util.Utilities");
                String markModifiedExpr = util + ".getMarkModifiedOverride(" + util + "_MARKMODIFIED)";
                String qualifier = addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName());
                file.startBlock((managerMode() ? "item." : "") + "setLinkedItems( ");
                file.add("ctx,");
                boolean isSource = desc.getRelationEnd().getOppositeEnd().isSource();
                file.add((isSource ? "true" : "false") + ",");
                if(getEnclosingClass().getConstantsClassName() != null)
                {
                    file.add(qualifier + ",");
                }
                else
                {
                    file.add("\"" + desc.getRelationEnd().getRelationCode() + "\",");
                }
                file.add(desc.isLocalized() ? "ctx.getLanguage()," : "null,");
                file.add("value,");
                file.add(srcOrderedExpr + ",");
                file.add(tgtOrderedExpr + ",");
                file.add(markModifiedExpr);
                file.endBlock(");");
            }
        }
        else if(useOne2ManyHandlerOnTheManySide(desc))
        {
            String handlerQualifier = ((ItemTypeWriter)getEnclosingClass()).getRelationHandlerMemberName(desc);
            file.add(handlerQualifier + ".addValue( ctx, value, " + handlerQualifier + "  );");
        }
        else if(desc.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY)
        {
            YType attributeType = desc.isLocalized() ? ((YMapType)desc.getType()).getReturnType() : desc.getType();
            boolean isCollection = attributeType instanceof de.hybris.bootstrap.typesystem.YCollectionType;
            file.add((managerMode() ? "item." : "") + "set" + (managerMode() ? "item." : "") + "Property(ctx, " + (desc.isLocalized() ? "Localized" : "") + "," +
                            getQualifierConstant() + (
                            mustCastValueToSerializable() ? ("(" +
                                            addRequiredImport("java.io.Serializable") + ")") :
                                            "") + ");");
        }
        else if(desc.getPersistenceType() != YAttributeDescriptor.PersistenceType.DYNAMIC)
        {
            throw new IllegalArgumentException("cannot generate getter for attribute " + desc + " - must be PROPERTY or relation end");
        }
        if(usePOHandler)
        {
            file.endBlock();
            file.endBlock("}.setValue( ctx, value );");
        }
    }


    private boolean useOne2ManyHandlerOnTheManySide(YAttributeDescriptor desc)
    {
        return (!managerMode() && desc
                        .isRelationEndAttribute() && desc.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.ONE && desc
                        .getRelationEnd().getOppositeEnd().getRelation().isAbstract());
    }


    public void addThrownExceptionIfRequired(MethodWriter methodWriter)
    {
        if(this.descriptor.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY && isThrownExceptionRequired())
        {
            if("Media".equals(this.descriptor.getEnclosingTypeCode()) || "AbstractMedia".equals(this.descriptor
                            .getEnclosingTypeCode()))
            {
                methodWriter.addRequiredImport("de.hybris.platform.jalo.JaloBusinessException");
                methodWriter.addThrownException("JaloBusinessException");
            }
            else
            {
                methodWriter.addRequiredImport("de.hybris.platform.jalo.ConsistencyCheckException");
                methodWriter.addThrownException("ConsistencyCheckException");
            }
        }
    }


    private boolean isThrownExceptionRequired()
    {
        String typeName = this.descriptor.getEnclosingTypeCode();
        String attrName = this.descriptor.getQualifier();
        if(this.customSettersSignatureInfoMap.containsKey(typeName))
        {
            List<String> attrNames = this.customSettersSignatureInfoMap.get(typeName);
            return (attrNames != null && attrNames.contains(attrName));
        }
        return false;
    }


    protected boolean mustCastValueToSerializable()
    {
        return false;
    }
}
