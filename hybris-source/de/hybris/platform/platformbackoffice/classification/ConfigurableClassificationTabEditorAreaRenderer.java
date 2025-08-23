package de.hybris.platform.platformbackoffice.classification;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaBeforeTabRenderLogicHandler;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class ConfigurableClassificationTabEditorAreaRenderer extends ClassificationTabEditorAreaRenderer implements EditorAreaBeforeTabRenderLogicHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableClassificationTabEditorAreaRenderer.class);
    protected static final String I18N_BACKOFFICE_DATA_CLASSIFICATION_NOT_FOUND = "backoffice.data.classification.not.found";
    protected static final String BEAN_ID = "configurableClassificationTabEditorAreaRenderer";
    private ClassificationSystemService classificationSystemService;
    private ClassificationService classificationService;
    private NotificationService notificationService;
    private ClassificationClassModel category;
    private ClassificationSystemVersionModel version;
    private ClassificationSystemModel system;
    private boolean exclusive = true;


    public void render(Component component, AbstractTab abstractTab, ProductModel product, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        try
        {
            if(abstractTab instanceof CustomTab)
            {
                applyConfiguration((CustomTab)abstractTab);
            }
            super.render(component, abstractTab, product, dataType, widgetInstanceManager);
        }
        catch(SystemException sex)
        {
            LOG.debug("Invalid definition of renderer's parameters; rendering empty tab", (Throwable)sex);
            renderEmptyTab(component, "backoffice.data.classification.not.found");
        }
    }


    protected Set<ClassificationClassModel> getFilteredClassificationClasses(FeatureList features)
    {
        return filterConfiguredClasses(super.getFilteredClassificationClasses(features));
    }


    protected Set<ClassificationClassModel> filterConfiguredClasses(Set<ClassificationClassModel> preFilteredClasses)
    {
        return (Set<ClassificationClassModel>)preFilteredClasses.stream()
                        .filter(clazz -> (categoryMatches(clazz) && systemVersionMatches(clazz) && systemMatches(clazz)))
                        .collect(Collectors.toSet());
    }


    public boolean isConfiguredCustomTab(AbstractTab abstractTab, Object object)
    {
        return (abstractTab instanceof CustomTab && object instanceof ProductModel && ((CustomTab)abstractTab)
                        .getSpringBean().equals("configurableClassificationTabEditorAreaRenderer"));
    }


    public boolean canRender(AbstractTab abstractTab, Object object)
    {
        try
        {
            if(isConfiguredCustomTab(abstractTab, object))
            {
                applyConfiguration((CustomTab)abstractTab);
                FeatureList features = getClassificationService().getFeatures((ProductModel)object);
                return CollectionUtils.isNotEmpty(getFilteredClassificationClasses(features));
            }
            return true;
        }
        catch(Exception sex)
        {
            LOG.debug("Invalid definition of renderer's parameters; rendering empty tab", sex);
            return true;
        }
    }


    protected boolean systemMatches(ClassificationClassModel clazz)
    {
        return (this.system == null || this.system.equals(clazz.getCatalogVersion().getCatalog()));
    }


    protected boolean systemVersionMatches(ClassificationClassModel clazz)
    {
        return (this.version == null || this.version.equals(clazz.getCatalogVersion()));
    }


    protected boolean categoryMatches(ClassificationClassModel clazz)
    {
        if(this.category == null)
        {
            return true;
        }
        return (this.category.equals(clazz) || (!isExclusive() && this.category.getAllSubcategories().contains(clazz)));
    }


    protected void applyConfiguration(CustomTab customTab)
    {
        String systemCode = null;
        String versionCode = null;
        String categoryCode = null;
        this.system = null;
        this.version = null;
        this.category = null;
        this.exclusive = true;
        for(Parameter parameter : customTab.getRenderParameter())
        {
            switch(parameter.getName())
            {
                case "system":
                    systemCode = parameter.getValue();
                    continue;
                case "version":
                    versionCode = parameter.getValue();
                    continue;
                case "category":
                    categoryCode = parameter.getValue();
                    continue;
                case "exclusive":
                    this.exclusive = Boolean.valueOf(parameter.getValue()).booleanValue();
                    continue;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Unknown setting: %s", new Object[] {parameter.getName()}));
            }
        }
        if(StringUtils.isNotBlank(systemCode))
        {
            this.system = getClassificationSystemService().getSystemForId(systemCode);
            if(StringUtils.isNotBlank(versionCode))
            {
                this.version = getClassificationSystemService().getSystemVersion(systemCode, versionCode);
                if(StringUtils.isNotBlank(categoryCode))
                {
                    this.category = getClassificationSystemService().getClassForCode(this.version, categoryCode);
                }
            }
        }
    }


    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    public ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public ClassificationClassModel getCategory()
    {
        return this.category;
    }


    public ClassificationSystemVersionModel getSystemVersion()
    {
        return this.version;
    }


    public ClassificationSystemModel getSystem()
    {
        return this.system;
    }


    public boolean isExclusive()
    {
        return this.exclusive;
    }
}
