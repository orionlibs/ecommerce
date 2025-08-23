/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * Default item renderer for the {@link AttributeChooserRenderer}
 */
public class AttributeChooserTreeItemRenderer implements TreeitemRenderer<DefaultTreeNode<Attribute>>
{
    private static final String SCLASS_Y_ATTRIBUTE_PICKER = "y-attributepicker";
    private LabelService labelService;


    @Override
    public void render(final Treeitem item, final DefaultTreeNode<Attribute> data, final int i)
    {
        final Treerow treerow = new Treerow();
        UITools.addSClass(treerow, SCLASS_Y_ATTRIBUTE_PICKER + "-level-" + (item.getLevel() + 1));
        treerow.setParent(item);
        final Treecell treecell = new Treecell();
        treecell.setParent(treerow);
        final Attribute selectedAttribute = data.getData();
        final String attrName = selectedAttribute.getDisplayName();
        if(selectedAttribute.isMandatory())
        {
            UITools.addSClass(treerow, SCLASS_Y_ATTRIBUTE_PICKER + "-mandatory-item");
        }
        treecell.appendChild(new Label(attrName));
        if(hasLocalizedChildren(selectedAttribute))
        {
            UITools.addSClass(treerow, SCLASS_Y_ATTRIBUTE_PICKER + "-localized");
        }
        else if(isLocalizedChild(selectedAttribute))
        {
            UITools.addSClass(treerow, SCLASS_Y_ATTRIBUTE_PICKER + "-localized-child");
            if(StringUtils.isNotBlank(selectedAttribute.getIsoCode()))
            {
                treecell.appendChild(createLanguageMark(selectedAttribute));
            }
        }
        if(item.getTree().getSelectedItems().contains(item.getParentItem()))
        {
            item.getTree().addItemToSelection(item);
            data.getModel().addToSelection(data);
        }
    }


    protected boolean isLocalizedChild(final Attribute selectedAttribute)
    {
        return StringUtils.isNotBlank(selectedAttribute.getIsoCode());
    }


    protected boolean hasLocalizedChildren(final Attribute selectedAttribute)
    {
        return selectedAttribute.hasSubAttributes()
                        && selectedAttribute.getSubAttributes().stream().anyMatch(this::isLocalizedChild);
    }


    protected Label createLanguageMark(final Attribute selectedAttribute)
    {
        final Locale locale = Locale.forLanguageTag(selectedAttribute.getIsoCode());
        final Label attrLanguageLabel = new Label(labelService.getObjectLabel(locale));
        attrLanguageLabel.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-language");
        attrLanguageLabel.setTooltiptext(labelService.getObjectDescription(locale));
        return attrLanguageLabel;
    }


    protected boolean isSubAttribute(final Attribute attribute)
    {
        return attribute.getParent() != null;
    }


    protected boolean isGroupingAttribute(final DefaultTreeNode<Attribute> node)
    {
        return node.getData().hasSubAttributes() && !node.isLeaf();
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
