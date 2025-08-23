package de.hybris.bootstrap.config;

public class CoreExtensionModule extends AbstractExtensionModule
{
    private String packageroot;
    private String manager;
    private boolean generate = false;
    private boolean generatePartOf = false;


    public CoreExtensionModule()
    {
        super("core");
    }


    public String getManager()
    {
        return this.manager;
    }


    public void setManager(String manager)
    {
        this.manager = manager;
    }


    public String getPackageRoot()
    {
        return this.packageroot;
    }


    public void setPackageRoot(String packageroot)
    {
        this.packageroot = packageroot;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }


    public void setGenerate(boolean generate)
    {
        this.generate = generate;
    }


    public boolean isGeneratePartOf()
    {
        return this.generatePartOf;
    }


    public void setGeneratePartOf(boolean generatePartOf)
    {
        this.generatePartOf = generatePartOf;
    }
}
