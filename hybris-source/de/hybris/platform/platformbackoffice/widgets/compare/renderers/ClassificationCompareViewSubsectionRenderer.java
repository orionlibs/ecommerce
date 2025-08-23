package de.hybris.platform.platformbackoffice.widgets.compare.renderers;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.renderer.AbstractCompareViewSectionRenderer;
import com.hybris.cockpitng.widgets.compare.renderer.DefaultCompareViewLayout;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import de.hybris.platform.platformbackoffice.services.BackofficeClassificationService;
import de.hybris.platform.platformbackoffice.widgets.compare.model.BackofficeComparisonResult;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.ClassificationValuesContainer;
import de.hybris.platform.platformbackoffice.widgets.compare.model.FeatureDescriptor;
import de.hybris.platform.platformbackoffice.widgets.compare.model.impl.BackofficePartialRendererData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class ClassificationCompareViewSubsectionRenderer extends AbstractCompareViewSectionRenderer<ClassificationDescriptor, FeatureDescriptor>
{
    private static final String SCLASS_SUBSECTION_HEADER_ROW = "yw-compareview-subsection-header-row";
    private static final String SCLASS_SUBSECTION_ROW = "yw-compareview-subsection-row";
    protected BackofficeClassificationService backofficeClassificationService;
    protected WidgetComponentRenderer<TableRow, FeatureDescriptor, BackofficePartialRendererData<Collection>> featureRenderer;
    protected PermissionFacade permissionFacade;


    protected String getConfiguredSectionIdentifier(ClassificationDescriptor configuration)
    {
        return configuration.getName();
    }


    protected boolean isSectionContentRendered(TableRowsGroup parent, ClassificationDescriptor configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        return parent.rowsIterator().hasNext();
    }


    protected void renderSectionHeader(TableRowsGroup parent, TableRow row, ClassificationDescriptor configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        super.renderSectionHeader(parent, row, configuration, data, dataType, widgetInstanceManager);
        UITools.addSClass((HtmlBasedComponent)row, "yw-compareview-subsection-header-row");
    }


    protected void renderSection(TableRowsGroup parent, TableRow headerRow, ClassificationDescriptor configuration, PartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        List<Object> allObjects = new ArrayList(data.getComparisonState().getAllObjects());
        Set<ProductModel> products = BackofficeClassificationUtils.extractProducts(allObjects);
        Map<ProductModel, Map<String, Map<FeatureDescriptor, ClassificationInfo>>> featureValuesForProducts = new HashMap<>();
        products.forEach(product -> {
            Map<ProductModel, FeatureList> compareObjectFeatureListMap = (Map<ProductModel, FeatureList>)widgetInstanceManager.getModel().getValue("comparison-object-featureList-map", Map.class);
            if(compareObjectFeatureListMap != null && compareObjectFeatureListMap.containsKey(product))
            {
                featureValuesForProducts.put(product, this.backofficeClassificationService.getFeatureValuesFromFeatureList(Collections.singletonList(configuration), compareObjectFeatureListMap.get(product)));
            }
            else
            {
                featureValuesForProducts.put(product, this.backofficeClassificationService.getFeatureValues(product, Collections.singletonList(configuration)));
            }
        });
        if(getPermissionFacade().canReadType("ClassificationAttribute"))
        {
            TableComponentIterator<TableRow> classificationRows = parent.rowsIterator();
            BackofficePartialRendererData<Collection> backofficePartialRendererData = new BackofficePartialRendererData(data.getComparisonResult(), data.getData(), data.getComparisonState(), featureValuesForProducts);
            configuration.getFeatures().forEach(feature -> {
                TableRow featureRow = (TableRow)classificationRows.request();
                UITools.addSClass((HtmlBasedComponent)featureRow, "yw-compareview-subsection-row");
                renderFeature(parent, featureRow, feature, backofficePartialRendererData, dataType, widgetInstanceManager);
                updateTableRowDifferentMark(parent, featureRow, feature, data);
                fireComponentRendered((Component)featureRow, parent, configuration, data);
            });
            classificationRows.removeRemaining();
        }
    }


    protected void updateTableRowDifferentMark(TableRowsGroup parent, TableRow tableRow, FeatureDescriptor featureDescriptor, PartialRendererData<Collection> data)
    {
        ComparisonResult result = data.getComparisonResult();
        if(result instanceof BackofficeComparisonResult)
        {
            BackofficeComparisonResult backofficeResult = (BackofficeComparisonResult)result;
            Collection<ClassificationValuesContainer> differentObjectsContainers = (Collection<ClassificationValuesContainer>)backofficeResult.getDifferentObjectsForFeatures().get(featureDescriptor);
            if(differentObjectsContainers != null && !differentObjectsContainers.isEmpty())
            {
                DefaultCompareViewLayout.markAsNotEqual((HtmlBasedComponent)tableRow);
            }
            else
            {
                DefaultCompareViewLayout.markAsEqual((HtmlBasedComponent)tableRow);
            }
        }
    }


    protected void updateSectionHeaderDifferentMark(ClassificationDescriptor configuration, TableRow tableRow, PartialRendererData<Collection> data)
    {
        ComparisonResult comparisonResult = data.getComparisonResult();
        if(comparisonResult instanceof BackofficeComparisonResult)
        {
            BackofficeComparisonResult backofficeComparisonResult = (BackofficeComparisonResult)comparisonResult;
            boolean isAtLastOneDifference = backofficeComparisonResult.getDifferentClassifications().stream().anyMatch(classificationDescriptor -> classificationDescriptor.getCode().equals(configuration.getCode()));
            if(isAtLastOneDifference)
            {
                DefaultCompareViewLayout.markAsNotEqual((HtmlBasedComponent)tableRow);
            }
            else
            {
                DefaultCompareViewLayout.markAsEqual((HtmlBasedComponent)tableRow);
            }
        }
    }


    protected void renderFeature(TableRowsGroup parent, TableRow row, FeatureDescriptor featureDescriptor, BackofficePartialRendererData<Collection> data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        ProxyRenderer<TableRow, FeatureDescriptor, BackofficePartialRendererData<Collection>> proxyRenderer = new ProxyRenderer((AbstractWidgetComponentRenderer)this, parent, featureDescriptor, data);
        proxyRenderer.render(getFeatureRenderer(), row, featureDescriptor, data, dataType, widgetInstanceManager);
    }


    protected WidgetComponentRenderer<TableRow, FeatureDescriptor, BackofficePartialRendererData<Collection>> getFeatureRenderer()
    {
        return this.featureRenderer;
    }


    @Required
    public void setFeatureRenderer(WidgetComponentRenderer<TableRow, FeatureDescriptor, BackofficePartialRendererData<Collection>> featureRenderer)
    {
        this.featureRenderer = featureRenderer;
    }


    protected BackofficeClassificationService getBackofficeClassificationService()
    {
        return this.backofficeClassificationService;
    }


    @Required
    public void setBackofficeClassificationService(BackofficeClassificationService compareViewClassificationService)
    {
        this.backofficeClassificationService = compareViewClassificationService;
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
