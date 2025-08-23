/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.context;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTreeCollectionContextStrategy implements CockpitConfigurationContextStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTreeCollectionContextStrategy.class);
    public static final String ROOT_CONTEXT = "_root_";
    private TypeFacade typeFacade;


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    /**
     * context is in the following form: Type1.att1.attr2.attrN
     * parent context will be:
     * <ul>
     * <li>SuperType1.attr1.attr2.attrN, if SuperType1 is super type of Type1</li>
     * <li>Attr1Type.attr2.attrN where Attr1Type is type of attr1</li>
     * <li>_root_ otherwise</li>
     * </ul>
     *
     *
     * @param context
     * @return
     */
    @Override
    public List<String> getParentContexts(final String context)
    {
        final ContextInfoAggregator info = new ContextInfoAggregator(context);
        if(!info.isValid())
        {
            LOG.error("Provided invalid context value {}", context);
            return Collections.singletonList(ROOT_CONTEXT);
        }
        if(info.isRoot())
        {
            return Collections.emptyList();
        }
        try
        {
            final DataType enclosingType = typeFacade.load(info.getEnclosingType());
            final String superTypeOfEnclosingType = enclosingType.getSuperType();
            if(superTypeOfEnclosingType != null)
            {
                final DataType superType = typeFacade.load(superTypeOfEnclosingType);
                final DataAttribute firstAttribute = superType.getAttribute(info.getAttributesChain().get(0));
                if(firstAttribute != null)
                {
                    return Collections.singletonList(String.format("%s.%s", superTypeOfEnclosingType,
                                    info.getAttributesChainAsString()));
                }
            }
            if(info.isNestedAttribute())
            {
                final DataAttribute firstAttribute = enclosingType.getAttribute(info.getAttributesChain().get(0));
                if(firstAttribute != null)
                {
                    final String newEnclosingType = firstAttribute.getValueType().getCode();
                    final List<String> newAttributesChain = new ArrayList<>(info.getAttributesChain());
                    newAttributesChain.remove(0);
                    return Collections.singletonList(String.format("%s.%s", newEnclosingType,
                                    StringUtils.join(newAttributesChain, '.')));
                }
                else
                {
                    return Collections.singletonList(ROOT_CONTEXT);
                }
            }
            else
            {
                return Collections.singletonList(ROOT_CONTEXT);
            }
        }
        catch(final TypeNotFoundException typeNotFound)
        {
            LOG.error("Provided invalid context value " + context, typeNotFound);
            return Collections.singletonList(ROOT_CONTEXT);
        }
    }


    @Override
    public boolean valueMatches(final String contextValue, final String value)
    {
        return StringUtils.equals(contextValue, value);
    }


    private static class ContextInfoAggregator
    {
        private static final Pattern pattern = Pattern.compile("(?<type>\\w+)\\.(?<intermediateAttr>(\\w+\\.)*+)(?<lastAttr>\\w+)");
        private final String enclosingType;
        private final List<String> attributesChain;
        private final boolean isRoot;
        private final boolean valid;


        public String getEnclosingType()
        {
            return enclosingType;
        }


        public List<String> getAttributesChain()
        {
            return attributesChain;
        }


        public String getAttributesChainAsString()
        {
            return StringUtils.join(attributesChain, '.');
        }


        public boolean isNestedAttribute()
        {
            return attributesChain.size() > 1;
        }


        public boolean isRoot()
        {
            return isRoot;
        }


        public boolean isValid()
        {
            return valid;
        }


        private ContextInfoAggregator(final String fullContext)
        {
            if(ROOT_CONTEXT.equals(fullContext))
            {
                isRoot = true;
                valid = true;
                enclosingType = null;
                attributesChain = Collections.emptyList();
            }
            else
            {
                isRoot = false;
                final Matcher matcher = pattern.matcher(fullContext);
                if(matcher.matches())
                {
                    valid = true;
                    enclosingType = matcher.group("type");
                    final String lastAttr = matcher.group("lastAttr");
                    String intermediateAttr = matcher.group("intermediateAttr");
                    if(StringUtils.isNotEmpty(intermediateAttr))
                    {
                        intermediateAttr = intermediateAttr.substring(0, intermediateAttr.length() - 1);
                        attributesChain = new ArrayList<>(Arrays.asList(intermediateAttr.split("\\.")));
                    }
                    else
                    {
                        attributesChain = new ArrayList<>();
                    }
                    attributesChain.add(lastAttr);
                }
                else
                {
                    valid = false;
                    enclosingType = null;
                    attributesChain = Collections.emptyList();
                }
            }
        }
    }
}
