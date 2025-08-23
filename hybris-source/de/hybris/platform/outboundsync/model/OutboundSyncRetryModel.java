package de.hybris.platform.outboundsync.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundSyncRetryModel extends ItemModel
{
    public static final String _TYPECODE = "OutboundSyncRetry";
    public static final String ITEMPK = "itemPk";
    public static final String CHANNEL = "channel";
    public static final String SYNCATTEMPTS = "syncAttempts";
    public static final String REMAININGSYNCATTEMPTS = "remainingSyncAttempts";
    public static final String REACHEDMAXRETRIES = "reachedMaxRetries";


    public OutboundSyncRetryModel()
    {
    }


    public OutboundSyncRetryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncRetryModel(OutboundChannelConfigurationModel _channel, Long _itemPk)
    {
        setChannel(_channel);
        setItemPk(_itemPk);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncRetryModel(OutboundChannelConfigurationModel _channel, Long _itemPk, ItemModel _owner)
    {
        setChannel(_channel);
        setItemPk(_itemPk);
        setOwner(_owner);
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
    public OutboundChannelConfigurationModel getChannel()
    {
        return (OutboundChannelConfigurationModel)getPersistenceContext().getPropertyValue("channel");
    }


    @Accessor(qualifier = "itemPk", type = Accessor.Type.GETTER)
    public Long getItemPk()
    {
        return (Long)getPersistenceContext().getPropertyValue("itemPk");
    }


    @Accessor(qualifier = "reachedMaxRetries", type = Accessor.Type.GETTER)
    public Boolean getReachedMaxRetries()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("reachedMaxRetries");
    }


    @Accessor(qualifier = "remainingSyncAttempts", type = Accessor.Type.GETTER)
    public Integer getRemainingSyncAttempts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("remainingSyncAttempts");
    }


    @Accessor(qualifier = "syncAttempts", type = Accessor.Type.GETTER)
    public Integer getSyncAttempts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("syncAttempts");
    }


    @Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
    public void setChannel(OutboundChannelConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("channel", value);
    }


    @Accessor(qualifier = "itemPk", type = Accessor.Type.SETTER)
    public void setItemPk(Long value)
    {
        getPersistenceContext().setPropertyValue("itemPk", value);
    }


    @Accessor(qualifier = "reachedMaxRetries", type = Accessor.Type.SETTER)
    public void setReachedMaxRetries(Boolean value)
    {
        getPersistenceContext().setPropertyValue("reachedMaxRetries", value);
    }


    @Accessor(qualifier = "remainingSyncAttempts", type = Accessor.Type.SETTER)
    public void setRemainingSyncAttempts(Integer value)
    {
        getPersistenceContext().setPropertyValue("remainingSyncAttempts", value);
    }


    @Accessor(qualifier = "syncAttempts", type = Accessor.Type.SETTER)
    public void setSyncAttempts(Integer value)
    {
        getPersistenceContext().setPropertyValue("syncAttempts", value);
    }
}
