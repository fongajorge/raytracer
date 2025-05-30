/**
 * Represents a directional light source, such as sunlight, which has a specific direction
 * but no defined position. The light rays are parallel and come from an infinite distance.
 */
package edu.up.isgc.cg.raytracer;

public class Vector3D {
    private static final Vector3D ZERO = new Vector3D(0.0, 0.0, 0.0);
    private double x, y, z;

    /**
     * Constructs a Vector3D with specified x, y, and z components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     * @param z The z-component of the vector.
     */
    public Vector3D(double x, double y, double z){
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * @return The x-component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-component of the vector.
     * @param x The new x-component value.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return The y-component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-component of the vector.
     * @param y The new y-component value.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return The z-component of the vector.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z-component of the vector.
     * @param z The new z-component value.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Creates a copy of this vector.
     * @return A new Vector3D instance with identical components.
     */
    public Vector3D clone(){
        return new Vector3D(getX(), getY(), getZ());
    }

    /**
     * Provides a static zero vector. Returns a clone to prevent modification of the static instance.
     * @return A new Vector3D instance representing (0, 0, 0).
     */
    public static Vector3D ZERO(){
        return ZERO.clone();
    }

    /**
     * Returns a string representation of the vector in the format "Vector3D{x=..., y=..., z=...}".
     * @return The formatted string.
     */
    @Override
    public String toString(){
        return "Vector3D{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                "}";
    }

    /**
     * Computes the dot product of two vectors.
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The scalar dot product result.
     */
    public static double dotProduct(Vector3D vectorA, Vector3D vectorB){
        return (vectorA.getX() * vectorB.getX()) + (vectorA.getY() * vectorB.getY()) + (vectorA.getZ() * vectorB.getZ());
    }

    /**
     * Computes the cross product of two vectors.
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return A new Vector3D perpendicular to both input vectors.
     */
    public static Vector3D crossProduct(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D((vectorA.getY() * vectorB.getZ()) - (vectorA.getZ() * vectorB.getY()),
                (vectorA.getZ() * vectorB.getX()) - (vectorA.getX() * vectorB.getZ()),
                (vectorA.getX() * vectorB.getY()) - (vectorA.getY() * vectorB.getX()));
    }

    /**
     * Calculates the magnitude (Euclidean length) of a vector.
     * @param vectorA The input vector.
     * @return The magnitude as a double.
     */
    public static double magnitude (Vector3D vectorA){
        return Math.sqrt(dotProduct(vectorA, vectorA));
    }

    /**
     * Adds two vectors component-wise.
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return A new Vector3D representing the sum.
     */
    public static Vector3D add(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY(), vectorA.getZ() + vectorB.getZ());
    }

    /**
     * Subtracts the second vector from the first component-wise.
     * @param vectorA The vector to subtract from.
     * @param vectorB The vector to subtract.
     * @return A new Vector3D representing the difference.
     */
    public static Vector3D substract(Vector3D vectorA, Vector3D vectorB){
        return new Vector3D(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY(), vectorA.getZ() - vectorB.getZ());
    }

    /**
     * Normalizes a vector to unit length.
     * @param vectorA The input vector.
     * @return A new Vector3D in the same direction with magnitude 1.
     * @throws ArithmeticException If the input vector has zero magnitude.
     */
    public static Vector3D normalize(Vector3D vectorA){
        double mag = Vector3D.magnitude(vectorA);
        return new Vector3D(vectorA.getX() / mag, vectorA.getY() / mag, vectorA.getZ() / mag);
    }

    /**
     * Multiplies a vector by a scalar value.
     * @param vectorA The input vector.
     * @param scalar The scalar multiplier.
     * @return A new Vector3D with scaled components.
     */
    public static Vector3D scalarMultiplication(Vector3D vectorA, double scalar){
        return new Vector3D(vectorA.getX() * scalar, vectorA.getY() * scalar, vectorA.getZ() * scalar);
    }

    public static Vector3D rotateVector(Vector3D v, Vector3D radRotation) {
        Vector3D rotation = degreesToRadians(radRotation);

        // X axis
        double cosX = Math.cos(rotation.getX());
        double sinX = Math.sin(rotation.getX());
        double y1 = v.getY() * cosX - v.getZ() * sinX;
        double z1 = v.getY() * sinX + v.getZ() * cosX;

        // Y axis
        double cosY = Math.cos(rotation.getY());
        double sinY = Math.sin(rotation.getY());
        double x2 = v.getX() * cosY + z1 * sinY;
        double z2 = -v.getX() * sinY + z1 * cosY;

        // Z axis
        double cosZ = Math.cos(rotation.getZ());
        double sinZ = Math.sin(rotation.getZ());
        double x3 = x2 * cosZ - y1 * sinZ;
        double y3 = x2 * sinZ + y1 * cosZ;

        return new Vector3D(x3, y3, z2);
    }

    public static Vector3D degreesToRadians(Vector3D degrees){
        return new Vector3D(
                Math.toRadians(degrees.getX()),
                Math.toRadians(degrees.getY()),
                Math.toRadians(degrees.getZ())
        );
    }
}
