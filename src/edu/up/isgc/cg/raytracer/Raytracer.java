package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.*;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.materials.*;
import edu.up.isgc.cg.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Raytracer {
    public static void main(String[] args) {
        System.out.println(new Date());

        // ---- Materials ----
        Material redPlasticMaterial = new Material(
                new Color(220, 40, 40),
                0.1,
                0.7,
                0.4,
                32.0,
                1.0,
                0.0,
                0.0
        );

        Material glassMaterial = new Material(
                new Color(180, 216, 245),
                0.17,
                0.12,
                0.95,
                200.0,
                1.52,
                0.28,
                0.85
        );

        Material silverMetallicMaterial = new Material(
                new Color(192, 192, 192),
                0.03,
                0.10,
                1.0,
                200.0,
                1.0,
                0.3,
                0.0
        );

        Material floorMaterial = new Material(
                new Color(240, 240, 240),
                0.2,
                0.8,
                0.1,
                32.0,
                1.0,
                0.0,
                0.0
        );

        Material floorMaterialGray = new Material(
                Color.DARK_GRAY,
                0.2,
                0.8,
                0.1,
                32.0,
                1.0,
                0.0,
                0.0
        );

        // ---- Scene 1 ----
        Scene scene_1 = new Scene();

        // Camera
        Camera camera4k = new Camera(
                new Vector3D(0, 1.5, 0),
                60.0,
                calculateFOVv(60.0, 1900, 1000),
                4096,
                2160,
                1.0,
                100.0
        );

        Camera cameraHD = new Camera(
                new Vector3D(0, 1.5, 0),
                60.0,
                calculateFOVv(60.0, 1900, 1000),
                1900,
                1000,
                1.0,
                100.0
        );

        Camera cameraLowRes = new Camera(
                new Vector3D(0, 1.5, 0),
                60.0,
                calculateFOVv(60.0, 475, 250),
                475,
                250,
                1.0,
                100.0
        );

        Camera cameraUltralowRes = new Camera(
                new Vector3D(0, 1.5, 0),
                60.0,
                calculateFOVv(60.0, 475, 250),
                190,
                100,
                1.0,
                100.0
        );

        // Lights
        DirectionalLight mainLight = new DirectionalLight(
                new Vector3D(0, 0, 1),
                Color.WHITE,
                1
        );

        PointLight pointLight = new PointLight(
                new Vector3D(0, 5, 28),
//                new Color(0, 216, 245),
                Color.WHITE,
                1
        );

        PointLight pointLightBlue = new PointLight(
                new Vector3D(-5, 5, 8.5),
//                new Color(0, 216, 245),
                Color.BLUE,
                1
        );

        PointLight pointLightGreen = new PointLight(
                new Vector3D(5, 5, 8.5),
//                new Color(0, 216, 245),
                Color.GREEN,
                1
        );

        PointLight pointLightRed = new PointLight(
                new Vector3D(0, 4, 8),
//                new Color(0, 216, 245),
                Color.RED,
                1
        );

        AreaLight areaLight = new AreaLight(
                new Vector3D(0, 0, 25),
                new Vector3D(40, 0, 0),
                new Vector3D(0, 0, 20),
                Color.WHITE,
                1.0,
                16
        );

        // 3D Objects
        Plane plane = new Plane(
                new Vector3D(0, -1, 0),
                new Vector3D(0, 1, 0),
                floorMaterial
        );

        Plane plane_2 = new Plane(
                new Vector3D(0, 0, 30),
                new Vector3D(0, 0, -1),
                floorMaterialGray
        );

        Model3D klein = OBJReader.getModel3D(
                "Klein.obj",
                new Vector3D(0, -0.75, 15),
                new Vector3D(2, 2, 2),
                new Vector3D(0, 0, 0),
                glassMaterial
        );

        Model3D cube_1 = OBJReader.getModel3D(
                "cube.obj",
                new Vector3D(-2.35, 0, 12),
                new Vector3D(1, 1, 1),
                new Vector3D(-45, -45, 0),
                glassMaterial
        );

        Model3D cube_2 = OBJReader.getModel3D(
                "cube.obj",
                new Vector3D(1.9, 0, 12),
                new Vector3D(1, 1, 1),
                new Vector3D(45, 45, 0),
                glassMaterial
        );

        Model3D ring_1 = OBJReader.getModel3D(
                "ring.obj",
                new Vector3D(-0.2, 1.1, 10),
                new Vector3D(0.5, 0.5, 0.5),
                new Vector3D(45, 45, 0),
                silverMetallicMaterial
        );

        Model3D ring_2 = OBJReader.getModel3D(
                "ring.obj",
                new Vector3D(0.2, 0.9, 10),
                new Vector3D(0.5, 0.5, 0.5),
                new Vector3D(45, 135, 0),
                silverMetallicMaterial
        );

        Model3D ring_3 = OBJReader.getModel3D(
                "ring.obj",
                new Vector3D(0, 1.5, 1.15),
                new Vector3D(0.5, 0.5, 0.5),
                new Vector3D(90, 0, 0),
                silverMetallicMaterial
        );

        // ---- Scene setup ----
        // Scene 1
        scene_1.setCamera(cameraLowRes);

//        scene_1.addLight(mainLight);
//        scene_1.addLight(pointLight);
        scene_1.addLight(pointLightBlue);
        scene_1.addLight(pointLightGreen);
        scene_1.addLight(pointLightRed);

        scene_1.addObject(plane);
        scene_1.addObject(plane_2);

        scene_1.addObject(klein);
        scene_1.addObject(ring_1);
        scene_1.addObject(ring_2);
        scene_1.addObject(ring_3);
        scene_1.addObject(cube_1);
        scene_1.addObject(cube_2);

        // ---- Render ---
        BufferedImage image = raytrace(scene_1);
        File outputImage = new File("Render1.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector3D pos = mainCamera.getPosition();
        double cameraZ = pos.getZ();

        // Depth of field parameters
        final int DOF_SAMPLES = 5;
        final double APERTURE_SIZE = 0.09;
        final double FOCUS_DISTANCE = 10.0;
        final int height = posRaytrace.length;
        final int width = posRaytrace[0].length;

        // -- Parallel Section --
        int availableThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableThreads);

        // Use a threadsafe variable for progress
        final AtomicInteger rowsCompleted = new AtomicInteger(0);
        final int[] lastPercent = new int[1]; // Only updated by main thread

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            final int row = i; // must be effectively final for lambda
            futures.add(executor.submit(() -> {
                for (int j = 0; j < width; j++) {
                    double[] rgb = new double[3];
                    for (int s = 0; s < DOF_SAMPLES; s++) {
                        Vector3D pixelOnImagePlane = posRaytrace[row][j];
                        Vector3D camPos = mainCamera.getPosition();

                        Vector3D dir = Vector3D.substract(pixelOnImagePlane, camPos);
                        double t_focal = (FOCUS_DISTANCE - camPos.getZ()) / dir.getZ();
                        Vector3D focusPoint = Vector3D.add(camPos, Vector3D.scalarMultiplication(dir, t_focal));

                        // Disk sample for lens
                        double[] lensSample = sampleDisk(APERTURE_SIZE * 0.5);
                        Vector3D lensPos = new Vector3D(camPos.getX() + lensSample[0], camPos.getY() + lensSample[1], camPos.getZ());

                        Vector3D dofDir = Vector3D.substract(focusPoint, lensPos);
                        dofDir = Vector3D.normalize(dofDir);
                        Ray ray = new Ray(lensPos, dofDir);

                        Color pixelColor = traceRay(ray, objects, lights, new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]}, 0, null);
                        rgb[0] += pixelColor.getRed();
                        rgb[1] += pixelColor.getGreen();
                        rgb[2] += pixelColor.getBlue();
                    }
                    // Average samples and clamp
                    int R = (int) Math.clamp(Math.round(rgb[0] / DOF_SAMPLES), 0, 255);
                    int G = (int) Math.clamp(Math.round(rgb[1] / DOF_SAMPLES), 0, 255);
                    int B = (int) Math.clamp(Math.round(rgb[2] / DOF_SAMPLES), 0, 255);

                    Color col = new Color(R, G, B);
                    image.setRGB(row, j, col.getRGB());
                }

                int done = rowsCompleted.incrementAndGet();
                int percent = (int) ((done) * 100.0 / height);
                // Only the first thread to reach a new percent milestone prints:
                synchronized (lastPercent) {
                    if (percent != lastPercent[0]) {
                        System.out.println("Render progress: " + percent + "%");
                        lastPercent[0] = percent;
                    }
                }
            }));
        }

        // Wait for all threads to complete
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        return image;
    }



    private static Color traceRay(Ray ray, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth, Object3D caster) {
        final int MAX_REFLECTIONS = 2;
        if (depth > MAX_REFLECTIONS) return Color.BLACK;
        Intersection closestIntersection = raycast(ray, objects, caster, clippingPlanes);
        if (closestIntersection == null) return Color.BLACK;

        Vector3D intersectionPos = closestIntersection.getPosition();
        Vector3D normal = Vector3D.normalize(closestIntersection.getNormal());
        Material material = closestIntersection.getObject().getMaterial();
        Color baseColor = material.getColor();

        // View direction
        Vector3D viewDir = Vector3D.normalize(Vector3D.substract(ray.getOrigin(), intersectionPos));

        // Ambient part
        double[] ambientRGB = new double[3];
        ambientRGB[0] = baseColor.getRed() / 255.0 * material.getAmbient();
        ambientRGB[1] = baseColor.getGreen() / 255.0 * material.getAmbient();
        ambientRGB[2] = baseColor.getBlue() / 255.0 * material.getAmbient();

        // Accumulators
        double[] rgb = { ambientRGB[0], ambientRGB[1], ambientRGB[2] };

        for (Light light : lights) {
            // Light direction: from intersection TO light
            Vector3D lightDir = Vector3D.normalize(Vector3D.substract(light.getPosition(), intersectionPos));
            double lightDistance = Vector3D.magnitude(Vector3D.substract(light.getPosition(), intersectionPos));

            // --- Shadow check ---
            // Offset to avoid self-intersection
            Vector3D shadowOrigin = Vector3D.add(intersectionPos, Vector3D.scalarMultiplication(normal, 1e-4));
            Ray shadowRay = new Ray(shadowOrigin, lightDir);

            // Ignore current object for self-occlusion
            Intersection shadowHit = raycast(shadowRay, objects, closestIntersection.getObject(), null);

            boolean inShadow = false;
            if (shadowHit != null) {
                double distToHit = shadowHit.getDistance();
                if (distToHit > 1e-5 && distToHit < lightDistance - 1e-5) {
                    inShadow = true;
                }
            }

            if (inShadow) {
                continue; // no diffuse/specular from this light if in shadow
            }

            double nDotL = Math.max(0.0, Vector3D.dotProduct(normal, lightDir));
            double intensity = light.getIntensity();
            Color lightColor = light.getColor();
            double[] lightRGB = new double[] {
                    lightColor.getRed() / 255.0,
                    lightColor.getGreen() / 255.0,
                    lightColor.getBlue() / 255.0
            };

            // ---- Diffuse ----
            for (int k = 0; k < 3; k++) {
                rgb[k] += getRGBComponent(material.getColor(), k) * nDotL * lightRGB[k] * material.getDiffuse() * intensity;
            }
            // ---- Specular ---- Blinn-Phong
            Vector3D h = Vector3D.normalize(Vector3D.add(lightDir, viewDir));
            double nDotH = Math.max(0.0, Vector3D.dotProduct(normal, h));
            double specIntensity = Math.pow(nDotH, material.getShininess()) * material.getSpecular() * intensity;
            for (int k = 0; k < 3; k++) {
                rgb[k] += lightRGB[k] * specIntensity;
            }
        }

        // Reflection
        double reflectivity = material.getReflectivity();
        Color reflectedColor = Color.BLACK;
        if (reflectivity > 0) {
            double dot = Vector3D.dotProduct(ray.getDirection(), normal);
            Vector3D R = Vector3D.substract(ray.getDirection(), Vector3D.scalarMultiplication(normal, 2 * dot));
            R = Vector3D.normalize(R);
            Vector3D offsetOrigin = Vector3D.add(intersectionPos, Vector3D.scalarMultiplication(normal, 1e-4));
            Ray reflectedRay = new Ray(offsetOrigin, R);
            reflectedColor = traceRay(reflectedRay, objects, lights, null, depth + 1, closestIntersection.getObject());
        }

        // ---- Refraction ----
        double transparency = material.getTransparency();
        double refractiveIndex = material.getRefractiveIndex();
        Color refractedColor = Color.BLACK;

        if (transparency > 0) {
            // n1: index outside, n2: index inside
            double n1, n2;
            Vector3D N = normal;
            Vector3D V = ray.getDirection();
            boolean outside = Vector3D.dotProduct(viewDir, normal) > 0;

            if (outside) {
                n1 = 1.0; // air
                n2 = refractiveIndex;
            } else {
                n1 = refractiveIndex;
                n2 = 1.0; // leaving material
                N = Vector3D.scalarMultiplication(normal, -1);
            }

            double cosI = -Vector3D.dotProduct(N, V);
            double eta = n1 / n2;
            double sin2T = eta * eta * (1.0 - cosI * cosI);

            if (sin2T <= 1.0) { // No Total Internal Reflection
                double cosT = Math.sqrt(1.0 - sin2T);
                Vector3D refractedDir = Vector3D.add(
                        Vector3D.scalarMultiplication(V, eta),
                        Vector3D.scalarMultiplication(N, eta * cosI - cosT)
                );
                refractedDir = Vector3D.normalize(refractedDir);

                Vector3D offsetOrigin = Vector3D.add(intersectionPos, Vector3D.scalarMultiplication(refractedDir, 1e-4));
                Ray refractedRay = new Ray(offsetOrigin, refractedDir);
                refractedColor = traceRay(refractedRay, objects, lights, null, depth + 1, closestIntersection.getObject());
            }
        }

        // Compose output
        Color surfaceColor = new Color(
                (float)Math.clamp(rgb[0], 0.0, 1.0),
                (float)Math.clamp(rgb[1], 0.0, 1.0),
                (float)Math.clamp(rgb[2], 0.0, 1.0)
        );
        double localDiffuseFactor = (1.0 - reflectivity) * (1.0 - transparency);
        double localReflectFactor = reflectivity * (1.0 - transparency);

        Color diffusePart = multiplyColor(surfaceColor, localDiffuseFactor);
        Color reflectedPart = multiplyColor(reflectedColor, localReflectFactor);
        Color refractedPart = multiplyColor(refractedColor, transparency);

        return addColor(addColor(diffusePart, reflectedPart), refractedPart);
    }

    public static Color multiplyColor(Color color, double factor) {
        float red = (float) (color.getRed() / 255.0 * factor);
        float green = (float) (color.getGreen() / 255.0 * factor);
        float blue = (float) (color.getBlue() / 255.0 * factor);
        red = (float) Math.clamp(red, 0.0, 1.0);
        green = (float) Math.clamp(green, 0.0, 1.0);
        blue = (float) Math.clamp(blue, 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static double getRGBComponent(Color color, int k) {
        switch(k) {
            case 0: return color.getRed() / 255.0;
            case 1: return color.getGreen() / 255.0;
            case 2: return color.getBlue() / 255.0;
            default: return 0;
        }
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }

    public static double calculateFOVv(double fovH, int w, int h) {
        double ar = (double) w / h;
        return 2.0 * Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fovH / 2.0)) / ar));
    }

    private static double[] sampleDisk(double radius) {
        double r = radius * Math.sqrt(Math.random());
        double theta = 2 * Math.PI * Math.random();
        return new double[] { r * Math.cos(theta), r * Math.sin(theta) };
    }
}
