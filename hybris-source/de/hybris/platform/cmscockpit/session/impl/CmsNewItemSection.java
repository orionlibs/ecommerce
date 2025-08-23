package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.NewItemSection;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class CmsNewItemSection extends NewItemSection
{
    private static final Logger LOG = Logger.getLogger(CmsNewItemSection.class);


    public CmsNewItemSection(EditorAreaController editorAreaController, String label)
    {
        super(editorAreaController, label);
    }


    public SectionRenderer getCustomRenderer()
    {
        return (SectionRenderer)new Object(this);
    }


    protected void createAllRequiredItems(SectionPanel panel)
    {
        ObjectValueContainer newContainer = getEditorArea().getCurrentObjectValues();
        PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("AbstractCMSComponent.slots");
        ObjectValueContainer.ObjectValueHolder objectValueHolder = newContainer.getValue(propertyDescriptor, null);
        Object currentValue = objectValueHolder.getCurrentValue();
        ContentSlotModel ultimateValueType = null;
        if("REFERENCE".equals(propertyDescriptor.getEditorType()))
        {
            if(PropertyDescriptor.Multiplicity.LIST.equals(propertyDescriptor.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET
                            .equals(propertyDescriptor.getMultiplicity()))
            {
                Collection rawCollection = (Collection)currentValue;
                for(Iterator iter = rawCollection.iterator(); iter.hasNext(); )
                {
                    Object singleReferenceObject = iter.next();
                    if(singleReferenceObject instanceof TypedObject)
                    {
                        ultimateValueType = (ContentSlotModel)((TypedObject)singleReferenceObject).getObject();
                    }
                    else if(singleReferenceObject instanceof de.hybris.platform.core.model.ItemModel)
                    {
                        ultimateValueType = (ContentSlotModel)singleReferenceObject;
                    }
                    if(ultimateValueType != null && getModelService().isNew(ultimateValueType))
                    {
                        getModelService().save(ultimateValueType);
                        ContentSlotForPageModel relation = new ContentSlotForPageModel();
                        relation.setContentSlot(ultimateValueType);
                        Object object = ((CmsPageBrowserModel)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser()).getCurrentPageObject().getObject();
                        if(object instanceof AbstractPageModel)
                        {
                            AbstractPageModel currentPage = (AbstractPageModel)object;
                            relation.setPage(currentPage);
                            relation.setPosition(ultimateValueType.getCurrentPosition());
                            getModelService().save(relation);
                            continue;
                        }
                        LOG.warn("Unexpected type of current page object.");
                    }
                }
            }
        }
        createNewItem(panel);
    }
}
