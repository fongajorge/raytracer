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
import java.util.Date;
import java.util.List;

public class Raytracer {
    public static void main(String[] args) {
        System.out.println(new Date());



//        BufferedImage image = raytrace(scene02);
//        File outputImage = new File("image.png");
//        try {
//            ImageIO.write(image, "png", outputImage);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                double x = posRaytrace[i][j].getX() + pos.getX();
                double y = posRaytrace[i][j].getY() + pos.getY();
                double z = posRaytrace[i][j].getZ() + pos.getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Color pixelColor = traceRay(ray, objects, lights, new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]}, 0, null);
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    private static Color traceRay(Ray ray, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth, Object3D caster) {
        final int MAX_REFLECTIONS = 5;
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
        double refractiveIndex = material.getRefractiveIndex(); // e.g. 1.5 for glass
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
                N = Vector3D.scalarMultiplication(normal, -1); // flip normal
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
}
