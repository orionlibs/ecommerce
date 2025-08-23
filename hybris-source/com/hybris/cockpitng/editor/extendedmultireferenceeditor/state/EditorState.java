/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.state;

import com.hybris.cockpitng.core.model.Observable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.util.DesktopAware;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Keeps a state of {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor}
 * .
 */
public class EditorState<T> implements DesktopAware
{
    private static final Pattern PATH_PATTERN = Pattern.compile("(.*)\\[([0-9]+)\\](\\..*)?");
    private ListheaderSortState listheaderSortState;
    private final String path;
    private final Map<T, RowState> rowToStateMap = new HashMap<>();
    private ValidationObservable validationObservable;


    public EditorState(final String path)
    {
        this.path = path;
    }


    /**
     * Parses full attribute path for i.e. editor or validation violation and extracts index of row which matches the path.
     *
     * @param path
     *           full attribute path
     * @return index of row or <code>-1</code>, if path does not match
     */
    public static int getRowIndex(final String path)
    {
        final Matcher matcher = PATH_PATTERN.matcher(path);
        if(matcher.find())
        {
            final String rowIndex = matcher.group(2);
            return Integer.parseInt(rowIndex);
        }
        else
        {
            return -1;
        }
    }


    /**
     * Parses full attribute path for i.e. editor or validation violation and extracts a path relative to single row object
     * from it.
     *
     * @param path
     *           full attribute path
     * @return row object path, empty string if a path point exactly to object or <code>null</code> if path does not match
     *         row object path
     */
    public static String getRowPath(final String path)
    {
        final Matcher matcher = PATH_PATTERN.matcher(path);
        if(matcher.find())
        {
            String result = matcher.group(3);
            if(result != null && result.startsWith("."))
            {
                result = result.substring(1);
            }
            else if(result == null)
            {
                result = StringUtils.EMPTY;
            }
            return result;
        }
        else
        {
            return null;
        }
    }


    /**
     * Parses full attribute path for i.e. editor or validation violation and extracts a path that points to property
     * containing array value.
     *
     * @param path
     *           full attribute path
     * @return array path or <code>null</code> if path does not match row object path
     */
    public static String getArrayPath(final String path)
    {
        final Matcher matcher = PATH_PATTERN.matcher(path);
        if(matcher.find())
        {
            return matcher.group(1);
        }
        else
        {
            return null;
        }
    }


    public void setListheaderSortState(final ListheaderSortState listheaderSortState)
    {
        this.listheaderSortState = listheaderSortState;
    }


    public Optional<ListheaderSortState> getListheaderSortState()
    {
        return Optional.ofNullable(listheaderSortState);
    }


    /**
     * Removes a row bound to provided row object
     *
     * @param row
     *           row object
     */
    public void removeRowState(final T row)
    {
        if(rowToStateMap.containsKey(row))
        {
            getObservable().observers.values()
                            .forEach(proxies -> proxies.removeIf(observerProxy -> Objects.equals(observerProxy.entry, row)));
            rowToStateMap.remove(row);
        }
    }


    /**
     * Resets all row states
     */
    public void removeAllRowStates()
    {
        rowToStateMap.clear();
    }


    /**
     * Adds new row state bound to provided row object. If editor's state already contains a row for this object, then
     * nothing is changed and it's state is returned.
     *
     * @param row
     *           row object to be added
     * @return state of row bound to provided row object
     */
    public RowState<T> addRowState(final T row)
    {
        RowState<T> result = getRowState(row);
        if(result == null)
        {
            result = new RowState<>(rowToStateMap.size(), row);
            rowToStateMap.put(row, result);
            addValueObserver(result);
        }
        return result;
    }


    public void refreshRowStateReference(final T row)
    {
        final RowState<T> oldRowState = getRowState(row);
        if(oldRowState != null)
        {
            final RowState<T> refreshedRowState = new RowState<>(oldRowState.getRowIndex(), row);
            oldRowState.getModifiedProperties().forEach(refreshedRowState::setPropertyModified);
            refreshedRowState.getValidationResult().setValidationInfo(oldRowState.getValidationResult().getAll());
            removeRowState(row);
            rowToStateMap.put(row, refreshedRowState);
            addValueObserver(refreshedRowState);
        }
    }


    private void addValueObserver(final RowState<T> result)
    {
        final int rowIndex = result.getRowIndex();
        final T row = result.getRow();
        getObservable().observers.forEach((observer, proxies) -> {
            final ValueObserverProxy proxy = new ValueObserverProxy(rowIndex, observer, row);
            final String rowPath = getRowPath(rowIndex);
            rowToStateMap.get(row).getValidationResult().addObserver(rowPath, proxy);
            proxies.add(proxy);
        });
    }


    /**
     * Gets a state of row bound to provided object.
     *
     * @param row
     *           row object
     * @return state of row or <code>null</code> if no row is bound to provided object
     */
    public RowState<T> getRowState(final T row)
    {
        return rowToStateMap.get(row);
    }


    /**
     * Gets index of row bound to provided object
     *
     * @param row
     *           row object
     * @return index of row or <code>-1</code> if no row is bound to provided object
     */
    public int getRowIndex(final T row)
    {
        final RowState<T> rowState = getRowState(row);
        return rowState != null ? rowState.getRowIndex() : -1;
    }


    /**
     * Gets a path to specified row, relative to current editor.
     *
     * @param rowIndex
     *           index of row
     * @return path to object bound to specified row
     */
    public String getRowPath(final int rowIndex)
    {
        return String.format("%s[%d]", path, rowIndex);
    }


