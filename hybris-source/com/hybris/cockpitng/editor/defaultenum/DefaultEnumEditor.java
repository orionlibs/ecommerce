/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultenum;

import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListSubModel;
import org.zkoss.zul.SimpleListModel;

/**
 * Editor dealing with enumeration values.
 */
public class DefaultEnumEditor extends AbstractCockpitEditorRenderer<Object>
{
    public static final int ENTER_CODE_KEY = 13;
    public static final String PARAM_L10N_KEY_NULL = "nullValueLabel";
    public static final String PARAM_L10N_KEY_DEFAULT_FALLBACK_NULL = "enum.editor.null";
    protected static final Pattern PATTERN_ENUM = Pattern.compile("java\\.lang\\.Enum(?:\\((.*)\\))?");
    private static final Object OPTIONAL_OBJECT = new Object();
    private static final String EDITOR_PARAM_RESOLVER = "valueResolver";
    private static final String EDITOR_PARAM_OPTIONAL = "isOptional";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEnumEditor.class);
    @Resource
    protected EnumValueResolver enumValueResolver;
    @Resource
    protected LabelService labelService;
    @Resource
    protected CockpitLocaleService cockpitLocaleService;
    @Resource
    protected CockpitProperties cockpitProperties;


    private static void logWarning(final String enumType)
    {
        if(LOG.isWarnEnabled())
        {
            LOG.warn(String.format("%s is not an enumeratation! Cannot retrieve values.", enumType));
        }
    }


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Combobox box = new Combobox();
        parent.appendChild(box);
        final Object initialValue = context.getInitialValue();
        List<Object> initialValues = new ArrayList<>();
        List<Object> allValues = getAllValues(context, initialValue);
        if(initialValue != null)
        {
            initialValues.add(initialValue);
        }
        if(isOptional(context))
        {
            initialValues = ListUtils.union(List.of(OPTIONAL_OBJECT), initialValues);
        }
        final FilteredListModelList<Object> model = new FilteredListModelList<>(initialValues, context);
        if(initialValue != null)
        {
            if(initialValue instanceof Collection)
            {
                model.setSelection((Collection<?>)initialValue);
            }
            else
            {
                model.setSelection(Collections.singletonList(initialValue));
            }
        }
        box.setModel(model);
        box.setAutodrop(true);
        box.setItemRenderer((item, data, index) -> {
            if(data == OPTIONAL_OBJECT)
            {
                item.setSclass("ye-enum-editor-null-element");
                item.setValue(null);
            }
            else
            {
                item.setValue(data);
            }
            item.setLabel(mapEnumToString(data, context));
        });
        final RestoreContext restoreContext = new RestoreContext();
        box.addEventListener(Events.ON_OPEN,
                        new ComboboxOpenEventListener(listener, box, restoreContext, context, this, allValues));
        box.addEventListener(Events.ON_BLUR, new ComboboxBlurEventListener(listener, box, restoreContext, context, this));
        box.addEventListener(Events.ON_OK, new ComboboxOkEventListener(listener, box, restoreContext, context, this));
        box.addEventListener(Events.ON_CHANGE, new ComboboxChangeEventListener(listener, box, restoreContext, context, this));
        box.setDisabled(!context.isEditable());
    }


    protected String mapEnumToString(final Object value, final EditorContext<Object> context)
    {
        if(value == OPTIONAL_OBJECT)
        {
            return StringUtils.defaultIfBlank(getL10nDecorator(context, PARAM_L10N_KEY_NULL, PARAM_L10N_KEY_DEFAULT_FALLBACK_NULL),
                            "-");
        }
        else
        {
            String label = labelService.getObjectLabel(value);
            if(StringUtils.isBlank(label))
            {
                label = String.valueOf(value);
            }
            return label;
        }
    }


    /**
     * @deprecated since 2005, use {@link #getAllValues(EditorContext, Object)} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected List<Object> getAllValues(final String valueType, final Object initialValue)
    {
        return Collections.emptyList();
    }


    protected List<Object> getAllValues(final EditorContext<Object> context, final Object initialValue)
    {
        final String valueType = context.getValueType();
        final EnumValueResolver resolver = getEnumValueResolver(context);
        if(resolver != null)
        {
            final List<Object> allValues = resolver.getAllValues(valueType, initialValue);
            if(CollectionUtils.isNotEmpty(allValues))
            {
                return allValues;
            }
        }
        final List<Object> values;
        if(initialValue instanceof Enum<?>)
        {
            values = Arrays.asList(((Enum<?>)initialValue).getDeclaringClass().getEnumConstants());
        }
        else if(valueType != null)
        {
            values = getEnumValues(valueType);
        }
        else
        {
            values = Collections.emptyList();
        }
        return Stream.concat(values.stream(), getAllValues(valueType, initialValue).stream()).distinct()
                        .collect(Collectors.toList());
    }


    private List<Object> getEnumValues(final String valueType)
    {
        try
        {
            final Matcher enumMatcher = PATTERN_ENUM.matcher(valueType);
            if(enumMatcher.matches() && enumMatcher.groupCount() == 1 && enumMatcher.group(1) != null)
            {
                final String enumType = enumMatcher.group(1);
                final Class<?> enumClass = Class.forName(enumType, true, getClass().getClassLoader());
                if(!enumClass.isEnum())
                {
                    logWarning(enumType);
                    return Collections.emptyList();
                }
                return Arrays.asList(enumClass.getEnumConstants());
            }
        }
        catch(final ClassNotFoundException e)
        {
            LOG.debug(e.getMessage(), e);
        }
        return Collections.emptyList();
    }


    protected EnumValueResolver getEnumValueResolver(final EditorContext<Object> context)
    {
        if(context.containsParameter(EDITOR_PARAM_RESOLVER))
        {
            return (EnumValueResolver)SpringUtil.getBean(context.getParameterAs(EDITOR_PARAM_RESOLVER), EnumValueResolver.class);
        }
        return enumValueResolver;
    }


    protected boolean isOptional(final EditorContext<Object> context)
    {
        return context.getParameterAsBoolean(EDITOR_PARAM_OPTIONAL, context.isOptional());
    }


    protected Object getSelectedItemValue(final Combobox box)
    {
        final Comboitem selectedItem = box.getSelectedItem();
        if(selectedItem != null)
        {
            return selectedItem.getValue();
        }
        else
        {
            final int size = box.getItems().size();
            for(int i = 0; i < size; i++)
            {
                if(StringUtils.equals(box.getValue(), box.getItems().get(i).getLabel()))
                {
                    return box.getItems().get(i).getValue();
                }
            }
            return null;
        }
    }


    private static class RestoreContext
    {
        private Object valueOnOpen;
        private String previousSelectedValue;


        public RestoreContext()
        {
            this.valueOnOpen = new Object();
            this.previousSelectedValue = "";
        }


        public Object getValueOnOpen()
        {
            return valueOnOpen;
        }


        public void setValueOnOpen(final Object valueOnOpen)
        {
            this.valueOnOpen = valueOnOpen;
        }


        public String getPreviousSelectedValue()
        {
            return previousSelectedValue;
        }


        public void setPreviousSelectedValue(final String previousSelectedValue)
        {
            this.previousSelectedValue = previousSelectedValue;
        }
    }


    private class ComboboxListener
    {
        private EditorListener<Object> listener;
        private RestoreContext restoreContext;
        private Combobox box;
        private EditorContext<Object> context;
        private DefaultEnumEditor editor;
        private List<Object> allValues;


        public ComboboxListener(final EditorListener<Object> listener, final Combobox box, final RestoreContext restoreContext,
                        final EditorContext<Object> context, final DefaultEnumEditor editor, final List<Object> allValues)
        {
            this.listener = listener;
            this.box = box;
            this.restoreContext = restoreContext;
            this.context = context;
            this.editor = editor;
            this.allValues = allValues;
        }


        public EditorListener<Object> getListener()
        {
            return this.listener;
        }


        public Combobox getBox()
        {
            return this.box;
        }


        public DefaultEnumEditor getEditor()
        {
            return this.editor;
        }


        public RestoreContext getRestoreContext()
        {
            return this.restoreContext;
        }


        public void setRestoreContext(final RestoreContext restoreContext)
        {
            this.restoreContext = restoreContext;
        }


        public EditorContext<Object> getContext()
        {
            return this.context;
        }


        public List<Object> getAllValues()
        {
            return this.allValues;
        }


        public void setAllValues(final List<Object> allValues)
        {
            this.allValues = allValues;
        }


        protected void renderComboboxItems(final FilteredListModelList<Object> model)
        {
            box.setValue("");
            box.setModel(model);
            box.invalidate();
        }


        protected boolean isValidEnumInput()
        {
            if(this.editor.getSelectedItemValue(getBox()) == null)
            {
                final String value = getBox().getValue();
                // If the editor property "optional" is true, it means empty and null value are allowed
                final String enumEditorNullValue = StringUtils.defaultIfBlank(
                                editor.getL10nDecorator(getContext(), PARAM_L10N_KEY_NULL, PARAM_L10N_KEY_DEFAULT_FALLBACK_NULL), "-");
                return (editor.isOptional(context) && (StringUtils.isBlank(value) || StringUtils.equals(value, enumEditorNullValue)));
            }
            return true;
        }
    }


    private class ComboboxOpenEventListener extends ComboboxListener implements EventListener<OpenEvent>
    {
        private boolean alreadyOpened = false;


        public ComboboxOpenEventListener(final EditorListener<Object> listener, final Combobox box,
                        final RestoreContext restoreContext, final EditorContext<Object> context, final DefaultEnumEditor editor,
                        final List<Object> allValues)
        {
            super(listener, box, restoreContext, context, editor, allValues);
        }


        public void onEvent(final OpenEvent event)
        {
            if(!this.alreadyOpened)
            {
                if(isAutoSort(getContext()))
                {
                    final Collator localeAwareStringComparator = Collator.getInstance(cockpitLocaleService.getCurrentLocale());
                    super.getAllValues()
                                    .sort(Comparator.comparing(f -> mapEnumToString(f, getContext()), localeAwareStringComparator));
                }
                if(isOptional(getContext()))
                {
                    setAllValues(ListUtils.union(List.of(OPTIONAL_OBJECT), super.getAllValues()));
                }
                final FilteredListModelList<Object> model = new FilteredListModelList<>(super.getAllValues(), getContext());
                final Object initialValue = getContext().getInitialValue();
                final RestoreContext restoreContext = getRestoreContext();
                restoreContext.setValueOnOpen(initialValue);
                restoreContext.setPreviousSelectedValue(getBox().getValue());
                setRestoreContext(restoreContext);
                if(initialValue instanceof Collection)
                {
                    model.setSelection((Collection<?>)initialValue);
                }
                else if(initialValue != null)
                {
                    model.setSelection(Collections.singletonList(initialValue));
                }
                renderComboboxItems(model);
                getBox().setOpen(true);
                this.alreadyOpened = true;
            }
            else if(event.isOpen())
            {
                final RestoreContext restoreContext = getRestoreContext();
                restoreContext.setValueOnOpen(getEditor().getSelectedItemValue(getBox()));
                restoreContext.setPreviousSelectedValue(getBox().getValue());
                setRestoreContext(restoreContext);
            }
            else
            {
                final FilteredListModelList<Object> model = (FilteredListModelList)getBox().getModel();
                final Object selectedValue = getEditor().getSelectedItemValue(getBox());
                if(selectedValue != null)
                {
                    model.setSelection(Collections.singletonList(selectedValue));
                }
                renderComboboxItems(model);
            }
        }


        private Object getSelectedItemValue()
        {
            Object selectedValue = null;
            final Comboitem selectedItem = getBox().getSelectedItem();
            if(selectedItem != null)
            {
                selectedValue = selectedItem.getValue();
            }
            return selectedValue;
        }


        private boolean isAutoSort(final EditorContext<Object> context)
        {
            final boolean isAutoSortGlobalEnabled = cockpitProperties.getBoolean("cockpitng.defaultenumeditor.autosort", true);
            final boolean isAutoSortEditorEnabled = context.getParameterAsBoolean("autoSort", true);
            return context.getParameter("autoSort") == null ? isAutoSortGlobalEnabled : isAutoSortEditorEnabled;
        }
    }


    private class ComboboxOkEventListener extends ComboboxListener implements EventListener<KeyEvent>
    {
        public ComboboxOkEventListener(final EditorListener<Object> listener, final Combobox box,
                        final RestoreContext restoreContext, final EditorContext<Object> context, final DefaultEnumEditor editor)
        {
            super(listener, box, restoreContext, context, editor, Collections.emptyList());
        }


        @Override
        public void onEvent(final KeyEvent event)
        {
            if(event.getKeyCode() == ENTER_CODE_KEY)
            {
                final Object selectedVal = getEditor().getSelectedItemValue(getBox());
                if(ObjectUtils.notEqual(selectedVal, getRestoreContext().getValueOnOpen()))
                {
                    getListener().onValueChanged(selectedVal);
                    getListener().onEditorEvent(EditorListener.ENTER_PRESSED);
                }
            }
        }
    }


    private class ComboboxChangeEventListener extends ComboboxListener implements EventListener<InputEvent>
    {
        public ComboboxChangeEventListener(final EditorListener<Object> listener, final Combobox box,
                        final RestoreContext restoreContext, final EditorContext<Object> context, final DefaultEnumEditor editor)
        {
            super(listener, box, restoreContext, context, editor, Collections.emptyList());
        }


        @Override
        public void onEvent(final InputEvent event)
        {
            final Object selectedVal = getEditor().getSelectedItemValue(getBox());
            final boolean isSelectedObjChanged = ObjectUtils.notEqual(selectedVal, getRestoreContext().getValueOnOpen())
                            && !StringUtils.equals(String.valueOf(event.getValue()), String.valueOf(event.getPreviousValue()));
            if(isValidEnumInput() && isSelectedObjChanged)
            {
                getListener().onValueChanged(selectedVal);
            }
        }
    }


    private class ComboboxBlurEventListener extends ComboboxListener implements EventListener<Event>
    {
        public ComboboxBlurEventListener(final EditorListener<Object> listener, final Combobox box,
                        final RestoreContext restoreContext, final EditorContext<Object> context, final DefaultEnumEditor editor)
        {
            super(listener, box, restoreContext, context, editor, Collections.emptyList());
        }


        @Override
        public void onEvent(final Event event)
        {
            if(StringUtils.equals(event.getName(), Events.ON_BLUR))
            {
                revertToPreviousValidEnumValueIfNecessary();
            }
        }


        private void revertToPreviousValidEnumValueIfNecessary()
        {
            if(!isValidEnumInput())
            {
                final FilteredListModelList<Object> model = (FilteredListModelList)getBox().getModel();
                final Object valueOnOpen = getRestoreContext().getValueOnOpen();
                final String enumEditorNullValue = StringUtils.defaultIfBlank(
                                getEditor().getL10nDecorator(getContext(), PARAM_L10N_KEY_NULL, PARAM_L10N_KEY_DEFAULT_FALLBACK_NULL), "-");
                if(valueOnOpen == null && StringUtils.equals(getRestoreContext().getPreviousSelectedValue(), enumEditorNullValue))
                {
                    getBox().setValue(enumEditorNullValue);
                }
                else
                {
                    model.setSelection(Collections.singletonList(valueOnOpen));
                    renderComboboxItems(model);
                }
            }
        }
    }


    protected class FilteredListModelList<E> extends ListModelList<E> implements ListSubModel
    {
        private static final long serialVersionUID = -7363443468394544632L;
        private final SimpleListModel simpleList;
        private final transient Map<Object, String> labelsMap;


        public FilteredListModelList(final Collection<? extends E> c, final EditorContext<Object> context)
        {
            super(c);
            this.labelsMap = c.stream().collect(Collectors.toMap(Function.identity(), val -> mapEnumToString(val, context)));
            this.simpleList = new SimpleListModel(new ArrayList<>(c))
            {
                @Override
                protected boolean inSubModel(final Object key, final Object value)
                {
                    final String idx = String.valueOf(key);
                    if(idx.isEmpty())
                    {
                        return false;
                    }
                    final String valueString = labelsMap.get(value);
                    return !valueString.isEmpty() && valueString.startsWith(idx);
                }
            };
        }


        @Override
        public ListModel getSubModel(final Object value, final int nRows)
        {
            if(value instanceof String && StringUtils.isEmpty((String)value))
            {
                return this;
            }
            return simpleList.getSubModel(value, nRows);
        }


        @Override
        public boolean equals(final Object o)
        {
            return super.equals(o);
        }


        @Override
        public int hashCode()
        {
            return super.hashCode();
        }
    }
}
