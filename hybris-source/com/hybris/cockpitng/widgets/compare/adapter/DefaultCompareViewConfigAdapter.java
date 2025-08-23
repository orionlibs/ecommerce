/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter;

import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.Header;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.core.util.impl.MergeUtils;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import com.hybris.cockpitng.widgets.compare.adapter.converters.CompareViewConverter;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

public class DefaultCompareViewConfigAdapter implements CockpitConfigurationAdapter<CompareView>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCompareViewConfigAdapter.class);
    private static final String DEFAULT_CONFIG_CONTEXT_USED = "Default config context used";
    private static final String DEFAULT_EDITOR_AREA_CONFIGURATION = "editor-area";
    private static final String DEFAULT_GRID_VIEW_CONFIGURATION = "grid-view";
    private static final String EDITOR_AREA_CAN_NOT_BE_NULL = "EditorArea to covert configuration from, can not be null";
    private PositionedSort<Positioned> positionedSort;
    private CockpitConfigurationService cockpitConfigurationService;
    private MergeUtils mergeUtils;
    private CompareViewConverter<AbstractTab, Section> tabToSectionConverter;
    private CompareViewConverter<Essentials, Section> essentialsToSectionConverter;
    private CompareViewConverter<GridView, Header> gridViewToHeaderConverter;


    @Override
    public Class<CompareView> getSupportedType()
    {
        return CompareView.class;
    }


    @Override
    public CompareView adaptBeforeStore(final ConfigContext context, final CompareView compareView)
    {
        return compareView;
    }


    @Override
    public CompareView adaptAfterLoad(final ConfigContext context, final CompareView compareView)
    {
        this.<Section>getPositionedSort().sort(compareView.getSection());
        compareView.getSection()
                        .forEach(section -> this.<com.hybris.cockpitng.config.compareview.jaxb.Attribute>getPositionedSort()
                                        .sort(section.getAttribute()));
        return compareView;
    }


    @Override
    public CompareView adaptBeforeMerge(final ConfigContext context, final CompareView compareView)
                    throws CockpitConfigurationException
    {
        final Optional<EditorArea> editorAreaConfiguration = loadEditorAreaConfiguration(context, compareView);
        final Optional<GridView> gridViewConfiguration = loadGridViewConfiguration(context, compareView);
        final CompareView mergedCompareView = new CompareView();
        editorAreaConfiguration
                        .ifPresent(editorArea -> mergeUtils.merge(mergedCompareView, createCompareViewFromEditorArea(editorArea)));
        gridViewConfiguration.ifPresent(gridView -> mergeUtils.merge(mergedCompareView, createCompareViewFromGridView(gridView)));
        mergeUtils.merge(mergedCompareView, compareView);
        if(!editorAreaConfiguration.isPresent() && (!isEditorAreaConfigurationContextProvided(compareView)
                        || shouldLoadSpecifiedEditorAreaConfiguration(compareView)))
        {
            mergedCompareView.getSection().addAll(compareView.getSection().stream()
                            .filter(section -> section.getMergeMode() == MergeMode.REMOVE).collect(Collectors.toList()));
        }
        return mergedCompareView;
    }


    protected boolean isEditorAreaConfigurationContextProvided(final CompareView compareView)
    {
        return compareView.getEditorAreaCtx() != null;
    }


    protected boolean shouldLoadSpecifiedEditorAreaConfiguration(final CompareView compareView)
    {
        return isEditorAreaConfigurationContextProvided(compareView) && compareView.getEditorAreaCtx().isUse();
    }


    protected DefaultConfigContext copyConfigContext(final ConfigContext context, final String newComponent)
    {
        return DefaultConfigContext.clone(context, newComponent, null);
    }


    protected Optional<EditorArea> loadEditorAreaConfiguration(final ConfigContext context, final CompareView compareView)
                    throws CockpitConfigurationException
    {
        if(shouldLoadSpecifiedEditorAreaConfiguration(compareView))
        {
            final boolean shouldUseDefault = StringUtils.isEmpty(compareView.getEditorAreaCtx().getCtx());
            final ConfigContext configContext = copyConfigContext(context,
                            shouldUseDefault ? DEFAULT_EDITOR_AREA_CONFIGURATION : compareView.getEditorAreaCtx().getCtx());
            return Optional.of(cockpitConfigurationService.loadConfiguration(configContext, EditorArea.class));
        }
        return Optional.empty();
    }


    protected Optional<GridView> loadGridViewConfiguration(final ConfigContext context, final CompareView compareView)
                    throws CockpitConfigurationException
    {
        if(compareView.getGridViewCtx() != null && compareView.getGridViewCtx().isUse())
        {
            final boolean shouldUseDefault = StringUtils.isEmpty(compareView.getGridViewCtx().getCtx());
            final ConfigContext configContext = copyConfigContext(context,
                            shouldUseDefault ? DEFAULT_GRID_VIEW_CONFIGURATION : compareView.getGridViewCtx().getCtx());
            LOG.debug(DEFAULT_CONFIG_CONTEXT_USED);
            return Optional.of(cockpitConfigurationService.loadConfiguration(configContext, GridView.class));
        }
        return Optional.empty();
    }


    protected CompareView createCompareViewFromGridView(final GridView gridView)
    {
        final CompareView compareView = new CompareView();
        compareView.setHeader(gridViewToHeaderConverter.convert(gridView));
        return compareView;
    }


    protected CompareView createCompareViewFromEditorArea(final EditorArea editorArea)
    {
        final CompareView compareView = new CompareView();
        addSectionsToCompareViewConfig(editorArea, compareView);
        return compareView;
    }


    protected void addSectionsToCompareViewConfig(final EditorArea editorAreaConfiguration, final CompareView compareView)
    {
        if(editorAreaConfiguration == null)
        {
            throw new IllegalArgumentException(EDITOR_AREA_CAN_NOT_BE_NULL);
        }
        Optional.ofNullable(editorAreaConfiguration.getEssentials()).map(this::constructSectionFromEssentials)
                        .ifPresent(compareView.getSection()::add);
        editorAreaConfiguration.getCustomTabOrTab().stream().map(this::constructSectionFromTab).filter(Objects::nonNull)
                        .forEach(compareView.getSection()::add);
    }


    protected Section constructSectionFromTab(final AbstractTab tab)
    {
        return tabToSectionConverter.convert(tab);
    }


    protected Section constructSectionFromEssentials(final Essentials essentials)
    {
        if(essentials.getEssentialSection() != null)
        {
            return essentialsToSectionConverter.convert(essentials);
        }
        else
        {
            return null;
        }
    }


    protected <P extends Positioned> PositionedSort<P> getPositionedSort()
    {
        return (PositionedSort<P>)positionedSort;
    }


    @Required
    public void setPositionedSort(final PositionedSort<Positioned> positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setMergeUtils(final MergeUtils mergeUtils)
    {
        this.mergeUtils = mergeUtils;
    }


    @Required
    public void setTabToSectionConverter(final CompareViewConverter<AbstractTab, Section> tabToSectionConverter)
    {
        this.tabToSectionConverter = tabToSectionConverter;
    }


    @Required
    public void setEssentialsToSectionConverter(final CompareViewConverter<Essentials, Section> essentialsToSectionConverter)
    {
        this.essentialsToSectionConverter = essentialsToSectionConverter;
    }


    @Required
    public void setGridViewToHeaderConverter(final CompareViewConverter<GridView, Header> gridViewToHeaderConverter)
    {
        this.gridViewToHeaderConverter = gridViewToHeaderConverter;
    }
}
