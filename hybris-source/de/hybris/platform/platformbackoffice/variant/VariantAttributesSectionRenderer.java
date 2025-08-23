package de.hybris.platform.platformbackoffice.variant;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AttributesComponentRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaComponentRenderer;
import de.hybris.platform.variants.model.VariantProductModel;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

public class VariantAttributesSectionRenderer extends AbstractEditorAreaComponentRenderer<AbstractSection, VariantProductModel> implements AttributesComponentRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(VariantAttributesSectionRenderer.class);
    public static final int DEFAULT_COLUMN_NUMBER = 2;
    public static final String SCLASS_CELL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell";
    public static final String SCLASS_CELL_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    public static final String SCLASS_MANDATORY_ATTRIBUTE_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute";
    public static final String SCLASS_GRPBOX = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox";
    public static final String SCLASS_CELL_LABEL_CONTAINER = "yw-editorarea-label-container";
    public static final String VARIANT_ATTRIBUTES_MAP_MODEL = "variantAttributesMapModel";
    public static final String EDITED_VARIANT_PRODUCT = "EditedVariantProduct";
    private ObjectValueService objectValueService;
    private TypeFacade typeFacade;


    public void render(Component parent, AbstractSection configuration, VariantProductModel variantProductModel, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(getObjectFacade().isNew(variantProductModel))
        {
            parent.appendChild((Component)new Label(Labels.getLabel("variant.attributes.section.no.editing")));
            return;
        }
        UITools.modifySClass((HtmlBasedComponent)parent, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox", true);
        Collection<String> attributes = getRenderedQualifiers(dataType);
        Collection<String> writeThroughAttributes = getWriteThroughAttributes(dataType, attributes);
        Hbox container = null;
        int columnNumber = getColumnNumber(configuration);
        registerDataModelForVariants(variantProductModel, widgetInstanceManager, writeThroughAttributes);
        int counter = 0;
        for(String attribute : attributes)
        {
            if(counter++ % columnNumber == 0)
            {
                container = new Hbox();
                parent.appendChild((Component)container);
            }
            Cell cell = new Cell();
            cell.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell");
            Attribute confAttribute = new Attribute();
            confAttribute.setQualifier(attribute);
            Editor editor = createEditor(dataType, widgetInstanceManager, confAttribute, variantProductModel);
            if(!dataType.getAttribute(attribute).isLocalized())
            {
                Div labelCtr = new Div();
                UITools.modifySClass((HtmlBasedComponent)labelCtr, "yw-editorarea-label-container", true);
                Label label = new Label(resolveAttributeLabel(confAttribute, dataType));
                label.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label");
                label.setTooltiptext(attribute);
                labelCtr.appendChild((Component)label);
                if(editor.isOptional())
                {
                    UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label", true);
                }
                else
                {
                    UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute", true);
                }
                cell.appendChild((Component)labelCtr);
            }
            cell.appendChild((Component)editor);
            if(container != null)
            {
                container.appendChild((Component)cell);
            }
        }
    }


    protected void registerDataModelForVariants(VariantProductModel variantProductModel, WidgetInstanceManager widgetInstanceManager, Collection<String> attributes)
    {
        Object lastInstance = widgetInstanceManager.getModel().getValue("EditedVariantProduct", Object.class);
        if(lastInstance == null || !lastInstance.equals(variantProductModel))
        {
            widgetInstanceManager.getModel().put("EditedVariantProduct", variantProductModel);
            widgetInstanceManager.getModel().removeObserver("variantAttributesMapModel");
            widgetInstanceManager.getModel().put("variantAttributesMapModel",
                            prepareNewVariantsModel(attributes, variantProductModel));
            EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager
                            .getModel(), Long.toString(System.currentTimeMillis()), event -> widgetInstanceManager.getModel().put("variantAttributesMapModel", prepareNewVariantsModel(attributes, variantProductModel)), false);
            EditorAreaRendererUtils.setAfterSaveListener(widgetInstanceManager.getModel(), Long.toString(System.currentTimeMillis()), event -> {
                Map map = (Map)widgetInstanceManager.getModel().getValue("variantAttributesMapModel", Map.class);
                if(map != null)
                {
                    for(Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)map.entrySet())
                    {
                        getObjectValueService().setValue(entry.getKey(), variantProductModel, entry.getValue());
                    }
                }
            } false);
            widgetInstanceManager.getModel().addObserver("variantAttributesMapModel", (ValueObserver)new Object(this, widgetInstanceManager));
        }
    }


    protected HashMap<Object, Object> prepareNewVariantsModel(Collection<String> attributes, VariantProductModel variantProductModel)
    {
        HashMap<Object, Object> map = Maps.newHashMap();
        for(String attribute : attributes)
        {
            map.put(attribute, getObjectValueService().getValue(attribute, variantProductModel));
        }
        return map;
    }


    protected void processEditorBeforeComposition(Editor editor, DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        if(editor != null)
        {
            DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
            if(genericAttribute == null || !genericAttribute.isWriteThrough())
            {
                return;
            }
            String referencedModelProperty = "variantAttributesMapModel." + genericAttribute.getQualifier();
            editor.setProperty(referencedModelProperty);
        }
    }


    protected int getColumnNumber(AbstractSection configuration)
    {
        if(configuration instanceof Section)
        {
            BigDecimal columns = ((Section)configuration).getColumns();
            if(columns != null)
            {
                return Math.max(columns.intValue(), 1);
            }
        }
        return 2;
    }


    public Collection<String> getRenderedQualifiers(WidgetInstanceManager widgetInstanceManager)
    {
        try
        {
            DataType type = (DataType)widgetInstanceManager.getModel().getValue("currentType", DataType.class);
            if(type == null)
            {
                String typeCode = this.typeFacade.getType(widgetInstanceManager.getModel().getValue("EditedVariantProduct", Object.class));
                if(StringUtils.isBlank(typeCode))
                {
                    return Collections.emptyList();
                }
                type = getTypeFacade().load(typeCode);
            }
            return getRenderedQualifiers(type);
        }
        catch(TypeNotFoundException e)
        {
            LOG.debug("Type not found", (Throwable)e);
            return Collections.emptyList();
        }
    }


    public Collection<String> getRenderedQualifiers(DataType dataType)
    {
        return (Collection<String>)dataType.getAttributes().stream().filter(DataAttribute::isVariantAttribute).map(DataAttribute::getQualifier)
                        .collect(Collectors.toList());
    }


    protected List<String> getWriteThroughAttributes(DataType dataType, Collection<String> renderedAttributes)
    {
        Objects.requireNonNull(dataType);
        return (List<String>)renderedAttributes.stream().map(dataType::getAttribute).filter(DataAttribute::isWriteThrough)
                        .map(DataAttribute::getQualifier).collect(Collectors.toList());
    }


    public ObjectValueService getObjectValueService()
    {
        return this.objectValueService;
    }


    @Required
    public void setObjectValueService(ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }
}
