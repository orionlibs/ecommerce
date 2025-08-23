/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.tree.util.TreeUtils;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

/**
 * Renders two lists: available attributes and chosen attributes. Every time list of chosen attributes is changed an
 * event will be posted on parent component {@link #EVENT_ATTRIBUTES_SELECTED} passed to render method.
 */
public class AttributeChooserRenderer implements WidgetComponentRenderer<Component, AttributesChooserConfig, AttributeChooserForm>
{
    public static final String EVENT_ATTRIBUTES_SELECTED = "onAttributesSelected";
    private static final String EVENT_NODES_MOVED_TO_TREE = "onNodesMoved";
    private static final String SCLASS_Y_ATTRIBUTE_PICKER = "y-attributepicker";
    private static final String LABEL_ALL_ATTRIBUTES_SELECTED = "attributechooser.all.attributes.selected";
    private static final String LABEL_EMPTY_ATTRIBUTES = "attributechooser.empty.attributes";
    private static final String LABEL_INCLUDE_ALL_ATTRIBUTES = "attributechooser.include.all.attributes";
    private static final String LABEL_SECTION = "attributechooser.section.";
    private static final String LABEL_SEARCH = "attributechooser.search.";
    private static final String LABEL_BUTTON = "attributechooser.button.";
    protected static final String RENDERER_PREFIX = AttributeChooserRenderer.class.getSimpleName();
    protected static final String MODEL_INCLUDE_ALL_SELECTED = RENDERER_PREFIX + "IncludeAllSelected";
    protected static final String MODE_VISIBLE_ROWS_NUMBER = "visibleRowNumber";
    protected static final String MODEL_SELECTED_PATHS = RENDERER_PREFIX + "SelectedPaths_";
    protected static final String MODEL_SEARCH_TEXT = RENDERER_PREFIX + "SearchText_";
    protected static final String SECTION_ID_AVAILABLE = "available";
    protected static final String SECTION_ID_SELECTED = "selected";
    private Integer visibleRowsNumber;
    private TreeitemRenderer<DefaultTreeNode<Attribute>> treeItemRenderer;
    private NodeOperationsHandler nodeOperationsHandler;
    private AttributeChooserNodeExpander attributeChooserNodeExpander;


    @Override
    public void render(final Component parent, final AttributesChooserConfig config, final AttributeChooserForm form,
                    final DataType dataType, final WidgetInstanceManager wim)
    {
        if(!form.hasPopulatedAttributes())
        {
            parent.appendChild(createNoAttributesOverlay(config, wim));
        }
        final Tree availableValuesTree = createTree(getVisibleRowsNumber(wim));
        final Tree selectedValuesTree = createTree(getVisibleRowsNumber(wim));
        populateTreeData(availableValuesTree, selectedValuesTree, form, config, wim);
        restoreSelectedPaths(availableValuesTree, uniquePrefix(config, SECTION_ID_AVAILABLE), wim);
        restoreSelectedPaths(selectedValuesTree, uniquePrefix(config, SECTION_ID_SELECTED), wim);
        addSelectedPathsListener(availableValuesTree, wim, uniquePrefix(config, SECTION_ID_AVAILABLE));
        addSelectedPathsListener(selectedValuesTree, wim, uniquePrefix(config, SECTION_ID_SELECTED));
        addChosenAttributesListener(availableValuesTree, selectedValuesTree, wim, form, config, parent);
        openFirstMultiChildLevel(availableValuesTree, config);
        openFirstMultiChildLevel(selectedValuesTree, config);
        final Div mainContainer = new Div();
        mainContainer.setSclass(SCLASS_Y_ATTRIBUTE_PICKER);
        mainContainer.setParent(parent);
        final Div mainSelectionContainer = new Div();
        mainSelectionContainer.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-main");
        final Div attributesSelectionContainer = new Div();
        attributesSelectionContainer.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-main-container");
        mainSelectionContainer.appendChild(attributesSelectionContainer);
        if(config != null && config.isIncludeAllSupported())
        {
            final Component includeAllOverlay = createIncludeAllOverlay();
            includeAllOverlay.setParent(mainSelectionContainer);
            includeAllOverlay.setVisible(isIncludeAllSelected(wim, config));
            final Component includeAllSection = createIncludeAllSection(isIncludeAllSelected(wim, config), (CheckEvent event) -> {
                wim.getModel().setValue(uniquePrefix(config, MODEL_INCLUDE_ALL_SELECTED), event.isChecked());
                form.setIncludeAll(event.isChecked());
                includeAllOverlay.setVisible(event.isChecked());
                notifyChosenAttributesChanged(form, parent);
            });
            mainContainer.appendChild(includeAllSection);
        }
        mainContainer.appendChild(mainSelectionContainer);
        final Div availableAttributeSection = createAttributesSection(availableValuesTree, wim, config, SECTION_ID_AVAILABLE);
        final Div selectedAttributeSection = createAttributesSection(selectedValuesTree, wim, config, SECTION_ID_SELECTED);
        attributesSelectionContainer.appendChild(availableAttributeSection);
        attributesSelectionContainer.appendChild(selectedAttributeSection);
        attributesSelectionContainer.insertBefore(createButtonsSection(availableValuesTree, selectedValuesTree),
                        selectedAttributeSection);
    }


