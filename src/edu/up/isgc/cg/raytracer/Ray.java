/**
 * Represents a ray in 3D space defined by an origin point and a direction vector.
 * The direction vector is stored as provided but automatically normalized when retrieved.
 */
package edu.up.isgc.cg.raytracer;

public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    /**
     * Constructs a Ray with the specified origin and direction.
     * Note: The direction vector is stored as-is but normalized during retrieval.
     *
     * @param origin The starting point of the ray in 3D space.
     * @param direction The direction vector of the ray (normalized when retrieved via {@link #getDirection()}).
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * Gets the origin point of the ray.
     *
     * @return The Vector3D representing the ray's origin.
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     * Sets the origin point of the ray.
     *
     * @param origin The new origin vector.
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * Returns the <strong>normalized</strong> direction vector of the ray.
     *
     * @return A unit vector representing the ray's direction.
     * @throws ArithmeticException If the stored direction vector has zero magnitude (cannot be normalized).
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }

    /**
     * Sets the direction vector of the ray. The vector is stored as provided but will be
     * automatically normalized when accessed via {@link #getDirection()}.
     *
     * @param direction The new direction vector (does not need to be normalized).
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
