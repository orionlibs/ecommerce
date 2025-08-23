package de.hybris.platform.spring.config;

import java.util.List;

class AddListProcessor implements ListMergeDirectiveBeanPostProcessor.ListMergeDirectiveProcessor
{
    public String getName()
    {
        return "Add";
    }


    public void processDirective(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, String listBeanName)
    {
        listBean.add(directive.getAdd());
    }
}
