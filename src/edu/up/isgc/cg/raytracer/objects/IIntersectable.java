package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;

/**
 * Interface representing any object that can be intersected by a ray in the ray tracer.
 *
 * Implementing classes must provide a method to calculate the intersection
 * of the object with a given ray.
 */
public interface IIntersectable {

    /**
     * Computes the intersection between this object and the specified ray.
     *
     * @param ray the ray to test for intersection with this object
     * @return an Intersection object describing the point and details of the intersection,
     *         or null if there is no intersection
     */
    public abstract Intersection getIntersection(Ray ray);
}
