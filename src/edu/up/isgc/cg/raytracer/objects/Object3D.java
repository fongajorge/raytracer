/**
 * An abstract class representing a 3D object in a raytracing context.
 * Provides common properties like position and material, and implements the {@link IIntersectable} interface
 * to enable ray-object intersection calculations. Concrete subclasses must implement intersection logic.
 */
package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.materials.Material;

public abstract class Object3D implements IIntersectable {
    private Vector3D position;
    private Material material;

    /**
     * Constructs a 3D object with specified position and material.
     *
     * @param position The 3D coordinates of the object's position/origin in space
     * @param material The material properties of the object, including color and shading characteristics
     */
    public Object3D(Vector3D position, Material material) {
        setPosition(position);
        setMaterial(material);
    }

    /**
     * Gets the object's position in 3D space.
     * @return A {@link Vector3D} representing the object's position
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the object's position in 3D space.
     * @param position The new position as a {@link Vector3D}
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Gets the material properties of the object.
     * @return The {@link Material} associated with this object
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties of the object.
     * @param material The new {@link Material} to associate with this object
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
