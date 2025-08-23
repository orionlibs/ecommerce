package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;

public class PrimitiveGetter extends AbstractPrimitiveAttributeMethod
{
    public PrimitiveGetter(TypedGetter realOne)
    {
        super((AbstractTypedAttributeMethod)realOne, getPrimitiveType(realOne.getAttribute()).getName(), "AsPrimitive");
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
        Class prim = getPrimitiveType(desc);
        file.add(addRequiredImport(AbstractTypedAttributeMethod.calculateReturnType((ClassWriter)getEnclosingClass(), desc)) + " value = " + addRequiredImport(AbstractTypedAttributeMethod.calculateReturnType((ClassWriter)getEnclosingClass(), desc)) + "( ctx" +
                        getRealOne().getName() + " );");
        file.add("return value != null ? value." + prim
                        .getName() + "Value() : " + getPrimitveDefaultValueString(prim) + ";");
    }
}
