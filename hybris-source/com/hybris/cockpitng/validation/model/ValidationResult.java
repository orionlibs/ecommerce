/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.impl.AbstractObservable;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.ValidationInfoFactory;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfoFactory;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithPrefix;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithoutPrefix;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResult extends AbstractObservable
{
    public static final ValidationResult EMPTY = new ValidationResult()
    {
        @Override
        protected List<ValidationInfo> createResultList(final List<ValidationInfo> init)
        {
            return Collections.unmodifiableList(init);
        }


        @Override
        protected synchronized boolean checkDirty()
        {
            return false;
        }


        @Override
        public void addObserver(final String key, final ValueObserver observer)
        {
            // not implemented
        }


        @Override
        public void removeObserver(final String key, final ValueObserver observer)
        {
            // not implemented
        }


        @Override
        public void removeObserver(final ValueObserver observer)
        {
            // not implemented
        }
    };
    private final List<ValidationInfo> result;
    private final ValidationInfoFactory infoFactory;
    private boolean dirty;


    /**
     * Creates empty validation result
     */
    public ValidationResult()
    {
        this(Collections.emptyList());
    }


    /**
     * Creates validation result with or without given prefix
     */
    public ValidationResult(final List<ValidationInfo> result, final String prefix, final boolean withPrefix)
    {
        this.infoFactory = withPrefix ? ValidationResult.this.createWithPrefixFactory(prefix)
                        : ValidationResult.this.createWithoutPrefixFactory(prefix);
        this.result = ValidationResult.this
                        .createResultList(result.stream().map(this.infoFactory::createValidationInfo).collect(Collectors.toList()));
        this.dirty = true;
    }


    /**
     * Creates validation result with root validation violations as specified
     *
     * @param result
     *           root violations
     */
    public ValidationResult(final List<ValidationInfo> result)
    {
        this(new DefaultValidationInfoFactory(), true, result);
    }


    /**
     * @param infoFactory
     *           root info factory
     * @param dirty
     *           <code>true</code> if provided violations are not sorted
     * @param result
     *           root violations
     */
    protected ValidationResult(final ValidationInfoFactory infoFactory, final boolean dirty, final List<ValidationInfo> result)
    {
        this.infoFactory = infoFactory;
        this.result = ValidationResult.this.createResultList(result);
        this.dirty = dirty;
    }


    protected List<ValidationInfo> createResultList(final List<ValidationInfo> init)
    {
        return new ArrayList<>(init);
    }


    /**
     * Checks whether container is sorted. If then, then all violations are being sorted descending by their severity.
     *
     * @return <code>true</code> if container was just sorted
     */
    protected synchronized boolean checkDirty()
    {
        if(dirty)
        {
            result.sort(ValidationInfo.COMPARATOR_SEVERITY_DESC);
            dirty = false;
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Marks container as unsorted
     */
    protected synchronized void markDirty()
    {
        dirty = true;
    }


    /**
     * Checks whether container is sorted.
     *
     * @return <code>true</code> if container need to be sorted
     * @see #checkDirty()
     */
    protected synchronized boolean isDirty()
    {
        return dirty;
    }


    /**
     * Gets number of violations contained by this container
     *
     * @return number of contained violations
     */
    public int size()
    {
        return result.size();
    }


    /**
     * Gets all validation violations that are contained by this container. Violations remain as relative to containers
     * root.
     *
     * @return all validation violations from container
     */
    public List<ValidationInfo> getAll()
    {
        checkDirty();
        return result.stream().map(infoFactory::createValidationInfo).collect(Collectors.toList());
    }


    /**
     * Gets all validation violations contained by this container that has specified severity level. Violations remain as
     * relative to containers root.
     *
     * @param severity
     *           requested severity level
     * @return search result
     */
    public ValidationResultSet get(final ValidationSeverity severity)
    {
        return new ValidationResultSet(this, infoFactory,
                        result.stream().filter(info -> info.getValidationSeverity().equals(severity)));
    }


    /**
     * Gets all validation violations contained by this container that are not confirmed. Violations remain as relative
     * to containers root.
     *
     * @return search result
     */
    public ValidationResultSet getNotConfirmed()
    {
        return new ValidationResultSet(this, infoFactory, result.stream().filter(info -> !info.isConfirmed()));
    }


    /**
     * Gets all validation violations contained by this container that are confirmed. Violations remain as relative to
     * containers root.
     *
     * @return search result
     */
    public ValidationResultSet getConfirmed()
    {
        return new ValidationResultSet(this, infoFactory, result.stream().filter(ValidationInfo::isConfirmed));
    }


    /**
     * Gets all validation violations contained by this container that has specified severity level are not confirmed.
     * Violations remain as relative to containers root.
     *
     * @param severity
     *           requested severity level
     * @return search result
     */
    public ValidationResultSet getNotConfirmed(final ValidationSeverity severity)
    {
        return new ValidationResultSet(this, infoFactory,
                        result.stream().filter(info -> !info.isConfirmed() && info.getValidationSeverity().equals(severity)));
    }


    /**
     * Checks whether provided paths matches
     *
     * @param expected
     *           path that is expected
     * @param actual
     *           path found in violation
     * @return <code>true</code> if violation path is equal to expected or it's subpath
     */
    protected boolean pathsMatches(final String expected, final String actual)
    {
        return ObjectValuePath.parse(actual).startsWith(expected);
    }


    protected ValidationInfoFactory createWithPrefixFactory(final String prefix)
    {
        return new ValidationInfoFactoryWithPrefix(prefix);
    }


    protected ValidationInfoFactory createWithoutPrefixFactory(final String prefix)
    {
        return new ValidationInfoFactoryWithoutPrefix(prefix);
    }


    protected Stream<ValidationInfo> getStream(final String path)
    {
        if(ObjectValuePath.isLocalizedPath(path))
        {
            return result.stream().filter(info -> pathsMatches(path, info.getInvalidPropertyPath()));
        }
        else
        {
            return result.stream().filter(info -> {
                if(info.getInvalidPropertyPath() != null && pathsMatches(path, info.getInvalidPropertyPath()))
                {
                    return true;
                }
                else if(info.getInvalidPropertyPath() != null)
                {
                    final ObjectValuePath valuePath = ObjectValuePath.parse(info.getInvalidPropertyPath());
                    return valuePath.isLocalized() && pathsMatches(path, valuePath.internationalize().buildPath());
                }
                else
                {
                    return false;
                }
            });
        }
    }


    /**
     * Gets all validation violations contained by this container that are related to specified prefix or it's subpaths.
     * Violations returned are relative to provided prefix.
     *
     * @param prefix
     *           violations path
     * @return search result
     */
    public ValidationResultSet get(final String prefix)
    {
        return new ValidationResultSet(this, createWithoutPrefixFactory(prefix), getStream(prefix));
    }


    /**
     * Gets all validation violations contained by this container that are related to specified path or it's subpaths.
     * Violations remain as relative to containers root.
     *
     * @param path
     *           violations path
     * @return search result
     */
    public ValidationResultSet find(final String path)
    {
        return new ValidationResultSet(this, infoFactory, getStream(path));
    }


    protected Stream<ValidationInfo> getNotConfirmedStream(final String path)
    {
        if(ObjectValuePath.isLocalizedPath(path))
        {
            return result.stream().filter(info -> !info.isConfirmed() && info.getInvalidPropertyPath() != null
                            && pathsMatches(path, info.getInvalidPropertyPath()));
        }
        else
        {
            return result.stream().filter(info -> {
                if(info.getInvalidPropertyPath() != null && pathsMatches(path, info.getInvalidPropertyPath()))
                {
                    return !info.isConfirmed();
                }
                else if(!info.isConfirmed() && info.getInvalidPropertyPath() != null)
                {
                    final ObjectValuePath valuePath = ObjectValuePath.parse(info.getInvalidPropertyPath());
                    return valuePath.isLocalized() && pathsMatches(path, valuePath.internationalize().buildPath());
                }
                else
                {
                    return false;
                }
            });
        }
    }


    /**
     * Gets all validation violations contained by this container that are related to specified path or it's subpaths and
     * are not confirmed. Violations returned are relative to provided path.
     *
     * @param path
     *           violations path
     * @return search result
     */
    public ValidationResultSet getNotConfirmed(final String path)
    {
        return new ValidationResultSet(this, createWithoutPrefixFactory(path), getNotConfirmedStream(path));
    }


    /**
     * Gets all validation violations contained by this container that are related to specified path or it's subpaths and
     * are not confirmed. Violations remain as relative to containers root.
     *
     * @param path
     *           violations path
     * @return search result
     */
    public ValidationResultSet findNotConfirmed(final String path)
    {
        return new ValidationResultSet(this, infoFactory, getNotConfirmedStream(path));
    }


    /**
     * Gets highest severity of violations contained by container.
     *
     * @return highest severity or {@link ValidationSeverity#NONE} if container is empty
     */
    public ValidationSeverity getHighestSeverity()
    {
        if(result.isEmpty())
        {
            return ValidationSeverity.NONE;
        }
        else
        {
            checkDirty();
            return result.get(0).getValidationSeverity();
        }
    }


    /**
     * Gets violation with highest severity of violations contained by container.
     *
     * @return violation with highest severity or <code>null</code> if container is empty
     */
    public ValidationInfo getHighestSeverityInfo()
    {
        if(result.isEmpty())
        {
            return null;
        }
        else
        {
            checkDirty();
            return infoFactory.createValidationInfo(result.get(0));
        }
    }


    /**
     * Gets highest severity of violations contained by container that are not confirmed.
     *
     * @return highest severity or {@link ValidationSeverity#NONE} if container is empty
     */
    public ValidationSeverity getHighestNotConfirmedSeverity()
    {
        checkDirty();
        final Optional<ValidationInfo> first = result.stream().filter(info -> !info.isConfirmed()).findFirst();
        if(first.isPresent())
        {
            return first.get().getValidationSeverity();
        }
        else
        {
            return ValidationSeverity.NONE;
        }
    }


    /**
     * Gets violation with highest severity of violations contained by container that are not confirmed.
     *
     * @return violation with highest severity or <code>null</code> if container is empty
     */
    public ValidationInfo getHighestNotConfirmedSeverityInfo()
    {
        checkDirty();
        final Optional<ValidationInfo> first = result.stream().filter(info -> !info.isConfirmed()).findFirst();
        if(first.isPresent())
        {
            return infoFactory.createValidationInfo(first.get());
        }
        else
        {
            return null;
        }
    }


    /**
     * Checks whether container contains any validation violation with specified severity.
     *
     * @param severity
     *           violations severity
     * @return <code>true</code> if at least one violation is of specified severity
     */
    public boolean containsSeverity(final ValidationSeverity severity)
    {
        return result.stream().anyMatch(info -> info.getValidationSeverity().equals(severity));
    }


    /**
     * Checks whether container contains any validation violation with specified severity that is not confirmed.
     *
     * @param severity
     *           violations severity
     * @return <code>true</code> if at least one violation is of specified severity and is not confirmed
     */
    public boolean containsNotConfirmedSeverity(final ValidationSeverity severity)
    {
        return result.stream().anyMatch(info -> !info.isConfirmed() && info.getValidationSeverity().equals(severity));
    }


    /**
     * Checks whether container contains any validation violation for specified path.
     *
     * @param path
     *           violations path
     * @return <code>true</code> if at least one violation is related to provided path or any of its children
     */
    public boolean contains(final String path)
    {
        return getStream(path).findAny().isPresent();
    }


    /**
     * Adds new validation violations to current container. All violation are considered to be relative containers root
     * and specified path.
     *
     * @param path
     *           relative path for added violations
     * @param validationInfos
     *           validation violations to be added
     */
    public void addValidationInfo(final String path, final Collection<ValidationInfo> validationInfos)
    {
        synchronized(this.mutex)
        {
            final ValidationInfoFactory withPrefixFactory = createWithPrefixFactory(path);
            validationInfos.forEach(info -> addValidationInfo(withPrefixFactory.createValidationInfo(info)));
            markDirty();
        }
        changed(path);
    }


    /**
     * Adds new validation violation to current container. violation is considered to be relative containers root.
     *
     * @param info
     *           validation violation to be added
     */
    public void addValidationInfo(final ValidationInfo info)
    {
        synchronized(this)
        {
            result.add(info);
            markDirty();
        }
        changed(info.getInvalidPropertyPath());
    }


    protected void removeImmediately(final String path)
    {
        synchronized(this.mutex)
        {
            getStream(path).collect(Collectors.toList()).forEach(result::remove);
        }
    }


    /**
     * Removes all validation violations registered in current container.
     */
    public void clear()
    {
        synchronized(this)
        {
            result.clear();
        }
        changed();
    }


    /**
     * Replaces all validation violations registered in current container with provided.
     *
     * @param validationInfos
     *           new violations for container
     * @see #updateValidationInfo(Collection)
     */
    public void setValidationInfo(final Collection<ValidationInfo> validationInfos)
    {
        synchronized(this)
        {
            result.clear();
            result.addAll(validationInfos);
            markDirty();
        }
        changed();
    }


    /**
     * Replaces violations related to provided path with their new instances. All new violation are considered to be
     * relative to provided root path and prefix is added to them.
     * <p>
     * As first all violation related to provided path are removed. Then new violations are wrapped with prefix and added
     * to container.
     *
     * @param path
     *           root path for added violations
     * @param validationInfos
     *           validation violations to be added
     * @see #updateValidationInfo(String, Collection)
     */
    public void setValidationInfo(final String path, final Collection<ValidationInfo> validationInfos)
    {
        synchronized(this)
        {
            getStream(path).collect(Collectors.toList()).forEach(result::remove);
            final ValidationInfoFactory prefixFactory = createWithPrefixFactory(path);
            validationInfos.stream().map(prefixFactory::createValidationInfo).forEach(result::add);
            markDirty();
        }
        changed(path);
    }


    /**
     * Replaces violations with their new instances.
     * <p>
     * As first all violations related to same paths that new ones are removed. Then new violations are added to
     * container.
     * <p>
     * Notice that only violations related to paths from provided violations are changed, though method does nothing, if
     * provided list is empty.
     *
     * @param validationInfos
     *           new violations
     * @see #removeValidationInfo(String)
     * @see #setValidationInfo(Collection)
     */
    public void updateValidationInfo(final Collection<ValidationInfo> validationInfos)
    {
        synchronized(this)
        {
            validationInfos.forEach(info -> removeImmediately(info.getInvalidPropertyPath()));
            this.result.addAll(validationInfos);
            markDirty();
        }
        final Set<String> notified = new HashSet<>();
        validationInfos.forEach(info -> {
            if(!notified.contains(info.getInvalidPropertyPath()))
            {
                changed(info.getInvalidPropertyPath());
                notified.add(info.getInvalidPropertyPath());
            }
        });
    }


    /**
     * Replaces violations with their new instances.
     * <p>
     * As first all violation related to provided path are removed. Then new violations are added to container. New
     * violations should be related to provided path and define their violation path as a subpath of it.
     *
     * @param path
     *           root path which violations are being changed
     * @param validationInfos
     *           new violations
     * @see #removeValidationInfo(String)
     * @see #setValidationInfo(String, Collection)
     * @throws IllegalArgumentException
     *            if any of provided violation defines a path that is not relative to <code>path</code>
     */
    public void updateValidationInfo(final String path, final Collection<ValidationInfo> validationInfos)
    {
        if(validationInfos.stream().anyMatch(info -> !ObjectValuePath.parse(info.getInvalidPropertyPath()).startsWith(path)))
        {
            throw new IllegalArgumentException("Provided violations does not match provided path: " + path);
        }
        synchronized(this)
        {
            getStream(path).collect(Collectors.toList()).forEach(result::remove);
            this.result.addAll(validationInfos);
            markDirty();
        }
        changed(path);
    }


    /**
     * Removes all validation violations from specified path and its subpaths from current container with provided.
     *
     * @param path
     *           relative path for which to remove violations
     */
    public void removeValidationInfo(final String path)
    {
        removeImmediately(path);
        changed(path);
    }


    /**
     * Removes validation violation from current container
     *
     * @param info
     *           violation to be removed
     */
    public void removeValidationInfo(final ValidationInfo info)
    {
        result.remove(info);
        changed(info.getInvalidPropertyPath());
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o instanceof ValidationResult)
        {
            final ValidationResult that = (ValidationResult)o;
            return Objects.equals(result, that.result);
        }
        else
        {
            return false;
        }
    }


    @Override
    public int hashCode()
    {
        return result != null ? result.hashCode() : 0;
    }
}
