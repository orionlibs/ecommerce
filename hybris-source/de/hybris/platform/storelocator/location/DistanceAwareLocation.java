package de.hybris.platform.storelocator.location;

public interface DistanceAwareLocation extends Location, Comparable<DistanceAwareLocation>
{
    Double getDistance();
}
