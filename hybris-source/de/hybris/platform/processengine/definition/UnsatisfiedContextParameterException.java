package de.hybris.platform.processengine.definition;

public class UnsatisfiedContextParameterException extends RuntimeException
{
    private final ContextParameterDeclaration decl;


    public UnsatisfiedContextParameterException()
    {
        this.decl = null;
    }


    public UnsatisfiedContextParameterException(ContextParameterDeclaration decl)
    {
        super("Required context parameter '" + decl.getName() + "' of type '" + decl
                        .getDeclaredType().getName() + "' is not specified.");
        this.decl = decl;
    }


    public ContextParameterDeclaration getDecl()
    {
        return this.decl;
    }
}
