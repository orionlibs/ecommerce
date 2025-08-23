package de.hybris.platform.patches.data;

public class ImpexDataFile extends AbstractImpexFile implements Cloneable
{
    public ImpexDataFile clone()
    {
        return (ImpexDataFile)clone(new ImpexDataFile());
    }
}
