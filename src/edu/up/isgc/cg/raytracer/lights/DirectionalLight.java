package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

/**
 * Represents a directional light source in the ray tracer.
 * <p>
 * A directional light simulates light coming from a specific direction, such as sunlight,
 * where the rays are parallel and the light source is considered to be at an infinite distance.
 * This light does not have a position, only a direction.
 * </p>
 *
 * <p>
 * The intensity and color of the light can be configured. The class provides
 * a method to compute the dot product between the light direction and a surface normal
 * at the intersection point, which is used for lighting calculations.
 * </p>
 */
public class DirectionalLight extends Light {
    /**
     * The normalized direction vector from which the light rays are coming.
     * This vector points from the light source toward the scene.
     */
    private Vector3D direction;

    /**
     * Constructs a new DirectionalLight with the specified direction, color, and intensity.
     *
     * @param direction the direction vector of the light (will be normalized)
     * @param color     the color of the light
     * @param intensity the intensity (brightness) of the light
     */
    public DirectionalLight(Vector3D direction, Color color, double intensity) {
        super(Vector3D.ZERO(), color, intensity);
        setDirection(direction);
    }

    /**
     * Returns the normalized direction vector of this directional light.
     *
     * @return the direction vector
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the direction vector of this light.
     * The vector is normalized internally.
     *
     * @param direction the new direction vector (will be normalized)
     */
    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    /**
     * Computes the dot product of the surface normal at the intersection point
     * and the negated light direction vector.
     *
     * This value is clamped to a minimum of 0 and represents the cosine of the angle
     * between the surface normal and the incoming light, useful for diffuse lighting calculations.
     *
     * @param intersection the intersection containing surface information
     * @return the non-negative dot product of normal and light direction
     */
    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}