    /**
     * Sets new validation violations of specified row
     *
     * @param row
     *           row object
     * @param validationInfos
     *           new violations
     * @return validation result of row after modification
     */
    public ValidationResult updateValidationResult(final T row, final List<ValidationInfo> validationInfos)
    {
        final RowState rowState = getRowState(row);
        final ValidationResult rowValidationResult = rowState.getValidationResult();
        final String rowPath = getRowPath(rowState.getRowIndex());
        rowValidationResult.setValidationInfo(rowPath, validationInfos);
        return rowValidationResult;
    }


    /**
     * Creates new instance of {@link ValidationResult} that contains validation violations for all rows. Violations for
     * each row is available under prefix equal to its index.
     *
     * @return collected validation results for all rows
     */
    public ValidationResult collectValidationResult()
    {
        final ValidationResult result = new ValidationResult();
        rowToStateMap.forEach((row, rowState) -> result.updateValidationInfo(getRowPath(rowState.getRowIndex()),
                        rowState.getValidationResult().getAll()));
        return result;
    }


    /**
     * Gets validation result of specified row
     *
     * @param row
     *           row object
     * @return validation result of row bound to provided object or {@link ValidationResult#EMPTY} if no row bound to object
     */
    public ValidationResult getValidationResult(final T row)
    {
        final RowState rowState = getRowState(row);
        return rowState != null ? rowState.getValidationResult() : ValidationResult.EMPTY;
    }


    /**
     * Notifies all validation observers about changes in violations.
     *
     * @param path
     *           path relative to current editor
     * @see #getRowPath(int)
     */
    public void validationChanged(final String path)
    {
        if(StringUtils.isNotBlank(path))
        {
            getValidationObservable().changed(path);
        }
        else
        {
            getValidationObservable().changed();
        }
    }


    protected synchronized ValidationObservable getObservable()
    {
        if(validationObservable == null)
        {
            validationObservable = new ValidationObservable();
        }
        return validationObservable;
    }


    public Observable getValidationObservable()
    {
        return getObservable();
    }


    /**
     * Gets states of all rows defined for editor
     *
     * @return states of all rows
     */
    public Collection<RowState> getRowStates()
    {
        return rowToStateMap.values();
    }


    /**
     * Gets states of all rows with object bound to them
     *
     * @return entries representing a row object with its state
     */
    public Collection<Map.Entry<T, RowState>> getEntries()
    {
        return rowToStateMap.entrySet();
    }


    /**
     * Gets an object bound to row on specified index
     *
     * @param index
     *           row index
     * @return object bound to row or <code>null</code> if no row is defined on specified index
     */
    public T getRow(final int index)
    {
        final Optional<Map.Entry<T, RowState>> rowEntry = getEntries().stream()
                        .filter(entry -> entry.getValue().getRowIndex() == index).findFirst();
        return rowEntry.isPresent() ? rowEntry.get().getKey() : null;
    }


    private class ValueObserverProxy implements ValueObserver
    {
        private final int rowIndex;
        private final ValueObserver observer;
        private final T entry;


        public ValueObserverProxy(final int rowIndex, final ValueObserver observer, final T entry)
        {
            this.rowIndex = rowIndex;
            this.observer = observer;
            this.entry = entry;
        }


        @Override
        public void modelChanged()
        {
            observer.modelChanged(getRowPath(rowIndex));
        }


        @Override
        public void modelChanged(final String property)
        {
            observer.modelChanged(property);
        }
    }


    private class ValidationObservable implements Observable
    {
        private final Map<ValueObserver, List<ValueObserverProxy>> observers = new LinkedHashMap<>();


        @Override
        public void changed()
        {
            observers.keySet().forEach(observer -> observer.modelChanged(path));
        }


        @Override
        public void changed(final String key)
        {
            observers.keySet().forEach(observer -> observer.modelChanged(key));
        }


        @Override
        public void addObserver(final String key, final ValueObserver observer)
        {
            final int rowIndex = getRowIndex(key);
            final List<ValueObserverProxy> proxies = new ArrayList<>();
            if(StringUtils.isEmpty(key))
            {
                rowToStateMap.values().forEach(state -> {
                    final ValueObserverProxy proxy = new ValueObserverProxy(state.getRowIndex(), observer, (T)state.getRow());
                    final String rowPath = getRowPath(state.getRowIndex());
                    state.getValidationResult().addObserver(rowPath, proxy);
                    proxies.add(proxy);
                });
            }
            else if(rowIndex > -1)
            {
                final ValueObserverProxy proxy = new ValueObserverProxy(rowIndex, observer, getRow(rowIndex));
                getRowState(getRow(rowIndex)).getValidationResult().addObserver(key, proxy);
                proxies.add(proxy);
            }
            observers.put(observer, proxies);
        }


        @Override
        public void removeObserver(final String key, final ValueObserver observer)
        {
            removeObserver(observer);
        }


        @Override
        public void removeObserver(final ValueObserver observer)
        {
            final List<ValueObserverProxy> proxies = observers.remove(observer);
            if(proxies != null)
            {
                proxies.forEach(proxy -> {
                    final RowState rowState = getRowState(getRow(proxy.rowIndex));
                    if(rowState != null)
                    {
                        rowState.getValidationResult().removeObserver(proxy);
                    }
                });
                proxies.clear();
            }
        }
    }


    @Override
    public void afterDesktopChanged()
    {
        getObservable().observers.clear();
        rowToStateMap.forEach((row, rowState) -> {
            if(rowState != null)
            {
                rowState.afterDesktopChanged();
            }
        });
    }
}
