package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;

public class PrimitiveSetter extends AbstractPrimitiveAttributeMethod
{
    private final TypedSetter typedSetter;


    public PrimitiveSetter(TypedSetter realOne)
    {
        super((AbstractTypedAttributeMethod)realOne, null, null);
        this.typedSetter = realOne;
        this.typedSetter.addThrownExceptionIfRequired((MethodWriter)this);
        addParameter(getPrimitiveType(getAttribute()).getName(), "value");
        if(isInitialOnly())
        {
            setVisibility(Visibility.PROTECTED);
        }
    }


    public MethodWriter createNonCtxDelegateMethod()
    {
        MethodWriter writer = super.createNonCtxDelegateMethod();
        this.typedSetter.addThrownExceptionIfRequired(writer);
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
        file.add(getRealOne().getName() + "( ctx," + getRealOne().getName() + (managerMode() ? " item, " : "") + ".valueOf( value ) );");
    }
}
