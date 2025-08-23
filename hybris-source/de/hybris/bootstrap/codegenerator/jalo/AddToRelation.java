package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YRelationEnd;

public class AddToRelation extends AbstractTypedAttributeMethod
{
    static final String UTILITIES_FQN = "de.hybris.platform.util.Utilities";
    static final String JALO_INVALID_PARAMETER_EXCEPTION_FQN = "de.hybris.platform.jalo.JaloInvalidParameterException";


    public AddToRelation(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, null, "addTo", managerMode);
        if(desc.isLocalized())
        {
            addParameter("de.hybris.platform.jalo.c2l.Language", "lang");
        }
        addParameter(RemoveFromRelation.getRelatedType((ClassWriter)forClass, desc), "value");
        if(isInitialOnly())
        {
            setVisibility(Visibility.PROTECTED);
        }
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
                            getEnclosingClass().getClassName() + "." + getName() + " requires a language language\", 0 );");
            file.endBlock();
        }
        if(isUsingOneToManyHandler)
        {
            String handlerQualifier = ((ItemTypeWriter)getEnclosingClass()).getRelationHandlerMemberName(desc);
            file.add(handlerQualifier + ".addValue( ctx, " + handlerQualifier + ", value );");
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
            file.startBlock((managerMode() ? "item." : "") + "addLinkedItems( ");
            file.add("ctx,");
            file.add((desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false") + ",");
            if(getEnclosingClass().getConstantsClassName() == null)
            {
                file.add("\"" + desc.getRelationEnd().getRelationCode() + "\".intern(),");
            }
            else
            {
                file.add(qualifier + ",");
            }
            file.add(desc.isLocalized() ? "lang," : "null,");
            file.add(addRequiredImport("java.util.Collections") + ".singletonList(value),");
            file.add(srcOrderedExpr + ",");
            file.add(tgtOrderedExpr + ",");
            file.add(markModifiedExpr);
            file.endBlock(");");
        }
    }
}
