package de.hybris.platform.audit.internal;

import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.audit.gateway.ModelAuditRecord;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ModelAuditRecordConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(ModelAuditRecordConverter.class);
    private ModelService modelService;


    public AuditRecord toAuditRecord(SLDDataContainer sldDataContainer, Type typeConfiguration)
    {
        return toAuditRecord(sldDataContainer, typeConfiguration.getCode());
    }


    public AuditRecord toAuditRecord(SLDDataContainer sldDataContainer, String typeCode)
    {
        AttributesProcessor processor = new AttributesProcessor(this, sldDataContainer);
        return (AuditRecord)ModelAuditRecord.builder()
                        .withAuditType(AuditType.CURRENT)
                        .withType(typeCode)
                        .withTypePk(sldDataContainer.getTypePk())
                        .withPk(sldDataContainer.getPk())
                        .withTimestamp((Date)sldDataContainer.getAttributeValue("modifiedtime", null)
                                        .getValue())
                        .withAttributes(processor.getAttributes())
                        .withLocalizedAttributes(processor.getLocalizedAttributes())
                        .withChangingUser("system")
                        .build();
    }


    public LinkAuditRecord toLinkAuditRecord(SLDDataContainer sldDataContainer, String typeCode, LinkAuditRecord.LinkSide parentSide)
    {
        PK sourcePk = ((ItemPropertyValue)sldDataContainer.getAttributeValue("source", null).getValue()).getPK();
        PK targetPk = ((ItemPropertyValue)sldDataContainer.getAttributeValue("target", null).getValue()).getPK();
        return LinkAuditRecord.builder()
                        .withAuditRecord(toAuditRecord(sldDataContainer, typeCode))
                        .withParentSide(parentSide)
                        .withSourcePk(sourcePk)
                        .withTargetPk(targetPk)
                        .build();
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
