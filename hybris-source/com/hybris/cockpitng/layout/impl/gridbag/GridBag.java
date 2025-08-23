/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout.impl.gridbag;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.layout.Division;
import com.hybris.cockpitng.layout.ElementPlacement;
import com.hybris.cockpitng.layout.LayoutManager;
import com.hybris.cockpitng.layout.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

/**
 * Layout manager responsible for rendering elements in a grid.
 */
public class GridBag<T> implements LayoutManager<T>
{
    public static final int DEFAULT_COLUMN_NUMBER = 3;
    /**
     * @deprecated since 2205, not used anymore
     */
    @Deprecated(since = "2205", forRemoval = true)
    public static final float FULL_WIDTH = 100f;
    public static final double GRID_FULL_WIDTH = 100;
    public static final String CSS_PERCENT = "%";
    private static final Logger LOG = LoggerFactory.getLogger(GridBag.class);
    private String dndKeyPrefix;
    private String columnClass;
    private String rowClass;
    private String slotClass;
    private String fillClass;
    private String draggableHeaderClass;


    @Override
    public Collection<HtmlBasedComponent> positionElements(final Component parent,
                    final List<ElementPlacement<T>> elementPlacements, final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Placements may not be null", elementPlacements);
        Validate.notNull("Renderer may not be null", renderer);
        final List<ElementPlacement<T>> placements = Lists.newArrayList();
        placements.addAll(elementPlacements);
        positionFloatingPlacements(placements);
        final Collection<ElementPlacement<T>> fill = fillEmptySpaces(placements);
        placements.addAll(fill);
        final float cellWidth = (float)(GridBag.GRID_FULL_WIDTH / calculateWidth(placements));
        final String dndKey = dndKeyPrefix + Long.toString(System.nanoTime(), 24);
        final Division division = divideHorizontally(parent, 0, 0, cellWidth, placements, true, dndKey, renderer);
        final Collection<HtmlBasedComponent> draggableElements;
        if(!division.isSuccessful())
        {
            LOG.debug("Could not find possible division, using fallback");
            draggableElements = renderFallbackDivision(parent, placements, GridBag.DEFAULT_COLUMN_NUMBER, dndKey,
                            (float)(GridBag.GRID_FULL_WIDTH / GridBag.DEFAULT_COLUMN_NUMBER), renderer);
        }
        else
        {
            draggableElements = division.getDraggableElements();
        }
        return draggableElements;
    }


    protected void positionFloatingPlacements(final List<ElementPlacement<T>> placements)
    {
        Validate.notNull("Placements may not be null", placements);
        Integer maxX = null;
        Integer maxY = null;
        final List<ElementPlacement<T>> positioned = Lists.newArrayList();
        final List<ElementPlacement<T>> notPositioned = Lists.newArrayList();
        for(final ElementPlacement<T> placement : placements)
        {
            if(Point.NULL_POINT != placement.getBottomRight() && Point.NULL_POINT != placement.getTopLeft())
            {
                maxX = maxX == null ? placement.getBottomRight().getX() : Math.max(maxX, placement.getBottomRight().getX());
                maxY = maxY == null ? placement.getBottomRight().getY() : Math.max(maxY, placement.getBottomRight().getY());
                positioned.add(placement);
            }
            else
            {
                notPositioned.add(placement);
            }
        }
        if(positioned.isEmpty())
        {
            int x = -1;
            int y = -1;
            for(final ElementPlacement<T> placement : placements)
            {
                if(++x % GridBag.DEFAULT_COLUMN_NUMBER == 0)
                {
                    x = 0;
                    y++;
                }
                final Point point = new Point(x, y);
                placement.setTopLeft(point);
                placement.setBottomRight(point);
            }
        }
        else
        {
            maxX = Math.max(maxX, GridBag.DEFAULT_COLUMN_NUMBER - 1);
            int x = 0;
            int y = 0;
            for(final ElementPlacement<T> placement : notPositioned)
            {
                while(overlaps(x, y, positioned))
                {
                    if(++x % (maxX + 1) == 0)
                    {
                        x = 0;
                        y++;
                    }
                }
                final Point point = new Point(x, y);
                placement.setTopLeft(point);
                placement.setBottomRight(point);
                if(++x % (maxX + 1) == 0)
                {
                    x = 0;
                    y++;
                }
            }
        }
    }


