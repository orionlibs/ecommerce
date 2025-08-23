package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.jalo.SessionContext;

public class SyncAttributeDescriptorData
{
    private final String qualifier;
    private final boolean includeInSync;
    private final boolean copyByValue;
    private final boolean untranslatable;
    private final Object preset;
    private final Boolean isPartialTranslationEnabled;


    public SyncAttributeDescriptorData(SessionContext ctx, SyncAttributeDescriptorConfig cfg)
    {
        this(cfg
                                        .getAttributeDescriptor(ctx).getQualifier(), cfg
                                        .isIncludedInSyncAsPrimitive(ctx), cfg
                                        .isCopyByValueAsPrimitive(ctx), cfg
                                        .isUntranslatableAsPrimitive(ctx),
                        cfg.isIncludedInSyncAsPrimitive(ctx) ? cfg.getPresetValue(ctx) : null, cfg
                                        .isPartiallyTranslatable(ctx));
    }


    public SyncAttributeDescriptorData(String qualifier, boolean includeInSync, boolean copyByValue, boolean untranslatable, Object preset, Boolean isPartialTranslationEnabled)
    {
        this.qualifier = qualifier;
        this.includeInSync = includeInSync;
        this.copyByValue = copyByValue;
        this.untranslatable = untranslatable;
        this.preset = preset;
        this.isPartialTranslationEnabled = isPartialTranslationEnabled;
    }


    public boolean isIncludedInSync()
    {
        return this.includeInSync;
    }


    public boolean isCopyByValue()
    {
        return this.copyByValue;
    }


    public boolean isUntranslatable()
    {
        return this.untranslatable;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public Object getPresetValue()
    {
        return this.preset;
    }


    public Boolean isPartialTranslationEnabled()
    {
        return this.isPartialTranslationEnabled;
    }
}
