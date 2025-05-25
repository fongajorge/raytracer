/**
 * An abstract base class representing a 3D object in a raytracing context.
 * Provides common properties like position and color, and implements the {@link IIntersectable} interface
 * to enable ray-object intersection calculations. Concrete subclasses must implement intersection logic.
 */
package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double reflectivity; // New field

    /**
     * Constructs a 3D object with specified position and color.
     *
     * @param position The 3D coordinates of the object's position/origin in space.
     * @param color The base color of the object, used for shading calculations.
     */
    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }

    /**
     * Constructs a 3D object with specified position, color, and reflectivity.
     *
     * @param position The 3D coordinates of the object's position/origin in space.
     * @param color The base color of the object.
     * @param reflectivity The reflection coefficient (0.0 for no reflection, 1.0 for mirror-like).
     */
    public Object3D(Vector3D position, Color color, double reflectivity) {
        setPosition(position);
        setColor(color);
        setReflectivity(reflectivity);
    }

    /**
     * Gets the object's color.
     * @return The {@link Color} of the object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the object's color.
     * @param color The new {@link Color} to apply to the object.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the object's position in 3D space.
     * @return A {@link Vector3D} representing the object's position.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the object's position in 3D space.
     * @param position The new position as a {@link Vector3D}.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Gets the object's reflectivity.
     * @return The reflection coefficient as a double.
     */
    public double getReflectivity() {
        return reflectivity;
    }

    /**
     * Sets the object's reflectivity.
     * @param reflectivity The new reflection coefficient (clamped between 0.0 and 1.0).
     */
    public void setReflectivity(double reflectivity) {
        this.reflectivity = Math.max(0.0, Math.min(1.0, reflectivity)); // Ensure valid range
    }
}
