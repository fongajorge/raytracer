package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.objects.Object3D;
import edu.up.isgc.cg.raytracer.objects.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 3D scene for the raytracer.
 *
 * A scene contains a camera, a list of objects that can be rendered (objects in the world),
 * and a list of lights that illuminate the scene.
 *
 * @author Your Name
 */
public class Scene {

    /** The camera used to view and render the scene. */
    private Camera camera;
    /** The list of 3D objects present in the scene. */
    private List<Object3D> objects;
    /** The list of lights illuminating the scene. */
    private List<Light> lights;

    /**
     * Creates an empty scene with no camera, objects, or lights.
     * The object and light lists are initialized to be empty.
     */
    public Scene() {
        setObjects(new ArrayList<>());
        setLights(new ArrayList<>());
    }

    /**
     * Gets the camera of the scene.
     *
     * @return The camera object.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the camera for the scene.
     *
     * @param camera The camera to set.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Adds an object to the scene.
     *
     * @param object The 3D object to add.
     */
    public void addObject(Object3D object) {
        getObjects().add(object);
    }

    /**
     * Gets the list of objects in the scene.
     *
     * @return A list of all Object3D instances in the scene.
     */
    public List<Object3D> getObjects() {
        if(objects == null){
            objects = new ArrayList<>();
        }
        return objects;
    }

    /**
     * Sets the list of objects in the scene.
     *
     * @param objects The list of objects to set.
     */
    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    /**
     * Gets the list of lights in the scene.
     *
     * @return A list of Light instances illuminating the scene.
     */
    public List<Light> getLights() {
        if(lights == null){
            lights = new ArrayList<>();
        }
        return lights;
    }

    /**
     * Sets the list of lights in the scene.
     *
     * @param lights The list of lights to set.
     */
    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    /**
     * Adds a light source to the scene.
     *
     * @param light The Light instance to add.
     */
    public void addLight(Light light){
        getLights().add(light);
    }
}
