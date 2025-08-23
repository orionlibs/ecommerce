package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.listview.impl.ContextAreaValueContainer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.listview.ListViewHelper;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBrowserTableModelListener implements TableModelListener
{
    private static final Logger log = LoggerFactory.getLogger(DefaultBrowserTableModelListener.class);
    protected final UIListView view;
    protected final AdvancedBrowserModel browser;


    public DefaultBrowserTableModelListener(BrowserModel browser, UIListView view)
    {
        this.view = view;
        if(browser instanceof AdvancedBrowserModel)
        {
            this.browser = (AdvancedBrowserModel)browser;
        }
        else
        {
            throw new IllegalArgumentException("Browser model not of type 'AdvancedBrowserModel'.");
        }
    }


    public void cellChanged(int columnIndex, int rowIndex)
    {
        ListViewHelper.cellChanged(this.view, columnIndex, rowIndex, this.browser.getArea());
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        this.browser.setSelectedIndexes(rowIndexes);
        this.view.updateSelection();
    }


    public void onEvent(String eventName, Object value)
    {
        if(eventName == null || eventName.length() < 1)
        {
            throw new IllegalArgumentException("An event name must be specified");
        }
        if(eventName.equalsIgnoreCase("opencontextarea"))
        {
            Collection<TypedObject> contextItems = null;
            ObjectTemplate type = null;
            TypedObject rootItem = null;
            PropertyDescriptor rootProperty = null;
            if(value instanceof ContextAreaValueContainer)
            {
                contextItems = ((ContextAreaValueContainer)value).getValues();
                type = ((ContextAreaValueContainer)value).getType();
                rootItem = ((ContextAreaValueContainer)value).getRootItem();
                rootProperty = ((ContextAreaValueContainer)value).getRootProperty();
            }
            else
            {
                log.error("Can not open context area (Reason: Can not parse data).");
                return;
            }
            if(contextItems != null)
            {
                if(this.browser.isContextVisible())
                {
                    if(type == null)
                    {
                        this.browser.setContextItems(rootItem, contextItems);
                    }
                    else
                    {
                        this.browser.setContextRootTypePropertyDescriptor(rootProperty);
                        this.browser.setContextItems(rootItem, contextItems, type);
                    }
                }
                else
                {
                    this.browser.setContextItemsDirectly(rootItem, contextItems);
                    if(type != null)
                    {
                        this.browser.setContextRootType(type);
                    }
                    this.browser.setContextVisible(true);
                }
            }
        }
        else if(eventName.equalsIgnoreCase("updateitems"))
        {
            if(this.browser instanceof AbstractPageableBrowserModel)
            {
                AbstractPageableBrowserModel pageBrowserModel = (AbstractPageableBrowserModel)this.browser;
                pageBrowserModel.updateItems(pageBrowserModel.getCurrentPage());
            }
            else
            {
                this.browser.updateItems();
            }
        }
        else if(eventName.equalsIgnoreCase("hidecontextarea"))
        {
            if(value instanceof Collection)
            {
                this.browser.setContextItemsDirectly(null, (Collection)value);
            }
            this.browser.setContextVisible(false);
        }
        else if(eventName.equalsIgnoreCase("activateitem"))
        {
            if(value instanceof TypedObject)
            {
                this.browser.setActiveItem((TypedObject)value);
            }
        }
        else if(eventName.equalsIgnoreCase("shownotification"))
        {
            if(this.browser.getArea().getPerspective() instanceof BaseUICockpitPerspective)
            {
                BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)this.browser.getArea().getPerspective();
                if(basePerspective.getNotifier() != null)
                {
                    basePerspective.getNotifier().setNotification((Notification)value);
                }
            }
        }
    }
}
