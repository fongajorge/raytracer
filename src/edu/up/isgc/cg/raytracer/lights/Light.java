package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

/**
 * Represents a light source in the ray tracer.
 * <p>
 * This class holds the position, color, and intensity of the light.
 * It does not inherit from any other class.
 * </p>
 */
public abstract class Light {
    /**
     * Position of the light in 3D space.
     */
    private Vector3D position;

    /**
     * Color of the light.
     */
    private Color color;

    /**
     * Intensity of the light.
     */
    private double intensity;

    /**
     * Constructs a Light object with specified position, color, and intensity.
     *
     * @param position  the position of the light in 3D space
     * @param color     the color of the light
     * @param intensity the intensity of the light
     */
    public Light(Vector3D position, Color color, double intensity) {
        this.position = position;
        this.color = color;
        setIntensity(intensity);
    }

    /**
     * Returns the position of the light.
     *
     * @return the position as a Vector3D
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the position of the light.
     *
     * @param position the position to set
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Returns the color of the light.
     *
     * @return the color as a java.awt.Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the light.
     *
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the intensity as a double
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity of the light.
     *
     * @param intensity the intensity to set
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Computes the dot product between the light direction and the surface normal at the intersection.
     * <p>
     * This value is often used in lighting calculations such as Lambertian reflection.
     * </p>
     *
     * @param intersection the intersection information where lighting is calculated
     * @return the dot product of the light direction and surface normal (N·L)
     */
    public abstract double getNDotL(Intersection intersection);

    /**
     * Returns an intersection between a ray and this light source.
     * <p>
     * Since lights do not have physical geometry in this context, this returns a default
     * intersection indicating no intersection.
     * </p>
     *
     * @param ray the ray to test for intersection
     * @return an Intersection object representing no hit
     */
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
