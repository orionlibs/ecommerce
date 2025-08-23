package de.hybris.bootstrap.config;

public class WebExtensionModule extends AbstractExtensionModule
{
    private String webroot;
    private boolean jspcompile = false;


    public WebExtensionModule()
    {
        super("web");
    }


    public String getWebRoot()
    {
        return this.webroot;
    }


    public void setWebRoot(String webroot)
    {
        this.webroot = webroot;
    }


    public boolean isJspCompile()
    {
        return this.jspcompile;
    }


    public void setJspCompile(boolean jspcompile)
    {
        this.jspcompile = jspcompile;
    }
}