    protected boolean overlaps(final int x, final int y, final List<ElementPlacement<T>> positioned)
    {
        Validate.notNull("Placements may not be null", positioned);
        return positioned.stream().filter(placement -> placement.contains(x, y)).findAny().isPresent();
    }


    protected Collection<ElementPlacement<T>> fillEmptySpaces(final List<ElementPlacement<T>> placements)
    {
        Validate.notNull("Placements may not be null", placements);
        int maxX = 0;
        int maxY = 0;
        for(final ElementPlacement<T> placement : placements)
        {
            maxX = Math.max(maxX, placement.getBottomRight().getX());
            maxY = Math.max(maxY, placement.getBottomRight().getY());
        }
        final List<ElementPlacement<T>> toAdd = Lists.newArrayList();
        for(int y = 0; y <= maxY; y++)
        {
            for(int x = 0; x <= maxX; x++)
            {
                if(!overlaps(x, y, placements))
                {
                    final Point point = new Point(x, y);
                    toAdd.add(new ElementPlacement<>(point, point));
                }
            }
        }
        return toAdd;
    }


    protected int calculateWidth(final List<ElementPlacement<T>> placements)
    {
        Validate.notNull("Placements may not be null", placements);
        Integer maxX = null;
        Integer minX = null;
        for(final ElementPlacement<T> placement : placements)
        {
            maxX = maxX == null ? placement.getBottomRight().getX() : Math.max(maxX, placement.getBottomRight().getX());
            minX = minX == null ? placement.getTopLeft().getX() : Math.min(minX, placement.getTopLeft().getX());
        }
        return maxX == null ? 0 : maxX - minX + 1;
    }


    protected Division divideHorizontally(final Component parent, final int startX, final int startY, final float cellWidth,
                    final List<ElementPlacement<T>> children, final boolean iterate, final String dndKey,
                    final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Children may not be null", children);
        Validate.notNull("D'n'D key may not be null", dndKey);
        Validate.notNull("Renderer may not be null", renderer);
        if(children.isEmpty())
        {
            return new Division(0, 0, true, Collections.emptyList());
        }
        if(children.size() == 1)
        {
            return prepareSingleElementDivision(parent, cellWidth, dndKey, children.get(0), renderer);
        }
        Integer maxY = null;
        for(final ElementPlacement<T> child : children)
        {
            maxY = maxY == null ? child.getBottomRight().getY() : Math.max(maxY, child.getBottomRight().getY());
        }
        Integer divisionLine = null;
        division:
        for(int i = startY; i < maxY; i++)
        {
            for(final ElementPlacement<T> child : children)
            {
                final Point topLeft = child.getTopLeft();
                final Point bottomRight = child.getBottomRight();
                if(topLeft.getY() <= i && bottomRight.getY() > i)
                {
                    continue division;
                }
            }
            divisionLine = i;
            break;
        }
        if(divisionLine != null)
        {
            final List<ElementPlacement<T>> above = Lists.newArrayList();
            final List<ElementPlacement<T>> below = Lists.newArrayList();
            for(final ElementPlacement<T> child : children)
            {
                if(child.getBottomRight().getY() <= divisionLine)
                {
                    above.add(child);
                }
                else
                {
                    below.add(child);
                }
            }
            final Div column = new Div();
            column.setSclass(columnClass);
            final float cellWidthPercents = (float)(GRID_FULL_WIDTH / calculateWidth(children));
            final Division topDivision = divideVertically(column, startX, startY, cellWidthPercents, above, true, dndKey, renderer);
            final Division bottomDivision = topDivision.isSuccessful()
                            ? divideVertically(column, startX, divisionLine + 1, cellWidthPercents, below, true, dndKey, renderer) : null;
            if(topDivision.isSuccessful() && bottomDivision != null && bottomDivision.isSuccessful())
            {
                adjustParentContainer(parent, column, columnClass, rowClass);
                final ArrayList<HtmlBasedComponent> draggableElements = Lists.newArrayList();
                draggableElements.addAll(topDivision.getDraggableElements());
                draggableElements.addAll(bottomDivision.getDraggableElements());
                final int width = topDivision.getWidth();
                final int height = topDivision.getHeight() + bottomDivision.getHeight();
                final Division division = new Division(width, height, true, draggableElements);
                column.setWidth(width * cellWidth + CSS_PERCENT);
                return division;
            }
            return new Division(0, 0, false, Collections.emptyList());
        }
        if(iterate)
        {
            return divideVertically(parent, startX, startY, cellWidth, children, false, dndKey, renderer);
        }
        return new Division(0, 0, false, Collections.emptyList());
    }


