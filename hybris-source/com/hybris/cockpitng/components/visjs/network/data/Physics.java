/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Represents physics simulation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Physics implements Serializable
{
    /**
     * Enables physics system. Default value is true.
     */
    private final Boolean enabled;
    /**
     * It's a quadtree based gravity model. It's default and recommended model for non-hierarchical layout.
     */
    private final BarnesHut barnesHut;
    /**
     * Represents physics model similar to {@link BarnesHut}. The main difference is that repulsion is linear instead of
     * quadratic. Moreover, weight of node is multiplied by number of edges plus one.
     */
    private final ForceAtlas2Based forceAtlas2Based;
    /**
     * Repulsion model used in {@link Physics}. It assumes that node has repulsion value and the value is decreased from
     * 1 to 0.
     */
    private final Repulsion repulsion;
    /**
     * Hierarchical Repulsion model used in {@link Physics}. In this model levels are taken into account and forces are
     * normalized.
     */
    private final HierarchicalRepulsion hierarchicalRepulsion;
    /**
     * Maximum velocity of nodes set in order to increasing time to stabilization. Default value is 50.
     */
    private final Double maxVelocity;
    /**
     * Minimum velocity of nodes. The simulation stops and network is stabilized when minimum value is reached. Default
     * value is 0.1
     */
    private final Double minVelocity;
    /**
     * Indicates which solver should be used. Possible values are: 'barnesHut', 'repulsion', 'hierarchicalRepulsion',
     * 'forceAtlas2Based'. Default value is barnesHut.
     */
    private final String solver;
    /**
     * Indicates whether network should be stabilized on load using default settings
     */
    private final Stabilization stabilization;
    /**
     * Because a simulation is discrete, each iteration is done in particular interval. Default value is 0.5
     */
    private final Double timestep;
    /**
     * If set to true then timestep will be adapted intelligently. Default value is true.
     */
    private final Boolean adaptiveTimestep;


    @JsonCreator
    protected Physics(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("barnesHut") final BarnesHut barnesHut,
                    @JsonProperty("forceAtlas2Based") final ForceAtlas2Based forceAtlas2Based,
                    @JsonProperty("repulsion") final Repulsion repulsion,
                    @JsonProperty("hierarchicalRepulsion") final HierarchicalRepulsion hierarchicalRepulsion,
                    @JsonProperty("maxVelocity") final Double maxVelocity, @JsonProperty("minVelocity") final Double minVelocity,
                    @JsonProperty("solver") final String solver, @JsonProperty("stabilization") final Stabilization stabilization,
                    @JsonProperty("timestep") final Double timestep, @JsonProperty("adaptiveTimestep") final Boolean adaptiveTimestep)
    {
        this.enabled = enabled;
        this.barnesHut = barnesHut;
        this.forceAtlas2Based = forceAtlas2Based;
        this.repulsion = repulsion;
        this.hierarchicalRepulsion = hierarchicalRepulsion;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;
        this.solver = solver;
        this.stabilization = stabilization;
        this.timestep = timestep;
        this.adaptiveTimestep = adaptiveTimestep;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public BarnesHut getBarnesHut()
    {
        return barnesHut;
    }


    public ForceAtlas2Based getForceAtlas2Based()
    {
        return forceAtlas2Based;
    }


    public Repulsion getRepulsion()
    {
        return repulsion;
    }


    public HierarchicalRepulsion getHierarchicalRepulsion()
    {
        return hierarchicalRepulsion;
    }


    public Double getMaxVelocity()
    {
        return maxVelocity;
    }


    public Double getMinVelocity()
    {
        return minVelocity;
    }


    public String getSolver()
    {
        return solver;
    }


    public Stabilization getStabilization()
    {
        return stabilization;
    }


    public Double getTimestep()
    {
        return timestep;
    }


    public Boolean getAdaptiveTimestep()
    {
        return adaptiveTimestep;
    }


    public static class Builder
    {
        private Boolean enabled;
        private BarnesHut barnesHut;
        private ForceAtlas2Based forceAtlas2Based;
        private Repulsion repulsion;
        private HierarchicalRepulsion hierarchicalRepulsion;
        private Double maxVelocity;
        private Double minVelocity;
        private String solver;
        private Stabilization stabilization;
        private Double timestep;
        private Boolean adaptiveTimestep;


        /**
         * Enables physics system. Default value is true.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * It's a quadtree based gravity model. It's default and recommended model for non-hierarchical layout.
         */
        public Builder withBarnesHut(final BarnesHut barnesHut)
        {
            this.barnesHut = barnesHut;
            return this;
        }


        /**
         * Represents physics model similar to {@link BarnesHut}. The main difference is that repulsion is linear instead
         * of quadratic. Moreover, weight of node is multiplied by number of edges plus one.
         */
        public Builder withForceAtlas2Based(final ForceAtlas2Based forceAtlas2Based)
        {
            this.forceAtlas2Based = forceAtlas2Based;
            return this;
        }


        /**
         * Repulsion model used in {@link Physics}. It assumes that node has repulsion value and the value is decreased
         * from 1 to 0.
         */
        public Builder withRepulsion(final Repulsion repulsion)
        {
            this.repulsion = repulsion;
            return this;
        }


        /**
         * Hierarchical Repulsion model used in {@link Physics}. In this model levels are taken into account and forces
         * are normalized.
         */
        public Builder withHierarchicalRepulsion(final HierarchicalRepulsion hierarchicalRepulsion)
        {
            this.hierarchicalRepulsion = hierarchicalRepulsion;
            return this;
        }


        /**
         * Maximum velocity of nodes set in order to increasing time to stabilization. Default value is 50.
         */
        public Builder withMaxVelocity(final Double maxVelocity)
        {
            this.maxVelocity = maxVelocity;
            return this;
        }


        /**
         * Minimum velocity of nodes. The simulation stops and network is stabilized when minimum value is reached.
         * Default value is 0.1
         */
        public Builder withMinVelocity(final Double minVelocity)
        {
            this.minVelocity = minVelocity;
            return this;
        }


        /**
         * Indicates which solver should be used. Possible values are: 'barnesHut', 'repulsion', 'hierarchicalRepulsion',
         * 'forceAtlas2Based'. Default value is barnesHut.
         */
        public Builder withSolver(final String solver)
        {
            this.solver = solver;
            return this;
        }


        /**
         * Indicates whether network should be stabilized on load using default settings
         */
        public Builder withStabilization(final Stabilization stabilization)
        {
            this.stabilization = stabilization;
            return this;
        }


        /**
         * Because a simulation is discrete, each iteration is done in particular interval. Default value is 0.5
         */
        public Builder withTimestep(final Double timestep)
        {
            this.timestep = timestep;
            return this;
        }


        /**
         * If set to true then timestep will be adapted intelligently. Default value is true.
         */
        public Builder withAdaptiveTimestep(final Boolean adaptiveTimestep)
        {
            this.adaptiveTimestep = adaptiveTimestep;
            return this;
        }


        public Physics build()
        {
            return new Physics(enabled, barnesHut, forceAtlas2Based, repulsion, hierarchicalRepulsion, maxVelocity, minVelocity,
                            solver, stabilization, timestep, adaptiveTimestep);
        }
    }
}
