package de.hybris.platform.cms2.cloning.service.predicate;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class CMSItemCloneablePredicate implements Predicate<ItemModel>
{
    private TypeService typeService;
    private List<String> typeNonCloneableList;
    private Set<String> typeBlacklistCloneableSet;
    private Set<String> typeBlacklistSet;


    public boolean test(ItemModel itemModel)
    {
        if(itemModel != null)
        {
            boolean isTypeNotBlacklisted = getTypeBlacklistSet().stream().noneMatch(type -> type.equals(itemModel.getItemtype()));
            boolean isBlacklistedTypeCloneable = getTypeBlacklistCloneableSet().stream().anyMatch(type -> type.equals(itemModel.getItemtype()));
            boolean isTypeCloneable = getTypeNonCloneableList().stream().noneMatch(type -> getTypeService().isAssignableFrom(type, itemModel.getItemtype()));
            return (isBlacklistedTypeCloneable || (isTypeCloneable && isTypeNotBlacklisted));
        }
        return false;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected List<String> getTypeNonCloneableList()
    {
        return this.typeNonCloneableList;
    }


    @Required
    public void setTypeNonCloneableList(List<String> typeNonCloneableList)
    {
        this.typeNonCloneableList = typeNonCloneableList;
    }


    protected Set<String> getTypeBlacklistSet()
    {
        return this.typeBlacklistSet;
    }


    @Required
    public void setTypeBlacklistSet(Set<String> typeBlacklistSet)
    {
        this.typeBlacklistSet = typeBlacklistSet;
    }


    protected Set<String> getTypeBlacklistCloneableSet()
    {
        return this.typeBlacklistCloneableSet;
    }


    @Required
    public void setTypeBlacklistCloneableSet(Set<String> typeBlacklistCloneableSet)
    {
        this.typeBlacklistCloneableSet = typeBlacklistCloneableSet;
    }
}
