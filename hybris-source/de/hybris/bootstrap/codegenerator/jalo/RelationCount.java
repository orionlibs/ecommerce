package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YType;
import java.util.Collection;
import java.util.LinkedList;

public class RelationCount extends AbstractAttributeMethodWriter
{
    public RelationCount(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, "long", "get" + ClassWriter.firstLetterUpperCase(desc.getQualifier()) + "Count", managerMode);
        addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        if(managerMode())
        {
            addParameter(forClass.getGenerator().getJavaClassName((YType)desc.getEnclosingType()), "item");
        }
        if(desc.isLocalized())
        {
            addParameter("de.hybris.platform.jalo.c2l.Language", "lang");
        }
    }


    public Collection<MethodWriter> createDelegateMethods()
    {
        Collection<MethodWriter> ret = new LinkedList<>();
        AttributeMethodDelegate delegate = new AttributeMethodDelegate(this);
        if(managerMode())
        {
            delegate.addParameter(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType()), "item");
        }
        if(getAttribute().isLocalized())
        {
            delegate.addParameter("de.hybris.platform.jalo.c2l.Language", "lang");
        }
        delegate.setContentPlain("return " + getName() + "( getSession().getSessionContext()" + (managerMode() ? ", item" : "") + (
                        getAttribute().isLocalized() ? ",lang" : "") + " );");
        ret.add(delegate);
        if(getAttribute().isLocalized())
        {
            delegate = new AttributeMethodDelegate(this);
            delegate.addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
            if(managerMode())
            {
                delegate.addParameter(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType()), "item");
            }
            delegate.setContentPlain("return " +
                            getName() + "( ctx" + (managerMode() ? ", item" : "") + ", ctx.getLanguage() );");
            ret.add(delegate);
            delegate = new AttributeMethodDelegate(this);
            if(managerMode())
            {
                delegate.addParameter(getEnclosingClass().getGenerator().getJaloClassName(getAttribute().getEnclosingType()), "item");
            }
            delegate.setContentPlain("return " +
                            getName() + "( getSession().getSessionContext()" + (managerMode() ? ", item" : "") + ", getSession().getSessionContext().getLanguage() );");
            ret.add(delegate);
        }
        return ret;
    }


    protected void writeContent(JavaFile file)
    {
        YAttributeDescriptor desc = getAttribute();
        file.startBlock("return " + (managerMode() ? "item." : "") + "getLinkedItemsCount(");
        file.add("ctx,");
        file.add((desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false") + ",");
        if(getEnclosingClass().getConstantsClassName() != null)
        {
            file.add(addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName()) + ",");
        }
        else
        {
            file.add("\"" + desc.getRelationEnd().getRelationCode() + "\".intern(),");
        }
        file.add("\"" + desc.getRelationEnd().getType().getCode() + "\",");
        file.add(desc.isLocalized() ? "lang" : "null");
        file.endBlock(");");
    }
}
