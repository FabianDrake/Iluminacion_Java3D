package pkg3djava;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

public class Main {

    public static void main(String[] args) {
        // Configuración de la ventana
        JFrame frame = new JFrame("Practica 12 - 22110092");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); // Centrar la ventana
        frame.setVisible(true);

        // Crear el lienzo 3D
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        frame.add(canvas);

        // Crear un universo simple
        SimpleUniverse universe = new SimpleUniverse(canvas);

        // Crear un nodo raíz para el contenido
        BranchGroup group = new BranchGroup();

        // Añadir imagen de fondo
        addBackgroundImage(group, "C:\\Users\\arnol\\Documents\\3DJAVA\\src\\pkg3djava\\galaxia.jpg");

        // Crear TransformGroups para las esferas
        TransformGroup tgSphere1 = createTransformGroup(new Vector3f(0.0f, 0.5f, -2.0f)); // Esfera superior
        TransformGroup tgSphere2 = createTransformGroup(new Vector3f(0.0f, -0.5f, -2.0f)); // Esfera inferior

        // Crear las esferas
        Appearance appearance1 = createSphereAppearance(new Color3f(1.0f, 0.0f, 0.0f)); // Esfera roja
        Appearance appearance2 = createSphereAppearance(new Color3f(0.0f, 1.0f, 0.0f)); // Esfera verde
        Sphere sphere1 = new Sphere(0.2f, Sphere.GENERATE_NORMALS, 80, appearance1);
        Sphere sphere2 = new Sphere(0.2f, Sphere.GENERATE_NORMALS, 80, appearance2);

        // Añadir las esferas a sus TransformGroups
        tgSphere1.addChild(sphere1);
        tgSphere2.addChild(sphere2);

        // Añadir los TransformGroups al grupo principal
        group.addChild(tgSphere1);
        group.addChild(tgSphere2);

        // Animaciones: traslación y rotación
        applyTranslationAnimation(tgSphere1, 0.0f, 0.5f, 0.0f); // Traslación cíclica en Y
        applyTranslationAnimation(tgSphere2, 0.0f, -0.5f, 0.0f); // Traslación cíclica en Y
        applyRotationAnimation(tgSphere1, true);  // Rotación en sentido horario
        applyRotationAnimation(tgSphere2, false); // Rotación en sentido antihorario

        // Configurar iluminación
        addLighting(group);

        // Configurar el punto de vista
        universe.getViewingPlatform().setNominalViewingTransform();

        // Añadir el grupo principal al universo
        universe.addBranchGraph(group);
    }

    // Crear un TransformGroup con capacidad de escritura y una posición inicial
    private static TransformGroup createTransformGroup(Vector3f position) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);
        TransformGroup tg = new TransformGroup(transform);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        return tg;
    }

    // Crear la apariencia de una esfera con un material específico
    private static Appearance createSphereAppearance(Color3f color) {
        Appearance appearance = new Appearance();
        Material material = new Material(color, new Color3f(0.0f, 0.0f, 0.0f), 
                                         color, new Color3f(1.0f, 1.0f, 1.0f), 64.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);
        return appearance;
    }

    // Aplicar animación de traslación
    private static void applyTranslationAnimation(TransformGroup tg, float x, float y, float z) {
        Transform3D transform = new Transform3D();
        Alpha alpha = new Alpha(-1, 4000); // Ciclos infinitos con duración de 4 segundos
        PositionInterpolator translator = new PositionInterpolator(
            alpha, tg, transform, -0.5f, 0.5f);
        translator.setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
        tg.addChild(translator);
    }

    // Aplicar animación de rotación
    private static void applyRotationAnimation(TransformGroup tg, boolean clockwise) {
        Alpha alpha = new Alpha(-1, 2000); // Ciclos infinitos con duración de 2 segundos
        RotationInterpolator rotator = new RotationInterpolator(
            alpha, tg, new Transform3D(), 0.0f, clockwise ? (float) Math.PI * 2 : (float) -Math.PI * 2);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
        tg.addChild(rotator);
    }

    // Añadir iluminación al grupo raíz
    private static void addLighting(BranchGroup group) {
        // Luz direccional
        DirectionalLight directionalLight = new DirectionalLight(
            new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(-1.0f, -1.0f, -1.0f));
        directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 100.0));
        group.addChild(directionalLight);

        // Luz ambiental
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 100.0));
        group.addChild(ambientLight);
    }

    // Añadir una imagen de fondo
    private static void addBackgroundImage(BranchGroup group, String imagePath) {
        TextureLoader loader = new TextureLoader(imagePath, null);
        ImageComponent2D image = loader.getImage();

        if (image != null) {
            Background background = new Background();
            background.setImage(image);
            background.setImageScaleMode(Background.SCALE_FIT_ALL);
            background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
            group.addChild(background);
        } else {
            System.err.println("Error: No se pudo cargar la imagen de fondo.");
        }
    }
}
