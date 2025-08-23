package de.hybris.platform.spring.config;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

class SortListProcessor implements ListMergeDirectiveBeanPostProcessor.ListMergeDirectiveProcessor
{
    private static final Logger LOG = Logger.getLogger(SortListProcessor.class);
    private final ConfigurableListableBeanFactory beanFactory;


    SortListProcessor(ConfigurableListableBeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public String getName()
    {
        return "Sort";
    }


    public void processDirective(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, String listBeanName)
    {
        if(requiresSorting(directive))
        {
            int index = listBean.indexOf(directive.getAdd());
            if(index != -1)
            {
                processAfterDirectives(directive, listBean, directiveBeanName, index);
                processBeforeDirectives(directive, listBean, directiveBeanName, index);
            }
        }
    }


    protected void processAfterDirectives(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, int index)
    {
        if(hasAfterDirectives(directive))
        {
            int highestIndex = -1;
            highestIndex = getHighestIndexByBean(directive, listBean, directiveBeanName, highestIndex);
            highestIndex = getHighestIndexByClasses(directive, listBean, index, highestIndex);
            highestIndex = getHighestIndexByValue(directive, listBean, highestIndex);
            setAfterPosition(directive, listBean, index, highestIndex);
        }
    }


    private int getHighestIndexByBean(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, int highestIndex)
    {
        for(String beanName : getList(directive.getAfterBeanNames()))
        {
            Supplier<String> errorSupplier = () -> "list merge directive[" + directiveBeanName + "] afterBeanName [" + beanName + "] does not exist";
            Object bean = getBean(beanName, errorSupplier);
            highestIndex = getIndex(listBean, highestIndex, bean, this::isHighest);
        }
        return highestIndex;
    }


    private int getHighestIndexByClasses(ListMergeDirective directive, List<Object> listBean, int index, int highestIndex)
    {
        List<Class<?>> afterClasses = getList(directive.getAfterClasses());
        for(int i = listBean.size() - 1; i >= 0 || i >= index; i--)
        {
            for(Class<?> clazz : afterClasses)
            {
                Object element = listBean.get(i);
                if(element != null && clazz.isAssignableFrom(element.getClass()))
                {
                    return i;
                }
            }
        }
        return highestIndex;
    }


    private int getHighestIndexByValue(ListMergeDirective directive, List<Object> listBean, int highestIndex)
    {
        for(Object value : getList(directive.getAfterValues()))
        {
            highestIndex = getIndex(listBean, highestIndex, value, this::isHighest);
        }
        return highestIndex;
    }


    private void setAfterPosition(ListMergeDirective directive, List<Object> listBean, int index, int highestIndex)
    {
        if(highestIndex > index)
        {
            int newIndex = highestIndex + 1;
            if(newIndex >= listBean.size())
            {
                listBean.add(directive.getAdd());
            }
            else
            {
                listBean.add(newIndex, directive.getAdd());
            }
            listBean.remove(index);
        }
    }


    protected void processBeforeDirectives(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, int index)
    {
        if(hasBeforeDirectives(directive))
        {
            int lowestIndex = -1;
            lowestIndex = getLowestIndexByBean(directive, listBean, directiveBeanName, lowestIndex);
            lowestIndex = getLowestIndexByClass(directive, listBean, index, lowestIndex);
            lowestIndex = getLowestIndexByValue(directive, listBean, lowestIndex);
            setBeforePostion(directive, listBean, directiveBeanName, index, lowestIndex);
        }
    }


    private int getLowestIndexByBean(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, int lowestIndex)
    {
        for(String beanName : getList(directive.getBeforeBeanNames()))
        {
            Supplier<String> errorSupplier = () -> "list merge directive[" + directiveBeanName + "] beforeBeanName [" + beanName + "] does not exist";
            Object bean = getBean(beanName, errorSupplier);
            lowestIndex = getIndex(listBean, lowestIndex, bean, this::isLowest);
        }
        return lowestIndex;
    }


    private int getLowestIndexByClass(ListMergeDirective directive, List<Object> listBean, int index, int lowestIndex)
    {
        List<Class<?>> beforeClasses = getList(directive.getBeforeClasses());
        for(int i = 0; i < listBean.size() && i < index && (lowestIndex < 0 || i < lowestIndex); i++)
        {
            for(Class<?> clazz : beforeClasses)
            {
                Object element = listBean.get(i);
                if(element != null && clazz.isAssignableFrom(element.getClass()))
                {
                    return i;
                }
            }
        }
        return lowestIndex;
    }


    private int getLowestIndexByValue(ListMergeDirective directive, List<Object> listBean, int lowestIndex)
    {
        for(Object value : getList(directive.getBeforeValues()))
        {
            lowestIndex = getIndex(listBean, lowestIndex, value, this::isLowest);
        }
        return lowestIndex;
    }


    private void setBeforePostion(ListMergeDirective directive, List<Object> listBean, String directiveBeanName, int index, int lowestIndex)
    {
        if(lowestIndex < index)
        {
            int newIndex = lowestIndex;
            Preconditions.checkArgument((listBean.remove(index) == directive.getAdd()), "Software Error Directive [" + directiveBeanName + "]. Tried to remove wrong bean ");
            if(newIndex < 0)
            {
                listBean.add(0, directive.getAdd());
            }
            else
            {
                listBean.add(newIndex, directive.getAdd());
            }
        }
    }


    protected boolean requiresSorting(ListMergeDirective directive)
    {
        return (hasAfterDirectives(directive) || hasBeforeDirectives(directive));
    }


    protected boolean hasAfterDirectives(ListMergeDirective directive)
    {
        return (directive.getAfterBeanNames() != null || directive.getAfterClasses() != null || directive.getAfterValues() != null);
    }


    protected boolean hasBeforeDirectives(ListMergeDirective directive)
    {
        return (directive.getBeforeBeanNames() != null || directive.getBeforeClasses() != null || directive
                        .getBeforeValues() != null);
    }


    private int getIndex(List<Object> list, int oldIndex, Object item, BiIntPredicate predicate)
    {
        if(item == null)
        {
            return oldIndex;
        }
        int result = oldIndex;
        int newIndex = list.indexOf(item);
        if(predicate.test(newIndex, oldIndex))
        {
            result = newIndex;
        }
        return result;
    }


    private Object getBean(String beanName, Supplier<String> errorSupplier)
    {
        try
        {
            return this.beanFactory.getBean(beanName);
        }
        catch(NoSuchBeanDefinitionException e)
        {
            LOG.warn(errorSupplier.get());
            return null;
        }
    }


    private <T> List<T> getList(List<T> list)
    {
        return (list == null) ? Collections.EMPTY_LIST : list;
    }


    private boolean isHighest(int currentIndex, int highestndex)
    {
        return (currentIndex > highestndex);
    }


    private boolean isLowest(int currentIndex, int lowestIndex)
    {
        return (currentIndex >= 0 && (lowestIndex == -1 || currentIndex < lowestIndex));
    }
}
