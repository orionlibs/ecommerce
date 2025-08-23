package de.hybris.platform.storelocator.route.impl;

import de.hybris.platform.storelocator.route.DistanceAndRoute;
import de.hybris.platform.storelocator.route.Route;

public class DefaultDistanceAndRoute implements DistanceAndRoute
{
    private double roadDistance;
    private double eagleFliesDistance;
    private final Route route;


    public DefaultDistanceAndRoute(double roadDistance, double eagleFliesDistance, Route route)
    {
        this.roadDistance = roadDistance;
        this.eagleFliesDistance = eagleFliesDistance;
        this.route = route;
    }


    public DefaultDistanceAndRoute(Route route)
    {
        this.route = route;
    }


    public Route getRoute()
    {
        return this.route;
    }


    public double getRoadDistance()
    {
        return this.roadDistance;
    }


    public double getEagleFliesDistance()
    {
        return this.eagleFliesDistance;
    }
}
