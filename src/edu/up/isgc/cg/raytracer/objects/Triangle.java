package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

/**
 * Represents a triangle in 3D space that can be intersected by rays.
 * Implements the {@link IIntersectable} interface to support ray-tracing operations.
 * <p>
 * The triangle is defined by three vertices, and optionally by vertex normals.
 * If vertex normals are not provided, the face normal is computed from the vertices.
 * </p>
 */
public class Triangle implements IIntersectable {

    /**
     * A small epsilon value used for floating-point comparison tolerances.
     */
    public static final double EPSILON = 1e-13;

    private Vector3D[] vertices;
    private Vector3D[] normals;

    /**
     * Constructs a Triangle with the given three vertices.
     * The normals are set to null and will be computed as the face normal when needed.
     *
     * @param v0 the first vertex of the triangle
     * @param v1 the second vertex of the triangle
     * @param v2 the third vertex of the triangle
     */
    public Triangle(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(v0, v1, v2);
        setNormals(null);
    }

    /**
     * Constructs a Triangle with the specified vertices and vertex normals.
     * If the vertices array length is not 3, default zero vectors are assigned.
     *
     * @param vertices an array of three vertices defining the triangle
     * @param normals an array of vertex normals, or null if normals are not provided
     */
    public Triangle(Vector3D[] vertices, Vector3D[] normals) {
        if (vertices.length == 3) {
            setVertices(vertices[0], vertices[1], vertices[2]);
        } else {
            setVertices(Vector3D.ZERO(), Vector3D.ZERO(), Vector3D.ZERO());
        }
        setNormals(normals);
    }

    /**
     * Returns the vertices of the triangle.
     *
     * @return an array of three vertices
     */
    public Vector3D[] getVertices() {
        return vertices;
    }

    /**
     * Sets the vertices of the triangle.
     *
     * @param vertices an array of three vertices
     */
    private void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }

    /**
     * Sets the vertices of the triangle using individual vertex parameters.
     *
     * @param v0 the first vertex
     * @param v1 the second vertex
     * @param v2 the third vertex
     */
    public void setVertices(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(new Vector3D[]{v0, v1, v2});
    }

    /**
     * Computes and returns the averaged normal vector of the triangle.
     * If vertex normals are provided, the method returns their normalized average.
     * Otherwise, it calculates the face normal from the vertices using the cross product.
     *
     * @return the normalized normal vector of the triangle
     */
    public Vector3D getNormal() {
        Vector3D normal = Vector3D.ZERO();

        if (normals == null) {
            Vector3D[] vertices = getVertices();
            Vector3D v = Vector3D.substract(vertices[1], vertices[0]);
            Vector3D w = Vector3D.substract(vertices[0], vertices[2]);
            normal = Vector3D.normalize(Vector3D.crossProduct(v, w));
        } else {
            for (Vector3D n : normals) {
                normal.setX(normal.getX() + n.getX());
                normal.setY(normal.getY() + n.getY());
                normal.setZ(normal.getZ() + n.getZ());
            }
            normal.setX(normal.getX() / normals.length);
            normal.setY(normal.getY() / normals.length);
            normal.setZ(normal.getZ() / normals.length);
        }
        return normal;
    }

    /**
     * Returns the vertex normals of the triangle.
     * If they are not initialized, computes the face normal and sets it for all vertices.
     *
     * @return an array of three vertex normals
     */
    public Vector3D[] getNormals() {
        if (normals == null) {
            Vector3D normal = getNormal();
            setNormals(new Vector3D[]{normal, normal, normal});
        }
        return normals;
    }

    /**
     * Sets the vertex normals of the triangle.
     *
     * @param normals an array of vertex normals
     */
    private void setNormals(Vector3D[] normals) {
        this.normals = normals;
    }

    /**
     * Sets the vertex normals of the triangle using individual normal vectors.
     *
     * @param vn0 the normal at the first vertex
     * @param vn1 the normal at the second vertex
     * @param vn2 the normal at the third vertex
     */
    public void setNormals(Vector3D vn0, Vector3D vn1, Vector3D vn2) {
        setNormals(new Vector3D[]{vn0, vn1, vn2});
    }

    /**
     * Computes the intersection between this triangle and a given ray.
     * Uses the Möller–Trumbore intersection algorithm.
     *
     * @param ray the ray to test for intersection
     * @return an Intersection object describing the intersection, or
     *         an Intersection with distance -1 if no intersection occurs
     */
    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null);

        Vector3D[] vert = getVertices();
        Vector3D v2v0 = Vector3D.substract(vert[2], vert[0]);
        Vector3D v1v0 = Vector3D.substract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double det = Vector3D.dotProduct(v2v0, vectorP);

        if (Math.abs(det) < EPSILON) {
            return intersection;  // No intersection if determinant is near zero
        }

        double invDet = 1.0 / det;
        Vector3D vectorT = Vector3D.substract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector3D.dotProduct(vectorT, vectorP);

        if (u < 0 || u > 1) {
            return intersection;  // Outside triangle
        }

        Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
        double v = invDet * Vector3D.dotProduct(ray.getDirection(), vectorQ);

        if (v < 0 || (u + v) > (1.0 + EPSILON)) {
            return intersection;  // Outside triangle
        }

        double t = invDet * Vector3D.dotProduct(vectorQ, v1v0);
        intersection.setDistance(t);

        return intersection;
    }
}
