package de.hybris.platform.mediaconversion.web.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class UrlTag extends AbstractUrlTag
{
    private String var;
    private Scope scope = Scope.page;


    public void doTag() throws JspException, IOException
    {
        String url = retrieveURL();
        if(getVar() == null)
        {
            getJspContext().getOut().print(url);
        }
        else
        {
            if(!(getJspContext() instanceof PageContext))
            {
                throw new IllegalStateException("PageContext cannot be accessed. (JspContext is not a PageContext.)");
            }
            getScope().set((PageContext)getJspContext(), getVar(), url);
        }
    }


    public String getVar()
    {
        return this.var;
    }


    public void setVar(String var)
    {
        this.var = var;
    }


    public Scope getScope()
    {
        return this.scope;
    }


    public void setScope(String scope)
    {
        this.scope = (scope == null) ? Scope.page : Scope.valueOf(scope);
    }
}
