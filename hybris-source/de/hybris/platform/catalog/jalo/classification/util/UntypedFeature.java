package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.jalo.SessionContext;

@Deprecated(since = "ages", forRemoval = false)
public class UntypedFeature<T> extends Feature<T>
{
    private final String qualifier;


    protected UntypedFeature(FeatureContainer parent, String qualifier, boolean localized)
    {
        super(parent, localized);
        this.qualifier = qualifier;
    }


    protected UntypedFeature(UntypedFeature<T> src) throws CloneNotSupportedException
    {
        super(src);
        this.qualifier = src.qualifier;
    }


    public UntypedFeature<T> clone() throws CloneNotSupportedException
    {
        return new UntypedFeature(this);
    }


    public String getName(SessionContext ctx)
    {
        return this.qualifier;
    }


    protected String getUniqueKey()
    {
        return this.qualifier;
    }


    public FeatureValue<T> createValue(SessionContext ctx, int index, T value)
    {
        FeatureValue<T> ret = new FeatureValue(this, value);
        add(ctx, index, ret);
        return ret;
    }
}
