package de.hybris.y2ysync.impex.typesystem;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ImpexHeaderBuilder
{
    private TypeService typeService;


    public String getHeaderFor(String composedTypeCode, String qualifier)
    {
        return getHeaderFor(composedTypeCode, qualifier, null);
    }


    public String getHeaderFor(AttributeDescriptorModel attributeDescriptor)
    {
        return getHeaderFor(attributeDescriptor, null);
    }


    public String getHeaderFor(String composedTypeCode, String qualifier, String langIsoCode)
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(composedTypeCode, qualifier);
        return getHeaderFor(attributeDescriptor, langIsoCode);
    }


    public String getHeaderFor(AttributeDescriptorModel attributeDescriptor, String langIsoCode)
    {
        StringBuilder sb = new StringBuilder(getAttributeMapping(attributeDescriptor, langIsoCode));
        if(isUnique(attributeDescriptor))
        {
            sb.append("[unique=true]");
        }
        if(isDate(attributeDescriptor))
        {
            sb.append("[dateformat=dd.MM.yyyy hh:mm:ss]");
        }
        if(StringUtils.isNotBlank(langIsoCode))
        {
            sb.append("[lang=").append(langIsoCode).append("]");
        }
        return sb.toString();
    }


    private String getAttributeMapping(AttributeDescriptorModel attributeDescriptor, String langIsoCode)
    {
        StringBuilder sb = new StringBuilder();
        TypeModel attributeType = attributeDescriptor.getAttributeType();
        sb.append(attributeDescriptor.getQualifier());
        if(isReference(attributeType))
        {
            sb.append(renderColumnHeader(attributeType, langIsoCode));
        }
        return sb.toString();
    }


    private boolean isReference(TypeModel attributeType)
    {
        return !(attributeType instanceof de.hybris.platform.core.model.type.AtomicTypeModel);
    }


    private String renderColumnHeader(TypeModel attributeType, String langIsoCode)
    {
        StringBuilder sb = new StringBuilder();
        if(attributeType instanceof ComposedTypeModel)
        {
            sb.append(renderColumnHeaderForComposedType((ComposedTypeModel)attributeType, langIsoCode));
        }
        else if(attributeType instanceof CollectionTypeModel)
        {
            sb.append(renderColumnHeader(((CollectionTypeModel)attributeType).getElementType(), null));
        }
        else if(attributeType instanceof MapTypeModel && StringUtils.isBlank(langIsoCode))
        {
            String argTypeHeader = renderColumnHeader(((MapTypeModel)attributeType).getArgumentType(), null);
            boolean argHeaderPresent = StringUtils.isNotBlank(argTypeHeader);
            sb.append("(");
            if(argHeaderPresent)
            {
                sb.append("key").append(argTypeHeader);
            }
            String retTypeHeader = renderColumnHeader(((MapTypeModel)attributeType).getReturntype(), null);
            if(StringUtils.isNotBlank(retTypeHeader))
            {
                if(argHeaderPresent)
                {
                    sb.append(",");
                }
                sb.append("value").append(retTypeHeader);
            }
            sb.append(")");
        }
        else if(attributeType instanceof MapTypeModel)
        {
            renderColumnHeader(((MapTypeModel)attributeType).getReturntype(), langIsoCode);
        }
        return sb.toString();
    }


    private String renderColumnHeaderForComposedType(ComposedTypeModel composedType, String langIsoCode)
    {
        String typeCode = composedType.getCode();
        Set<AttributeDescriptorModel> uniqueAttrs = (Set<AttributeDescriptorModel>)this.typeService.getUniqueAttributes(typeCode).stream().map(a -> this.typeService.getAttributeDescriptor(typeCode, a)).collect(Collectors.toSet());
        String header = uniqueAttrs.stream().sorted((Comparator)new AttributeDescriptorComparator(this)).map(ad -> getAttributeMapping(ad, langIsoCode)).collect(Collectors.joining(","));
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(header);
        sb.append(")");
        return sb.toString();
    }


    private boolean isUnique(AttributeDescriptorModel attributeDescriptor)
    {
        return attributeDescriptor.getUnique().booleanValue();
    }


    private boolean isDate(AttributeDescriptorModel attributeDescriptor)
    {
        return "java.util.Date".equalsIgnoreCase(attributeDescriptor.getAttributeType().getCode());
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
