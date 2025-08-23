package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YType;
import java.util.Collections;

public class TypedGetter extends AbstractTypedAttributeMethod
{
    public TypedGetter(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, calculateReturnType((ClassWriter)forClass, desc), desc.isBooleanAttribute() ? "is" : "get", managerMode);
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        MethodWriter writer = super.createNonCtxDelegateMethod();
        if(writer.getJavadoc() == null || writer.getJavadoc().length() < 1)
        {
            writer.setJavadoc(getJavadoc());
        }
        writer.setContentPlain("return " + getName() + "( getSession().getSessionContext()" + (managerMode() ? ", item" : "") + " );");
        return writer;
    }


    protected void writeContent(JavaFile file)
    {
        YAttributeDescriptor desc = getAttribute();
        if(desc.isLocalized())
        {
            file.add("if( ctx == null || ctx.getLanguage() == null )");
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
                file.add("return " + (getReturnType().startsWith("Collection") ?
                                "" : ("(" + getReturnType() + ")")) + handlerQualifier + ".getValues( ctx, " + (
                                managerMode() ? "item" : "this") + " );");
            }
            else
            {
                String srcOrderedExpr, tgtOrderedExpr, resultItemsType = addRequiredImport(getEnclosingClass().getGenerator().getJaloClassName(desc
                                .getRelationEnd().getType()));
                if(desc.getRelationEnd().getRelation().getTargetEnd().isOrdered())
                {
                    String util = addRequiredImport("de.hybris.platform.util.Utilities");
                    srcOrderedExpr = util + ".getRelationOrderingOverride(" + util + "_SRC_ORDERED, true)";
                }
                else
                {
                    srcOrderedExpr = "false";
                }
                if(desc.getRelationEnd().getRelation().getSourceEnd().isOrdered())
                {
                    String util = addRequiredImport("de.hybris.platform.util.Utilities");
                    tgtOrderedExpr = util + ".getRelationOrderingOverride(" + util + "_TGT_ORDERED, true)";
                }
                else
                {
                    tgtOrderedExpr = "false";
                }
                String qualifier = addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName());
                file.startBlock("final " + addRequiredImport("java.util.List<" + resultItemsType + ">") + " items = " + (
                                managerMode() ? "item." : "") + "getLinkedItems( ");
                file.add("ctx,");
                file.add((desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false") + ",");
                if(getEnclosingClass().getConstantsClassName() != null)
                {
                    file.add(qualifier + ",");
                }
                else
                {
                    file.add("\"" + desc.getRelationEnd().getRelationCode() + "\",");
                }
                file.add("\"" + desc.getRelationEnd().getType().getCode() + "\",");
                file.add((desc.isLocalized() ? "ctx.getLanguage()" : "null") + ",");
                file.add(srcOrderedExpr + ",");
                file.add(tgtOrderedExpr);
                file.endBlock(");");
                YCollectionType collType = desc.isLocalized() ? (YCollectionType)((YMapType)desc.getType()).getReturnType() : (YCollectionType)desc.getType();
                if(collType.getTypeOfCollection() == YCollectionType.TypeOfCollection.SET)
                {
                    file.add("return new " + addRequiredImport("java.util.LinkedHashSet") + "<" + resultItemsType + ">(items);");
                }
                else
                {
                    file.add("return items;");
                }
            }
        }
        else if(desc.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY)
        {
            YType attributeType = desc.isLocalized() ? ((YMapType)desc.getType()).getReturnType() : desc.getType();
            if(attributeType instanceof YCollectionType)
            {
                String emptyReturnValue;
                file.add(getReturnType() + " coll = (" + getReturnType() + ")" + getReturnType() + "get" + (managerMode() ? "item." : "") + "Property( ctx, " + (
                                desc.isLocalized() ? "Localized" : "") + ");");
                if(((YCollectionType)attributeType).getTypeOfCollection() == YCollectionType.TypeOfCollection.SET)
                {
                    emptyReturnValue = addRequiredImport(Collections.class.getName()) + ".EMPTY_SET";
                }
                else
                {
                    emptyReturnValue = addRequiredImport(Collections.class.getName()) + ".EMPTY_LIST";
                }
                file.add("return coll != null ? coll : " + emptyReturnValue + ";");
            }
            else if(attributeType instanceof YMapType)
            {
                file.add(getReturnType() + " map = (" + getReturnType() + ")" + getReturnType() + "get" + (managerMode() ? "item." : "") + "Property( ctx, " + (
                                desc.isLocalized() ? "Localized" : "") + ");");
                file.add("return map != null ? map : " + addRequiredImport(Collections.class.getName()) + ".EMPTY_MAP;");
            }
            else
            {
                file.add("return " + ("Object".equals(getReturnType()) ? "" : ("(" + getReturnType() + ")")) + (
                                managerMode() ? "item." : "") + "get" + (desc.isLocalized() ? "Localized" : "") + "Property( ctx, " +
                                getQualifierConstant() + ");");
            }
        }
        else if(desc.getPersistenceType() != YAttributeDescriptor.PersistenceType.DYNAMIC)
        {
            throw new IllegalArgumentException("cannot generate getter for attribute " + desc + " - must be PROPERTY or relation end");
        }
    }
}
