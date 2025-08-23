package de.hybris.y2ysync.deltadetection;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class InfoExpressionGenerator
{
    private static final Comparator<AttributeDescriptorModel> ATTRIBUTES_COMPARATOR;
    private final TypeService typeService;

    static
    {
        ATTRIBUTES_COMPARATOR = ((a1, a2) -> a1.getQualifier().compareTo(a2.getQualifier()));
    }

    public InfoExpressionGenerator(TypeService typeService)
    {
        this.typeService = Objects.<TypeService>requireNonNull(typeService);
    }


    public String getInfoExpressionForType(String composedTypeCode)
    {
        Objects.requireNonNull(composedTypeCode);
        requireExistingComposedType(composedTypeCode);
        return getInfoExpression("", composedTypeCode, "|");
    }


    private String getInfoExpression(String pathFromRoot, String composedTypeCode, String delimiter)
    {
        return getUniqueAttributesInOrder(composedTypeCode).stream()
                        .map(a -> getInfoExpression(pathFromRoot, a))
                        .collect(
                                        Collectors.joining(delimiter));
    }


    private String getInfoExpression(String pathFromRoot, AttributeDescriptorModel attribute)
    {
        TypeModel type = attribute.getAttributeType();
        boolean isAtomicType = type instanceof de.hybris.platform.core.model.type.AtomicTypeModel;
        String path = (StringUtils.isBlank(pathFromRoot) ? "" : (pathFromRoot + ".")) + "get" + (StringUtils.isBlank(pathFromRoot) ? "" : (pathFromRoot + ".")) + "()";
        if(isAtomicType)
        {
            return "#{" + path + "}";
        }
        return getInfoExpression(path, type.getCode(), ":");
    }


    private List<AttributeDescriptorModel> getUniqueAttributesInOrder(String composedTypeCode)
    {
        return (List<AttributeDescriptorModel>)this.typeService.getUniqueAttributes(composedTypeCode).stream()
                        .map(qualifier -> this.typeService.getAttributeDescriptor(composedTypeCode, qualifier))
                        .sorted(ATTRIBUTES_COMPARATOR)
                        .collect(Collectors.toList());
    }


    private void requireExistingComposedType(String composedTypeCode)
    {
        try
        {
            this.typeService.getComposedTypeForCode(composedTypeCode);
        }
        catch(SystemException e)
        {
            throw new IllegalArgumentException("Can't find composed type with code '" + composedTypeCode + "'", e);
        }
    }
}
