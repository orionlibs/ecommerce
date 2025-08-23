/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.admin.BreadboardSnippetService;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.breadboard.BreadboardSnippet;
import com.hybris.cockpitng.components.Codeeditor;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Panel;
import org.zkoss.zul.SimpleListModel;

public class InputTestController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(InputTestController.class);
    private static final String DOTS = " ...";
    private static final int HISTORY_STRING_LENGTH_MAX = 35;
    private static final int HISTORY_STRING_LENGTH_THRESHOLD = 30;
    private static final int HISTORY_MAX_SIZE = 5;
    private static final int INITIAL_TEXT_AREA_HEIGHT = 300;
    private static final long serialVersionUID = 1L;
    private static final String TEXT_AREA_HEIGHT = "textAreaHeight";
    private static final String DEFAULT_SCRIPT = "defaultScript";
    protected static final String SEND_BUTTON = "sendButton";
    protected static final String GROOVY_CONSOLE = "groovyConsole";
    protected static final String HISTORY = "historyBox";
    protected static final String SOCKET_OUT_COLLECTION_OUTPUT = "collectionoutput";
    private transient BreadboardSnippetService breadboardSnippetService;
    // groovyConsole variable should not be changed because changing it breaks BreadBoard feature
    // it should be subject of further improvements in the future so we won't rely on that name
    private Codeeditor groovyConsole;
    private Listbox historyBox;
    private transient GroovyShell shell;
    private Menupopup snippetMenu;
    private Label consoleOutput;
    private Panel outputConsoleBox;


    @Override
    public void preInitialize(final Component comp)
    {
        initWidgetSetting(DEFAULT_SCRIPT);
        initWidgetSetting(TEXT_AREA_HEIGHT, INITIAL_TEXT_AREA_HEIGHT);
        final String script = getModel().getValue(GROOVY_CONSOLE, String.class);
        if(script == null)
        {
            getModel().setValue(GROOVY_CONSOLE, getWidgetSettings().getString(DEFAULT_SCRIPT));
        }
    }


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final Map<String, List<BreadboardSnippet>> map = createSnippetMap();
        for(final Map.Entry<String, List<BreadboardSnippet>> entry : map.entrySet())
        {
            final Menu categoryMenu = new Menu();
            categoryMenu.setLabel(entry.getKey());
            final Menupopup menupopup = new Menupopup();
            for(final BreadboardSnippet snippetObject : entry.getValue())
            {
                final Menuitem snippetMenuItem = new Menuitem();
                snippetMenuItem.setValue(snippetObject.getCodeSnippet());
                snippetMenuItem.addEventListener(Events.ON_CLICK, event2 -> setGroovyConsoleValue(snippetObject.getCodeSnippet()));
                snippetMenuItem.setLabel(snippetObject.getLabel());
                snippetMenuItem.setTooltiptext(snippetObject.getDescription());
                snippetMenuItem.setParent(menupopup);
            }
            menupopup.setParent(categoryMenu);
            categoryMenu.setParent(snippetMenu);
        }
        historyBox.setModel(new SimpleListModel<>(getHistoryDisplayStrings()));
    }


    @ViewEvent(componentID = GROOVY_CONSOLE, eventName = Events.ON_CHANGE)
    public void groovyConsoleChanged()
    {
        getModel().setValue(GROOVY_CONSOLE, groovyConsole.getValue());
    }


    public GroovyShell generateShell()
    {
        final Binding binding = new Binding();
        final Map<String, Object> beans = new HashMap<String, Object>()
        {
            private static final long serialVersionUID = 1L;


            @Override
            public Object get(final Object key)
            {
                return SpringUtil.getBean(String.valueOf(key));
            }
        };
        binding.setVariable("springBeans", beans);
        final ApplicationContext appCtx = SpringUtil.getApplicationContext();
        binding.setVariable("ctx", appCtx);
        shell = new GroovyShell(ClassLoaderUtils.getCurrentClassLoader(this.getClass()), binding);
        return shell;
    }


    @ViewEvent(componentID = SEND_BUTTON, eventName = Events.ON_CLICK)
    public void buttonClicked()
    {
        shell = generateShell();
        try
        {
            if(StringUtils.isNotBlank(groovyConsole.getValue()))
            {
                final Object value = shell.evaluate(groovyConsole.getValue());
                if(value instanceof List)
                {
                    sendOutput("listoutput", value);
                    sendOutput(SOCKET_OUT_COLLECTION_OUTPUT, value);
                }
                else if(value instanceof Set)
                {
                    sendOutput("setoutput", value);
                    sendOutput(SOCKET_OUT_COLLECTION_OUTPUT, value);
                }
                else if(value instanceof Collection)
                {
                    sendOutput(SOCKET_OUT_COLLECTION_OUTPUT, value);
                }
                else
                {
                    sendOutput("singleoutput", value);
                }
                if(checkDoubleEntries(groovyConsole.getValue()))
                {
                    correctHistoryList(groovyConsole.getValue());
                    getHistory().add(groovyConsole.getValue());
                }
                else
                {
                    getHistory().add(groovyConsole.getValue());
                }
                if(getHistory().size() > HISTORY_MAX_SIZE)
                {
                    getHistory().removeFirst();
                }
                historyBox.setModel(new SimpleListModel<>(getHistoryDisplayStrings()));
                consoleOutput.setValue(String.valueOf(value));
            }
        }
        catch(final CompilationFailedException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred", e);
            }
            outputConsoleBox.setOpen(true);
            consoleOutput.setValue(getLabel("evaluationerror", new Object[]
                            {e.toString()}));
        }
    }


    /**
     * Check the if there are double entries
     */
    private boolean checkDoubleEntries(final String str)
    {
        boolean doubles = false;
        final List<String> history = getHistory();
        for(final String st : history)
        {
            if(st.equals(str))
            {
                doubles = true;
            }
        }
        return doubles;
    }


    /**
     * change the row of the historyList if there is an older or newer selected one
     **/
    private List<String> correctHistoryList(final String str)
    {
        final List<String> ret = new ArrayList<>();
        final List<String> history = getHistory();
        for(final String string : history)
        {
            if(str.equals(string))
            {
                ret.add(string);
                history.remove(string);
                break;
            }
            else
            {
                ret.add(string);
            }
        }
        return ret;
    }


    LinkedList<String> getHistory()
    {
        Object attribute = Sessions.getCurrent().getAttribute("historyList");
        if(attribute == null)
        {
            attribute = new LinkedList<String>();
            Sessions.getCurrent().setAttribute("historyList", attribute);
        }
        return (LinkedList<String>)attribute;
    }


    private List<String> getHistoryDisplayStrings()
    {
        final List<String> ret = new ArrayList<>();
        final List<String> history = getHistory();
        for(final String string : history)
        {
            if(string.length() >= HISTORY_STRING_LENGTH_MAX)
            {
                ret.add(string.substring(0, HISTORY_STRING_LENGTH_THRESHOLD) + DOTS);
            }
            else
            {
                ret.add(string);
            }
        }
        return ret;
    }


    /**
     * create a Map included all Snippets
     */
    private Map<String, List<BreadboardSnippet>> createSnippetMap()
    {
        final List<BreadboardSnippet> snippetList = buildSnippetList();
        final Map<String, List<BreadboardSnippet>> snippetMap = new HashMap<>();
        for(final BreadboardSnippet snipp : snippetList)
        {
            if(snippetMap.containsKey(snipp.getCategory()))
            {
                final List<BreadboardSnippet> groupSnippet = snippetMap.get(snipp.getCategory());
                groupSnippet.add(snipp);
            }
            else
            {
                final List<BreadboardSnippet> groupSnippet = new ArrayList<>();
                groupSnippet.add(snipp);
                snippetMap.put(snipp.getCategory(), groupSnippet);
            }
        }
        return snippetMap;
    }


    private void setGroovyConsoleValue(final String value)
    {
        groovyConsole.setValue(value);
        getModel().setValue(GROOVY_CONSOLE, value);
    }


    /**
     * Returns the selected Snippet from the historyBox
     **/
    @ViewEvent(componentID = HISTORY, eventName = Events.ON_SELECT)
    public void showHistorySnippet()
    {
        final int selectedItemIndex = historyBox.getSelectedIndex();
        setGroovyConsoleValue(getHistory().get(selectedItemIndex));
    }


    public List<BreadboardSnippet> buildSnippetList()
    {
        return breadboardSnippetService == null ? Collections.<BreadboardSnippet>emptyList()
                        : breadboardSnippetService.getSnippets();
    }


    public Panel getOutputConsoleBox()
    {
        return outputConsoleBox;
    }


    public Label getConsoleOutput()
    {
        return consoleOutput;
    }


    public Menupopup getSnippetMenu()
    {
        return snippetMenu;
    }


    public GroovyShell getShell()
    {
        return shell;
    }


    public Listbox getHistoryBox()
    {
        return historyBox;
    }


    public Codeeditor getGroovyConsole()
    {
        return groovyConsole;
    }


    public BreadboardSnippetService getBreadboardSnippetService()
    {
        return breadboardSnippetService;
    }
}
