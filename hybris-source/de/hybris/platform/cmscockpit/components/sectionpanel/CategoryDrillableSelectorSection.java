package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.AbstractDrillableSelectorSection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class CategoryDrillableSelectorSection extends AbstractDrillableSelectorSection
{
    private static final Logger LOG = Logger.getLogger(CategoryDrillableSelectorSection.class);


    public List<TypedObject> getItems()
    {
        TypedObject currentObject = null;
        List<TypedObject> ret = new ArrayList<>();
        if(currentLevel() != 0)
        {
            currentObject = getLastElement();
            if(currentObject.getObject() instanceof CategoryModel)
            {
                CategoryModel categoryModel = (CategoryModel)currentObject.getObject();
                ret = UISessionUtils.getCurrentSession().getTypeService().wrapItems(categoryModel.getCategories());
            }
        }
        else if(getParentSection() != null)
        {
            currentObject = getParentSection().getRelatedObject();
            if(currentObject.getObject() instanceof CatalogVersionModel)
            {
                CatalogVersionModel catalogVersionModel = (CatalogVersionModel)currentObject.getObject();
                ret = UISessionUtils.getCurrentSession().getTypeService().wrapItems(catalogVersionModel.getRootCategories());
            }
        }
        return ret;
    }


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
}
