package de.hybris.platform.platformbackoffice.classification.config;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultEditorAreaConfigFallbackStrategy;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationAwareEditorAreaConfigFallbackStrategy extends DefaultEditorAreaConfigFallbackStrategy
{
    public static final String CLASSIFICATION_TAB_IMPL_BEAN_NAME = "classificationTabEditorAreaRenderer";
    public static final String EXTENDED_CLASSIFICATION_ATTRIBUTES_TAB = "extended.classification.attributes.tab";
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationAwareEditorAreaConfigFallbackStrategy.class);
    private TypeService typeService;


    public EditorArea loadFallbackConfiguration(ConfigContext context, Class<EditorArea> configurationType)
    {
        String typeCode = context.getAttribute("type");
        EditorArea configuration = super.loadFallbackConfiguration(context, configurationType);
        try
        {
            TypeModel typeForCode = getTypeService().getTypeForCode(typeCode);
            if(configuration != null && typeForCode instanceof ViewTypeModel)
            {
                List<String> qualifiers = (List<String>)((ViewTypeModel)typeForCode).getColumns().stream().map(DescriptorModel::getQualifier).collect(Collectors.toList());
                Essentials essentials = configuration.getEssentials();
                if(essentials != null && essentials.getEssentialSection() != null)
                {
                    essentials.getEssentialSection().getAttributeOrCustom()
                                    .removeIf(next -> (next instanceof Attribute && !qualifiers.contains(((Attribute)next).getQualifier())));
                }
            }
            if(configuration != null && getTypeService().isAssignableFrom("Product", typeCode))
            {
                CustomTab classTab = new CustomTab();
                classTab.setSpringBean("classificationTabEditorAreaRenderer");
                classTab.setName("extended.classification.attributes.tab");
                classTab.setEssentials(configuration.getEssentials());
                configuration.getCustomTabOrTab().add(classTab);
            }
        }
        catch(UnknownIdentifierException uie)
        {
            LOG.debug("Could not find type [{}]. Skipping classification tab's resolution", typeCode);
        }
        return configuration;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
