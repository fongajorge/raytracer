package edu.up.isgc.cg.raytracer.materials;

import java.awt.Color;

/**
 * A class representing material properties used in ray tracing.
 * This class defines how light interacts with objects. All relevant properties
 * (ambient, diffuse, specular, reflectivity, transparency) are automatically clamped between 0 and 1.
 *
 * @apiNote Requires Java 22 or newer for Math.clamp(double, double, double).
 */
public class Material {
    private Color color;           // Base color of the material
    private double ambient;        // [0, 1]: Ambient reflection coefficient
    private double diffuse;        // [0, 1]: Diffuse reflection coefficient
    private double specular;       // [0, 1]: Specular reflection coefficient
    private double shininess;      // >0: Sharpness of highlights (not clamped)
    private double reflectivity;   // [0, 1]: Portion of reflected light
    private double refractiveIndex;// >=1: Index of refraction for transmission
    private double transparency;   // [0, 1]: Portion of transmitted light

    /**
     * Constructs a new material object with the given properties.
     * Clamped properties are automatically limited to [0, 1].
     *
     * @param color The base color of the material.
     * @param ambient The ambient reflection coefficient [0, 1].
     * @param diffuse The diffuse reflection coefficient [0, 1].
     * @param specular The specular reflection coefficient [0, 1].
     * @param shininess Determines the sharpness of highlights.
     * @param refractiveIndex The material's index of refraction for light entering the surface (>=1).
     * @param reflectivity The amount of light reflected by the material (0 to 1).
     * @param transparency The degree to which light passes through the material (0 to 1).
     */
    public Material(
            Color color,
            double ambient, double diffuse, double specular,
            double shininess,
            double refractiveIndex,
            double reflectivity,
            double transparency
    ) {
        this.color = color;
        this.ambient = Math.clamp(ambient, 0.0, 1.0);
        this.diffuse = Math.clamp(diffuse, 0.0, 1.0);
        this.specular = Math.clamp(specular, 0.0, 1.0);
        this.shininess = shininess;
        this.refractiveIndex = refractiveIndex;
        this.reflectivity = Math.clamp(reflectivity, 0.0, 1.0);
        this.transparency = Math.clamp(transparency, 0.0, 1.0);
    }

    /** Gets the base color of the material. */
    public Color getColor() { return color; }
    /** Sets the base color of the material. */
    public void setColor(Color color) { this.color = color; }

    /** Gets the ambient reflection coefficient (always [0,1]). */
    public double getAmbient() { return ambient; }
    /** Sets the ambient reflection coefficient. Value is clamped to [0,1]. */
    public void setAmbient(double ambient) { this.ambient = Math.clamp(ambient, 0.0, 1.0); }

    /** Gets the diffuse reflection coefficient (always [0,1]). */
    public double getDiffuse() { return diffuse; }
    /** Sets the diffuse reflection coefficient. Value is clamped to [0,1]. */
    public void setDiffuse(double diffuse) { this.diffuse = Math.clamp(diffuse, 0.0, 1.0); }

    /** Gets the specular reflection coefficient (always [0,1]). */
    public double getSpecular() { return specular; }
    /** Sets the specular reflection coefficient. Value is clamped to [0,1]. */
    public void setSpecular(double specular) { this.specular = Math.clamp(specular, 0.0, 1.0); }

    /** Gets the shininess, which determines the sharpness of highlights. */
    public double getShininess() { return shininess; }
    /** Sets the shininess, which determines the sharpness of highlights; higher values = sharper highlights. */
    public void setShininess(double shininess) { this.shininess = shininess; }

    /** Gets the refractive index for light entering the surface. */
    public double getRefractiveIndex() { return refractiveIndex; }
    /** Sets the refractive index for light entering the surface. */
    public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }

    /** Gets the reflectivity of the material ([0,1]). */
    public double getReflectivity() { return reflectivity; }
    /** Sets the reflectivity of the material. Value is clamped to [0,1]. */
    public void setReflectivity(double reflectivity) { this.reflectivity = Math.clamp(reflectivity, 0.0, 1.0); }

    /** Gets the transparency of the material ([0,1]). */
    public double getTransparency() { return transparency; }
    /** Sets the transparency of the material. Value is clamped to [0,1]. */
    public void setTransparency(double transparency) { this.transparency = Math.clamp(transparency, 0.0, 1.0); }

    /** Checks if the material is reflective (reflectivity > 0). */
    public boolean isReflective() { return reflectivity > 0.0; }

    /** Checks if the material is transparent (transparency > 0). */
    public boolean isTransparent() { return transparency > 0.0; }
}
