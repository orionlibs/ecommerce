package de.hybris.platform.cmscockpit.navigationnode.browserarea.list;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataListener;

public class RelatedItemListModel implements ListModel
{
    private final TypedObject naviNode;
    private TypeService typeService;
    private final List<ListDataListener> listeners = new ArrayList<>();


    public RelatedItemListModel(TypedObject naviNode)
    {
        this.naviNode = naviNode;
    }


    public Object getElementAt(int index)
    {
        Object ret = null;
        List<ItemModel> items = extractRelatedItems(getNaviNode());
        if(0 <= index && index < items.size())
        {
            ret = (this.naviNode == null) ? null : getTypeService().wrapItem(items.get(index));
        }
        return ret;
    }


    public int getSize()
    {
        return (this.naviNode == null) ? 0 : extractRelatedItems(getNaviNode()).size();
    }


    protected CMSNavigationNodeModel getNaviNode()
    {
        return (CMSNavigationNodeModel)this.naviNode.getObject();
    }


    public void addListDataListener(ListDataListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeListDataListener(ListDataListener listener)
    {
        if(this.listeners.contains(listener))
        {
            this.listeners.remove(listener);
        }
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected List<ItemModel> extractRelatedItems(CMSNavigationNodeModel cmsNaviagtionNode)
    {
        List<ItemModel> ret = new ArrayList<>();
        if(cmsNaviagtionNode == null)
        {
            return ret;
        }
        for(CMSNavigationEntryModel entry : cmsNaviagtionNode.getEntries())
        {
            ret.add(entry.getItem());
        }
        return ret;
    }
}
