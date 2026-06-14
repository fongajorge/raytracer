# Java 3D Raytracer Engine

A custom, multi-threaded CPU 3D Raytracer built from scratch in Java. This engine simulates physical light transport to render photo-realistic 3D scenes, supporting complex OBJ meshes, recursive reflections, refraction, and realistic camera effects.

## 🚀 Features

* **Multi-Threaded Rendering:** Utilizes Java's `ExecutorService` to distribute ray tracing calculations across all available CPU cores, significantly speeding up render times.
* **Advanced Lighting & Shadows:** * Blinn-Phong specular reflection model.
  * `AreaLight` implementation for realistic **soft shadows** using jittered disk sampling.
  * Support for colored `PointLight`s and intersecting light bounds.
* **Physically Based Materials:**
  * **Refraction (Glass/Water):** Calculates light bending using Snell's Law, including Total Internal Reflection (TIR).
  * **Reflection (Mirrors/Metals):** Recursive ray bouncing (up to 2 max bounces) for metallic surfaces.
  * Configurable ambient, diffuse, specular, and shininess coefficients.
* **Camera Effects:**
  * **Depth of Field (DoF):** Simulates physical camera lens apertures and focal distances to generate foreground and background blur.
  * Adjustable Horizontal/Vertical Field of View (FOV) and clipping planes.
* **3D Geometry & Meshes:**
  * Native Wavefront `.obj` file parser (`OBJReader`).
  * Vertex normal smoothing for smooth surface shading.

## 🖼️ Example Renders

![Render 1 - Depth of Field](Render_1.png)
*Example of light refraction through a glass Klein bottle and complex multi-colored light interactions, featuring heavy Depth of Field (DoF) blurring around the camera lens edges.*

![Render 2 - Depth of Field](Render_1.png)
*Example of complex .obj mesh rendering, showcasing a metallic bust on a pedestal framed against low-poly mountains and a prominent background light source.*

![Render 3 - Abstract Shapes and Point Lights](Render_3.png)
*Example of multiple colored point lights (magenta and green) generating distinct specular highlights on a highly reflective, ribbed abstract model.*

## ⚙️ Project Architecture

* **`Raytracer.java`**: The core execution loop. Handles multi-threading, the recursive `traceRay` method, shadow occlusion checks, and pixel color accumulation.
* **`Scene.java`**: Orchestrates the world state, holding the `Camera`, `Object3D` entities, and `Light` sources.
* **`Camera.java`**: Calculates ray origins and directions mapped to a 2D image plane based on resolution and FOV.
* **`AreaLight.java`**: Distributes random samples across a rectangular plane to compute average light intensity, creating soft shadows.
* **`OBJReader.java`**: Reads `.obj` files, parses vertices and faces, applies affine transformations (scale/rotation), and groups faces into `Triangle` arrays.

## 👨‍💻 Authors
- **Jorge Fong @fongajorge**
- **Jafet Rodriguez**
