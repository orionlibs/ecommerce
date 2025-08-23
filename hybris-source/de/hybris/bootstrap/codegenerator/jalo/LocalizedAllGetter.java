package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YMapType;

public class LocalizedAllGetter extends AbstractTypedAttributeMethod
{
    public LocalizedAllGetter(JaloClassWriter forClass, YAttributeDescriptor desc, boolean managerMode)
    {
        super(forClass, desc, forClass.getGenerator().getJavaClassName(desc.getType()), "getAll", managerMode);
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
        writer.setContentPlain("return " + getName() + "( getSession().getSessionContext()" + (managerMode() ? ", item" : "") + " );");
        return writer;
    }


    protected void writeContent(JavaFile file)
    {
        YAttributeDescriptor desc = getAttribute();
        if(desc.isRelationEndAttribute())
        {
            String resultItemsType = addRequiredImport(getEnclosingClass().getGenerator().getJaloClassName(desc
                            .getRelationEnd().getType()));
            file.startBlock(getReturnType() + " values = " + getReturnType() + "getAllLinkedItems( ");
            file.add((desc.getRelationEnd().getOppositeEnd().isSource() ? "true" : "false") + ",");
            if(getEnclosingClass().getConstantsClassName() != null)
            {
                file.add(addRequiredImport(getEnclosingClass().getConstantsClassName()) + ".Relations." + addRequiredImport(getEnclosingClass().getConstantsClassName()));
            }
            else
            {
                file.add("\"" + desc.getRelationEnd().getRelationCode() + "\"");
            }
            file.endBlock(");");
            YCollectionType collType = (YCollectionType)((YMapType)desc.getType()).getReturnType();
            switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YCollectionType$TypeOfCollection[collType.getTypeOfCollection().ordinal()])
            {
                case 1:
                    file.add(addRequiredImport("java.util.Map<de.hybris.platform.jalo.c2l.Language,java.util.Collection<" + resultItemsType + ">>") + " ret = new " + addRequiredImport("java.util.Map<de.hybris.platform.jalo.c2l.Language,java.util.Collection<" + resultItemsType + ">>")
                                    + "( values );");
                    file.add("for( " +
                                    addRequiredImport("java.util.Map.Entry<de.hybris.platform.jalo.c2l.Language,java.util.Collection<" + resultItemsType + ">>") + " entry : ret.entrySet() )");
                    file.startBlock();
                    file.add("entry.setValue( new " + addRequiredImport("java.util.LinkedHashSet") + "<" + resultItemsType + ">(entry.getValue()));");
                    file.endBlock();
                    file.add("return (" + addRequiredImport("java.util.Map") + ") ret;");
                    return;
            }
            file.add("return values;");
        }
        else if(desc.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY)
        {
            file.add("return (" + getReturnType() + ")" + (managerMode() ? "item." : "") + "getAllLocalizedProperties(ctx," +
                            getQualifierConstant() + "," + addRequiredImport("de.hybris.platform.jalo.c2l.C2LManager") + ".getInstance().getAllLanguages());");
        }
        else
        {
            throw new IllegalArgumentException("cannot generate getter for attribute " + desc + " - must be PROPERTY or relation end");
        }
    }
}
