package de.hybris.platform.cockpit.model.browser.impl;

import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

public class DefaultExtendedSearchBrowserModel extends DefaultSearchBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExtendedSearchBrowserModel.class);
    private boolean allowDelete = false;
    private List<MainAreaComponentFactory> viewModes;


    public DefaultExtendedSearchBrowserModel()
    {
    }


    public DefaultExtendedSearchBrowserModel(ObjectTemplate rootType, boolean allowDelete, boolean showCreateButton)
    {
        super(rootType);
        this.allowDelete = allowDelete;
        setShowCreateButton(showCreateButton);
    }


    public String getLabel()
    {
        String rootTypeLabel = (getRootType() == null) ? "" : ((getRootType().getName() == null) ? ("[" + getRootType().getCode() + "]") : getRootType().getName());
        return rootTypeLabel + ": " + rootTypeLabel;
    }


    protected void confirmAndRemoveItems(Collection<Integer> indexes)
    {
        if(!this.allowDelete)
        {
            return;
        }
        List<TypedObject> items = new ArrayList<>();
        Object[] indexesArray = indexes.toArray();
        for(int i = 0; i < indexesArray.length; i++)
        {
            TypedObject typedObject = getItems().get(((Integer)indexesArray[i]).intValue());
            items.add(typedObject);
        }
        try
        {
            if(Messagebox.show(Labels.getLabel("general.confirm.delete"), Labels.getLabel("general.confirm"), 3, null) == 1)
            {
                ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
                UIEditorArea editorArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea();
                TypedObject currentObject = editorArea.getCurrentObject();
                int unremoved = 0;
                String lastUnremoved = null;
                for(TypedObject typedObject : items)
                {
                    if(canRemove(typedObject))
                    {
                        modelService.remove(typedObject.getObject());
                        if(typedObject.equals(currentObject))
                        {
                            editorArea.setCurrentObject(null);
                        }
                        continue;
                    }
                    unremoved++;
                    lastUnremoved = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(typedObject);
                }
                UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
                updateItems();
                UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
                editorArea.update();
                if(unremoved > 0)
                {
                    Notification notification = new Notification(
                                    (unremoved == 1) ? Labels.getLabel("browser.notification.removeitems.notallpermitted.one", (Object[])new String[] {lastUnremoved}) : Labels.getLabel("browser.notification.removeitems.notallpermitted.multiple", (Object[])new String[] {String.valueOf(unremoved)}));
                    BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
                    if(basePerspective.getNotifier() != null)
                    {
                        basePerspective.getNotifier().setNotification(notification);
                    }
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while removing items: ", e);
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier()
                            .setNotification(new Notification("Error", e.getMessage()));
        }
    }


    private boolean canRemove(TypedObject item)
    {
        return UISessionUtils.getCurrentSession().getSystemService()
                        .checkPermissionOn(item.getType().getCode(), "remove");
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new Object(this));
            this.viewModes.add(new Object(this));
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    public void setAllowDelete(boolean allowDelete)
    {
        this.allowDelete = allowDelete;
    }


    public boolean isAllowDelete()
    {
        return this.allowDelete;
    }
}
