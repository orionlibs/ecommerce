/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl.facet;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchController;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetType;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import com.hybris.cockpitng.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class ManageFacetPopup extends Window
{
    public static final int DEFAULT_MAX_NUMBER_OF_FACET_VALUES_TO_DISPLAY = 6;
    public static final int DEFAULT_FACET_RENDER_LIMIT = 24;
    public static final int DEFAULT_FACET_RENDER_INCREMENT_STEP = 20;
    public static final String FACET_WITH_COUNT_PATTERN = "%s (%s)";
    public static final String I18N_FACET_SUPPORT_APPLY = "facetSupport.apply";
    public static final String I18N_FACET_SUPPORT_CLOSE = "facetSupport.close";
    public static final String I18N_FACET_SUPPORT_CLEAR = "facetSupport.clear";
    public static final String I18N_FACET_SUPPORT_TYPE = "facetSupport.type";
    public static final String I18N_FACET_SUPPORT_VALUE = "facetSupport.value";
    public static final String I18N_FACET_SUPPORT_SHOW_ALL_FACET_VALUES = "facetSupport.showAllFacetValues";
    public static final String I18N_FACET_SUPPORT_BACK = "facetSupport.back";
    public static final String I18N_FACET_SUPPORT_SELECTED_NUMBER = "facetSupport.selectedNumber";
    public static final String I18N_FACET_SUPPORT_LIST_NO_MATCHES_FOR_QUERY = "facetSupport.list.noMatchesForQuery";
    public static final String I18N_FACET_SUPPORT_SHOW_MORE_FACETS = "facetSupport.showMoreFacetsButton.label";
    public static final String I18N_FACET_SUPPORT_SHOW_ALL_FACETS = "facetSupport.showAllFacetsButton.label";
    //Dreprecated
    public static final String SCLASS_YW_APPLY_LABEL = "yw-apply-label";
    public static final String SCLASS_YW_CANCEL_LABEL = "yw-cancel-label";
    public static final String SCLASS_YW_CLEAR_LABEL = "yw-clear-label";
    public static final String SCLASS_YW_TRANSPARENT_BUTTON = "y-btn-transparent";
    public static final String SCLASS_YW_APPLY_CONTAINER = "yw-apply-container";
    public static final String SCLASS_YW_TYPE_CONTAINER = "yw-type-container";
    public static final String SCLASS_YW_VALUE_CONTAINER = "yw-value-container";
    public static final String SCLASS_YW_FACET_MAIN_CONTAINER = "yw-facet-main-container";
    public static final String SCLASS_YW_FACET_SELECT_POPUP_CONTAINER = "yw-facet-select-popup-container";
    public static final String SCLASS_YW_FACET_LABEL_SELECTED_COUNT = "yw-facet-label-selected-count";
    public static final String SCLASS_YW_FACET_VALUE_CONTAINER = "yw-facet-value-container";
    public static final String SCLASS_YW_FACET_CONTAINER = "yw-facet-container";
    public static final String SCLASS_YW_FACET_LABEL_CONTAINER = "yw-facet-label-container";
    public static final String SCLASS_YW_FACET_LABEL_MORE = "yw-facet-label-more y-btn-transparent";
    public static final String SCLASS_YW_BUTTON_SHOW_MORE_FACETS_CONTAINER = "yw-show-more-facets-container";
    public static final String SCLASS_YW_BUTTON_SHOW_MORE_FACETS = "yw-show-more-facets-button ye-text-button";
    public static final String SCLASS_YW_FACET_SELECTOR_INPUT = "yw-facet-selector-input";
    public static final String SCLASS_YW_FACET_SELECTOR_INPUT_CONTAINER = SCLASS_YW_FACET_SELECTOR_INPUT + "-container";
    public static final String SCLASS_YW_FACET_LIST = "yw-facet-list";
    public static final String ON_EMPTY_LIST = "onEmptyListFacetPopupEvent";
    public static final String MODEL_KEY_FACET_SELECTION_STATUS = "facetSelectionStatus";
    public static final String MODEL_KEY_FILTER_QUERY_FACET_POPUP = "filterQueryFacetPopup";
    public static final String MODEL_KEY_CURRENT_FACET_RENDER_LIMIT = "currentFacetRenderLimit";
    public static final String MODEL_KEY_SHOW_MORE_EVENT_LISTENER = "showMoreEventListener";
    public static final String MODEL_KEY_SHOW_ALL_EVENT_LISTENER = "showAllEventListener";
    public static final String SHOW_MORE_CONTAINER_ID = "showMoreContainerId";
    public static final String SHOW_MORE_BUTTON_ID = "showMoreButtonId";
    public static final String SHOW_ALL_BUTTON_ID = "showAllButtonId";
    private final transient WidgetInstanceManager widgetInstanceManager;
    private final transient Function<Collection<FacetData>, Object> applyCallback;


    public ManageFacetPopup(final WidgetInstanceManager widgetInstanceManager,
                    final Function<Collection<FacetData>, ?> applyCallback)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        this.applyCallback = (Function<Collection<FacetData>, Object>)applyCallback;
        final Map<String, Set<FacetSelectionStatus>> status = ManageFacetPopup.this.getStatus();
        if(status == null)
        {
            widgetInstanceManager.getModel().put(MODEL_KEY_FACET_SELECTION_STATUS,
                            new TreeMap<>((Comparator<String>)(source, target) -> Comparator.<String>naturalOrder()
                                            .compare(StringUtils.lowerCase(source), StringUtils.lowerCase(target))));
        }
        else
        {
            status.values().forEach(col -> col.forEach(FacetSelectionStatus::clearListeners));
        }
        ManageFacetPopup.this.initializeComponents(false);
    }


    protected void initializeComponents(final boolean makeVisible)
    {
        this.getChildren().clear();
        initializePopupChooser();
        initializeInlineChooser();
        final Div applyContainer = new Div();
        applyContainer.setParent(this);
        UITools.modifySClass(applyContainer, SCLASS_YW_APPLY_CONTAINER, true);
        final var cancel = new Button(getLabel(I18N_FACET_SUPPORT_CLOSE));
        UITools.modifySClass(cancel, SCLASS_YW_TRANSPARENT_BUTTON, true);
        cancel.setParent(applyContainer);
        cancel.addEventListener(Events.ON_CLICK, click -> setVisible(false));
        if(!isInstant())
        {
            final var apply = new Button(getLabel(I18N_FACET_SUPPORT_APPLY));
            UITools.modifySClass(apply, SCLASS_YW_TRANSPARENT_BUTTON, true);
            apply.setParent(applyContainer);
            apply.addEventListener(Events.ON_CLICK, click -> applyFacets());
        }
        final var clear = new Button(getLabel(I18N_FACET_SUPPORT_CLEAR));
        UITools.modifySClass(clear, SCLASS_YW_TRANSPARENT_BUTTON, true);
        clear.setParent(applyContainer);
        clear.addEventListener(Events.ON_CLICK, click -> clearAllFacets(false));
        setClosable(true);
        setBorder(true);
        setVisible(makeVisible);
    }


    public void clearAllFacets(final Boolean isDisabledAutoSearch)
    {
        getStatus().values().stream().forEach(each -> each.stream().forEach(fss -> fss.setSelected(false)));
        initializeComponents(false);
        if(BooleanUtils.isNotTrue(isDisabledAutoSearch))
        {
            applyFacets();
        }
    }


    protected void initializeInlineChooser()
    {
        final Div container = new Div();
        this.appendChild(container);
        final Div facetContainer = new Div();
        final Div altContainer = new Div();
        final Map<String, Component> detailsComponentCache = new HashMap<>();
        final Div showMoreContainer = prepareShowMoreSection(facetContainer, altContainer, detailsComponentCache);
        showMoreContainer.setId(SHOW_MORE_CONTAINER_ID);
        UITools.modifySClass(showMoreContainer, SCLASS_YW_BUTTON_SHOW_MORE_FACETS_CONTAINER, true);
        container.appendChild(facetContainer);
        container.appendChild(showMoreContainer);
        altContainer.setVisible(false);
        this.appendChild(altContainer);
        renderFacetsChunk(facetContainer, showMoreContainer, altContainer, detailsComponentCache, 0, getFacetCurrentRenderLimit());
    }


    protected Div prepareShowMoreSection(final Div facetContainer, final Div altContainer,
                    final Map<String, Component> detailsComponentCache)
    {
        final Div container = new Div();
        final int facetsToRenderSize = getFacetsToRender().size();
        container.setVisible(getFacetCurrentRenderLimit() < facetsToRenderSize);
        final Button showMore = new Button();
        final Button showAll = new Button();
        showMore.setId(SHOW_MORE_BUTTON_ID);
        UITools.modifySClass(showMore, SCLASS_YW_BUTTON_SHOW_MORE_FACETS, true);
        showMore.setLabel(prepareShowMoreLabel(facetsToRenderSize, getFacetCurrentRenderLimit()));
        showMore.addEventListener(Events.ON_CLICK, e -> {
            renderFacetsChunk(facetContainer, container, altContainer, detailsComponentCache, getFacetCurrentRenderLimit(),
                            getFacetRenderIncrementStep());
            final int currentFacetsToRenderSize = getFacetsToRender().size();
            container.setVisible(getFacetCurrentRenderLimit() < currentFacetsToRenderSize);
            showMore.setLabel(prepareShowMoreLabel(currentFacetsToRenderSize, getFacetCurrentRenderLimit()));
            showAll.setLabel(prepareShowAllLabel(currentFacetsToRenderSize, getFacetCurrentRenderLimit()));
        });
        container.appendChild(showMore);
        showAll.setId(SHOW_ALL_BUTTON_ID);
        UITools.modifySClass(showAll, SCLASS_YW_BUTTON_SHOW_MORE_FACETS, true);
        showAll.setLabel(prepareShowAllLabel(facetsToRenderSize, getFacetCurrentRenderLimit()));
        showAll.addEventListener(Events.ON_CLICK, e -> {
            renderFacetsChunk(facetContainer, container, altContainer, detailsComponentCache, getFacetCurrentRenderLimit(),
                            getFacetsToRender().size());
            container.setVisible(false);
        });
        container.appendChild(showAll);
        return container;
    }


    protected void renderFacetsChunk(final Div facetContainer, final Div showAllContainer, final Div altContainer,
                    final Map<String, Component> detailsComponentCache, final int startIndex, final int chunkSize)
    {
        final List<Map.Entry<String, Set<FacetSelectionStatus>>> facetsToRender = getFacetsToRender();
        int addedFacets = 0;
        for(int i = startIndex; (i < startIndex + chunkSize) && (i < facetsToRender.size()); i++)
        {
            renderFacet(facetsToRender.get(i).getKey(), facetsToRender.get(i).getValue(), facetContainer, showAllContainer,
                            altContainer, detailsComponentCache);
            addedFacets++;
        }
        setFacetCurrentRenderLimit(startIndex + addedFacets);
    }


    protected boolean shouldRenderFacet(final Collection<FacetSelectionStatus> facetSelectionStatuses)
    {
        return facetSelectionStatuses.size() > 1
                        || facetSelectionStatuses.stream().findFirst().filter(f -> !f.isSelected()).isPresent();
    }


    /**
     * @see #renderFacet(String, Collection, Div, Div, Div, Map)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void renderFacet(final String facetCode, final Collection<FacetSelectionStatus> facetSelectionStatuses,
                    final Div facetsContainer, final Div altContainer, final Map<String, Component> detailsComponentCache)
    {
        renderFacet(facetCode, facetSelectionStatuses, facetsContainer, null, altContainer, detailsComponentCache);
    }


    protected void renderFacet(final String facetCode, final Collection<FacetSelectionStatus> facetSelectionStatuses,
                    final Div facetsContainer, final Div showAllContainer, final Div altContainer,
                    final Map<String, Component> detailsComponentCache)
    {
        final String facetDisplayName = facetSelectionStatuses.stream().findFirst().map(FacetSelectionStatus::getFacetDisplayName)
                        .orElse(StringUtils.EMPTY);
        final Div facetContainer = new Div();
        UITools.modifySClass(facetContainer, SCLASS_YW_FACET_CONTAINER, true);
        facetsContainer.appendChild(facetContainer);
        final Div facetLabelContainer = new Div();
        UITools.modifySClass(facetLabelContainer, SCLASS_YW_FACET_LABEL_CONTAINER, true);
        facetContainer.appendChild(facetLabelContainer);
        final Label facetLabel = new Label(facetDisplayName);
        facetLabelContainer.appendChild(facetLabel);
        final long selected = facetSelectionStatuses.stream().filter(FacetSelectionStatus::isSelected).count();
        final Label selectedNoLabel = new Label(getLabel(I18N_FACET_SUPPORT_SELECTED_NUMBER, new Object[]
                        {selected, facetSelectionStatuses.size()}));
        UITools.modifySClass(selectedNoLabel, SCLASS_YW_FACET_LABEL_SELECTED_COUNT, true);
        facetLabelContainer.appendChild(selectedNoLabel);
        if(facetSelectionStatuses.size() > getMaxNumberOfInlineFacets())
        {
            final var moreFacets = new Button(getLabel(I18N_FACET_SUPPORT_SHOW_ALL_FACET_VALUES));
            UITools.modifySClass(moreFacets, SCLASS_YW_FACET_LABEL_MORE, true);
            moreFacets.addEventListener(Events.ON_CLICK, click -> showAlternativeContainer(facetsContainer, showAllContainer,
                            altContainer, facetCode, facetDisplayName, detailsComponentCache));
            facetLabelContainer.appendChild(moreFacets);
        }
        facetContainer.appendChild(renderFacets(facetSelectionStatuses, getMaxNumberOfInlineFacets()));
    }


    protected String prepareShowMoreLabel(final int facetsToRender, final int renderedFacets)
    {
        return getLabel(I18N_FACET_SUPPORT_SHOW_MORE_FACETS, new Object[]
                        {Math.min(facetsToRender - renderedFacets, getFacetRenderIncrementStep())});
    }


    protected String prepareShowAllLabel(final int facetsToRender, final int renderedFacets)
    {
        return getLabel(I18N_FACET_SUPPORT_SHOW_ALL_FACETS, new Object[]
                        {facetsToRender - renderedFacets});
    }


    protected int getFacetRenderLimit()
    {
        final int facetLimit = widgetInstanceManager.getWidgetSettings()
                        .getInt(AdvancedSearchController.WIDGET_SETTING_FACET_RENDER_LIMIT);
        return facetLimit > 0 ? facetLimit : DEFAULT_FACET_RENDER_LIMIT;
    }


    protected int getFacetRenderIncrementStep()
    {
        final int incStep = widgetInstanceManager.getWidgetSettings()
                        .getInt(AdvancedSearchController.WIDGET_SETTING_FACET_RENDER_INCREMENT_STEP);
        return incStep > 0 ? incStep : DEFAULT_FACET_RENDER_INCREMENT_STEP;
    }


    protected int getMaxNumberOfInlineFacets()
    {
        final int max = widgetInstanceManager.getWidgetSettings().getInt(AdvancedSearchController.WIDGET_SETTING_MAX_INLINE_FACETS);
        return max > 0 ? max : DEFAULT_MAX_NUMBER_OF_FACET_VALUES_TO_DISPLAY;
    }


    protected int getFacetCurrentRenderLimit()
    {
        Integer renderLimit = widgetInstanceManager.getModel().getValue(MODEL_KEY_CURRENT_FACET_RENDER_LIMIT, Integer.class);
        if(renderLimit == null || renderLimit == 0)
        {
            renderLimit = getFacetRenderLimit();
            widgetInstanceManager.getModel().put(MODEL_KEY_CURRENT_FACET_RENDER_LIMIT, renderLimit);
        }
        return renderLimit.intValue();
    }


    protected void setFacetCurrentRenderLimit(final int renderLimit)
    {
        widgetInstanceManager.getModel().put(MODEL_KEY_CURRENT_FACET_RENDER_LIMIT, renderLimit);
    }


    protected Div renderFacets(final Collection<FacetSelectionStatus> facetSelectionStatuses, final int maxNumberOfFacets)
    {
        Validate.notEmpty("FacetData collection may not be empty", facetSelectionStatuses);
        final Div facetValueContainer = new Div();
        UITools.modifySClass(facetValueContainer, SCLASS_YW_FACET_VALUE_CONTAINER, true);
        int facetCounter = 0;
        Component checkboxParent = facetValueContainer;
        final FacetSelectionStatus status = facetSelectionStatuses.iterator().next();
        if(status.getFacetType() == FacetType.REFINE)
        {
            final Radiogroup radio = new Radiogroup();
            radio.setParent(facetValueContainer);
            radio.setName(status.getFacet().getDisplayName());
            facetValueContainer.appendChild(radio);
            checkboxParent = radio;
        }
        for(final FacetSelectionStatus fss : facetSelectionStatuses)
        {
            if(maxNumberOfFacets > 0 && facetCounter++ >= maxNumberOfFacets)
            {
                break;
            }
            final String label;
            if(fss.getFacetCount() > 0)
            {
                label = String.format(FACET_WITH_COUNT_PATTERN, fss.getFacetValueDisplayName(), fss.getFacetCount());
            }
            else
            {
                label = fss.getFacetValueDisplayName();
            }
            final boolean isRefined = (fss.getFacetType() == FacetType.REFINE);
            final Consumer<FacetSelectionStatus> onCheckPrerequisiteStep;
            final Checkbox facetSelection;
            if(isRefined)
            {
                facetSelection = new Radio(label);
                onCheckPrerequisiteStep = facet -> {
                    final Set<FacetSelectionStatus> facetSet = getStatus().get(facet.getFacet().getName());
                    facetSet.stream().filter(FacetSelectionStatus::isSelected).forEach(f -> f.setSelected(false));
                };
            }
            else
            {
                facetSelection = new Checkbox(label);
                onCheckPrerequisiteStep = facet -> {/* no op */
                };
            }
            facetSelection.setValue(fss);
            facetSelection.setTooltiptext(label);
            facetSelection.setChecked(fss.isSelected());
            facetSelection.addEventListener(Events.ON_CHECK, (EventListener<CheckEvent>)event -> {
                onCheckPrerequisiteStep.accept(fss);
                fss.setSelected(event.isChecked(), facetSelection);
                if(isInstant())
                {
                    applyFacets();
                }
            });
            fss.addModelListener(source -> {
                if(source != facetSelection)
                {
                    facetSelection.setChecked(fss.isSelected());
                }
                return null;
            });
            checkboxParent.appendChild(facetSelection);
        }
        return facetValueContainer;
    }


    protected boolean isInstant()
    {
        return widgetInstanceManager.getWidgetSettings().getBoolean(AdvancedSearchController.WIDGET_SETTING_INSTANT_FACETS);
    }


    /**
     * @see #showAlternativeContainer(Div, Div, Div, String, String, Map)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void showAlternativeContainer(final Div facetsContainer, final Div alternativeFacetContainer, final String facetCode,
                    final String facetDisplayName, final Map<String, Component> detailsComponentCache)
    {
        showAlternativeContainer(facetsContainer, null, alternativeFacetContainer, facetCode, facetDisplayName,
                        detailsComponentCache);
    }


    protected void showAlternativeContainer(final Div facetsContainer, final Div showMoreContainer,
                    final Div alternativeFacetContainer, final String facetCode, final String facetDisplayName,
                    final Map<String, Component> detailsComponentCache)
    {
        alternativeFacetContainer.getChildren().clear();
        if(detailsComponentCache.containsKey(facetCode))
        {
            alternativeFacetContainer.appendChild(detailsComponentCache.get(facetCode));
        }
        else
        {
            final Div facetContainer = new Div();
            UITools.modifySClass(facetContainer, SCLASS_YW_FACET_CONTAINER, true);
            final Div facetLabelContainer = new Div();
            UITools.modifySClass(facetLabelContainer, SCLASS_YW_FACET_LABEL_CONTAINER, true);
            facetContainer.appendChild(facetLabelContainer);
            final Label facetLabel = new Label(facetDisplayName);
            facetLabelContainer.appendChild(facetLabel);
            final var back = new Button(getLabel(I18N_FACET_SUPPORT_BACK));
            UITools.modifySClass(back, SCLASS_YW_FACET_LABEL_MORE, true);
            back.addEventListener(Events.ON_CLICK, click -> {
                alternativeFacetContainer.setVisible(false);
                facetsContainer.setVisible(true);
                if(showMoreContainer != null)
                {
                    showMoreContainer.setVisible(getFacetCurrentRenderLimit() < getFacetsToRender().size());
                }
            });
            facetLabelContainer.appendChild(back);
            facetContainer.appendChild(renderFacets(getStatus().get(facetCode), -1));
            detailsComponentCache.put(facetCode, facetContainer);
            alternativeFacetContainer.appendChild(facetContainer);
        }
        facetsContainer.setVisible(false);
        if(showMoreContainer != null)
        {
            showMoreContainer.setVisible(false);
        }
        alternativeFacetContainer.setVisible(true);
    }


    protected void initializePopupChooser()
    {
        if(isInlineFacetFilterEnabled())
        {
            final Div finderContainer = new Div();
            UITools.modifySClass(finderContainer, SCLASS_YW_FACET_SELECTOR_INPUT_CONTAINER, true);
            finderContainer.setParent(this);
            final Bandbox facetSelector = new Bandbox();
            UITools.modifySClass(facetSelector, SCLASS_YW_FACET_SELECTOR_INPUT, true);
            facetSelector.setButtonVisible(false);
            final Bandpopup facetSelectorContentContainer = new Bandpopup();
            UITools.modifySClass(facetSelectorContentContainer, SCLASS_YW_FACET_SELECT_POPUP_CONTAINER, true);
            final Listbox facetList = new Listbox();
            UITools.modifySClass(facetList, SCLASS_YW_FACET_LIST, true);
            facetList.setHflex("1");
            facetList.setEmptyMessage(getLabel(I18N_FACET_SUPPORT_LIST_NO_MATCHES_FOR_QUERY));
            facetSelector.addEventListener(Events.ON_CLICK, event -> facetSelector.open());
            facetSelector.addEventListener(Events.ON_CHANGING, (EventListener<InputEvent>)event -> {
                final String query = event.getValue();
                final ListModelList<FacetSelectionStatus> model = prepareFilteredFacetListModel(getListModelPredicate(query));
                facetList.setModel(model);
                if(model.isEmpty())
                {
                    facetSelector.close();
                    Events.echoEvent(ON_EMPTY_LIST, facetSelector, null);
                }
                getWidgetInstanceManager().getModel().put(MODEL_KEY_FILTER_QUERY_FACET_POPUP, query);
            });
            facetSelector.addEventListener(ON_EMPTY_LIST, e -> {
                facetSelector.open();
                final String text = widgetInstanceManager.getModel().getValue(MODEL_KEY_FILTER_QUERY_FACET_POPUP, String.class);
                if(StringUtils.isNotBlank(text))
                {
                    facetSelector.setText(text);
                }
            });
            facetList.setModel(prepareFilteredFacetListModel(getListModelPredicate(facetSelector.getText())));
            facetList.setItemRenderer((ListitemRenderer<FacetSelectionStatus>)(item, data, index) -> {
                final Listcell cell = new Listcell();
                final String label = getLabel(DefaultFacetRenderer.I18N_FACET_SUPPORT_APPLIED_FACET_LABEL, new Object[]
                                {data.getFacetDisplayName(), data.getFacetValueDisplayName()});
                cell.appendChild(new Label(label));
                item.appendChild(cell);
                item.addEventListener(Events.ON_CLICK, click -> {
                    facetSelector.getText();
                    data.setSelected(true);
                    applyFacets();
                });
            });
            facetSelector.appendChild(facetSelectorContentContainer);
            facetSelectorContentContainer.appendChild(facetList);
            finderContainer.appendChild(facetSelector);
        }
    }


    protected boolean isInlineFacetFilterEnabled()
    {
        return widgetInstanceManager.getWidgetSettings()
                        .getBoolean(AdvancedSearchController.WIDGET_SETTING_ENABLE_QUICK_FACET_FILTER);
    }


    protected Predicate<FacetSelectionStatus> getListModelPredicate(final String query)
    {
        Predicate<FacetSelectionStatus> predicate = x -> true;
        if(StringUtils.isNotBlank(query))
        {
            final String[] split = query.split("\\s+");
            for(final String queryPart : split)
            {
                predicate = predicate.and(facet -> StringUtils.containsIgnoreCase(facet.getFacetDisplayName(), queryPart)
                                || StringUtils.containsIgnoreCase(facet.getFacetValueDisplayName(), queryPart));
            }
        }
        return predicate;
    }


    protected ListModelList<FacetSelectionStatus> prepareFilteredFacetListModel(final Predicate<FacetSelectionStatus> predicate)
    {
        final List<FacetSelectionStatus> statuses = getStatus().values().stream().flatMap(Collection::stream)
                        .filter(x -> !x.isSelected()).filter(predicate).collect(Collectors.toList());
        return new ListModelList<>(statuses);
    }


    protected void applyFacets()
    {
        this.getChildren().clear();
        final Function<Collection<FacetData>, Object> callback = getApplyCallback();
        final Collection<FacetData> facetData = getSelectedFacetData();
        callback.apply(facetData);
    }


    protected Collection<FacetData> getSelectedFacetData()
    {
        final Collection<FacetData> result = new ArrayList<>();
        for(final String facet : getStatus().keySet())
        {
            final Set<FacetSelectionStatus> statuses = getStatus().get(facet);
            if(CollectionUtils.isNotEmpty(statuses))
            {
                final FacetSelectionStatus status = statuses.iterator().next();
                final List<FacetValueData> dataList = statuses.stream().map(fss -> new FacetValueData(fss.getFacetValue().getName(),
                                fss.getFacetValueDisplayName(), fss.getFacetCount(), fss.isSelected())).collect(Collectors.toList());
                result.add(new FacetData(status.getFacet().getName(), status.getFacetDisplayName(), status.getFacetType(), dataList));
            }
        }
        return result;
    }


    protected String getLabel(final String key)
    {
        return StringUtils.defaultIfBlank(getWidgetInstanceManager().getLabel(key), key);
    }


    protected String getLabel(final String key, final Object[] args)
    {
        return StringUtils.defaultIfBlank(getWidgetInstanceManager().getLabel(key, args), key);
    }


    protected void showPopup()
    {
        setClosable(false);
        setPosition("parent");
        setLeft("0px");
        setTop("35px");
        doPopup();
    }


    public void reset()
    {
        if(isVisible())
        {
            close();
        }
        getStatus().clear();
        setFacetCurrentRenderLimit(getFacetRenderLimit());
    }


    public void close()
    {
        Events.echoEvent(Events.ON_CLOSE, this, this);
    }


    public void adjustFacets(final Collection<FacetData> facets)
    {
        getStatus().clear();
        for(final FacetData facet : facets)
        {
            Set<FacetSelectionStatus> statusCollection = getStatus().get(facet.getName());
            if(statusCollection == null)
            {
                statusCollection = new LinkedHashSet<>();
                getStatus().put(facet.getName(), statusCollection);
            }
            for(final FacetValueData val : facet.getFacetValues())
            {
                statusCollection.add(new FacetSelectionStatus(facet, val));
            }
        }
        initializeComponents(isVisible());
    }


    protected boolean removeSelectedFacetValue(final FacetData field, final FacetValueData value)
    {
        final Collection<FacetSelectionStatus> fss = getStatus().get(field.getName());
        if(fss != null)
        {
            for(final FacetSelectionStatus fs : fss)
            {
                if(StringUtils.equals(fs.getFacet().getName(), field.getName())
                                && StringUtils.equals(fs.getFacetValue().getName(), value.getName()))
                {
                    fs.setSelected(false);
                    applyFacets();
                    return true;
                }
            }
        }
        return false;
    }


    protected Function<Collection<FacetData>, Object> getApplyCallback()
    {
        return applyCallback;
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public Map<String, Set<FacetSelectionStatus>> getStatus()
    {
        return getWidgetInstanceManager().getModel().getValue(MODEL_KEY_FACET_SELECTION_STATUS, Map.class);
    }


    protected List<Map.Entry<String, Set<FacetSelectionStatus>>> getFacetsToRender()
    {
        return getStatus().entrySet().stream().filter(entry -> shouldRenderFacet(entry.getValue())).collect(Collectors.toList());
    }


    protected static class FacetSelectionStatus implements Comparable<FacetSelectionStatus>
    {
        private final FacetData facet;
        private final FacetValueData facetValue;
        private boolean selected;
        private Collection<Function> listeners = new ArrayList<>();


        public FacetSelectionStatus(final FacetData facet, final FacetValueData facetValue)
        {
            this.facet = facet;
            this.facetValue = facetValue;
            this.selected = facetValue.isSelected();
        }


        public void addModelListener(final Function listener)
        {
            listeners.add(listener);
        }


        public boolean isSelected()
        {
            return selected;
        }


        public void setSelected(final boolean selected)
        {
            setSelected(selected, null);
        }


        public void setSelected(final boolean selected, final Object source)
        {
            this.selected = selected;
            notifyAllListeners(source);
        }


        public void notifyAllListeners(final Object source)
        {
            for(final Function listener : listeners)
            {
                listener.apply(source);
            }
        }


        public FacetData getFacet()
        {
            return facet;
        }


        public String getFacetDisplayName()
        {
            return facet.getDisplayName();
        }


        public FacetValueData getFacetValue()
        {
            return facetValue;
        }


        public String getFacetValueDisplayName()
        {
            return facetValue.getDisplayName();
        }


        public FacetType getFacetType()
        {
            return facet.getFacetType();
        }


        public long getFacetCount()
        {
            return facetValue.getCount();
        }


        public Collection<Function> getListeners()
        {
            return listeners;
        }


        public void setListeners(final Collection<Function> listeners)
        {
            this.listeners = listeners;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            final FacetSelectionStatus that = (FacetSelectionStatus)o;
            if(!Objects.equals(facet, that.facet))
            {
                return false;
            }
            return Objects.equals(facetValue, that.facetValue);
        }


        @Override
        public int hashCode()
        {
            int result = facet != null ? facet.hashCode() : 0;
            result = 31 * result + (facetValue != null ? facetValue.hashCode() : 0);
            return result;
        }


        @Override
        public int compareTo(final FacetSelectionStatus o)
        {
            if(o == null)
            {
                return -1;
            }
            else
            {
                if(o.isSelected() ^ isSelected())
                {
                    return isSelected() ? -1 : 1;
                }
                else if(o.getFacet().getName().compareTo(getFacet().getName()) == 0)
                {
                    return getFacetValue().getName().compareTo(o.getFacetValue().getName());
                }
                else
                {
                    return 1;
                }
            }
        }


        public void clearListeners()
        {
            listeners.clear();
        }
    }
}
