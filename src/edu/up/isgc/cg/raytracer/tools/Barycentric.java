package edu.up.isgc.cg.raytracer.tools;

import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.objects.*;

/**
 * Utility class for calculating barycentric coordinates of a point with respect to a triangle in 3D space.
 * <p>
 * This implementation is based on Christer Ericson's algorithm from
 * <a href="https://www.amazon.com/Real-Time-Collision-Detection-Interactive-Technology/dp/1558607323">Real-Time Collision Detection</a>.
 * </p>
 * <p>
 * Barycentric coordinates represent the point as a weighted average of the triangle's vertices.
 * They are often used in computer graphics for interpolation, hit testing, and collision detection.
 * </p>
 * <p>
 * This class is not meant to be instantiated; all methods are static.
 * </p>
 *
 * @author Jafet Rodríguez
 */
public class Barycentric {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Barycentric() {
    }

    /**
     * Calculates the barycentric coordinates (u, v, w) of a given point relative to a triangle.
     *
     * <p>The barycentric coordinates satisfy the relation:</p>
     * <pre>
     * point = u * vertexA + v * vertexB + w * vertexC
     * </pre>
     * <p>where u + v + w = 1.</p>
     *
     * @param point the 3D point for which barycentric coordinates are computed
     * @param triangle the triangle defined by three vertices
     * @return an array of doubles representing the barycentric coordinates {u, v, w}
     *
     * @throws IllegalArgumentException if the triangle's vertices are collinear (denominator equals zero)
     */
    public static double[] CalculateBarycentricCoordinates(Vector3D point, Triangle triangle) {
        double u, v, w;
        Vector3D[] vertices = triangle.getVertices();
        Vector3D a = vertices[0];
        Vector3D b = vertices[1];
        Vector3D c = vertices[2];

        Vector3D v0 = Vector3D.substract(b, a);
        Vector3D v1 = Vector3D.substract(c, a);
        Vector3D v2 = Vector3D.substract(point, a);
        double d00 = Vector3D.dotProduct(v0, v0);
        double d01 = Vector3D.dotProduct(v0, v1);
        double d11 = Vector3D.dotProduct(v1, v1);
        double d20 = Vector3D.dotProduct(v2, v0);
        double d21 = Vector3D.dotProduct(v2, v1);
        double denominator = d00 * d11 - d01 * d01;

        if (denominator == 0) {
            throw new IllegalArgumentException("Triangle vertices are collinear or too close to each other.");
        }

        v = (d11 * d20 - d01 * d21) / denominator;
        w = (d00 * d21 - d01 * d20) / denominator;
        u = 1.0 - v - w;

        return new double[]{u, v, w};
    }
}
