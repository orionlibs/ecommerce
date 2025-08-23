package de.hybris.platform.cms2.relateditems.visitors.page;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.relateditems.RelatedItemVisitor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RestrictionRelatedItemVisitor implements RelatedItemVisitor
{
    private Predicate<ItemModel> visitorPredicate;
    private ComponentRelatedItemVisitor componentRelatedItemVisitor;


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel, InterceptorContext interceptorContext)
    {
        return getRelatedItems(itemModel);
    }


    public List<CMSItemModel> getRelatedItems(ItemModel itemModel)
    {
        AbstractRestrictionModel restrictionModel = (AbstractRestrictionModel)itemModel;
        Collection<AbstractCMSComponentModel> components = CollectionUtils.emptyIfNull(restrictionModel.getComponents());
        List<CMSItemModel> componentRelatedItems = (List<CMSItemModel>)components.stream().flatMap(component -> getComponentRelatedItemVisitor().getRelatedItems((ItemModel)component).stream()).collect(Collectors.toList());
        Collection<AbstractPageModel> restrictionRelatedPages = CollectionUtils.emptyIfNull(restrictionModel.getPages());
        List<CMSItemModel> relatedItems = new ArrayList<>();
        relatedItems.add(restrictionModel);
        relatedItems.addAll(components);
        relatedItems.addAll(restrictionRelatedPages);
        relatedItems.addAll(componentRelatedItems);
        return relatedItems;
    }


    public Predicate<ItemModel> getConstrainedBy()
    {
        return this.visitorPredicate;
    }


    @Required
    public void setVisitorPredicate(Predicate<ItemModel> visitorPredicate)
    {
        this.visitorPredicate = visitorPredicate;
    }


    protected ComponentRelatedItemVisitor getComponentRelatedItemVisitor()
    {
        return this.componentRelatedItemVisitor;
    }


    @Required
    public void setComponentRelatedItemVisitor(ComponentRelatedItemVisitor componentRelatedItemVisitor)
    {
        this.componentRelatedItemVisitor = componentRelatedItemVisitor;
    }
}
