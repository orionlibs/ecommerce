package de.hybris.platform.configurablebundlebackoffice.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaSectionRenderer;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Cell;

public class NestedAttributeAwareEditorAreaSectionRenderer extends DefaultEditorAreaSectionRenderer
{
    protected static final String NESTED_ATTRIBUTE_PARAMETER = "nested";
    protected static final String MANDATORY_ATTRIBUTE_PARAMETER = "mandatory";
    private static final Logger LOG = LoggerFactory.getLogger(NestedAttributeAwareEditorAreaSectionRenderer.class);
    private TypeFacade typeFacade;


    public void render(Component parent, AbstractSection abstractSectionConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Section section = (Section)abstractSectionConfiguration;
        int columns = Math.max(section.getColumns().intValue(), 1);
        String width = calculateWidthPercentage(columns);
        renderAttributes(section.getAttributeOrCustom(), new ProxyRenderer((AbstractWidgetComponentRenderer)this, parent, abstractSectionConfiguration, object), columns, width, dataType, widgetInstanceManager, object);
    }


    protected void processEditorBeforeComposition(Editor editor, DataType genericType, WidgetInstanceManager widgetInstanceManager, Attribute attribute, Object object)
    {
        super.processEditorBeforeComposition(editor, genericType, widgetInstanceManager, attribute, object);
        attribute.getEditorParameter().stream().filter(p -> "mandatory".equals(p.getName())).findFirst()
                        .ifPresent(p -> editor.setOptional(!BooleanUtils.toBoolean(p.getValue())));
        attribute.getEditorParameter().stream()
                        .filter(p -> ("nested".equals(p.getName()) && BooleanUtils.toBoolean(p.getValue()))).findFirst()
                        .ifPresent(p -> editor.setProperty(String.format("%s.%s", new Object[] {editor.getProperty(), attribute.getQualifier()})));
    }


    protected WidgetComponentRenderer<Cell, Attribute, Object> createAttributeRenderer()
    {
        return (WidgetComponentRenderer<Cell, Attribute, Object>)new NestedAttributeRenderer(this);
    }


    public TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
