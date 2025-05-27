package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.materials.Material;
import edu.up.isgc.cg.raytracer.tools.Barycentric;

import java.util.*;

/**
 * A class representing a 3D model composed of multiple triangles.
 * The model inherits common properties from {@link Object3D} and supports ray intersection logic
 * using barycentric coordinates for smooth shading via interpolated normals.
 */
public class Model3D extends Object3D {
    private List<Triangle> triangles;

    /**
     * Constructs a 3D model at a given position using a list of triangles and a material.
     * All triangle vertex positions are adjusted relative to the model's position.
     *
     * @param position The position of the model in 3D space.
     * @param triangles The array of {@link Triangle} objects that make up the model.
     * @param material The {@link Material} defining the surface properties of the model.
     */
    public Model3D(Vector3D position, Triangle[] triangles, Material material) {
        super(position, material);
        setTriangles(triangles);
    }

    /**
     * Returns the list of triangles that compose the model.
     *
     * @return A {@link List} of {@link Triangle} objects.
     */
    public List<Triangle> getTriangles() {
        return triangles;
    }

    /**
     * Sets the triangles for the model and adjusts their vertices relative to the model's position.
     *
     * @param triangles An array of {@link Triangle} objects that define the model's geometry.
     */
    public void setTriangles(Triangle[] triangles) {
        Vector3D position = getPosition();
        Set<Vector3D> uniqueVertices = new HashSet<>();

        for (Triangle triangle : triangles) {
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for (Vector3D vertex : uniqueVertices) {
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }

        this.triangles = Arrays.asList(triangles);
    }

    /**
     * Calculates the closest intersection between the model's triangles and a given ray.
     * If an intersection is found, interpolates the normal using barycentric coordinates.
     *
     * @param ray The {@link Ray} to test for intersection.
     * @return An {@link Intersection} object if a hit is found; otherwise, null.
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        double closestDistance = -1;
        Vector3D intersectionPoint = Vector3D.ZERO();
        Vector3D interpolatedNormal = Vector3D.ZERO();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            double distance = intersection.getDistance();

            if (distance > 0 && (distance < closestDistance || closestDistance < 0)) {
                closestDistance = distance;
                intersectionPoint = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));

                double[] baryCoords = Barycentric.CalculateBarycentricCoordinates(intersectionPoint, triangle);
                Vector3D[] triangleNormals = triangle.getNormals();
                interpolatedNormal = Vector3D.ZERO();

                for (int i = 0; i < baryCoords.length; i++) {
                    interpolatedNormal = Vector3D.add(interpolatedNormal, Vector3D.scalarMultiplication(triangleNormals[i], baryCoords[i]));
                }
            }
        }

        if (closestDistance == -1) {
            return null;
        }

        return new Intersection(intersectionPoint, closestDistance, interpolatedNormal, this);
    }
}
