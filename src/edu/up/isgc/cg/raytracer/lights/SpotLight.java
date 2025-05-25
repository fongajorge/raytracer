package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import java.awt.*;

public class SpotLight extends Light {
    private Vector3D direction;
    private double cutoffAngle;
    private double exponent;

    public SpotLight(Vector3D position, Vector3D direction, Color color, double intensity, double cutoffAngle, double exponent) {
        super(position, color, intensity);
        setDirection(direction);
        this.cutoffAngle = cutoffAngle;
        this.exponent = exponent;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    public double getCutoffAngle() {
        return cutoffAngle;
    }

    public void setCutoffAngle(double cutoffAngle) {
        this.cutoffAngle = cutoffAngle;
    }

    public double getExponent() {
        return exponent;
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D lightToPoint = Vector3D.substract(intersection.getPosition(), getPosition());
        Vector3D lightToPointDir = Vector3D.normalize(lightToPoint);
        Vector3D spotDir = getDirection();

        double dotDirection = Vector3D.dotProduct(lightToPointDir, spotDir);
        double cosCutoff = Math.cos(cutoffAngle);

        if (dotDirection < cosCutoff) {
            return 0.0;
        }

        double attenuation = Math.pow(dotDirection, exponent);

        // Calculate N·L as in PointLight
        Vector3D pointToLightDir = Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()));
        double nDotL = Math.max(Vector3D.dotProduct(intersection.getNormal(), pointToLightDir), 0.0);

        return nDotL * attenuation;
    }
}