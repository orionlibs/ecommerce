package de.hybris.platform.cms2.model;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;

public class AbstractPageDynamicNavigationNodeList implements DynamicAttributeHandler<List<CMSNavigationNodeModel>, AbstractPageModel>
{
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private PersistentKeyGenerator cmsNavigationEntryCodeGenerator;


    public List<CMSNavigationNodeModel> get(AbstractPageModel model)
    {
        if(this.modelService.isNew(model))
        {
            return Collections.emptyList();
        }
        List<CMSNavigationNodeModel> result = new ArrayList<>();
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {ne.navigationNode} from {CMSNavigationEntry as ne} where {ne.item} = ?item and {ne.navigationNode} is not null");
        query.addQueryParameter("item", model);
        List<CMSNavigationNodeModel> queryResult = this.flexibleSearchService.search(query).getResult();
        for(CMSNavigationNodeModel node : queryResult)
        {
            if(!result.contains(node))
            {
                result.add(node);
            }
        }
        return result;
    }


    public void set(AbstractPageModel model, List<CMSNavigationNodeModel> value)
    {
        List<CMSNavigationNodeModel> allAssigned = get(model);
        if(value == null || value.isEmpty())
        {
            for(CMSNavigationNodeModel node : allAssigned)
            {
                removePageFromNode(node, model);
            }
        }
        else
        {
            for(CMSNavigationNodeModel node : allAssigned)
            {
                if(!value.contains(node))
                {
                    removePageFromNode(node, model);
                }
            }
            for(CMSNavigationNodeModel node : value)
            {
                if(!allAssigned.contains(node))
                {
                    addPageToNode(node, model);
                }
            }
        }
    }


    protected void addPageToNode(CMSNavigationNodeModel node, AbstractPageModel page)
    {
        if(this.modelService.isNew(page))
        {
            return;
        }
        for(CMSNavigationEntryModel cMSNavigationEntryModel : node.getEntries())
        {
            if(cMSNavigationEntryModel.getItem().equals(page))
            {
                return;
            }
        }
        CMSNavigationEntryModel entry = (CMSNavigationEntryModel)this.modelService.create(CMSNavigationEntryModel.class);
        entry.setItem((ItemModel)page);
        entry.setNavigationNode(node);
        entry.setCatalogVersion(page.getCatalogVersion());
        entry.setUid(ObjectUtils.toString(getCmsNavigationEntryCodeGenerator().generate()));
        this.modelService.save(entry);
    }


    protected void removePageFromNode(CMSNavigationNodeModel node, AbstractPageModel page)
    {
        CMSNavigationEntryModel toDelete = null;
        for(CMSNavigationEntryModel entry : node.getEntries())
        {
            if(entry.getItem().equals(page))
            {
                toDelete = entry;
                break;
            }
        }
        if(toDelete != null)
        {
            List<CMSNavigationEntryModel> entries = new ArrayList<>(node.getEntries());
            entries.remove(toDelete);
            node.setEntries(entries);
            this.modelService.save(node);
            this.modelService.remove(toDelete);
        }
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public PersistentKeyGenerator getCmsNavigationEntryCodeGenerator()
    {
        return this.cmsNavigationEntryCodeGenerator;
    }


    @Required
    public void setCmsNavigationEntryCodeGenerator(PersistentKeyGenerator cmsNavigationEntryCodeGenerator)
    {
        this.cmsNavigationEntryCodeGenerator = cmsNavigationEntryCodeGenerator;
    }
}