    protected Component createIncludeAllSection(final boolean initialState, final EventListener<CheckEvent> onIncludeAllListener)
    {
        final Div section = new Div();
        section.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-include");
        final Checkbox includeAllCheckbox = new Checkbox();
        includeAllCheckbox.setSclass("ye-switch-checkbox");
        includeAllCheckbox.addEventListener(Events.ON_CHECK, onIncludeAllListener);
        includeAllCheckbox.setChecked(initialState);
        includeAllCheckbox.setLabel(Labels.getLabel(LABEL_INCLUDE_ALL_ATTRIBUTES));
        includeAllCheckbox.setParent(section);
        return section;
    }


    protected Component createIncludeAllOverlay()
    {
        final Div includeAllGreyOutArea = new Div();
        UITools.addSClass(includeAllGreyOutArea, SCLASS_Y_ATTRIBUTE_PICKER + "-greyout-area");
        includeAllGreyOutArea.appendChild(new Label(Labels.getLabel(LABEL_ALL_ATTRIBUTES_SELECTED)));
        return includeAllGreyOutArea;
    }


    protected HtmlBasedComponent createNoAttributesOverlay(final AttributesChooserConfig config, final WidgetInstanceManager wim)
    {
        final Div includeAllGreyOutArea = new Div();
        UITools.addSClass(includeAllGreyOutArea, SCLASS_Y_ATTRIBUTE_PICKER + "-greyout-area");
        final String message = StringUtils.isNotBlank(config.getEmptyAttributesMessage()) ? config.getEmptyAttributesMessage()
                        : Labels.getLabel(LABEL_EMPTY_ATTRIBUTES);
        includeAllGreyOutArea.appendChild(new Label(message));
        return includeAllGreyOutArea;
    }


    protected Div createAttributesSection(final Tree attributesTree, final WidgetInstanceManager wim,
                    final AttributesChooserConfig config, final String sectionId)
    {
        final String initialSearchText = getFilterText(wim, uniquePrefix(config, sectionId));
        final Div availableAttributesContainer = new Div();
        availableAttributesContainer.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-" + sectionId + "-values-container");
        final Label availableAttributesLabel = new Label(Labels.getLabel(LABEL_SECTION + sectionId));
        availableAttributesLabel.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-values-label");
        availableAttributesContainer.appendChild(availableAttributesLabel);
        final Textbox availableValuesFilter = new Textbox(initialSearchText);
        availableValuesFilter.setPlaceholder(Labels.getLabel(LABEL_SEARCH));
        availableValuesFilter.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-values-filter");
        availableAttributesContainer.appendChild(availableValuesFilter);
        availableValuesFilter.addEventListener(Events.ON_CHANGING, (InputEvent event) -> {
            wim.getModel().setValue(MODEL_SEARCH_TEXT + uniquePrefix(config, sectionId), event.getValue());
            refreshTreeSearch(attributesTree, event.getValue());
            configureTreeNodes(attributesTree, config, event);
        });
        availableAttributesContainer.appendChild(attributesTree);
        return availableAttributesContainer;
    }


