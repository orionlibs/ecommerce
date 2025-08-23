package de.hybris.platform.cockpit.components.listeners;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import java.util.List;
import java.util.TreeSet;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

public class QueryRenameEventListener implements EventListener
{
    private final UIQuery query;


    public QueryRenameEventListener(UIQuery query)
    {
        this.query = query;
    }


    public void onEvent(Event event) throws Exception
    {
        Textbox textBox = (Textbox)event.getTarget();
        String newName = textBox.getValue();
        if(UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea() instanceof BaseUICockpitNavigationArea)
        {
            BaseUICockpitNavigationArea naviArea = (BaseUICockpitNavigationArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea();
            List<UICollectionQuery> queriesCollection = ((AbstractNavigationAreaModel)naviArea.getSectionModel()).getCollections();
            if("".equals(newName))
            {
                newName = Labels.getLabel("collection.unnamed");
                if(naviArea.getSectionModel() instanceof AbstractNavigationAreaModel)
                {
                    TreeSet<Integer> prefixes = new TreeSet<>();
                    for(UICollectionQuery collectionQuery : queriesCollection)
                    {
                        if(collectionQuery.getLabel().matches(Labels.getLabel("collection.unnamed") + "[\\d]+"))
                        {
                            int currentIdx = Integer.parseInt(collectionQuery.getLabel().substring(7));
                            prefixes.add(Integer.valueOf(currentIdx));
                        }
                    }
                    if(!prefixes.isEmpty())
                    {
                        for(int p = 1; p <= ((Integer)prefixes.last()).intValue() + 1; p++)
                        {
                            if(!prefixes.contains(Integer.valueOf(p)))
                            {
                                newName = newName + newName;
                                break;
                            }
                        }
                    }
                    else
                    {
                        newName = newName + "1";
                    }
                }
            }
            if(this.query instanceof UICollectionQuery)
            {
                naviArea.renameCollection((UICollectionQuery)this.query, newName);
            }
            else if(this.query instanceof UISavedQuery)
            {
                naviArea.renameSavedQuery((UISavedQuery)this.query, newName);
            }
        }
        ((Popup)textBox.getParent()).detach();
    }
}
