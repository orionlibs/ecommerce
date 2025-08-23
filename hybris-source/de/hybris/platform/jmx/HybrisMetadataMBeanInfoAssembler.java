package de.hybris.platform.jmx;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.management.Descriptor;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;

public class HybrisMetadataMBeanInfoAssembler extends MetadataMBeanInfoAssembler
{
    protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey)
    {
        Method[] methods = getClassToExpose(managedBean).getMethods();
        List<ModelMBeanOperationInfo> infos = new ArrayList<>();
        for(Method method : methods)
        {
            if(method.isSynthetic())
            {
                continue;
            }
            if(method.getDeclaringClass().equals(Object.class))
            {
                continue;
            }
            ModelMBeanOperationInfo info = null;
            PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
            if(pd != null)
            {
                if((method.equals(pd.getReadMethod()) && includeReadAttribute(method, beanKey)) || (method
                                .equals(pd.getWriteMethod()) && includeWriteAttribute(method, beanKey)))
                {
                    ManagedOperation ann = (ManagedOperation)AnnotationUtils.findAnnotation(method, ManagedOperation.class);
                    if(ann == null)
                    {
                        continue;
                    }
                    info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
                    Descriptor desc = info.getDescriptor();
                    if(method.equals(pd.getReadMethod()))
                    {
                        desc.setField("role", "getter");
                    }
                    else
                    {
                        desc.setField("role", "setter");
                    }
                    desc.setField("visibility", Integer.valueOf(4));
                    if(isExposeClassDescriptor())
                    {
                        desc.setField("class", getClassForDescriptor(managedBean).getName());
                    }
                    info.setDescriptor(desc);
                }
            }
            if(info == null && includeOperation(method, beanKey))
            {
                info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
                Descriptor desc = info.getDescriptor();
                desc.setField("role", "operation");
                if(isExposeClassDescriptor())
                {
                    desc.setField("class", getClassForDescriptor(managedBean).getName());
                }
                populateOperationDescriptor(desc, method, beanKey);
                info.setDescriptor(desc);
            }
            if(info != null)
            {
                infos.add(info);
            }
            continue;
        }
        return infos.<ModelMBeanOperationInfo>toArray(new ModelMBeanOperationInfo[infos.size()]);
    }
}
