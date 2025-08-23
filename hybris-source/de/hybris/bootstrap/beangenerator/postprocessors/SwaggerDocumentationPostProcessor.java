package de.hybris.bootstrap.beangenerator.postprocessors;

import de.hybris.bootstrap.beangenerator.BeansPostProcessor;
import de.hybris.bootstrap.beangenerator.ClassNameUtil;
import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNameAware;
import de.hybris.bootstrap.beangenerator.definitions.model.PropertyPrototype;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

public class SwaggerDocumentationPostProcessor implements BeansPostProcessor
{
    public Collection<? extends ClassNameAware> postProcess(Collection<? extends ClassNameAware> beans)
    {
        for(ClassNameAware bean : beans)
        {
            if(bean instanceof BeanPrototype)
            {
                postProcessBean((BeanPrototype)bean);
            }
        }
        return beans;
    }


    private void postProcessBean(BeanPrototype bean)
    {
        if(isNotExposed(bean))
        {
            return;
        }
        addRequiredImports(bean);
        addRequiredClassAnnotations(bean);
        addRequiredMemberAnnotations(bean);
    }


    private void addRequiredImports(BeanPrototype bean)
    {
        bean.addDeclaredImport("io.swagger.annotations.ApiModelProperty", false);
        bean.addDeclaredImport("io.swagger.annotations.ApiModel", false);
    }


    private void addRequiredClassAnnotations(BeanPrototype bean)
    {
        String classAnnotations = StringUtils.trimToEmpty(bean.getAnnotations());
        if(!classAnnotations.contains("@ApiModel"))
        {
            StringBuilder modifiedClassAnnotations = new StringBuilder(classAnnotations);
            if(!classAnnotations.isEmpty())
            {
                modifiedClassAnnotations.append("\n\t");
            }
            Object alias = bean.getHintValue("alias");
            String name = (alias != null) ? alias.toString() : ClassNameUtil.getShortClassName(bean.getClassName());
            modifiedClassAnnotations.append("@ApiModel(value=\"").append(name).append("\"");
            String description = convertToOneLine(bean.getDescription());
            if(!description.isEmpty())
            {
                modifiedClassAnnotations.append(", description=\"").append(description).append("\"");
            }
            modifiedClassAnnotations.append(")");
            bean.setAnnotations(modifiedClassAnnotations.toString());
        }
    }


    private void addRequiredMemberAnnotations(BeanPrototype bean)
    {
        for(PropertyPrototype property : bean.getProperties())
        {
            if(isAlreadyAnnotated(property))
            {
                continue;
            }
            String memberAnnotations = StringUtils.trimToEmpty(property.getMemberAnnotations());
            StringBuilder modifiedMemberAnnotations = new StringBuilder(memberAnnotations);
            if(memberAnnotations.isEmpty())
            {
                modifiedMemberAnnotations.append("\n\t");
            }
            String alias = StringUtils.trimToEmpty(property.getHintValue("alias"));
            String name = alias.isEmpty() ? property.getName() : alias;
            modifiedMemberAnnotations.append("@ApiModelProperty(name=\"").append(name).append("\"");
            String description = convertToOneLine(property.getDescription());
            if(!description.isEmpty())
            {
                modifiedMemberAnnotations.append(", value=\"").append(description).append("\"");
            }
            String required = property.getHintValue("required");
            if(required != null)
            {
                modifiedMemberAnnotations.append(", required=").append(Boolean.valueOf(required));
            }
            String allowedValues = StringUtils.trimToEmpty(property.getHintValue("allowedValues"));
            if(!allowedValues.isEmpty())
            {
                modifiedMemberAnnotations.append(", allowableValues=\"").append(allowedValues).append("\"");
            }
            String example = StringUtils.trimToEmpty(property.getHintValue("example"));
            if(!example.isEmpty())
            {
                modifiedMemberAnnotations.append(", example=\"").append(example).append("\"");
            }
            modifiedMemberAnnotations.append(")");
            property.setMemberAnnotations(modifiedMemberAnnotations.toString());
        }
    }


    private String convertToOneLine(String input)
    {
        return StringUtils.trimToEmpty(input)
                        .replace("\n", "")
                        .replace("\t", " ")
                        .replaceAll("( +)", " ");
    }


    private boolean isAlreadyAnnotated(PropertyPrototype property)
    {
        return (StringUtils.trimToEmpty(property.getMemberAnnotations()).contains("@ApiModelProperty") ||
                        StringUtils.trimToEmpty(property.getGetterAnnotations()).contains("@ApiModelProperty") ||
                        StringUtils.trimToEmpty(property.getSetterAnnotations()).contains("@ApiModelProperty"));
    }


    private boolean isNotExposed(BeanPrototype bean)
    {
        return !Boolean.parseBoolean(bean.getHintValue("wsRelated"));
    }
}