    private Division prepareSingleElementDivision(final Component parent, final float cellWidth, final String dndKey,
                    final ElementPlacement<T> child, final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Child may not be null", child);
        Validate.notNull("D'n'D key may not be null", dndKey);
        Validate.notNull("Renderer may not be null", renderer);
        final HtmlBasedComponent draggable = renderSingleWidget(parent, child, dndKey, cellWidth, renderer);
        final ArrayList<HtmlBasedComponent> draggableElements = Lists.newArrayList();
        if(draggable != null)
        {
            draggableElements.add(draggable);
        }
        return new Division(child.getWidth(), child.getHeight(), true, draggableElements);
    }


    protected Division divideVertically(final Component parent, final int startX, final int startY, final float cellWidth,
                    final List<ElementPlacement<T>> children, final boolean iterate, final String dndKey,
                    final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Children may not be null", children);
        Validate.notNull("D'n'D key may not be null", dndKey);
        Validate.notNull("Renderer may not be null", renderer);
        if(children.isEmpty())
        {
            return new Division(0, 0, true, Collections.emptyList());
        }
        if(children.size() == 1)
        {
            return prepareSingleElementDivision(parent, cellWidth, dndKey, children.get(0), renderer);
        }
        Integer maxX = null;
        for(final ElementPlacement<T> child : children)
        {
            maxX = maxX == null ? child.getBottomRight().getX() : Math.max(maxX, child.getBottomRight().getX());
        }
        Integer divisionLine = null;
        division:
        for(int i = startX; i < maxX; i++)
        {
            for(final ElementPlacement<T> child : children)
            {
                final Point topLeft = child.getTopLeft();
                final Point bottomRight = child.getBottomRight();
                if(topLeft.getX() <= i && bottomRight.getX() > i)
                {
                    continue division;
                }
            }
            divisionLine = i;
            break;
        }
        if(divisionLine != null)
        {
            final List<ElementPlacement<T>> left = Lists.newArrayList();
            final List<ElementPlacement<T>> right = Lists.newArrayList();
            for(final ElementPlacement<T> child : children)
            {
                if(child.getBottomRight().getX() <= divisionLine)
                {
                    left.add(child);
                }
                else
                {
                    right.add(child);
                }
            }
            final Div row = new Div();
            row.setSclass(rowClass);
            final float cellWidthPercent = (float)(GRID_FULL_WIDTH / calculateWidth(children));
            final Division leftDivision = divideHorizontally(row, startX, startY, cellWidthPercent, left, true, dndKey, renderer);
            final Division rightDivision = leftDivision.isSuccessful()
                            ? divideHorizontally(row, divisionLine + 1, startY, cellWidthPercent, right, true, dndKey, renderer) : null;
            if(leftDivision.isSuccessful() && rightDivision != null && rightDivision.isSuccessful())
            {
                final HtmlBasedComponent embedded = adjustParentContainer(parent, row, rowClass, columnClass);
                final ArrayList<HtmlBasedComponent> draggableElements = Lists.newArrayList();
                draggableElements.addAll(leftDivision.getDraggableElements());
                draggableElements.addAll(rightDivision.getDraggableElements());
                final int width = leftDivision.getWidth() + rightDivision.getWidth();
                final int height = leftDivision.getHeight();
                final Division division = new Division(width, height, true, draggableElements);
                if(embedded != null)
                {
                    embedded.setWidth(width * cellWidth + CSS_PERCENT);
                }
                return division;
            }
            return new Division(0, 0, false, Collections.emptyList());
        }
        if(iterate)
        {
            return divideHorizontally(parent, startX, startY, cellWidth, children, false, dndKey, renderer);
        }
        return new Division(0, 0, false, Collections.emptyList());
    }


    private HtmlBasedComponent adjustParentContainer(final Component parent, final Div component, final String parentClass,
                    final String containerClass)
    {
        if(parentClass.equals(((HtmlBasedComponent)parent).getSclass()))
        {
            final Div column = new Div();
            parent.appendChild(column);
            column.setSclass(containerClass);
            column.appendChild(component);
            return column;
        }
        else
        {
            parent.appendChild(component);
        }
        return null;
    }


