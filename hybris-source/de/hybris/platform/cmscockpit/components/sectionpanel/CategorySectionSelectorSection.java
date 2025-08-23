package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class CategorySectionSelectorSection extends DefaultSectionSelectorSection
{
    private static final Logger LOG = Logger.getLogger(CategorySectionSelectorSection.class);


    public void selectionChanged()
    {
        TypedObject selectedItem = getSelectedItem();
        if(selectedItem != null)
        {
            CategoryModel categoryModel = null;
            Object object = selectedItem.getObject();
            if(object instanceof CategoryModel)
            {
                categoryModel = (CategoryModel)object;
            }
            if(categoryModel == null)
            {
                LOG.error("Category could not be activated. Reason: Selected item not valid");
            }
        }
        refreshView();
    }


    public List<TypedObject> getItems()
    {
        TypedObject selectedCatalogVersionObject = getParentSection().getRelatedObject();
        CatalogVersionModel selectedCatalogVersion = (CatalogVersionModel)selectedCatalogVersionObject.getObject();
        List<TypedObject> ret = new ArrayList<>();
        if(selectedCatalogVersion != null)
        {
            ret.addAll(getTypeService().wrapItems(selectedCatalogVersion.getRootCategories()));
        }
        else
        {
            LOG.warn("It is not possible to retrieve categories for empty catalog version!");
        }
        if(!ret.isEmpty())
        {
            setItems(getTypeService().wrapItems(ret));
        }
        else
        {
            setItems(Collections.EMPTY_LIST);
        }
        return super.getItems();
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
