package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelationEnd;

public class RemoveFromRelation extends AbstractTypedAttributeMethod
{
    public RemoveFromRelation(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, null, "removeFrom", managerMode);
        if(desc.isLocalized())
        {
            addParameter("de.hybris.platform.jalo.c2l.Language", "lang");
        }
        addParameter(getRelatedType((ClassWriter)forClass, desc), "value");
        if(isInitialOnly())
        {
            setVisibility(Visibility.PROTECTED);
        }
    }


    protected static String getRelatedType(ClassWriter forClass, YAttributeDescriptor desc)
    {
        return desc.isLocalized() ? forClass.getGenerator().getJavaClassName(((YCollectionType)((YMapType)desc
                        .getType()).getReturnType()).getElementType()) :
                        forClass.getGenerator().getJavaClassName(((YCollectionType)desc
                                        .getType())
                                        .getElementType());
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        MethodWriter writer = super.createNonCtxDelegateMethod();
        if(writer.getJavadoc() == null || writer.getJavadoc().length() < 1)
        {
            writer.setJavadoc(getJavadoc());
        }
        if(getAttribute().isLocalized())
        {
            writer.addParameter("de.hybris.platform.jalo.c2l.Language", "lang");
        }
        writer.addParameter(getParameterType("value"), "value");
        writer.setContentPlain(getName() + "( getSession().getSessionContext()" + getName() + (managerMode() ? ", item" : "") + ", value );");
        return writer;
    }


    protected void writeContent(JavaFile file)
    {
        YAttributeDescriptor desc = getAttribute();
        boolean isUsingOneToManyHandler = (desc.isRelationEndAttribute() && desc.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY && desc.getRelationEnd().getRelation().isAbstract());
        boolean autoPartOf = (desc.isPartOf() && !isUsingOneToManyHandler && getEnclosingClass().isGeneratePartOf());
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
            file.add("if( lang == null )");
            file.startBlock();
            file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloInvalidParameterException") + "(\"" +
                            getEnclosingClass().getClassName() + "." + getName() + " requires a session language\", 0 );");
            file.endBlock();
        }
        if(isUsingOneToManyHandler)
        {
            String handlerQualifier = ((ItemTypeWriter)getEnclosingClass()).getRelationHandlerMemberName(desc);
            file.add(handlerQualifier + ".removeValue( ctx, " + handlerQualifier + ", value );");
        }
        else
        {
            String relQuali, srcOrderedExpr, tgtOrderedExpr, srcBool = desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false";
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
            String qualifier = addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName());
            if(getEnclosingClass().getConstantsClassName() != null)
            {
                relQuali = qualifier;
            }
            else
            {
                relQuali = "\"" + desc.getRelationEnd().getRelationCode() + "\".intern()";
            }
            String lang = desc.isLocalized() ? "lang" : "null";
            String util = addRequiredImport("de.hybris.platform.util.Utilities");
            String markModifiedExpr = util + ".getMarkModifiedOverride(" + util + "_MARKMODIFIED)";
            file.startBlock((managerMode() ? "item." : "") + "removeLinkedItems( ");
            file.add("ctx,");
            file.add(srcBool + ",");
            file.add(relQuali + ",");
            file.add(lang + ",");
            file.add(addRequiredImport("java.util.Collections") + ".singletonList(value),");
            file.add(srcOrderedExpr + ",");
            file.add(tgtOrderedExpr + ",");
            file.add(markModifiedExpr);
            file.endBlock(");");
            if(autoPartOf)
            {
                file.add("if( !" + (
                                managerMode() ? "item." : "") + "getLinkedItems( ctx, " + srcBool + "," + relQuali + "," + lang + ").contains( value ) )");
                file.startBlock();
                file.add("try");
                file.startBlock();
                file.add("value.remove( ctx );");
                file.endBlock();
                file.add("catch( " + addRequiredImport("de.hybris.platform.jalo.ConsistencyCheckException") + " e )");
                file.startBlock();
                file.add("throw new " + addRequiredImport("de.hybris.platform.jalo.JaloSystemException") + "(e);");
                file.endBlock();
                file.endBlock();
            }
        }
    }
}
