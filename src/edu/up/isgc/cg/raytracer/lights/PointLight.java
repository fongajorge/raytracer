package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

/**
 * Represents a point light source in the ray tracer.
 * <p>
 * A point light emits light equally in all directions from a specific position
 * in 3D space. It models localized light sources such as light bulbs or lamps.
 * </p>
 *
 * <p>
 * The intensity and color of the light can be specified. The class provides
 * a method to compute the dot product between the surface normal at an intersection
 * point and the direction vector from the point light to that point,
 * which is used for lighting calculations such as diffuse shading.
 * </p>
 */
public class PointLight extends Light {

    /**
     * Constructs a new PointLight with the specified position, color, and intensity.
     *
     * @param position  the position of the point light in 3D space
     * @param color     the color of the light
     * @param intensity the intensity (brightness) of the light
     */
    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);
    }

    /**
     * Computes the dot product of the surface normal at the intersection point
     * and the normalized vector pointing from the intersection position
     * to the light's position.
     *
     * This value is clamped to a minimum of 0 and represents the cosine of the angle
     * between the surface normal and the incoming light direction,
     * which is useful for diffuse lighting calculations.
     *
     * @param intersection the intersection containing surface information
     * @return the non-negative dot product of normal and light direction
     */
    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(
                Vector3D.dotProduct(
                        intersection.getNormal(),
                        Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()))
                ),
                0.0
        );
    }
}
