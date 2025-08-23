package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObjectNotFoundException;
import de.hybris.platform.servicelayer.internal.model.impl.AttributeProvider;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Locale;

public class ItemAttributeProvider implements AttributeProvider
{
    private final PK itemPK;
    private boolean removed = false;
    private boolean returnRemovedCachedItem = false;
    private transient PersistenceObject cachedItem;
    private transient ItemModelConverter converter;


    public ItemAttributeProvider(PersistenceObject item, ItemModelConverter converter)
    {
        ServicesUtil.validateParameterNotNull(item, "item was null");
        ServicesUtil.validateParameterNotNull(converter, "converter was null");
        this.itemPK = item.getPK();
        this.cachedItem = item;
        this.converter = converter;
    }


    public ItemAttributeProvider(PK pk, ItemModelConverter converter)
    {
        ServicesUtil.validateParameterNotNull(pk, "pk was null");
        ServicesUtil.validateParameterNotNull(converter, "converter was null");
        this.itemPK = pk;
        this.cachedItem = null;
        this.converter = converter;
    }


    public ItemModelConverter getConverter()
    {
        if(this.converter == null)
        {
            PersistenceObject persistenceObject = getPersistenceObject();
            if(persistenceObject != null)
            {
                ConverterRegistry reg = (ConverterRegistry)ServicelayerUtils.getApplicationContext().getBean("converterRegistry");
                ModelConverter ret = reg.getModelConverterBySourceType(persistenceObject.getTypeCode());
                if(ret instanceof EnumValueModelConverter)
                {
                    ret = reg.getModelConverterByModelType(EnumerationValueModel.class);
                }
                this.converter = (ItemModelConverter)ret;
            }
        }
        return this.converter;
    }


    public Object getAttribute(String qualifier)
    {
        if(this.removed)
        {
            return null;
        }
        PersistenceObject persistenceObject = getPersistenceObject();
        if(persistenceObject == null)
        {
            return null;
        }
        return getConverter().readSingleAttribute(null, persistenceObject, qualifier);
    }


    public Object getLocalizedAttribute(String qualifier, Locale dataLoc)
    {
        return this.removed ? null : getConverter().readSingleAttribute(dataLoc, getPersistenceObject(), qualifier);
    }


    public PersistenceObject getPersistenceObject()
    {
        if(this.removed)
        {
            return null;
        }
        if(this.returnRemovedCachedItem)
        {
            return this.cachedItem;
        }
        try
        {
            if(this.cachedItem != null)
            {
                this.cachedItem = this.cachedItem.getLatest();
            }
            else
            {
                this.cachedItem = getSourceTransformer().transformSource(this.itemPK);
            }
            return this.cachedItem;
        }
        catch(PersistenceObjectNotFoundException e)
        {
            if(this.cachedItem != null)
            {
                this.returnRemovedCachedItem = true;
                return this.cachedItem;
            }
            this.removed = true;
            return null;
        }
    }


    @Deprecated(since = "5.7.0", forRemoval = true)
    public Item getItem()
    {
        return getSourceTransformer().getItemFromPersistenceObject(getPersistenceObject());
    }


    public String toString()
    {
        return getClass().getSimpleName() + "[DO NOT LOAD<->" + getClass().getSimpleName() + "]";
    }


    private SourceTransformer getSourceTransformer()
    {
        return getConverter().getSourceTransformer();
    }
}
