package de.hybris.platform.processengine.definition;

public class ContextParameterDeclaration
{
    private final String name;
    private final boolean required;
    private final Class<?> declaredType;


    ContextParameterDeclaration(String name, boolean required, String declaredTypeClass) throws ClassNotFoundException
    {
        this(name, required, Class.forName(declaredTypeClass));
    }


    ContextParameterDeclaration(String name, boolean required, Class<?> declaredType)
    {
        this.name = name;
        this.required = required;
        this.declaredType = declaredType;
        if(this.name == null || "".equals(this.name))
        {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        if(this.declaredType == null)
        {
            throw new IllegalArgumentException("DeclaredType must not be null.");
        }
    }


    public String getName()
    {
        return this.name;
    }


    public boolean isRequired()
    {
        return this.required;
    }


    public Class<?> getDeclaredType()
    {
        return this.declaredType;
    }
}
