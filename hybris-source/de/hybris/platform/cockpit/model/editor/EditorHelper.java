package de.hybris.platform.cockpit.model.editor;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UICockpitArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class EditorHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorHelper.class);
    public static final String EVENT_SOURCE = "eventSource";
    public static final String ALLOWED_VALUES_LIST = "allowedValuesList";
    protected static final String PARAM_DISABLE_DRAG_AND_DROP = "disableDragDrop";


    public static CreateContext applyReferenceRelatedAttributes(ReferenceUIEditor referenceEditor, PropertyDescriptor propertyDescriptor, Map<String, ? extends Object> parameters, TypedObject currentObject, Object currentValue, String isoCode, UICockpitArea cockpitArea, UISession session)
    {
        SearchType rootSearchType = null;
        SearchType rootType = getRootSearchType(propertyDescriptor, session);
        if(!StringUtils.isEmpty((String)parameters
                        .get("allowCreate")))
        {
            referenceEditor.setAllowCreate(Boolean.valueOf((String)parameters.get("allowCreate")));
        }
        else
        {
            referenceEditor.setAllowCreate(Boolean.TRUE);
        }
        if(rootType != null)
        {
            referenceEditor.setRootType((ObjectType)rootType);
            referenceEditor.setRootSearchType((ObjectType)rootSearchType);
        }
        else
        {
            LOG.warn("Could not set root type for reference editor (Reason: No root search type could be retrieved for property descriptor '" + propertyDescriptor + "').");
        }
        if(currentValue instanceof Collection && !((Collection)currentValue).isEmpty() && ((Collection)currentValue)
                        .iterator().next() instanceof TypedObject)
        {
            currentValue = session.getTypeService().wrapItems((Collection)currentValue);
        }
        else if(!(currentValue instanceof TypedObject))
        {
            currentValue = session.getTypeService().wrapItem(currentValue);
        }
        CreateContext createContext = new CreateContext((ObjectType)rootType, currentObject, propertyDescriptor, isoCode);
        boolean fireSearch = BooleanUtils.toBoolean((String)parameters.get("fireSearchOnOpen"));
        createContext.setFireSearch(fireSearch);
        createContext.setExcludedTypes(
                        parseTemplateCodes((String)parameters.get("excludeCreateTypes"), session.getTypeService()));
        createContext.setAllowedTypes(
                        parseTemplateCodes((String)parameters.get("restrictToCreateTypes"), session.getTypeService()));
        return createContext;
    }


    private static boolean checkSingle(PropertyDescriptor propertyDescriptor)
    {
        PropertyDescriptor.Multiplicity multi = propertyDescriptor.getMultiplicity();
        return (multi == null || multi.equals(PropertyDescriptor.Multiplicity.SINGLE));
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, null);
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, EditorListener editorListener, String editorCode) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, null, editorListener);
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, null);
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, false);
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, boolean focus) throws IllegalArgumentException
    {
        if(propDescr == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing property descriptor.");
        }
        if(parent == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing parent component.");
        }
        if(valueContainer == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing value container.");
        }
        if(propDescr.isLocalized())
        {
            renderLocalizedEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus);
        }
        else
        {
            renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, null, focus);
        }
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, EditorListener editorListener) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, editorListener, false);
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, EditorListener editorListener, boolean focus)
                    throws IllegalArgumentException
    {
        if(propDescr == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing property descriptor.");
        }
        if(parent == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing parent component.");
        }
        if(valueContainer == null)
        {
            throw new IllegalArgumentException("Can not create editor. Reason: Missing value container.");
        }
        if(propDescr.isLocalized())
        {
            renderLocalizedEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus, editorListener);
        }
        else
        {
            renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, null, focus, editorListener);
        }
    }


    public static void createEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, EditorListener editorListener, boolean autoPersist) throws IllegalArgumentException
    {
        createEditor(item, propDescr, parent, valueContainer, autoPersist, editorListener, null);
    }


    @Deprecated
    public static <T> List<? extends T> filterValues(PropertyDescriptor propDescr, List<? extends T> values)
    {
        List<? extends T> ret = null;
        if(values != null)
        {
            ret = new ArrayList<>(values);
            if(propDescr != null)
            {
                String valueTypeCode = UISessionUtils.getCurrentSession().getTypeService().getValueTypeCode(propDescr);
                if(StringUtils.isNotBlank(valueTypeCode) && GeneratedCatalogConstants.TC.CATALOGVERSION.equalsIgnoreCase(valueTypeCode))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Found catalog version attribute. Checking access...");
                    }
                    List<T> toRemove = new ArrayList<>();
                    UserModel user = UISessionUtils.getCurrentSession().getUser();
                    for(T item : values)
                    {
                        if(item instanceof TypedObject)
                        {
                            Object object = ((TypedObject)item).getObject();
                            if(object instanceof CatalogVersionModel &&
                                            !getUIAccessRightService().canWrite(user, (CatalogVersionModel)object))
                            {
                                if(LOG.isDebugEnabled())
                                {
                                    LOG.debug("Found non-writable catalog version '" +
                                                    UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel((TypedObject)item) + "' - Filtering out...");
                                }
                                toRemove.add(item);
                            }
                        }
                    }
                    ret.removeAll(toRemove);
                }
            }
        }
        return ret;
    }


    private static EditorFactory getEditorFactory()
    {
        return (EditorFactory)SpringUtil.getBean("EditorFactory");
    }


    public static SearchType getRootSearchType(PropertyDescriptor propDescr, UISession session)
    {
        SearchType searchType = null;
        String valueTypeCode = session.getTypeService().getValueTypeCode(propDescr);
        if(valueTypeCode != null)
        {
            try
            {
                searchType = session.getSearchService().getSearchType(valueTypeCode);
            }
            catch(Exception e)
            {
                LOG.warn("Could not get search type for property descriptor (Reason: '" + e.getMessage() + "').");
            }
        }
        return searchType;
    }


    protected static UIAccessRightService getUIAccessRightService()
    {
        return (UIAccessRightService)SpringUtil.getBean("uiAccessRightService");
    }


    public static UIEditor getUIEditor(PropertyDescriptor propDescr, String editorCode) throws IllegalArgumentException
    {
        if(propDescr == null)
        {
            throw new IllegalArgumentException("Can not retrieve editor since property descriptor is null.");
        }
        Collection<PropertyEditorDescriptor> matching = getEditorFactory().getMatchingEditorDescriptors(propDescr.getEditorType());
        PropertyEditorDescriptor edDescr = matching.isEmpty() ? null : matching.iterator().next();
        UIEditor editor = null;
        if(edDescr == null)
        {
            LOG.error("Could not retrieve editor descriptor.");
        }
        else
        {
            if(!StringUtils.isBlank(editorCode))
            {
                editor = edDescr.createUIEditor(editorCode);
            }
            if(editor == null)
            {
                editor = checkSingle(propDescr) ? edDescr.createUIEditor("single") : edDescr.createUIEditor(PropertyDescriptor.Multiplicity.RANGE.equals(propDescr.getMultiplicity()) ?
                                "range" : "multi");
            }
        }
        if(editor != null)
        {
            editor.setOptional(!PropertyDescriptor.Occurrence.REQUIRED.equals(propDescr.getOccurence()));
        }
        return editor;
    }


    public static void initializeSections(EditorConfiguration cfg, ObjectType type, TypedObject object, TypeService typeService)
    {
        for(EditorSectionConfiguration secConf : cfg.getSections())
        {
            if(secConf instanceof CustomEditorSectionConfiguration)
            {
                ((CustomEditorSectionConfiguration)secConf).initialize(cfg, type, object);
            }
        }
        for(EditorSectionConfiguration secConf : cfg.getSections())
        {
            if(secConf instanceof CustomEditorSectionConfiguration)
            {
                ((CustomEditorSectionConfiguration)secConf).allInitialized(cfg, type, object);
            }
        }
        Set<ObjectType> types = new HashSet<>();
        if(object != null)
        {
            types.add(object.getType());
            types.addAll(object.getExtendedTypes());
        }
        else if(type != null)
        {
            types.add(type);
        }
        for(EditorSectionConfiguration secConf : cfg.getSections())
        {
            List<EditorRowConfiguration> rows = null;
            boolean modified = false;
            for(EditorRowConfiguration rowConf : secConf.getSectionRows())
            {
                if(!TypeTools.propertyBelongsTo(typeService, types, rowConf.getPropertyDescriptor()))
                {
                    if(rows == null)
                    {
                        rows = new ArrayList<>(secConf.getSectionRows());
                    }
                    rows.remove(rowConf);
                    modified = true;
                    LOG.warn("Removed property [" + rowConf.getPropertyDescriptor() + "] from editor configuration because it does not belong to object type of activated item.");
                }
            }
            if(modified)
            {
                secConf.setSectionRows(rows);
            }
        }
    }


    @Deprecated
    public static boolean isEditable(PropertyDescriptor propDescr, boolean creationMode)
    {
        BaseType baseType;
        ObjectType type = UISessionUtils.getCurrentSession().getTypeService().getObjectType(
                        UISessionUtils.getCurrentSession().getTypeService().getTypeCodeFromPropertyQualifier(propDescr.getQualifier()));
        if(type instanceof ObjectTemplate)
        {
            baseType = ((ObjectTemplate)type).getBaseType();
        }
        return getUIAccessRightService().isWritable((ObjectType)baseType, propDescr, creationMode);
    }


    public static void persistValues(TypedObject item, ObjectValueContainer valueContainer) throws ValueHandlerException
    {
        persistValues(item, valueContainer, Collections.EMPTY_MAP);
    }


    public static void persistValues(TypedObject item, ObjectValueContainer valueContainer, Map<String, ? extends Object> params) throws ValueHandlerException
    {
        for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                        .getValueHandlerChain((ObjectType)item.getType()))
        {
            valueHandler.storeValues(valueContainer);
        }
        Set<PropertyDescriptor> modifiedProperties = new HashSet<>();
        for(ObjectValueContainer.ObjectValueHolder value : valueContainer.getAllValues())
        {
            if(value.isModified())
            {
                modifiedProperties.add(value.getPropertyDescriptor());
            }
        }
        Object source = params.get("eventSource");
        if(CollectionUtils.isNotEmpty(modifiedProperties))
        {
            UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new ItemChangeUndoableOperation(item, valueContainer), null);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(source, item, modifiedProperties, params));
        }
        valueContainer.stored();
    }


    public static ObjectTemplate processVariantTypeCheck(ObjectTemplate type, TypedObject object, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        ObjectTemplate ret = null;
        if(type != null && typeService.getBaseType("VariantProduct").isAssignableFrom((ObjectType)type.getBaseType()) && object != null && typeService
                        .getBaseType("Product").isAssignableFrom((ObjectType)object.getType()) && propertyDescriptor instanceof ItemAttributePropertyDescriptor && ((ItemAttributePropertyDescriptor)propertyDescriptor)
                        .isSingleAttribute())
        {
            Object variantTypeObject = TypeTools.getObjectAttributeValue(object, GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE, typeService);
            if(variantTypeObject instanceof TypedObject && typeService
                            .getBaseType("ComposedType").isAssignableFrom((ObjectType)((TypedObject)variantTypeObject).getType()))
            {
                String variantTypeCode = (String)TypeTools.getObjectAttributeValue((TypedObject)variantTypeObject, "code", typeService);
                ret = typeService.getObjectTemplate(variantTypeCode);
            }
        }
        else
        {
            ret = type;
        }
        return ret;
    }


    public static void tryOpenContextAreaEditor(UICockpitPerspective perspective, ObjectTemplate type, Collection value, TypedObject currentObject, PropertyDescriptor propertyDescriptor, Map<String, ? extends Object> parameters)
    {
        if(perspective instanceof BaseUICockpitPerspective && type != null)
        {
            Map<String, Object> newParameters = new HashMap<>(parameters);
            newParameters.put("propertyDescriptor", propertyDescriptor);
            if(value.isEmpty() || !(value.iterator().next() instanceof TypedObject))
            {
                ((BaseUICockpitPerspective)perspective).openReferenceCollectionInBrowserContext(Collections.EMPTY_LIST, type, currentObject, newParameters);
            }
            else
            {
                ((BaseUICockpitPerspective)perspective).openReferenceCollectionInBrowserContext(value, type, currentObject, newParameters);
            }
        }
    }


    public static final Set<ObjectType> parseTemplateCodes(String input, TypeService typeService)
    {
        Set<ObjectType> ret = new HashSet<>();
        if(!StringUtils.isBlank(input))
        {
            if(input.contains(","))
            {
                String[] typeStrings = input.split(",");
                for(String string : typeStrings)
                {
                    ret.addAll(parseTemplateCode(string, typeService));
                }
            }
            else
            {
                ret.addAll(parseTemplateCode(input, typeService));
            }
        }
        return ret;
    }


    private static final Set<ObjectType> parseTemplateCode(String code, TypeService typeService)
    {
        if(StringUtils.isEmpty(code))
        {
            return Collections.EMPTY_SET;
        }
        String realCode = code.trim();
        if(StringUtils.isEmpty(realCode))
        {
            return Collections.EMPTY_SET;
        }
        Set<ObjectType> ret = new HashSet<>();
        try
        {
            boolean inclSubtypes = false;
            if(realCode.endsWith("+"))
            {
                inclSubtypes = true;
                realCode = realCode.substring(0, realCode.length() - 1);
            }
            ObjectType type = typeService.getObjectType(realCode);
            if(type != null)
            {
                ret.add(type);
                if(inclSubtypes)
                {
                    ret.addAll(typeService.getAllSubtypes(type));
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not parse template codes, check configuration: " + e.getMessage());
        }
        return ret;
    }


    public static void renderLocalizedEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, boolean focus)
    {
        Div container = new Div();
        container.setParent((Component)parent);
        container.setSclass("localized_container");
        Toolbarbutton toolbarButton = new Toolbarbutton();
        toolbarButton.setSclass("lang_button");
        toolbarButton.setWidth("20px");
        toolbarButton.setImage("cockpit/images/language_logo.gif");
        toolbarButton.setTooltiptext(Labels.getLabel("editor.button.language.tooltip"));
        Div div = new Div();
        div.setStyle("margin-right:22px");
        div.setHeight("100%");
        div.setParent((Component)container);
        renderSingleEditor(item, propDescr, (HtmlBasedComponent)div, valueContainer, autoPersist, editorCode, params,
                        UISessionUtils.getCurrentSession().getGlobalDataLanguageIso(), focus);
        toolbarButton.setParent((Component)container);
        toolbarButton.addEventListener("onClick", (EventListener)new Object(toolbarButton));
        toolbarButton.addEventListener("onLaterClick", (EventListener)new Object(container, item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus));
    }


    protected static boolean checkLanguageVisibility(TypedObject item, String languageIso)
    {
        List<String> readableLangs = removeHiddenLanguages(item,
                        UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguageIsos());
        return readableLangs.contains(languageIso);
    }


    public static void renderLocalizedEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, boolean focus, EditorListener editorListener)
    {
        if(editorListener instanceof LanguageAwareEditorListener)
        {
            LanguageAwareEditorListener laEditorListener = (LanguageAwareEditorListener)editorListener;
            Div container = new Div();
            container.setParent((Component)parent);
            container.setSclass("localized_container");
            Toolbarbutton toolbarButton = new Toolbarbutton();
            toolbarButton.setSclass("lang_button");
            toolbarButton.setWidth("20px");
            toolbarButton.setImage("cockpit/images/language_logo.gif");
            toolbarButton.setTooltiptext(Labels.getLabel("editor.button.language.tooltip"));
            Div div = new Div();
            div.setStyle("margin-right:22px");
            div.setHeight("100%");
            div.setParent((Component)container);
            renderSingleEditor(item, propDescr, (HtmlBasedComponent)div, valueContainer, autoPersist, editorCode, params,
                            UISessionUtils.getCurrentSession().getGlobalDataLanguageIso(), focus, (EditorListener)laEditorListener);
            toolbarButton.setParent((Component)container);
            toolbarButton.addEventListener("onClick", (EventListener)new Object(toolbarButton));
            toolbarButton.addEventListener("onLaterClick", (EventListener)new Object(container, item, propDescr, parent, valueContainer, autoPersist, editorCode, params, focus, laEditorListener));
        }
        else
        {
            LOG.error("editorListener must be an instance of '" + LanguageAwareEditorListener.class
                            .getName() + "', ignoring event.");
        }
    }


    public static void renderSingleEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, String isoCode, boolean focus)
    {
        UIEditor editor = getUIEditor(propDescr, editorCode);
        ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(propDescr, isoCode);
        Object object = new Object(editor, propDescr, item, params, valueHolder, autoPersist, valueContainer);
        renderSingleEditor(item, propDescr, parent, valueContainer, autoPersist, editorCode, params, isoCode, focus, (EditorListener)object);
    }


    public static void renderSingleEditor(TypedObject item, PropertyDescriptor propDescr, HtmlBasedComponent parent, ObjectValueContainer valueContainer, boolean autoPersist, String editorCode, Map<String, ? extends Object> params, String isoCode, boolean focus, EditorListener editorListener)
    {
        if(StringUtils.isNotBlank(isoCode) && !checkLanguageVisibility(item, isoCode))
        {
            Label label = new Label(Labels.getLabel("editor.cataloglanguage.hidden", (Object[])new String[] {isoCode}));
            label.setStyle("color:#ccc;");
            label.setParent((Component)parent);
            return;
        }
        UIEditor editor = getUIEditor(propDescr, editorCode);
        if(editor != null)
        {
            boolean editable;
            if(item == null || UISessionUtils.getCurrentSession().getModelService().isNew(item.getObject()))
            {
                ObjectType objectType = getObjectType(propDescr, valueContainer);
                editable = getUIAccessRightService().isWritable(objectType, propDescr, true);
            }
            else
            {
                editable = getUIAccessRightService().isWritable((ObjectType)item.getType(), item, propDescr, false);
            }
            if(editable && propDescr.isLocalized())
            {
                Set<String> writeableLangIsos = UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguageIsos();
                editable = (writeableLangIsos != null && writeableLangIsos.contains(isoCode));
            }
            editor.setEditable(editable);
            if(editor instanceof ListUIEditor)
            {
                setAvailableValuesInListEditor((ListUIEditor)editor, propDescr, params);
            }
            ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(propDescr, isoCode);
            Object currentValue = valueHolder.getCurrentValue();
            Map<String, Object> customParameters = new HashMap<>();
            if(params != null)
            {
                customParameters.putAll(params);
            }
            customParameters.put("attributeQualifier", propDescr.getQualifier());
            customParameters.put("parentObject", item);
            if(editor instanceof ReferenceUIEditor)
            {
                SearchType rootSearchType = null;
                SearchType rootType = getRootSearchType(propDescr, UISessionUtils.getCurrentSession());
                if(params != null && !StringUtils.isEmpty((String)params.get("allowCreate")))
                {
                    ((ReferenceUIEditor)editor).setAllowCreate(Boolean.valueOf((String)params.get("allowCreate")));
                }
                else
                {
                    ((ReferenceUIEditor)editor).setAllowCreate(Boolean.TRUE);
                }
                if(rootType != null)
                {
                    ((ReferenceUIEditor)editor).setRootType((ObjectType)rootType);
                    ((ReferenceUIEditor)editor).setRootSearchType((ObjectType)rootSearchType);
                }
                else
                {
                    LOG.warn("Could not set root type for reference editor (Reason: No root search type could be retrieved for property descriptor '" + propDescr + "').");
                }
                if(currentValue instanceof Collection && !((Collection)currentValue).isEmpty() && ((Collection)currentValue)
                                .iterator().next() instanceof TypedObject)
                {
                    currentValue = UISessionUtils.getCurrentSession().getTypeService().wrapItems((Collection)currentValue);
                }
                else if(!(currentValue instanceof TypedObject))
                {
                    currentValue = UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentValue);
                }
                CreateContext createContext = null;
                if(params != null)
                {
                    createContext = (CreateContext)params.get("createContext");
                    if(createContext == null)
                    {
                        createContext = applyReferenceRelatedAttributes((ReferenceUIEditor)editor, propDescr, params, item, currentValue, isoCode, null,
                                        UISessionUtils.getCurrentSession());
                    }
                    if(createContext != null)
                    {
                        createContext.setLangIso(isoCode);
                        createContext.setBaseType((ObjectType)rootType);
                        try
                        {
                            customParameters.put("createContext", createContext.clone());
                        }
                        catch(CloneNotSupportedException e)
                        {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
            }
            HtmlBasedComponent editorView = editor.createViewComponent(currentValue, customParameters, editorListener);
            parent.appendChild((Component)editorView);
            if(focus)
            {
                editor.setFocus(editorView, true);
            }
        }
        else
        {
            LOG.warn("UIEditor could not be retrieved");
        }
    }


    private static ObjectType getObjectType(PropertyDescriptor propDescr, ObjectValueContainer valueContainer)
    {
        ObjectType objectType = valueContainer.getType();
        if(objectType == null)
        {
            String typeCode;
            TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
            if(propDescr instanceof ItemAttributePropertyDescriptor)
            {
                typeCode = ((ItemAttributePropertyDescriptor)propDescr).getTypeCode();
            }
            else
            {
                typeCode = typeService.getTypeCodeFromPropertyQualifier(propDescr.getQualifier());
            }
            objectType = typeService.getObjectType(typeCode);
        }
        return objectType;
    }


    public static List<Object> getAvailableValues(Map<String, ? extends Object> params, PropertyDescriptor propertyDescriptor)
    {
        List<Object> result = null;
        if(params != null)
        {
            Object param = params.get("allowedValuesList");
            if(param instanceof AvailableValuesProvider)
            {
                result = new ArrayList(UISessionUtils.getCurrentSession().getTypeService().wrapItems(((AvailableValuesProvider)param).getAvailableValues(propertyDescriptor)));
            }
            else if(param instanceof String)
            {
                AvailableValuesProvider provider = (AvailableValuesProvider)SpringUtil.getBean((String)param);
                result = new ArrayList(UISessionUtils.getCurrentSession().getTypeService().wrapItems(provider.getAvailableValues(propertyDescriptor)));
            }
        }
        return result;
    }


    public static void setAvailableValuesInListEditor(ListUIEditor editor, PropertyDescriptor propertyDescriptor, Map<String, ? extends Object> params)
    {
        List<Object> availableValues = getAvailableValues(params, propertyDescriptor);
        if(availableValues == null)
        {
            availableValues = UISessionUtils.getCurrentSession().getTypeService().getAvailableValues(propertyDescriptor);
        }
        editor.setAvailableValues(filterValues(propertyDescriptor, availableValues));
    }


    public static TypedObject getCatalogVersionIfPresent(TypedObject item)
    {
        TypedObject ret = null;
        try
        {
            CatalogTypeService catTypeService = (CatalogTypeService)SpringUtil.getBean("catalogTypeService");
            String code = item.getType().getCode();
            if(catTypeService.isCatalogVersionAwareType(code))
            {
                String catVerAttr = catTypeService.getCatalogVersionContainerAttribute(item.getType().getCode());
                Object value = TypeTools.getObjectAttributeValue(item, catVerAttr,
                                UISessionUtils.getCurrentSession().getTypeService());
                if(value instanceof TypedObject)
                {
                    ret = (TypedObject)value;
                }
            }
        }
        catch(Exception e)
        {
            LOG.debug("Could not get catalogVersion for " + item + ", reason: ", e);
        }
        return ret;
    }


    public static List<String> removeHiddenLanguages(TypedObject item, Collection<String> languages)
    {
        if(item == null || CollectionUtils.isEmpty(languages))
        {
            return new ArrayList<>(languages);
        }
        List<String> ret = new ArrayList<>(languages.size());
        TypedObject catalogVersion = getCatalogVersionIfPresent(item);
        Object object = null;
        if(catalogVersion != null && object = catalogVersion.getObject() instanceof CatalogVersionModel)
        {
            Collection<LanguageModel> catLanguages = ((CatalogVersionModel)object).getLanguages();
            if(catLanguages == null || catLanguages.isEmpty())
            {
                ret.addAll(languages);
            }
            else
            {
                for(LanguageModel languageModel : catLanguages)
                {
                    if(languages.contains(languageModel.getIsocode()))
                    {
                        ret.add(languageModel.getIsocode());
                    }
                }
            }
        }
        else
        {
            ret.addAll(languages);
        }
        return ret;
    }


    public static boolean isDragAndDropDisabled(Map<String, ? extends Object> parameters)
    {
        String allowDragAndDrop = String.valueOf((parameters != null) ? parameters.get("disableDragDrop") : Boolean.FALSE);
        return Boolean.parseBoolean(allowDragAndDrop);
    }
}