    private void configureTreeNodes(final Tree attributesTree, final AttributesChooserConfig config, final InputEvent event)
    {
        final AbstractTreeModel<TreeNode<Attribute>> model = getModel(attributesTree);
        model.getOpenObjects().forEach(model::removeOpenObject);
        if(StringUtils.isNotEmpty(event.getValue()))
        {
            getAttributeChooserNodeExpander().filterTree(attributesTree, config, event.getValue());
        }
        else
        {
            openFirstMultiChildLevel(attributesTree, config);
        }
    }


    protected String getFilterText(final WidgetInstanceManager wim, final String sectionId)
    {
        return ObjectUtils.defaultIfNull(wim.getModel().getValue(MODEL_SEARCH_TEXT + sectionId, String.class), StringUtils.EMPTY);
    }


    protected void addSelectedPathsListener(final Tree tree, final WidgetInstanceManager wim, final String sectionId)
    {
        tree.addEventListener(Events.ON_SELECT,
                        (SelectEvent<Treeitem, TreeNode<Attribute>> event) -> storeSelectedPaths(tree, sectionId, wim));
    }


    protected void restoreSelectedPaths(final Tree tree, final String sectionId, final WidgetInstanceManager wim)
    {
        getModel(tree).addSelectionPaths(wim.getModel().getValue(MODEL_SELECTED_PATHS + sectionId, int[][].class));
    }


    protected void storeSelectedPaths(final Tree tree, final String sectionId, final WidgetInstanceManager wim)
    {
        storeSelectedPaths(sectionId, wim, getModel(tree).getSelectionPaths());
    }


    protected void storeSelectedPaths(final String sectionId, final WidgetInstanceManager wim, final int[][] paths)
    {
        wim.getModel().setValue(MODEL_SELECTED_PATHS + sectionId, paths);
    }


    protected Component createButtonsSection(final Tree availableTree, final Tree selectedTree)
    {
        final Div chooseButtons = new Div();
        UITools.addSClass(chooseButtons, SCLASS_Y_ATTRIBUTE_PICKER + "-choose-buttons");
        chooseButtons.appendChild(createMoveButton(selectedTree, availableTree, "remove"));
        chooseButtons.appendChild(createMoveButton(availableTree, selectedTree, "add"));
        return chooseButtons;
    }


    protected Button createMoveButton(final Tree from, final Tree to, final String buttonId)
    {
        final Button moveBtn = new Button(Labels.getLabel(LABEL_BUTTON + buttonId));
        UITools.addSClass(moveBtn, SCLASS_Y_ATTRIBUTE_PICKER + "-" + buttonId + "-btn");
        moveBtn.addEventListener(Events.ON_CLICK, event -> {
            moveSelectedNodes(from, to);
            moveBtn.setDisabled(from.getSelectedCount() < 1);
        });
        from.addEventListener(Events.ON_SELECT, event -> moveBtn.setDisabled(from.getSelectedCount() < 1));
        Optional.ofNullable(from.getParent()).map(Component::getChildren).stream().flatMap(Collection::stream)
                        .filter(child -> child instanceof Textbox).findFirst()
                        .ifPresent(textbox -> textbox.addEventListener(Events.ON_CHANGING, (InputEvent event) -> moveBtn.setDisabled(true)));
        moveBtn.setDisabled(getModel(from).getSelectionCount() < 1);
        return moveBtn;
    }


