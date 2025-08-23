package de.hybris.bootstrap.typesystem;

public class YDeploymentElement extends YNameSpaceElement
{
    protected final String deploymentName;
    private YDeployment depl;


    protected YDeploymentElement(YNamespace container, String deploymentName)
    {
        super(container);
        this.deploymentName = deploymentName.intern();
    }


    public void validate()
    {
        super.validate();
        getDeployment();
    }


    public void resetCaches()
    {
        super.resetCaches();
        this.depl = null;
    }


    public String getDeploymentName()
    {
        return this.deploymentName;
    }


    public YDeployment getDeployment()
    {
        if(this.depl == null)
        {
            this.depl = getTypeSystem().getDeployment(getDeploymentName());
            if(this.depl == null)
            {
                throw new IllegalStateException("invalid finder " + this + " due to missing deployment " + getDeploymentName());
            }
        }
        return this.depl;
    }
}
