package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeColumn;

public interface MacFinderTreeNode
{
    public static final String NAME_ATTR = "name";
    public static final String CODE_ATTR = "code";
    public static final String UNDEFINED_ATTR = "undefined";
    public static final String RIGH_SQUERE_BRACKET = " ]";
    public static final String LEFT_SQUERE_BRACKET = "[ ";


    void setParentColumn(MacFinderTreeColumn paramMacFinderTreeColumn);


    MacFinderTreeColumn getContainingColumn();


    boolean isSelected();


    void setSelected(boolean paramBoolean);


    void setQuantity(int paramInt);


    int getQuantity();


    String getDisplayedLabel() throws JaloSecurityException;


    TypedObject getOriginalItem();


    void setOriginalItem(TypedObject paramTypedObject);


    AbstractDao getDao();


    boolean isNameAttributeExist() throws JaloSecurityException;


    boolean isVisible();
}
