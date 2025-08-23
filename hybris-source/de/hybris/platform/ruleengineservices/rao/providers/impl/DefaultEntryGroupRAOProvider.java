package de.hybris.platform.ruleengineservices.rao.providers.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.ruleengineservices.rao.OrderEntryGroupRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEntryGroupRAOProvider implements RAOProvider<AbstractOrderModel>
{
    private EntryGroupService entryGroupService;
    private Converter<EntryGroup, OrderEntryGroupRAO> entryGroupRaoConverter;


    public Set expandFactModel(AbstractOrderModel order)
    {
        if(CollectionUtils.isEmpty(order.getEntryGroups()))
        {
            return Collections.emptySet();
        }
        Set<OrderEntryGroupRAO> entryGroups = Sets.newHashSet();
        for(AbstractOrderEntryModel orderEntry : order.getEntries())
        {
            for(Integer entryGroupNumber : orderEntry.getEntryGroupNumbers())
            {
                EntryGroup rootEntryGroup = getEntryGroupService().getRoot(orderEntry.getOrder(), entryGroupNumber);
                OrderEntryGroupRAO rootEntryGroupRao = (OrderEntryGroupRAO)getEntryGroupRaoConverter().convert(rootEntryGroup);
                entryGroups.addAll(collectNestedGroups(rootEntryGroup, rootEntryGroupRao));
            }
        }
        return entryGroups;
    }


    protected Set<OrderEntryGroupRAO> collectNestedGroups(EntryGroup rootEntryGroup, OrderEntryGroupRAO rootEntryGroupRao)
    {
        return (Set<OrderEntryGroupRAO>)getEntryGroupService().getNestedGroups(rootEntryGroup).stream().map(eg -> {
            OrderEntryGroupRAO rao = (OrderEntryGroupRAO)getEntryGroupRaoConverter().convert(eg);
            rao.setRootEntryGroup(rootEntryGroupRao);
            return rao;
        }).collect(Collectors.toSet());
    }


    protected EntryGroupService getEntryGroupService()
    {
        return this.entryGroupService;
    }


    @Required
    public void setEntryGroupService(EntryGroupService entryGroupService)
    {
        this.entryGroupService = entryGroupService;
    }


    protected Converter<EntryGroup, OrderEntryGroupRAO> getEntryGroupRaoConverter()
    {
        return this.entryGroupRaoConverter;
    }


    @Required
    public void setEntryGroupRaoConverter(Converter<EntryGroup, OrderEntryGroupRAO> entryGroupRaoConverter)
    {
        this.entryGroupRaoConverter = entryGroupRaoConverter;
    }
}
