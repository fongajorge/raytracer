package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.materials.Material;

/**
 * Represents a sphere in 3D space with a given position and radius.
 * Inherits material properties from {@link Object3D} and supports ray-sphere intersection testing.
 */
public class Sphere extends Object3D {
    private double radius;

    /**
     * Constructs a new Sphere object at a given position with a radius and material.
     *
     * @param position The position (center) of the sphere in 3D space.
     * @param radius   The radius of the sphere.
     * @param material The {@link Material} defining the sphere's surface properties.
     */
    public Sphere(Vector3D position, double radius, Material material) {
        super(position, material);
        setRadius(radius);
    }

    /**
     * Gets the radius of the sphere.
     *
     * @return The radius as a double.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the sphere.
     *
     * @param radius The new radius value.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Calculates the intersection point of the sphere with a given ray.
     * If a valid intersection exists, returns an {@link Intersection} object with hit details.
     *
     * @param ray The {@link Ray} to test for intersection.
     * @return An {@link Intersection} if the ray hits the sphere, otherwise null.
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D originToCenter = Vector3D.substract(getPosition(), ray.getOrigin());
        double tca = Vector3D.dotProduct(originToCenter, ray.getDirection());
        double distanceSquared = Vector3D.magnitude(originToCenter);
        double d2 = Math.pow(distanceSquared, 2) - Math.pow(tca, 2);

        double radiusSquared = Math.pow(getRadius(), 2);
        if (d2 > radiusSquared) {
            return null;
        }

        double thc = Math.sqrt(radiusSquared - d2);
        double t0 = tca - thc;
        double t1 = tca + thc;

        double distance = (t0 < 0) ? t1 : t0;
        if (distance < 0) {
            return null;
        }

        Vector3D intersectionPoint = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
        Vector3D normal = Vector3D.normalize(Vector3D.substract(intersectionPoint, getPosition()));
        return new Intersection(intersectionPoint, distance, normal, this);
    }
}
