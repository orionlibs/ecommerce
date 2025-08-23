package de.hybris.platform.platformbackoffice.classification;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaTabRenderer;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.platformbackoffice.classification.comparator.ClassificationClassComparator;
import de.hybris.platform.platformbackoffice.classification.comparator.FeatureComparator;
import de.hybris.platform.platformbackoffice.classification.provider.ClassificationSectionNameProvider;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tabpanel;

public class ClassificationTabEditorAreaRenderer extends AbstractEditorAreaTabRenderer<ProductModel>
{
    public static final String EDITED_PROPERTY_QUALIFIER = "editedPropertyQualifier";
    protected static final String LABEL_NO_READ_ACCESS = "backoffice.data.not.visible";
    private static final String LABEL_OBJECT_NOT_PERSISTED = "classificationtab.object.not.persisted";
    private static final String LABEL_NO_ATTRIBUTES = "classificationtab.no.attributes";
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationTabEditorAreaRenderer.class);
    private static final String MODIFIED_FEATURES_MAP_MODEL_PARAM = "modifiedProductFeatures";
    private static final String MODIFIED_FEATURES_MODEL_PARAM_PREFIX = "modifiedProductFeatures.pk";
    private static final String FEATURES_AFTER_SAVE_LISTENER_PARAM = "featuresAfterSaveListener";
    private static final String FEATURES_AFTER_CANCEL_LISTENER_PARAM = "featuresAfterCancelListener";
    private static final String SCLASS_GRPBOX = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox";
    private static final String SCLASS_CELL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell";
    private static final String SCLASS_GRPBOX_CAPTION = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-caption";
    private static final String RENDERED_CLASSIFICATION_CLASSES = "renderedClassificationClasses";
    private static final String CURRENT_PRODUCT = "currentProduct";
    private static final String INITIALLY_OPENED = "initiallyOpened";
    protected static final String CAN_CHANGE_CURRENT_PRODUCT_INSTANCE = "canChangeCurrentProductInstance";
    private static final String EXTENDED_CLASSIFICATION_ATTRIBUTES_TAB = "extended.classification.attributes.tab";
    private static final int NUMBER_OF_ROWS = 2;
    private ClassificationService classificationService;
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private ClassificationClassComparator classificationClassComparator;
    private FeatureComparator featureComparator;
    private WidgetInstanceManager widgetInstanceManager;
    private FeaturePeristanceHandler featurePeristanceHandler;
    private ClassificationSectionNameProvider classificationSectionNameProvider;
    private Boolean initiallyOpened = Boolean.valueOf(true);


    public void render(Component component, AbstractTab configuration, ProductModel product, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        setWidgetInstanceManager(widgetInstanceManager);
        initializeWidgetModel(widgetInstanceManager, product);
        registerAfterSaveListener(widgetInstanceManager, product, component);
        EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(),
                        String.format("%s_%s", new Object[] {"featuresAfterCancelListener", component.getUuid()}), event -> refreshCurrentTab(component), false);
        if(configuration instanceof CustomTab)
        {
            applyInitiallyOpened((CustomTab)configuration);
        }
        if(getObjectFacade().isNew(product))
        {
            renderEmptyTab(component, "classificationtab.object.not.persisted");
            return;
        }
        if(!hasPermissionTo("ClassificationClass") || !hasPermissionTo("ClassAttributeAssignment"))
        {
            renderEmptyTab(component, "backoffice.data.not.visible");
            return;
        }
        if(!prepareAndRenderFeatures(product, component, widgetInstanceManager))
        {
            renderNoAttributeEmptyTab(component);
        }
    }


    protected void applyInitiallyOpened(CustomTab configuration)
    {
        configuration.getRenderParameter().forEach(parameter -> {
            if(parameter.getName().equals("initiallyOpened"))
            {
                this.initiallyOpened = Boolean.valueOf(parameter.getValue());
            }
        });
    }


    protected boolean prepareAndRenderFeatures(ProductModel product, Component component, WidgetInstanceManager widgetInstanceManager)
    {
        Map<ClassificationClassModel, List<Feature>> featuresGroupedByClassificationClass = prepareDataForRendering(product);
        if(featuresGroupedByClassificationClass.isEmpty())
        {
            return false;
        }
        featuresGroupedByClassificationClass.forEach((key, value) -> renderSection(key, value, component, widgetInstanceManager));
        return true;
    }


    protected void renderNoAttributeEmptyTab(Component component)
    {
        renderEmptyTab(component, "classificationtab.no.attributes");
    }


    private boolean hasPermissionTo(String typeCode)
    {
        return getPermissionFacade().canReadType(typeCode);
    }


    protected void renderSection(ClassificationClassModel classificationClassModel, List<Feature> features, Component parent, WidgetInstanceManager widgetInstanceManager)
    {
        if(features.isEmpty())
        {
            return;
        }
        Groupbox sectionGroupBox = new Groupbox();
        sectionGroupBox.setParent(parent);
        String sectionName = getClassificationSectionNameProvider().provide(classificationClassModel);
        Caption caption = new Caption(sectionName);
        Button expandButton = new Button();
        expandButton.setSclass("yw-expandCollapse");
        expandButton.addEventListener("onClick", e -> {
            while(sectionGroupBox.getEventListeners("onClick").iterator().hasNext())
            {
                EventListener<? extends Event> eventEventListener = sectionGroupBox.getEventListeners("onClick").iterator().next();
                if(eventEventListener instanceof ClassificationSectionOpenListener)
                {
                    renderAttributes(sectionGroupBox, features, widgetInstanceManager);
                    sectionGroupBox.removeEventListener("onClick", eventEventListener);
                }
            }
            sectionGroupBox.setOpen(!sectionGroupBox.isOpen());
        });
        caption.appendChild((Component)expandButton);
        YTestTools.modifyYTestId((Component)caption, sectionName + "_caption");
        caption.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-caption");
        sectionGroupBox.appendChild((Component)caption);
        sectionGroupBox.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox");
        if(!hasPermissionTo("ClassificationAttribute"))
        {
            createSectionNameLabelSuffix(sectionGroupBox);
        }
        else if(!this.initiallyOpened.booleanValue())
        {
            sectionGroupBox.setOpen(false);
            sectionGroupBox.addEventListener("onClick", (EventListener)new ClassificationSectionOpenListener(this, this, sectionGroupBox, features, widgetInstanceManager));
        }
        else
        {
            renderAttributes(sectionGroupBox, features, widgetInstanceManager);
        }
        YTestTools.modifyYTestId((Component)sectionGroupBox, sectionName);
    }


    protected void createSectionNameLabelSuffix(Groupbox sectionGroupBox)
    {
        Div container = new Div();
        Separator separator = new Separator();
        Label noReadAccess = new Label(Labels.getLabel("backoffice.data.not.visible"));
        container.appendChild((Component)separator);
        container.appendChild((Component)noReadAccess);
        sectionGroupBox.appendChild((Component)container);
    }


    protected void renderAttributes(Groupbox section, List<Feature> features, WidgetInstanceManager widgetInstanceManager)
    {
        if(hasPermissionTo("ClassificationAttribute"))
        {
            Hbox hbox = createHbox();
            for(int i = 0; i < features.size(); i++)
            {
                try
                {
                    Feature feature = features.get(i);
                    hbox = createNewRowIfNeeded(hbox, (Component)section, 2, i);
                    Cell cell = new Cell();
                    hbox.appendChild((Component)cell);
                    cell.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell");
                    Editor editor = createEditor(feature, widgetInstanceManager, canWriteFeature(feature));
                    cell.appendChild((Component)editor);
                    cell.setParent((Component)hbox);
                }
                catch(JaloObjectNoLongerValidException e)
                {
                    LOG.warn("Attribute assignment was remove, try to refresh editor area", (Throwable)e);
                }
            }
        }
    }


    protected Hbox createHbox()
    {
        return new Hbox();
    }


    protected Hbox createNewRowIfNeeded(Hbox hbox, Component parent, int noOfColumns, int currentCellIndex)
    {
        Hbox result = hbox;
        if(currentCellIndex % noOfColumns == 0)
        {
            result = new Hbox();
            result.setParent(parent);
        }
        return result;
    }


    protected Editor createEditor(Feature feature, WidgetInstanceManager widgetInstanceManager, boolean canWriteFeature)
    {
        ProductModel productModel = lookupCurrentProduct(widgetInstanceManager);
        Editor editor = new Editor();
        editor.setReadableLocales(getReadableLocales(productModel));
        editor.setWritableLocales(getWritableLocales(productModel));
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType("Feature");
        editor.setTooltiptext(feature.getCode());
        editor.setOptional(BooleanUtils.isNotTrue(feature.getClassAttributeAssignment().getMandatory()));
        editor.setReadOnly(!canWriteFeature);
        editor.setInitialValue(BackofficeClassificationUtils.convertFeatureToClassificationInfo(feature));
        editor.setAtomic(true);
        editor.setEditorLabel(getFeatureNameOrCode(feature));
        String featureQualifier = BackofficeClassificationUtils.getFeatureQualifier(feature.getClassAttributeAssignment());
        String encodeFeatureQualifier = BackofficeClassificationUtils.getFeatureQualifierEncoded(featureQualifier);
        String classificationQualifier = "currentObject['" + encodeFeatureQualifier + "']";
        editor.setProperty(classificationQualifier);
        editor.addParameter("editedPropertyQualifier", encodeFeatureQualifier);
        ifRangeAllowOneValue(feature, editor);
        YTestTools.modifyYTestId((Component)editor, "editor_" + featureQualifier);
        editor.afterCompose();
        return editor;
    }


    protected void ifRangeAllowOneValue(Feature feature, Editor editor)
    {
        if(feature.getClassAttributeAssignment().getRange().booleanValue())
        {
            editor.addParameter("allowInfiniteEndpoints", Boolean.valueOf(true));
        }
    }


    protected void saveFeatures(ProductModel productModel, Map<String, Feature> modifiedProductFeatures)
    {
        this.featurePeristanceHandler.saveFeatures(productModel, modifiedProductFeatures);
    }


    private void initializeWidgetModel(WidgetInstanceManager widgetInstanceManager, ProductModel productModel)
    {
        WidgetModel widgetModel = widgetInstanceManager.getModel();
        widgetModel.setValue("modifiedProductFeatures", Maps.newHashMap());
        widgetModel.setValue(createFeatureMapForProductKey(productModel), Maps.newHashMap());
        widgetModel.setValue("currentProduct", productModel);
        widgetModel.setValue("renderedClassificationClasses", getClassificationClassesOrEmptyList(productModel));
        boolean canChangeInstance = getPermissionFacade().canChangeInstance(productModel);
        widgetModel.setValue("canChangeCurrentProductInstance", Boolean.valueOf(canChangeInstance));
    }


    private ProductModel lookupCurrentProduct(WidgetInstanceManager widgetInstanceManager)
    {
        return (ProductModel)widgetInstanceManager.getModel().getValue("currentProduct", ProductModel.class);
    }


    private Map<String, Feature> lookupFeaturesMapForProduct(WidgetModel widgetModel, ProductModel productModel)
    {
        return (Map<String, Feature>)widgetModel.getValue(createFeatureMapForProductKey(productModel), Map.class);
    }


    private String createFeatureMapForProductKey(ProductModel productModel)
    {
        return "modifiedProductFeatures.pk" + productModel.getPk();
    }


    protected void renderEmptyTab(Component component, String label_key)
    {
        Groupbox sectionGroupBox = new Groupbox();
        sectionGroupBox.setParent(component);
        String sectionName = getEmptyAttributesSectionName();
        Caption caption = new Caption(sectionName);
        YTestTools.modifyYTestId((Component)caption, sectionName + "_caption");
        caption.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-caption");
        sectionGroupBox.appendChild((Component)caption);
        sectionGroupBox.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox");
        YTestTools.modifyYTestId((Component)sectionGroupBox, sectionName);
        sectionGroupBox.appendChild((Component)new Label(Labels.getLabel(label_key)));
    }


    protected String getEmptyAttributesSectionName()
    {
        return Labels.getLabel("extended.classification.attributes.tab");
    }


    protected Map<ClassificationClassModel, List<Feature>> prepareDataForRendering(ProductModel product)
    {
        FeatureList features = getClassificationService().getFeatures(product);
        Set<ClassificationClassModel> allReadableClassificationClasses = getFilteredClassificationClasses(features);
        Map<ClassificationClassModel, List<Feature>> featuresGroupedByClassificationClass = getFeaturesGroupedByClassificationClassMap(features, allReadableClassificationClasses);
        sortAttributes(featuresGroupedByClassificationClass);
        return featuresGroupedByClassificationClass;
    }


    protected Map<ClassificationClassModel, List<Feature>> getFeaturesGroupedByClassificationClassMap(FeatureList features, Set<ClassificationClassModel> allReadableClassificationClasses)
    {
        List<ClassificationClassModel> sortedClassificationClasses = new ArrayList<>(allReadableClassificationClasses);
        sortedClassificationClasses.sort((Comparator<? super ClassificationClassModel>)this.classificationClassComparator);
        Map<ClassificationClassModel, List<Feature>> featuresGroupedByClassificationClass = new LinkedHashMap<>();
        sortedClassificationClasses.forEach(ccm -> featuresGroupedByClassificationClass.put(ccm, new ArrayList()));
        for(Feature feature : features.getFeatures())
        {
            ClassAttributeAssignmentModel assignment = feature.getClassAttributeAssignment();
            if(assignment == null)
            {
                LOG.warn("Could not find assignment for feature: {}", feature.getCode());
                continue;
            }
            List<Feature> featuresForGivenClassificationClass = featuresGroupedByClassificationClass.get(assignment.getClassificationClass());
            if(featuresForGivenClassificationClass != null)
            {
                featuresForGivenClassificationClass.add(feature);
            }
        }
        return featuresGroupedByClassificationClass;
    }


    protected Set<ClassificationClassModel> getFilteredClassificationClasses(FeatureList features)
    {
        return getAllReadableClassificationClasses(features.getClassificationClasses());
    }


    protected Set<ClassificationClassModel> getAllReadableClassificationClasses(Set<ClassificationClassModel> classificationClasses)
    {
        Set<ClassificationClassModel> readableClassificationClasses = new HashSet<>();
        for(ClassificationClassModel ccm : classificationClasses)
        {
            boolean canRead = this.catalogVersionService.canRead((CatalogVersionModel)ccm.getCatalogVersion(), this.userService.getCurrentUser());
            if(canRead)
            {
                readableClassificationClasses.add(ccm);
            }
        }
        return readableClassificationClasses;
    }


    private boolean canWriteFeature(Feature feature)
    {
        ClassificationSystemVersionModel catalogVersion = feature.getClassAttributeAssignment().getClassificationClass().getCatalogVersion();
        return (this.catalogVersionService.canWrite((CatalogVersionModel)catalogVersion, this.userService.getCurrentUser()) && ((Boolean)this.widgetInstanceManager
                        .getModel().getValue("canChangeCurrentProductInstance", Boolean.class)).booleanValue());
    }


    private Integer featurePosition(Feature feature)
    {
        return feature.getClassAttributeAssignment().getPosition();
    }


    private void sortAttributes(Map<ClassificationClassModel, List<Feature>> featuresGroupedByClassificationClass)
    {
        for(List<Feature> features : featuresGroupedByClassificationClass.values())
        {
            features.sort(Comparator.comparing(this::featurePosition, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing((Comparator<? super Feature>)this.featureComparator));
        }
    }


    private void registerAfterSaveListener(WidgetInstanceManager wim, ProductModel productModel, Component component)
    {
        WidgetModel widgetModel = wim.getModel();
        Map<String, Feature> modifiedProductFeatures = lookupFeaturesMapForProduct(widgetModel, productModel);
        EditorAreaRendererUtils.setAfterSaveListener(widgetModel, "featuresAfterSaveListener", event -> {
            if(BooleanUtils.isFalse((Boolean)widgetModel.getValue("inputObjectIsNew", Boolean.class)))
            {
                saveFeatures(productModel, modifiedProductFeatures);
            }
            if(hasClassificationClassesListChanged(widgetModel))
            {
                refreshCurrentTab(component);
            }
        } false);
    }


    private boolean hasClassificationClassesListChanged(WidgetModel model)
    {
        Collection renderedClasses = (Collection)model.getValue("renderedClassificationClasses", Collection.class);
        ProductModel currentProduct = (ProductModel)model.getValue("currentObject", ProductModel.class);
        if(currentProduct != null)
        {
            return !CollectionUtils.isEqualCollection(getClassificationClassesOrEmptyList(currentProduct), renderedClasses);
        }
        return CollectionUtils.isNotEmpty(renderedClasses);
    }


    void refreshCurrentTab(Component component)
    {
        Tabpanel tabPanel = findClosestTabPanel(component);
        if(tabPanel != null)
        {
            postEvent(new Event("onTabSelected", (Component)tabPanel));
        }
    }


    protected void postEvent(Event event)
    {
        Events.postEvent(event);
    }


    private Tabpanel findClosestTabPanel(Component component)
    {
        if(component instanceof Tabpanel)
        {
            return (Tabpanel)component;
        }
        return (component != null) ? findClosestTabPanel(component.getParent()) : null;
    }


    private List<ClassificationClassModel> getClassificationClassesOrEmptyList(ProductModel productModel)
    {
        List<ClassificationClassModel> classificationClasses = new ArrayList<>();
        if(productModel != null && productModel.getClassificationClasses() != null)
        {
            classificationClasses.addAll(productModel.getClassificationClasses());
        }
        return classificationClasses;
    }


    String getFeatureNameOrCode(Feature feature)
    {
        String localizedName = feature.getClassAttributeAssignment().getClassificationAttribute().getName(getCockpitLocaleService().getCurrentLocale());
        if(StringUtils.isNotBlank(localizedName))
        {
            return localizedName;
        }
        if(feature.getName() != null)
        {
            return feature.getName();
        }
        return feature.getClassAttributeAssignment().getClassificationAttribute().getCode();
    }


    private Set<Locale> getReadableLocales(ProductModel productModel)
    {
        return getPermissionFacade().getReadableLocalesForInstance(productModel);
    }


    private Set<Locale> getWritableLocales(ProductModel productModel)
    {
        return getPermissionFacade().getWritableLocalesForInstance(productModel);
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    public ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setClassificationClassComparator(ClassificationClassComparator classificationClassComparator)
    {
        this.classificationClassComparator = classificationClassComparator;
    }


    @Required
    public void setFeatureComparator(FeatureComparator featureComparator)
    {
        this.featureComparator = featureComparator;
    }


    @Required
    public void setFeaturePeristanceHandler(FeaturePeristanceHandler featurePeristanceHandler)
    {
        this.featurePeristanceHandler = featurePeristanceHandler;
    }


    private void setWidgetInstanceManager(WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public ClassificationSectionNameProvider getClassificationSectionNameProvider()
    {
        return this.classificationSectionNameProvider;
    }


    @Required
    public void setClassificationSectionNameProvider(ClassificationSectionNameProvider classificationSectionNameProvider)
    {
        this.classificationSectionNameProvider = classificationSectionNameProvider;
    }
}
