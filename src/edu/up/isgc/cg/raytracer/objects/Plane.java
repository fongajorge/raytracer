package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.materials.Material;

public class Plane extends Object3D {
    private Vector3D normal; // Should be normalized

    public Plane(Vector3D point, Vector3D normal, Material material) {
        super(point, material); // point = any point on plane, like the "origin"
        this.normal = Vector3D.normalize(normal);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        double denom = Vector3D.dotProduct(normal, ray.getDirection());
        if (Math.abs(denom) > 1e-8) { // not parallel
            Vector3D pointOnPlane = getPosition();
            double t = Vector3D.dotProduct(Vector3D.substract(pointOnPlane, ray.getOrigin()), normal) / denom;
            if (t > 0) {
                Vector3D hitPoint = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), t));
                return new Intersection(hitPoint, t, normal, this);
            }
        }
        return null;
    }
}
