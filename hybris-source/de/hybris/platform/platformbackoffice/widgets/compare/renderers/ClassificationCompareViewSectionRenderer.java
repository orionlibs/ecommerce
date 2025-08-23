package de.hybris.platform.platformbackoffice.widgets.compare.renderers;

import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.renderer.DefaultCompareViewSectionRenderer;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.services.impl.DefaultBackofficeClassificationService;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationCompareViewSectionRenderer extends DefaultCompareViewSectionRenderer
{
    private PermissionFacade permissionFacade;
    private DefaultBackofficeClassificationService compareViewClassificationService;
    private WidgetComponentRenderer<TableRowsGroup, ClassificationDescriptor, PartialRendererData<Collection>> subsectionRenderer;


    protected boolean isSectionContentRendered(TableRowsGroup parent, Section configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return parent.groupsIterator().hasNext();
    }


    protected void renderSection(TableRowsGroup parent, TableRow headerRow, Section configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        TableComponentIterator<TableRowsGroup> rowsGroups = parent.groupsIterator();
        Object referenceObject = data.getComparisonState().getReference();
        if(referenceObject instanceof ProductModel && isPermitted())
        {
            List<ClassificationDescriptor> classificationDescriptors = this.compareViewClassificationService.getClassificationDescriptors(Collections.singleton((ProductModel)referenceObject));
            classificationDescriptors.forEach(classificationDescriptor -> {
                TableRowsGroup rowsGroup = (TableRowsGroup)rowsGroups.request();
                renderClassificationSection(rowsGroup, classificationDescriptor, data, dataType, widgetInstanceManager);
            });
        }
        rowsGroups.removeRemaining();
    }


    protected boolean isPermitted()
    {
        return (getPermissionFacade().canReadType("ClassificationClass") &&
                        getPermissionFacade().canReadType("ClassAttributeAssignment"));
    }


    protected void renderClassificationSection(TableRowsGroup parent, ClassificationDescriptor classificationDescriptor, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        ProxyRenderer<TableRowsGroup, ClassificationDescriptor, PartialRendererData<Collection>> proxyRenderer = new ProxyRenderer((AbstractWidgetComponentRenderer)this, parent, classificationDescriptor, data);
        proxyRenderer.render(getSubsectionRenderer(), parent, classificationDescriptor, data, dataType, widgetInstanceManager);
    }


    protected DefaultBackofficeClassificationService getCompareViewClassificationService()
    {
        return this.compareViewClassificationService;
    }


    @Required
    public void setCompareViewClassificationService(DefaultBackofficeClassificationService compareViewClassificationService)
    {
        this.compareViewClassificationService = compareViewClassificationService;
    }


    protected WidgetComponentRenderer<TableRowsGroup, ClassificationDescriptor, PartialRendererData<Collection>> getSubsectionRenderer()
    {
        return this.subsectionRenderer;
    }


    @Required
    public void setSubsectionRenderer(WidgetComponentRenderer<TableRowsGroup, ClassificationDescriptor, PartialRendererData<Collection>> subsectionRenderer)
    {
        this.subsectionRenderer = subsectionRenderer;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
