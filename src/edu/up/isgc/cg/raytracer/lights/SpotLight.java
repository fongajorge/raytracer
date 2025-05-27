package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import java.awt.*;

/**
 * Represents a spotlight in the ray tracer.
 * <p>
 * A spotlight emits light from a specific position in a given direction,
 * confined within a cone defined by a cutoff angle.
 * The intensity within the cone can be controlled by an exponent to simulate
 * how focused or diffuse the spotlight beam is.
 * </p>
 *
 * <p>
 * The spotlight attenuates light outside the cutoff angle to zero,
 * and inside the cone applies an angular attenuation based on the angle
 * between the spotlight direction and the vector from the light to the point.
 * </p>
 */
public class SpotLight extends Light {
    /**
     * The normalized direction vector of the spotlight.
     * Defines the center axis of the spotlight cone.
     */
    private Vector3D direction;

    /**
     * The cutoff angle (in radians) defining the spotlight cone.
     * Points outside this angle relative to the spotlight direction receive no light.
     */
    private double cutoffAngle;

    /**
     * The exponent used to control the concentration of the spotlight beam.
     * Higher values result in a narrower, more focused beam.
     */
    private double exponent;

    /**
     * Constructs a new SpotLight with the specified position, direction, color,
     * intensity, cutoff angle, and exponent.
     *
     * @param position    the position of the spotlight in 3D space
     * @param direction   the direction vector of the spotlight (will be normalized)
     * @param color       the color of the light
     * @param intensity   the intensity (brightness) of the light
     * @param cutoffAngle the cutoff angle in radians defining the spotlight cone
     * @param exponent    the exponent controlling the spotlight beam concentration
     */
    public SpotLight(Vector3D position, Vector3D direction, Color color, double intensity, double cutoffAngle, double exponent) {
        super(position, color, intensity);
        setDirection(direction);
        this.cutoffAngle = cutoffAngle;
        this.exponent = exponent;
    }

    /**
     * Returns the normalized direction vector of the spotlight.
     *
     * @return the direction vector
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the direction vector of the spotlight.
     * The vector is normalized internally.
     *
     * @param direction the new direction vector (will be normalized)
     */
    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    /**
     * Returns the cutoff angle of the spotlight in radians.
     *
     * @return the cutoff angle
     */
    public double getCutoffAngle() {
        return cutoffAngle;
    }

    /**
     * Sets the cutoff angle of the spotlight in radians.
     *
     * @param cutoffAngle the new cutoff angle
     */
    public void setCutoffAngle(double cutoffAngle) {
        this.cutoffAngle = cutoffAngle;
    }

    /**
     * Returns the exponent controlling the spotlight's beam concentration.
     *
     * @return the exponent value
     */
    public double getExponent() {
        return exponent;
    }

    /**
     * Sets the exponent controlling the spotlight's beam concentration.
     *
     * @param exponent the new exponent value
     */
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    /**
     * Computes the dot product of the surface normal at the intersection point
     * and the normalized vector pointing from the intersection position
     * to the light's position, attenuated by the spotlight's angular falloff.
     * <p>
     * First, it checks if the point lies within the spotlight cone by comparing
     * the angle between the spotlight direction and the vector from the light
     * to the point with the cutoff angle.
     * If outside the cone, it returns 0.
     * Inside the cone, it applies an angular attenuation using the exponent,
     * and then multiplies by the diffuse term (N·L).
     * </p>
     *
     * @param intersection the intersection containing surface information
     * @return the attenuated dot product of normal and light direction, or 0 if outside cutoff
     */
    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D lightToPoint = Vector3D.substract(intersection.getPosition(), getPosition());
        Vector3D lightToPointDir = Vector3D.normalize(lightToPoint);
        Vector3D spotDir = getDirection();

        double dotDirection = Vector3D.dotProduct(lightToPointDir, spotDir);
        double cosCutoff = Math.cos(cutoffAngle);

        if (dotDirection < cosCutoff) {
            // Outside the spotlight cone
            return 0.0;
        }

        double attenuation = Math.pow(dotDirection, exponent);

        // Calculate diffuse term N·L as in PointLight
        Vector3D pointToLightDir = Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()));
        double nDotL = Math.max(Vector3D.dotProduct(intersection.getNormal(), pointToLightDir), 0.0);

        return nDotL * attenuation;
    }
}
