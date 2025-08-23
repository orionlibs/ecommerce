package de.hybris.platform.cockpit.util;

import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface ComponentInjector
{
    public static final String CODE = "code";
    public static final String VALUE = "value";
    public static final String ATTRIBUTES = "attrs";


    void injectComponent(HtmlBasedComponent paramHtmlBasedComponent, Map<String, ? extends Object> paramMap);
}
