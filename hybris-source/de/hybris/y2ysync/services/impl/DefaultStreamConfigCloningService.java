package de.hybris.y2ysync.services.impl;

import com.google.common.collect.Sets;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.StreamConfigCloningService;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultStreamConfigCloningService implements StreamConfigCloningService
{
    private ItemModelCloneCreator itemModelCloneCreator;


    public Y2YStreamConfigurationContainerModel cloneStreamContainer(Y2YStreamConfigurationContainerModel y2YStreamConfigurationContainer, String containerId)
    {
        Y2YStreamConfigurationContainerModel streamConfigContainerClone = (Y2YStreamConfigurationContainerModel)this.itemModelCloneCreator.copy((ItemModel)y2YStreamConfigurationContainer);
        Set<StreamConfigurationModel> clonedStreams = (Set)cloneStreams(y2YStreamConfigurationContainer
                        .getConfigurations());
        clonedStreams.stream().forEach(e -> e.setContainer((StreamConfigurationContainerModel)streamConfigContainerClone));
        streamConfigContainerClone.setConfigurations(clonedStreams);
        streamConfigContainerClone.setId(containerId);
        return streamConfigContainerClone;
    }


    public Set<Y2YStreamConfigurationModel> cloneStreamConfigurations(Y2YStreamConfigurationModel... streamConfigurations)
    {
        return (Set)cloneStreams(Sets.newHashSet((Object[])streamConfigurations));
    }


    private Set<? extends StreamConfigurationModel> cloneStreams(Set<StreamConfigurationModel> streamConfigurations)
    {
        Set<StreamConfigurationModel> clonedStreams = new HashSet<>();
        if(CollectionUtils.isNotEmpty(streamConfigurations))
        {
            for(StreamConfigurationModel streamConfig : streamConfigurations)
            {
                Y2YStreamConfigurationModel clonedStreamConfig = (Y2YStreamConfigurationModel)this.itemModelCloneCreator.copy((ItemModel)streamConfig);
                clonedStreamConfig.setContainer(null);
                clonedStreamConfig.setStreamId(clonedStreamConfig.getStreamId() + clonedStreamConfig.getStreamId());
                clonedStreams.add(clonedStreamConfig);
            }
        }
        return clonedStreams;
    }


    @Required
    public void setItemModelCloneCreator(ItemModelCloneCreator itemModelCloneCreator)
    {
        this.itemModelCloneCreator = itemModelCloneCreator;
    }
}
