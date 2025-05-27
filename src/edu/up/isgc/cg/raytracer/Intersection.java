package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.objects.Object3D;

/**
 * Represents the result of an intersection between a Ray and a 3D object in the scene.
 * Stores information about the intersection point, such as its position, distance from
 * the ray's origin, the surface normal at the point of intersection, and the intersected object.
 *
 * @author Your Name
 */
public class Intersection {

    /**
     * Distance from the ray origin to the intersection point.
     */
    private double distance;

    /**
     * Position in 3D space where the intersection occurs.
     */
    private Vector3D position;

    /**
     * Surface normal at the intersection point.
     */
    private Vector3D normal;

    /**
     * The Object3D instance that the ray intersects with.
     */
    private Object3D object;

    /**
     * Constructs an Intersection instance.
     *
     * @param position The position in 3D space where the intersection occurs.
     * @param distance The distance from the ray origin to the intersection point.
     * @param normal The surface normal at the intersection point.
     * @param object The object that has been intersected.
     */
    public Intersection(Vector3D position, double distance, Vector3D normal, Object3D object) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }

    /**
     * Returns the distance from the ray origin to the intersection point.
     * @return The distance to the intersection.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the distance from the ray origin to the intersection point.
     * @param distance The distance to set.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Returns the position of the intersection in 3D space.
     * @return The intersection position.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the position of the intersection in 3D space.
     * @param position The position to set.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Returns the surface normal at the intersection point.
     * @return The normal at the intersection.
     */
    public Vector3D getNormal() {
        return normal;
    }

    /**
     * Sets the surface normal at the intersection point.
     * @param normal The normal to set.
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    /**
     * Returns the object that was intersected.
     * @return The intersected object.
     */
    public Object3D getObject() {
        return object;
    }

    /**
     * Sets the object that was intersected.
     * @param object The object to set.
     */
    public void setObject(Object3D object) {
        this.object = object;
    }
}
