package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import java.awt.*;
import java.util.Random;

/**
 * Represents an area light source in the ray tracer.
 * The area light is a rectangular surface that emits light evenly.
 */
public class AreaLight extends Light {
    private Vector3D u; // One edge vector (width)
    private Vector3D v; // Another edge vector (height)
    private int sampleCount;

    /**
     * Constructs a rectangular area light.
     *
     * @param center      Center position of the area light.
     * @param u           Edge vector representing the width direction and length.
     * @param v           Edge vector representing the height direction and length.
     * @param color       Light color.
     * @param intensity   Light intensity.
     * @param sampleCount Number of samples to take across the area (affects shadow softness)
     */
    public AreaLight(Vector3D center, Vector3D u, Vector3D v, Color color, double intensity, int sampleCount) {
        super(center, color, intensity);
        this.u = u;
        this.v = v;
        this.sampleCount = Math.max(1, sampleCount);
    }

    public Vector3D getU() { return u; }
    public Vector3D getV() { return v; }
    public int getSampleCount() { return sampleCount; }

    /**
     * Computes the average N·L over multiple samples distributed over the area.
     * This produces soft-shadow and more realistic shading.
     *
     * @param intersection the intersection containing surface information
     * @return the non-negative averaged dot product of normal and light direction.
     */
    @Override
    public double getNDotL(Intersection intersection) {
        double total = 0.0;
        Random rng = new Random(intersection.hashCode()); // deterministic jitter for each intersection

        for (int i = 0; i < sampleCount; i++) {
            // Uniform random sample in [0,1)
            double ru = rng.nextDouble();
            double rv = rng.nextDouble();

            // Compute sample position on the area rectangle
            Vector3D samplePos = Vector3D.add(
                    Vector3D.add(getPosition(), Vector3D.scalarMultiplication(u, ru - 0.5)),
                    Vector3D.scalarMultiplication(v, rv - 0.5)
            );

            Vector3D toLight = Vector3D.normalize(Vector3D.substract(samplePos, intersection.getPosition()));
            double dot = Math.max(Vector3D.dotProduct(intersection.getNormal(), toLight), 0.0);
            total += dot;
        }
        return total / sampleCount;
    }
}