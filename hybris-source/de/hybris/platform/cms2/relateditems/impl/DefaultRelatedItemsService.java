package de.hybris.platform.cms2.relateditems.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;

public class DefaultRelatedItemsService implements RelatedItemsService, InitializingBean
{
    private List<RelatedItemVisitor> visitors;
    private TypeService typeService;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        return (List<CMSItemModel>)this.visitors
                        .stream()
                        .filter(visitor -> visitor.getConstrainedBy().test(itemModel))
                        .flatMap(visitor -> visitor.getRelatedItems(itemModel).stream())
                        .collect(Collectors.toList());
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        return (List<CMSItemModel>)this.visitors
                        .stream()
                        .filter(visitor -> visitor.getConstrainedBy().test(itemModel))
                        .flatMap(visitor -> visitor.getRelatedItems(itemModel, interceptorContext).stream())
                        .collect(Collectors.toList());
    }


    public void afterPropertiesSet()
    {
        Collections.reverse(this.visitors);
    }


    public List<RelatedItemVisitor> getVisitors()
    {
        return this.visitors;
    }


    public void setVisitors(List<RelatedItemVisitor> visitors)
    {
        this.visitors = visitors;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
