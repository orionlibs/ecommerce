/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.adminmode.exception.ContextModificationException;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Codeeditor;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.cache.CachedContext;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchNode;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchProgress;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRequest;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRestriction;
import com.hybris.cockpitng.core.config.impl.model.DefaultContextSearchNode;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.InputSource;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.North;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

/**
 * Cockpit admin view's logic and event handling.
 */
public class CockpitAdminComposer extends ViewAnnotationAwareComposer
{
    public static final String REFRESH_COCKPIT = "refreshCockpit";
    public static final String TOOLBAR_COLOR_PICKER = "toolbarColorPicker";
    public static final String CLIPBOARD_DROP_BOX = "clipboardDropBox";
    public static final String EXPANDED_DISABLED = "expanded-disabled";
    public static final String EXPANDED = "expanded";
    public static final String DRAGGED_WIDGET = "draggedWidget";
    public static final String SHOW_FILTER_OPTIONS_ID = "showFilterOptions";
    public static final String COCKPIT_CONFIGURATION_FILTERED = "cockpitConfigurationFiltered";
    public static final String COCKPIT_CONFIGURATION_SEARCH_NODES_APPROVED = "searchSimulation-approvedNodes";
    public static final String CONFIG_XML_CLOSE_BUTTON_ID = "closeButton";
    public static final String CLOSE_SEARCH_CONFIG_SIMULATOR_BTN_ID = "closeSearchConfigSimulatorBtn";
    protected static final String SYMBOLIC_WIDGETS_CHK_ID = "symbolicWidgetsChk";
    protected static final String SHOW_CONNECTIONS_CHK_ID = "showConnectionsChk";
    protected static final String SHOW_WIDGETS_XML_ID = "showWidgetsXml";
    protected static final String SHOW_COCKPIT_CONFIG_XML_ID = "showCockpitConfigXml";
    protected static final String SIMULATE_COCKPIT_CONFIG_SEARCH_ID = "simulateConfigSearch";
    protected static final String PERFORM_SIMULATION_BUTTON_ID = "simulateButton";
    protected static final String SIMULATION_PROGRESS_TREE = "searchConfigProgress";
    protected static final String SIMULATION_CLASS_CHOOSER = "simulateSearchClass";
    protected static final String RESET_WIDGETS_XML_ID = "resetWidgetsXml";
    protected static final String RESET_COCKPIT_CONFIG_XML_ID = "resetCockpitConfigXml";
    protected static final String RESET_EVERYTHING_ID = "resetEverything";
    protected static final String SAVE_XML_CONFIG_ID = "saveXmlConfig";
    protected static final String VALIDATE_XML_CONFIG_ID = "validateXmlConfig";
    protected static final String EXIT_ORCHESTRATOR_ID = "exitOrchestratorLabel";
    protected static final String YCON_ID = "ycon";
    protected static final String CONFIG_SEARCH_SIMULATION_MODEL = "simulationModel";
    private static final String CONNECTION_POPUPS_ATTRIBUTE = "connectionPopups";
    private static final String CSS_STYLING = "position: absolute;";
    private static final String YW_ERROR_SCLASS = "yw-error";
    private static final String YW_WARNING_SCLASS = "yw-warning";
    private static final String YW_SUCCESS_SCLASS = "yw-success";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitAdminComposer.class);
    private static final String XML_TRANSFORMER_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
    protected transient DefaultCockpitConfigurationService cockpitConfigurationService;
    protected transient CockpitAdminService cockpitAdminService;
    protected transient XMLWidgetPersistenceService widgetPersistenceService;
    protected transient CockpitSessionService cockpitSessionService;
    protected Codeeditor xmlConfig;
    protected Codeeditor xmlTb;
    protected Popup xmlConfigWin;
    protected Popup xmlWin;
    protected Widgetslot slot;
    protected Checkbox symbolicWidgetsChk;
    protected Checkbox showConnectionsChk;
    protected Checkbox showSlotIdChk;
    protected Div adminModeToolbar;
    protected Div connectionsToolbar;
    protected Div outsetCnt;
    protected Div ycon;
    protected Menupopup mainMenu;
    protected Toolbarbutton showFilterOptions;
    protected Hlayout filterOptions;
    protected Button searchButton;
    protected Hlayout searchButtonSection;
    protected North north;
    // Config search simulator
    protected Popup searchConfigSimulator;
    protected Combobox simulateSearchClass;
    protected Hlayout simulateSearchContext;
    protected Button simulateButton;
    protected Textbox searchConfigMatches;
    protected Textbox searchConfigMerged;
    protected Textbox searchConfigFinal;
    protected Tree searchConfigProgress;
    protected Textbox searchConfigProgressNode;
    protected Textbox searchConfigNodeResult;
    private transient SearchCriteriaModel searchCriteriaModel;
    private boolean minimalized = false;
    private transient JAXBContextFactory cockpitJAXBContextFactory;
    private transient CockpitComponentDefinitionService cockpitComponentDefinitionService;
    private boolean wasFiltered = false;
    private transient LabelService labelService;


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CONFIG_XML_CLOSE_BUTTON_ID)
    public void closeConfigXml()
    {
        showFilterOptions.setChecked(false);
        xmlConfigWin.close();
        getCockpitSessionService().setAttribute(COCKPIT_CONFIGURATION_FILTERED, false);
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CREATE, componentID = SYMBOLIC_WIDGETS_CHK_ID)
    public void createSymbolicWidgets()
    {
        symbolicWidgetsChk.setChecked(cockpitAdminService.isSymbolicAdminFlagEnabled());
        toggleSymbolicWidgetsView();
    }


    @ViewEvent(eventName = Events.ON_CREATE, componentID = SHOW_CONNECTIONS_CHK_ID)
    public void createShowConnectionsChk()
    {
        final boolean checked = cockpitAdminService.isShowConnectionsFlagEnabled();
        showConnectionsChk.setChecked(checked);
        if(checked)
        {
            Clients.evalJavaScript("showConnectionsChecked({data:true});");
            connectionsToolbar.setVisible(true);
        }
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CHECK, componentID = SYMBOLIC_WIDGETS_CHK_ID)
    public void showSymbolicWidgets()
    {
        cockpitAdminService.setSymbolicAdminFlag(symbolicWidgetsChk.isChecked());
        cockpitAdminService.setShowConnectionsFlagEnabled(false);
        cockpitAdminService.setAdminMode(true, slot);
        slot.updateView();
        toggleSymbolicWidgetsView();
        showConnectionsChk.setChecked(false);
        connectionsToolbar.setVisible(false);
    }


    /**
     * Set visibility states of view components.
     */
    protected void toggleSymbolicWidgetsView()
    {
        showSlotIdChk.setVisible(symbolicWidgetsChk.isChecked());
        showConnectionsChk.setVisible(symbolicWidgetsChk.isChecked());
        outsetCnt.setVisible(!symbolicWidgetsChk.isChecked());
    }


    protected void persistCockpitConfigXml() throws CockpitConfigurationException, JAXBException
    {
        try
        {
            if(wasFiltered)
            {
                LOG.debug("Storing filtered configuration");
                final Config original = getConfigWithAppliedChanges(
                                cockpitConfigurationService.getChangesAsConfig(xmlConfig.getValue(), getConfigUnmarshaller()));
                cockpitConfigurationService.storeRootConfig(original);
            }
            else
            {
                LOG.debug("Storing complete configuration");
                cockpitConfigurationService.setConfigAsString(xmlConfig.getValue());
            }
            cockpitAdminService.refreshCockpit();
        }
        catch(final ContextModificationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            final String message = StringUtils.defaultIfBlank(getLabelService().getObjectLabel(e), "");
            Messagebox.show(message, null, Messagebox.OK, Messagebox.ERROR, 0, null);
        }
    }


    protected Config getConfigWithAppliedChanges(final Config changes) throws CockpitConfigurationException
    {
        final Config original = cockpitConfigurationService.loadRootConfiguration();
        FilteredConfigRewriter.applyChangesInFilteredConfig(original, changes);
        return original;
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = EXIT_ORCHESTRATOR_ID)
    public void exitOrchestrator()
    {
        cockpitAdminService.toggleAdminMode(slot);
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = RESET_EVERYTHING_ID)
    public void resetEverything()
    {
        Messagebox.show(Labels.getLabel("reset_config.description"), Labels.getLabel("reset_configuration.title"),
                        new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, event -> {
                            final Messagebox.Button selection = event.getButton();
                            if(Messagebox.Button.YES.equals(selection))
                            {
                                widgetPersistenceService.resetToDefaults();
                                cockpitConfigurationService.resetToDefaults();
                                cockpitAdminService.refreshCockpit();
                            }
                        });
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = RESET_COCKPIT_CONFIG_XML_ID)
    public void resetCockpitConfigXml()
    {
        Messagebox.show(Labels.getLabel("reset_ui_config.description"), Labels.getLabel("reset_ui_configuration.title"),
                        new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, event -> {
                            final Messagebox.Button selection = event.getButton();
                            if(Messagebox.Button.YES.equals(selection))
                            {
                                cockpitConfigurationService.resetToDefaults();
                                cockpitAdminService.refreshCockpit();
                            }
                        });
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = RESET_WIDGETS_XML_ID)
    public void resetWidgetsXml()
    {
        Messagebox.show(Labels.getLabel("reset_app_config.description"), Labels.getLabel("reset_app_config.title"),
                        new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, event -> {
                            final Messagebox.Button selection = event.getButton();
                            if(Messagebox.Button.YES.equals(selection))
                            {
                                widgetPersistenceService.resetToDefaults();
                                cockpitAdminService.refreshCockpit();
                            }
                        });
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = SHOW_COCKPIT_CONFIG_XML_ID)
    public void showCockpitConfigXml()
    {
        showCockpitConfigXml(false);
    }


    protected void showCockpitConfigXml(final boolean applyChangesFromFilteredConfig)
    {
        try
        {
            final String configuration = applyChangesFromFilteredConfig ? cockpitConfigurationService.getConfigAsString(
                            getConfigWithAppliedChanges(
                                            cockpitConfigurationService.getChangesAsConfig(xmlConfig.getValue(), getConfigUnmarshaller())),
                            getConfigMarshaller()) : cockpitConfigurationService.getConfigAsString();
            showCockpitConfigXmlInternal(configuration);
        }
        catch(final JAXBException | CockpitConfigurationException e)
        {
            LOG.error("Error in applying filtered changes", e);
            showCockpitConfigXmlInternal(cockpitConfigurationService.getConfigAsString());
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SIMULATE_COCKPIT_CONFIG_SEARCH_ID)
    public void showConfigSearchSimulator()
    {
        searchConfigSimulator.open(0, 0);
        final SearchCriteriaModel contextModel = createConfigContextModel();
        contextModel.addObserver(this::handleSearchSimulatorContextChanged);
        renderConfigContextOptions(simulateSearchContext, contextModel);
        simulateButton.setAttribute(CONFIG_SEARCH_SIMULATION_MODEL, contextModel);
        simulateButton.setDisabled(true);
        final Set<String> classes = new HashSet<>();
        classes.addAll(cockpitConfigurationService.getFallbackStrategies().keySet());
        classes.addAll(cockpitConfigurationService.getConfigTypesAdapters().keySet());
        final List<String> modelList = new ArrayList<>(classes);
        Collections.sort(modelList);
        final ListModelList model = new ListModelList(modelList);
        simulateSearchClass.setModel(model);
        simulateSearchClass.setSelectedItem(null);
        simulateSearchClass.setValue(null);
        searchConfigProgress.setItemRenderer(new ConfigSearchProgressRenderer());
        searchConfigProgress.setModel(new DefaultTreeModel<>(new DefaultTreeNode<>(null)));
        searchConfigProgressNode.setValue("");
        searchConfigNodeResult.setValue("");
        searchConfigMatches.setValue("");
        searchConfigMerged.setValue("");
        searchConfigFinal.setValue("");
    }


    protected void handleSearchSimulatorContextChanged(final String attributeName)
    {
        configSearchSimulatorClassChanged();
        if(StringUtils.isBlank(simulateSearchClass.getValue()))
        {
            final SearchCriteriaModel contextModel = (SearchCriteriaModel)simulateButton
                            .getAttribute(CONFIG_SEARCH_SIMULATION_MODEL);
            if(DefaultConfigContext.CONTEXT_COMPONENT.equals(attributeName))
            {
                final String component = contextModel.getSearchCriteria().get(DefaultConfigContext.CONTEXT_COMPONENT);
                final Optional<String> exactMatch = Optional.ofNullable(component).map(cmp -> cmp.replace("-", ""))
                                .map(simpleClassName -> simulateSearchClass
                                                .getItems().stream().map(Comboitem::getValue).map(Object::toString).filter(className -> className
                                                                .toLowerCase(Locale.ENGLISH).substring(className.lastIndexOf('.') + 1).equals(simpleClassName))
                                                .findFirst())
                                .filter(Optional::isPresent).map(Optional::get);
                exactMatch.ifPresent(this::updateClassName);
                if(!exactMatch.isPresent() && StringUtils.isNotBlank(component))
                {
                    final Optional<String> partialMatch = simulateSearchClass.getItems().stream().map(Comboitem::getValue)
                                    .map(Object::toString)
                                    .filter(className -> component.toLowerCase(Locale.ENGLISH).contains(predictComponentName(className)))
                                    .findFirst();
                    partialMatch.ifPresent(this::updateClassName);
                }
            }
        }
    }


    @ViewEvent(eventName = Events.ON_CHANGE, componentID = SIMULATION_CLASS_CHOOSER)
    public void configSearchSimulatorClassChanged()
    {
        final SearchCriteriaModel contextModel = (SearchCriteriaModel)simulateButton.getAttribute(CONFIG_SEARCH_SIMULATION_MODEL);
        simulateButton.setDisabled(!contextModel.hasAnySearchCriteria() || StringUtils.isBlank(simulateSearchClass.getValue()));
    }


    protected void updateClassName(final String className)
    {
        simulateSearchClass.setValue(className);
        configSearchSimulatorClassChanged();
    }


    protected String predictComponentName(final String className)
    {
        final String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        final Matcher matcher = Pattern.compile("[A-Z][a-z|0-9]*").matcher(simpleClassName);
        final StringBuilder componentName = new StringBuilder();
        while(matcher.find())
        {
            if(componentName.length() > 0)
            {
                componentName.append("-");
            }
            componentName.append(matcher.group().toLowerCase(Locale.ENGLISH));
        }
        return componentName.toString();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = PERFORM_SIMULATION_BUTTON_ID)
    public void performConfigSearchSimulation()
    {
        final SearchCriteriaModel contextModel = (SearchCriteriaModel)simulateButton.getAttribute(CONFIG_SEARCH_SIMULATION_MODEL);
        try
        {
            final Class configType = Class.forName(simulateSearchClass.getValue(), false,
                            Thread.currentThread().getContextClassLoader());
            final Map<String, String> query = contextModel.getSearchCriteria();
            final DefaultConfigContext configContext = new DefaultConfigContext();
            query.forEach(configContext::addAttribute);
            final Config rootConfiguration = cockpitConfigurationService.loadRootConfiguration();
            final Marshaller configMarshaller = getConfigMarshaller(configType);
            final Map<String, List<String>> convertedNeedle = DefaultCockpitConfigurationService.convertAttributes(query);
            final ContextSearchRequest request = new ContextSearchRequest(rootConfiguration, convertedNeedle,
                            ContextSearchRestriction.EMPTY);
            request.setNotEmpty(true);
            final ContextSearchProgress searchProgress = cockpitConfigurationService.findContext(rootConfiguration, request);
            final Marshaller marshaller = getConfigMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final Config newConfig = new Config();
            newConfig.getContext()
                            .addAll(searchProgress.getResult().stream()
                                            .map(context -> context instanceof CachedContext ? ((CachedContext)context).getOriginal() : context)
                                            .collect(Collectors.toList()));
            try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
            {
                marshaller.marshal(newConfig, out);
                searchConfigMatches.setValue(out.toString(Charset.defaultCharset().name()));
            }
            final Object mergedConfig = cockpitConfigurationService.mergeContexts(configContext, searchProgress.getResult(),
                            configType);
            try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
            {
                configMarshaller.marshal(mergedConfig, out);
                searchConfigMerged.setValue(out.toString(Charset.defaultCharset().name()));
            }
            final Object finalConfig = cockpitConfigurationService.adaptConfigAfterLoad(mergedConfig, configType, configContext);
            try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
            {
                configMarshaller.marshal(finalConfig, out);
                searchConfigFinal.setValue(out.toString(Charset.defaultCharset().name()));
            }
            final List<DefaultTreeNode<ContextSearchNode>> children = exportProgressNodeToTree(searchProgress);
            final DefaultTreeModel<ContextSearchNode> progressModel = new DefaultTreeModel<>(
                            new DefaultTreeNode<>(searchProgress, children));
            searchConfigProgress.setModel(progressModel);
            searchConfigProgress.setAttribute(COCKPIT_CONFIGURATION_SEARCH_NODES_APPROVED,
                            DefaultContextSearchNode.getResultChildren(searchProgress));
            searchConfigProgressNode.setValue("");
            searchConfigNodeResult.setValue("");
        }
        catch(final CockpitConfigurationException | JAXBException | IOException | ClassNotFoundException ex)
        {
            LOG.error(ex.getLocalizedMessage(), ex);
        }
    }


    protected List<DefaultTreeNode<ContextSearchNode>> exportProgressNodeToTree(final ContextSearchNode node)
    {
        return node.getChildren().stream().map(child -> {
            final List<DefaultTreeNode<ContextSearchNode>> nodes = exportProgressNodeToTree(child);
            return !nodes.isEmpty() ? new DefaultTreeNode<>(child, nodes) : new DefaultTreeNode<>(child);
        }).collect(Collectors.toList());
    }


    @ViewEvent(eventName = Events.ON_SELECT, componentID = SIMULATION_PROGRESS_TREE)
    public void displayConfigSearchSimulationProgress(final SelectEvent<Treeitem, TreeNode<ContextSearchNode>> event)
    {
        searchConfigProgressNode.setValue("");
        final TreeNode<ContextSearchNode> node = event.getSelectedObjects().iterator().next();
        final Collection<ContextSearchNode> approvedNodes = (Collection<ContextSearchNode>)event.getReference().getTree()
                        .getAttribute(COCKPIT_CONFIGURATION_SEARCH_NODES_APPROVED);
        final boolean approved = approvedNodes.stream().anyMatch(approvedNode -> approvedNode == node.getData());
        final String details = "Relevance: " + node.getData().getRelevance() + "\n" + "Egalitarian: "
                        + node.getData().isEgalitarian() + "\n" + "Included in result: " + approved + "\n";
        searchConfigProgressNode.setValue(details);
        final Config newConfig = new Config();
        newConfig.getContext()
                        .addAll(node.getData().getNodeResult().stream()
                                        .map(context -> context instanceof CachedContext ? ((CachedContext)context).getOriginal() : context)
                                        .collect(Collectors.toList()));
        try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            final Marshaller marshaller = getConfigMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(newConfig, out);
            searchConfigNodeResult.setValue(out.toString(Charset.defaultCharset().name()));
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
        catch(final JAXBException ex)
        {
            LOG.error(ex.getLocalizedMessage(), ex);
        }
    }


    protected Marshaller getConfigMarshaller(final Class configClass) throws JAXBException
    {
        return cockpitJAXBContextFactory.createContext(configClass).createMarshaller();
    }


    protected Marshaller getConfigMarshaller() throws JAXBException
    {
        return getConfigMarshaller(Config.class);
    }


    protected Unmarshaller getConfigUnmarshaller() throws JAXBException
    {
        return cockpitJAXBContextFactory.createContext(Config.class).createUnmarshaller();
    }


    protected void showCockpitConfigXmlInternal(final String content)
    {
        xmlConfig.invalidate();
        xmlConfig.setValue(content);
        UITools.modifySClass(xmlConfigWin, YW_ERROR_SCLASS, false);
        UITools.modifySClass(xmlConfigWin, YW_WARNING_SCLASS, false);
        UITools.modifySClass(xmlConfigWin, YW_SUCCESS_SCLASS, false);
        xmlConfigWin.open(0, 0);
        wasFiltered = false;
        showFilterOptions.setLabel(Labels.getLabel("cockpit_config_filter.show.filter.options"));
        filterOptions.setVisible(false);
        searchButtonSection.setVisible(false);
        north.setHeight("40px");
    }


    @ViewEvent(componentID = SHOW_FILTER_OPTIONS_ID, eventName = Events.ON_CHECK)
    public void onShowOrHideFilterOptions(final CheckEvent ev)
    {
        if(ev.isChecked())
        {
            showFilterOptions.setLabel(Labels.getLabel("cockpit_config_filter.switch.to.full.config"));
            prepareFilterData();
            renderFilterOptions();
            filterOptions.setVisible(true);
            searchButtonSection.setVisible(true);
            north.setHeight("385px");
        }
        else
        {
            LOG.debug("Applying changes in filtered configuration to full configuration");
            showCockpitConfigXml(wasFiltered);
        }
        getCockpitSessionService().setAttribute(COCKPIT_CONFIGURATION_FILTERED, ev.isChecked());
    }


    protected void prepareFilterData()
    {
        searchCriteriaModel = createConfigContextModel();
        minimalized = false;
    }


    protected SearchCriteriaModel createConfigContextModel()
    {
        return new SearchCriteriaModel(cockpitConfigurationService);
    }


    protected void renderFilterOptions()
    {
        final Toolbarbutton minimalize = createMinimalizeButton();
        final List<FilterByAttributeUI> attributeUIs = renderConfigContextOptions(filterOptions, searchCriteriaModel);
        addMinimalizeButtonEventListener(minimalize, attributeUIs);
        searchButton.addEventListener(Events.ON_CLICK, ev -> {
            final Marshaller marshaller = getConfigMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final Config newConfig = new Config();
            final List<Context> filteredContext = searchCriteriaModel.getFilteredContext();
            newConfig.getContext().addAll(filteredContext);
            try(final ByteArrayOutputStream out = new ByteArrayOutputStream())
            {
                marshaller.marshal(newConfig, out);
                xmlConfig.setValue(out.toString(Charset.defaultCharset().name()));
            }
            wasFiltered = true;
        });
    }


    protected List<FilterByAttributeUI> renderConfigContextOptions(final Hlayout container, final SearchCriteriaModel model)
    {
        container.getChildren().clear();
        final List<String> attributeNames = model.getAllAttributeNames();
        final List<FilterByAttributeUI> attributeUIs = new ArrayList<>();
        for(final String attributeName : attributeNames)
        {
            final FilterByAttributeUI filterByAttributeUI = new FilterByAttributeUI(attributeName, model,
                            cockpitConfigurationService, container);
            filterByAttributeUI.render();
            attributeUIs.add(filterByAttributeUI);
        }
        return attributeUIs;
    }


    protected Toolbarbutton createMinimalizeButton()
    {
        final Toolbarbutton btn = new Toolbarbutton();
        btn.setMode("toggle");
        btn.setSclass("btn_minimalize");
        btn.setDisabled(true);
        UITools.modifySClass(btn, EXPANDED_DISABLED, true);
        searchCriteriaModel.addObserver(e -> {
            if(!minimalized)
            {
                if(searchCriteriaModel.hasAnySearchCriteria())
                {
                    btn.setDisabled(false);
                    UITools.modifySClass(btn, EXPANDED_DISABLED, false);
                    UITools.modifySClass(btn, EXPANDED, true);
                }
                else
                {
                    btn.setDisabled(true);
                    UITools.modifySClass(btn, EXPANDED_DISABLED, true);
                    UITools.modifySClass(btn, EXPANDED, false);
                }
            }
        });
        filterOptions.appendChild(btn);
        return btn;
    }


    protected void addMinimalizeButtonEventListener(final Toolbarbutton minimalize, final List<FilterByAttributeUI> attributeUIs)
    {
        minimalize.addEventListener(Events.ON_CHECK, (EventListener<CheckEvent>)event -> {
            minimalized = event.isChecked();
            for(final FilterByAttributeUI attributeUI : attributeUIs)
            {
                if(minimalized)
                {
                    attributeUI.collapse();
                }
                else
                {
                    attributeUI.expand();
                }
            }
            north.setHeight(minimalized ? "160px" : "385px");
            UITools.modifySClass(minimalize, "collapsed", minimalized);
            UITools.modifySClass(minimalize, EXPANDED, !minimalized);
        });
    }


    /**
     * @deprecated since 2105 the method is not used anymore and will be removed in future releases
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected String formatXml(final String xml)
    {
        try(final OutputStream baos = new ByteArrayOutputStream();
                        final InputStream bais = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))
        {
            final TransformerFactory factory = TransformerFactory
                            .newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, StringUtils.EMPTY);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StringUtils.EMPTY);
            factory.setAttribute("indent-number", "2");
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            final Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(XML_TRANSFORMER_INDENT_AMOUNT, "2");
            final Source xmlSource = new SAXSource(new InputSource(bais));
            final StreamResult res = new StreamResult(baos);
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
        }
        catch(final Exception e)
        {
            LOG.warn("Exception during xml formatting", e);
            return xml;
        }
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CLICK, componentID = SHOW_WIDGETS_XML_ID)
    public void showWidgetsXml()
    {
        if(slot.getWidgetInstance() == null)
        {
            xmlTb.setValue(widgetPersistenceService.getWidgetTreeAsString(null, null));
        }
        else
        {
            xmlTb.setValue(widgetPersistenceService.getWidgetTreeAsString(slot.getWidgetInstance().getWidget(), null));
        }
        xmlWin.open(0, 0);
    }


    /**
     * Handle view's event.
     */
    @ViewEvent(eventName = Events.ON_CHECK, componentID = SHOW_CONNECTIONS_CHK_ID)
    public void showConnections()
    {
        cockpitAdminService.setShowConnectionsFlagEnabled(showConnectionsChk.isChecked());
        if(desktop.getAttribute(CONNECTION_POPUPS_ATTRIBUTE) != null)
        {
            final List<Window> windows = (List<Window>)desktop.getAttribute(CONNECTION_POPUPS_ATTRIBUTE);
            for(final Window window : windows)
            {
                if(showConnectionsChk.isChecked())
                {
                    window.doOverlapped();
                }
                else
                {
                    window.setVisible(false);
                }
            }
            if(showConnectionsChk.isChecked())
            {
                adminModeToolbar.setStyle(CSS_STYLING);
            }
            else
            {
                adminModeToolbar.setStyle(null);
            }
        }
        connectionsToolbar.setVisible(showConnectionsChk.isChecked());
    }


    @ViewEvent(componentID = REFRESH_COCKPIT, eventName = Events.ON_CLICK)
    public void refreshCockpit()
    {
        cockpitAdminService.refreshCockpit();
    }


    @ViewEvent(componentID = TOOLBAR_COLOR_PICKER, eventName = Events.ON_CHANGE)
    public void toolbarColorPickerColorChanged(final InputEvent event)
    {
        cockpitAdminService.setWidgetToolbarColor(event.getValue(), slot);
    }


    @ViewEvent(componentID = CLIPBOARD_DROP_BOX, eventName = Events.ON_DROP)
    public void dropWidgetToClipboard(final DropEvent event)
    {
        if(event.getDragged().getAttribute(DRAGGED_WIDGET) instanceof Widget)
        {
            final Widget widget = (Widget)event.getDragged().getAttribute(DRAGGED_WIDGET);
            final WidgetDefinition definition = (WidgetDefinition)cockpitComponentDefinitionService
                            .getComponentDefinitionForCode(widget.getWidgetDefinitionId());
            if(!definition.isStubWidget())
            {
                cockpitAdminService.moveWidgetToClipboard((Widget)event.getDragged().getAttribute(DRAGGED_WIDGET));
            }
        }
    }


    @ViewEvent(componentID = YCON_ID, eventName = Events.ON_CLICK)
    public void onYconMenuClicked()
    {
        mainMenu.open(ycon, "after_end");
    }


    @ViewEvent(componentID = CLOSE_SEARCH_CONFIG_SIMULATOR_BTN_ID, eventName = Events.ON_CLICK)
    public void closeSearchConfigSimulator()
    {
        searchConfigSimulator.close();
    }


    @Required
    public void setCockpitJAXBContextFactory(final JAXBContextFactory cockpitJAXBContextFactory)
    {
        this.cockpitJAXBContextFactory = cockpitJAXBContextFactory;
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


    protected CockpitComponentDefinitionService getCockpitComponentDefinitionService()
    {
        return cockpitComponentDefinitionService;
    }


    public void setCockpitComponentDefinitionService(final CockpitComponentDefinitionService cockpitComponentDefinitionService)
    {
        this.cockpitComponentDefinitionService = cockpitComponentDefinitionService;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    @Required
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }
}