    protected Collection<HtmlBasedComponent> renderFallbackDivision(final Component parent,
                    final List<ElementPlacement<T>> placements, final int columnNumber, final String dndKey, final float cellWidth,
                    final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Placements may not be null", placements);
        Validate.notNull("D'n'D key may not be null", dndKey);
        Validate.notNull("Renderer may not be null", renderer);
        final Div column = new Div();
        column.setSclass(columnClass);
        parent.appendChild(column);
        Div row = null;
        int counter = 0;
        final Collection<HtmlBasedComponent> draggableElements = Lists.newArrayList();
        for(final ElementPlacement<T> placement : placements)
        {
            if(counter++ % columnNumber == 0)
            {
                row = new Div();
                row.setSclass(rowClass);
                column.appendChild(row);
            }
            final ElementPlacement tmp = new ElementPlacement(placement.getElement(), Point.NULL_POINT, Point.NULL_POINT);
            final HtmlBasedComponent draggable = renderSingleWidget(row, tmp, dndKey, cellWidth, renderer);
            if(draggable != null)
            {
                draggableElements.add(draggable);
            }
        }
        while(counter++ % columnNumber != 0)
        {
            final HtmlBasedComponent draggable = renderSingleWidget(row, new ElementPlacement<>(Point.NULL_POINT, Point.NULL_POINT),
                            dndKey, cellWidth, renderer);
            if(draggable != null)
            {
                draggableElements.add(draggable);
            }
        }
        return draggableElements;
    }


    protected HtmlBasedComponent renderSingleWidget(final Component parent, final ElementPlacement<T> placement,
                    final String dndKey, final float cellWidth, final BiFunction<Component, ElementPlacement<T>, Component> renderer)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Placement may not be null", placement);
        Validate.notNull("D'n'D key may not be null", dndKey);
        Validate.notNull("Renderer may not be null", renderer);
        final Div slot = new Div();
        slot.setSclass(slotClass);
        final Div column = new Div();
        column.appendChild(slot);
        parent.appendChild(column);
        if(rowClass.equals(((HtmlBasedComponent)parent).getSclass()))
        {
            column.setSclass(columnClass);
            column.setWidth(placement.getWidth() * cellWidth + CSS_PERCENT);
        }
        else
        {
            column.setSclass(rowClass);
        }
        if(placement.getElement() == null)
        {
            final Div fill = new Div();
            fill.setSclass(fillClass);
            slot.appendChild(fill);
            renderer.apply(fill, null);
            return fill;
        }
        else
        {
            final Div draggableHeader = new Div();
            draggableHeader.setSclass(draggableHeaderClass);
            draggableHeader.setAttribute(DND_KEY, dndKey);
            slot.appendChild(draggableHeader);
            final Component component = renderer.apply(slot, placement);
            if(component != null)
            {
                component.setAttribute(DND_KEY, dndKey);
                if(component.getParent() == null)
                {
                    slot.appendChild(component);
                }
            }
            return draggableHeader;
        }
    }


    public String getDndKeyPrefix()
    {
        return dndKeyPrefix;
    }


    @Required
    public void setDndKeyPrefix(final String dndKeyPrefix)
    {
        this.dndKeyPrefix = dndKeyPrefix;
    }


    public String getColumnClass()
    {
        return columnClass;
    }


    @Required
    public void setColumnClass(final String columnClass)
    {
        this.columnClass = columnClass;
    }


    public String getRowClass()
    {
        return rowClass;
    }


    @Required
    public void setRowClass(final String rowClass)
    {
        this.rowClass = rowClass;
    }


    public String getSlotClass()
    {
        return slotClass;
    }


    @Required
    public void setSlotClass(final String slotClass)
    {
        this.slotClass = slotClass;
    }


    public String getFillClass()
    {
        return fillClass;
    }


    @Required
    public void setFillClass(final String fillClass)
    {
        this.fillClass = fillClass;
    }


    public String getDraggableHeaderClass()
    {
        return draggableHeaderClass;
    }


    @Required
    public void setDraggableHeaderClass(final String draggableHeaderClass)
    {
        this.draggableHeaderClass = draggableHeaderClass;
    }
}