    protected Tree createTree(final int visibleRows)
    {
        final Tree tree = new Tree();
        tree.setMultiple(true);
        tree.setCheckmark(true);
        tree.setRows(visibleRows);
        final Treecols treecols = new Treecols();
        treecols.appendChild(new Treecol());
        tree.appendChild(treecols);
        tree.setSclass(SCLASS_Y_ATTRIBUTE_PICKER + "-values-tree");
        addLocalizedNodesSelectionListener(tree);
        tree.setItemRenderer(getTreeItemRenderer());
        return tree;
    }


    protected void populateTreeData(final Tree availableValuesTree, final Tree selectedValuesTree, final AttributeChooserForm form,
                    final AttributesChooserConfig config, final WidgetInstanceManager wim)
    {
        final List<DefaultTreeNode<Attribute>> availableTreeNodes = nodeOperationsHandler
                        .createTreeNodes(form.getAvailableAttributes());
        availableValuesTree
                        .setModel(createTreeModel(availableTreeNodes, getFilterText(wim, uniquePrefix(config, SECTION_ID_AVAILABLE))));
        final List<DefaultTreeNode<Attribute>> selectedTreeNodes = nodeOperationsHandler
                        .createTreeNodes(form.getChosenAttributes());
        selectedValuesTree
                        .setModel(createTreeModel(selectedTreeNodes, getFilterText(wim, uniquePrefix(config, SECTION_ID_SELECTED))));
    }


    protected boolean isIncludeAllSelected(final WidgetInstanceManager wim, final AttributesChooserConfig config)
    {
        return BooleanUtils.isTrue(wim.getModel().getValue(uniquePrefix(config, MODEL_INCLUDE_ALL_SELECTED), Boolean.class));
    }


    protected String uniquePrefix(final AttributesChooserConfig config, final String modelKey)
    {
        return config.getUniqueModelPrefix() + modelKey;
    }


    protected AbstractTreeModel<TreeNode<Attribute>> createTreeModel(final List<DefaultTreeNode<Attribute>> treeNodes,
                    final String searchText)
    {
        final DefaultTreeModel<Attribute> model = new DefaultTreeModel<>(new DefaultTreeNode<>(null, treeNodes));
        model.setMultiple(true);
        nodeOperationsHandler.sort(model, true);
        return nodeOperationsHandler.filterTreeModel(model, searchText);
    }


    protected void refreshTreeSearch(final Tree tree, final String searchText)
    {
        tree.setModel(nodeOperationsHandler.filterTreeModel(getModel(tree), searchText));
    }


    protected AbstractTreeModel<TreeNode<Attribute>> getModel(final Tree tree)
    {
        return ((AbstractTreeModel)tree.getModel());
    }


    protected AbstractTreeModel<TreeNode<Attribute>> getUnfilteredModel(final Tree tree)
    {
        if(tree.getModel() instanceof TreeUtils.FilteredTreeModel)
        {
            return (AbstractTreeModel)((TreeUtils.FilteredTreeModel<Object>)tree.getModel()).getOriginalModel();
        }
        return (AbstractTreeModel)tree.getModel();
    }


    protected void moveSelectedNodes(final Tree sourceTree, final Tree targetTree)
    {
        nodeOperationsHandler.moveNodesBetweenTrees(getModel(sourceTree), getModel(targetTree));
        Events.sendEvent(EVENT_NODES_MOVED_TO_TREE, targetTree, null);
    }


    protected void addLocalizedNodesSelectionListener(final Tree tree)
    {
        tree.addEventListener(Events.ON_SELECT, (SelectEvent<Treeitem, TreeNode<Attribute>> event) -> nodeOperationsHandler
                        .updateGroupingNodesSelection(getModel(tree), event));
    }


