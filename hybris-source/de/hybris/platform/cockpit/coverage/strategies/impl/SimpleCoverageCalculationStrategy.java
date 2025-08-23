package de.hybris.platform.cockpit.coverage.strategies.impl;

import com.google.common.base.Joiner;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.coverage.CoverageInfo;
import de.hybris.platform.validation.coverage.strategies.CoverageCalculationStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fest.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class SimpleCoverageCalculationStrategy implements CoverageCalculationStrategy
{
    private TypeService typeService;
    private ModelService modelService;
    private CommonI18NService commonI18nService;
    private SessionService sessionService;
    private Set<String> attributeQualifiers = Collections.EMPTY_SET;
    private Set<AttributeDescriptorModel> attributes;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleCoverageCalculationStrategy.class);


    public CoverageInfo calculate(ItemModel model)
    {
        List<CoverageInfo.CoveragePropertyInfoMessage> infoMsgs = new ArrayList<>();
        synchronized(this)
        {
            if(this.attributes == null)
            {
                this.attributes = new HashSet<>();
                for(String quali : getAttributeQualifiers())
                {
                    this.attributes.add(this.typeService.getAttributeDescriptor(model.getItemtype(), quali));
                }
            }
        }
        int nrValues = 0;
        int nrEmpty = 0;
        for(AttributeDescriptorModel attribute : this.attributes)
        {
            Object value = null;
            try
            {
                value = this.modelService.getAttributeValue(model, attribute.getQualifier());
            }
            catch(AttributeNotSupportedException ex)
            {
                LOG.info(ex.getMessage(), (Throwable)ex);
                continue;
            }
            if(Boolean.TRUE.equals(attribute.getLocalized()))
            {
                int[] locCov = calculateLocalizedAttributeCoverage(model, attribute.getQualifier(), infoMsgs);
                nrValues += locCov[1];
                nrEmpty += locCov[0];
            }
            else if(isEmptyValue(value))
            {
                nrEmpty++;
                infoMsgs.add(new CoverageInfo.CoveragePropertyInfoMessage(model.getItemtype() + "." + model.getItemtype(),
                                Labels.getLabel("cockpit.covarage.empty")));
            }
            nrValues++;
        }
        int nrFilled = nrValues - nrEmpty;
        CoverageInfo coverageInfo = new CoverageInfo((nrValues == 0) ? 1.0D : ((nrFilled == 0) ? 0.0D : (nrFilled / nrValues * 1.0D)));
        Collections.sort(infoMsgs, (Comparator<? super CoverageInfo.CoveragePropertyInfoMessage>)new Object(this));
        coverageInfo.setPropertyInfoMessages(infoMsgs);
        return coverageInfo;
    }


    protected boolean isEmptyValue(Object value)
    {
        return ((value instanceof Collection && CollectionUtils.isEmpty((Collection)value)) || value == null || (value instanceof String &&
                        StringUtils.isBlank((String)value)));
    }


    protected int[] calculateLocalizedAttributeCoverage(ItemModel itemModel, String attributeQualifier, List<CoverageInfo.CoveragePropertyInfoMessage> infoMsgs)
    {
        int nrLanguages = 0;
        List<LanguageModel> languages = this.commonI18nService.getAllLanguages();
        List<String> langsEmpty = new ArrayList<>();
        for(LanguageModel languageModel : languages)
        {
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, languageModel, itemModel, attributeQualifier, langsEmpty));
            nrLanguages++;
        }
        if(!langsEmpty.isEmpty())
        {
            String label = Labels.getLabel("cockpit.covarage.empty.multilanguage",
                            Arrays.array((Object[])new String[] {Joiner.on(", ").join(langsEmpty)}));
            infoMsgs.add(new CoverageInfo.CoveragePropertyInfoMessage(itemModel.getItemtype() + "." + itemModel.getItemtype(), label));
        }
        return new int[] {langsEmpty
                        .size(), nrLanguages};
    }


    public void setAttributeQualifiers(Set<String> attributeQualifiers)
    {
        this.attributeQualifiers = attributeQualifiers;
    }


    protected Set<String> getAttributeQualifiers()
    {
        return this.attributeQualifiers;
    }


    public void resetAttributes()
    {
        this.attributes = null;
    }


    public boolean hasAttributeQualifiersAssigned()
    {
        return CollectionUtils.isNotEmpty(getAttributeQualifiers());
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setCommonI18nService(CommonI18NService commonI18nService)
    {
        this.commonI18nService = commonI18nService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
