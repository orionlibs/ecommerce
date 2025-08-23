package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import de.hybris.platform.productcockpit.dao.CategoryDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryNode extends MacFinderTreeNodeAbstract implements MacFinderTreeNodeChildable
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryNode.class);
    private final AbstractDao categoryDao = (AbstractDao)new CategoryDao();


    public AbstractDao getDao()
    {
        return this.categoryDao;
    }


    public String getDisplayedLabel()
    {
        String label = null;
        Object object = getOriginalItem().getObject();
        if(object instanceof CategoryModel)
        {
            try
            {
                label = ((CategoryModel)object).getName(UISessionUtils.getCurrentSession().getGlobalDataLocale());
            }
            catch(Exception e)
            {
                LOG.warn("Could not retrieve category name. Trying with super class...", e);
            }
        }
        if(StringUtils.isBlank(label))
        {
            label = super.getDisplayedLabel();
        }
        return StringUtils.isBlank(label) ? "" : label;
    }


    public boolean isNameAttributeExist() throws JaloSecurityException
    {
        return ((Item)TypeTools.getModelService().getSource(getOriginalItem().getObject())).getAllAttributes()
                        .containsKey("name");
    }
}