    protected void addChosenAttributesListener(final Tree availableTree, final Tree selectedTree, final WidgetInstanceManager wim,
                    final AttributeChooserForm form, final AttributesChooserConfig config, final Component parent)
    {
        final EventListener<Event> nodesMovedEventListener = event -> {
            storeSelectedPaths(availableTree, uniquePrefix(config, SECTION_ID_AVAILABLE), wim);
            storeSelectedPaths(selectedTree, uniquePrefix(config, SECTION_ID_SELECTED), wim);
            form.setAvailableAttributes(new HashSet<>(nodeOperationsHandler.extractAttributes(getModel(availableTree))));
            form.setChosenAttributes(new HashSet<>(nodeOperationsHandler.extractAttributes(getModel(selectedTree))));
            notifyChosenAttributesChanged(form, parent);
        };
        selectedTree.addEventListener(EVENT_NODES_MOVED_TO_TREE, nodesMovedEventListener);
        availableTree.addEventListener(EVENT_NODES_MOVED_TO_TREE, nodesMovedEventListener);
    }


    protected void notifyChosenAttributesChanged(final AttributeChooserForm form, final Component parent)
    {
        Events.postEvent(EVENT_ATTRIBUTES_SELECTED, parent, form);
    }


    protected Set<Attribute> getAttributes(final Tree available, final Tree selected, final boolean includeAll)
    {
        final Set<Attribute> allAttributes = new HashSet<>();
        allAttributes.addAll(nodeOperationsHandler.extractAttributes(getModel(selected)));
        if(includeAll)
        {
            allAttributes.addAll(nodeOperationsHandler.extractAttributes(getModel(available)));
        }
        return allAttributes;
    }


    protected void openFirstMultiChildLevel(final Tree tree, final AttributesChooserConfig config)
    {
        if(config.isOpenFirstMultiChildNode())
        {
            final AbstractTreeModel<TreeNode<Attribute>> model = getModel(tree);
            openFirstMultiChildLevel(model, model.getRoot());
        }
    }


    protected void openFirstMultiChildLevel(final AbstractTreeModel<TreeNode<Attribute>> model, final TreeNode<Attribute> root)
    {
        if(root != null && root.getChildCount() == 1)
        {
            final TreeNode<Attribute> theOnlyChild = root.getChildAt(0);
            model.addOpenObject(theOnlyChild);
            openFirstMultiChildLevel(model, theOnlyChild);
        }
    }


    public TreeitemRenderer<DefaultTreeNode<Attribute>> getTreeItemRenderer()
    {
        return treeItemRenderer;
    }


    @Required
    public void setTreeItemRenderer(final TreeitemRenderer<DefaultTreeNode<Attribute>> treeItemRenderer)
    {
        this.treeItemRenderer = treeItemRenderer;
    }


    public void setVisibleRowsNumber(final Integer visibleRowsNumber)
    {
        this.visibleRowsNumber = visibleRowsNumber;
    }


    public NodeOperationsHandler getNodeOperationsHandler()
    {
        return nodeOperationsHandler;
    }


    @Required
    public void setNodeOperationsHandler(final NodeOperationsHandler nodeOperationsHandler)
    {
        this.nodeOperationsHandler = nodeOperationsHandler;
    }


    protected int getVisibleRowsNumber()
    {
        return ObjectUtils.defaultIfNull(visibleRowsNumber, 12);
    }


    protected int getVisibleRowsNumber(final WidgetInstanceManager wim)
    {
        return ObjectUtils.defaultIfNull(wim.getModel().getValue(MODE_VISIBLE_ROWS_NUMBER, Integer.class), getVisibleRowsNumber());
    }


    public AttributeChooserNodeExpander getAttributeChooserNodeExpander()
    {
        return attributeChooserNodeExpander;
    }


    @Required
    public void setAttributeChooserNodeExpander(final AttributeChooserNodeExpander attributeChooserNodeExpander)
    {
        this.attributeChooserNodeExpander = attributeChooserNodeExpander;
    }
}
