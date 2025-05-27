package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

/**
 * Represents a camera in the raytracer scene.
 *
 * The camera defines the field of view (FOV), resolution, near and far clipping planes,
 * and position in 3D space. It provides functionality to calculate the world space
 * positions of rays corresponding to pixels on the camera's image plane.
 *
 * Implements {@link IIntersectable} but does not physically intersect with rays.
 */
public class Camera implements IIntersectable {
    /**
     * Camera position in world coordinates.
     */
    private Vector3D position;

    /**
     * Field of view angles in degrees.
     * Index 0: Horizontal FOV
     * Index 1: Vertical FOV
     */
    private double[] fieldOfView = new double[2];

    /**
     * Default distance from the camera position to the image plane along the Z-axis.
     */
    private double defaultZ = 15.0;

    /**
     * Resolution of the camera's image plane.
     * Index 0: width (pixels)
     * Index 1: height (pixels)
     */
    private int[] resolution = new int[2];

    /**
     * Near and far clipping planes.
     * Index 0: near plane distance
     * Index 1: far plane distance
     */
    private double[] nearFarPlanes = new double[2];

    /**
     * Constructs a camera with specified position, field of view, resolution,
     * and near/far clipping planes.
     *
     * @param position 3D position of the camera in world space
     * @param fovH horizontal field of view in degrees
     * @param fovV vertical field of view in degrees
     * @param width image width in pixels
     * @param height image height in pixels
     * @param nearPlane near clipping plane distance
     * @param farPlane far clipping plane distance
     */
    public Camera(Vector3D position, double fovH, double fovV,
                  int width, int height, double nearPlane, double farPlane) {
        this.position = position;
        setFOV(fovH, fovV);
        setResolution(width, height);
        setNearFarPlanes(new double[]{nearPlane, farPlane});
    }

    /**
     * Returns the camera position in world coordinates.
     *
     * @return the position vector of the camera
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the camera position in world coordinates.
     *
     * @param position the new position vector of the camera
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Returns the field of view angles (horizontal and vertical) in degrees.
     *
     * @return array containing [horizontalFOV, verticalFOV]
     */
    public double[] getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Sets the field of view angles.
     *
     * @param fieldOfView array with horizontal and vertical FOV in degrees
     */
    private void setFieldOfView(double[] fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    /**
     * Returns the horizontal field of view angle in degrees.
     *
     * @return horizontal FOV angle
     */
    public double getFOVHorizontal() {
        return fieldOfView[0];
    }

    /**
     * Returns the vertical field of view angle in degrees.
     *
     * @return vertical FOV angle
     */
    public double getFOVVertical() {
        return fieldOfView[1];
    }

    /**
     * Sets the horizontal field of view angle in degrees.
     *
     * @param fovH horizontal FOV angle
     */
    public void setFOVHorizontal(double fovH) {
        fieldOfView[0] = fovH;
    }

    /**
     * Sets the vertical field of view angle in degrees.
     *
     * @param fovV vertical FOV angle
     */
    public void setFOVVertical(double fovV) {
        fieldOfView[1] = fovV;
    }

    /**
     * Sets both horizontal and vertical field of view angles in degrees.
     *
     * @param fovH horizontal field of view
     * @param fovV vertical field of view
     */
    public void setFOV(double fovH, double fovV) {
        setFOVHorizontal(fovH);
        setFOVVertical(fovV);
    }

    /**
     * Returns the default distance from the camera to the image plane.
     *
     * @return default Z distance
     */
    public double getDefaultZ() {
        return defaultZ;
    }

    /**
     * Sets the default distance from the camera to the image plane.
     *
     * @param defaultZ new default Z distance
     */
    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }

    /**
     * Returns the resolution (width and height) of the camera image in pixels.
     *
     * @return array containing [width, height]
     */
    public int[] getResolution() {
        return resolution;
    }

    /**
     * Sets the width (in pixels) of the camera image.
     *
     * @param width image width in pixels
     */
    public void setResolutionWidth(int width) {
        resolution[0] = width;
    }

    /**
     * Sets the height (in pixels) of the camera image.
     *
     * @param height image height in pixels
     */
    public void setResolutionHeight(int height) {
        resolution[1] = height;
    }

    /**
     * Sets the resolution (width and height) of the camera image in pixels.
     *
     * @param width image width in pixels
     * @param height image height in pixels
     */
    public void setResolution(int width, int height) {
        setResolutionWidth(width);
        setResolutionHeight(height);
    }

    /**
     * Returns the width (in pixels) of the camera image.
     *
     * @return image width
     */
    public int getResolutionWidth() {
        return resolution[0];
    }

    /**
     * Returns the height (in pixels) of the camera image.
     *
     * @return image height
     */
    public int getResolutionHeight() {
        return resolution[1];
    }

    /**
     * Sets the resolution array directly.
     *
     * @param resolution array with width and height
     */
    private void setResolution(int[] resolution) {
        this.resolution = resolution;
    }

    /**
     * Returns the near and far clipping planes.
     *
     * @return array containing [nearPlane, farPlane]
     */
    public double[] getNearFarPlanes() {
        return nearFarPlanes;
    }

    /**
     * Sets the near and far clipping planes.
     *
     * @param nearFarPlanes array with near and far plane distances
     */
    private void setNearFarPlanes(double[] nearFarPlanes) {
        this.nearFarPlanes = nearFarPlanes;
    }

    /**
     * Calculates the 3D world positions for each pixel on the camera's image plane.
     *
     * The image plane is positioned at a fixed distance (defaultZ) along the Z axis
     * from the camera position. The method computes the spatial coordinates corresponding
     * to each pixel's ray origin on the image plane based on the camera's field of view
     * and resolution.
     *
     * @return a 2D array of {@link Vector3D} positions indexed as [x][y] corresponding to pixels
     */
    public Vector3D[][] calculatePositionsToRay() {
        double angleMaxX = getFOVHorizontal() / 2.0;
        double radiusMaxX = getDefaultZ() / Math.cos(Math.toRadians(angleMaxX));

        double maxX = Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
        double minX = -maxX;

        double angleMaxY = getFOVVertical() / 2.0;
        double radiusMaxY = getDefaultZ() / Math.cos(Math.toRadians(angleMaxY));

        double maxY = Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
        double minY = -maxY;

        Vector3D[][] positions = new Vector3D[getResolutionWidth()][getResolutionHeight()];
        double posZ = defaultZ;

        double stepX = (maxX - minX) / getResolutionWidth();
        double stepY = (maxY - minY) / getResolutionHeight();

        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[x].length; y++) {
                double posX = minX + (stepX * x);
                double posY = maxY - (stepY * y);
                // Position relative to camera position
                positions[x][y] = new Vector3D(
                        position.getX() + posX,
                        position.getY() + posY,
                        position.getZ() + posZ
                );
            }
        }
        return positions;
    }

    /**
     * Since the camera is not a physical object in the scene,
     * it does not intersect with rays.
     *
     * This method always returns an empty intersection indicating no hit.
     *
     * @param ray the ray to test intersection with
     * @return an empty {@link Intersection} with no hit
     */
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
