package banana.pekan.raytracing.gui;

import banana.pekan.raytracing.math.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Panel extends JPanel {

    int width;
    int height;
    int lastWidth;
    int lastHeight;
    Scene scene;
    Image buffer;

    int mouseX, mouseY = 0;

    float aspectRatio = 1;

    public Panel(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new Image(width, height, Image.TYPE_INT_ARGB);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point onScreen = getLocationOnScreen();
                robot.mouseMove(onScreen.x + width / 2, onScreen.y + height / 2);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                scene.rotateCameraX((float) (mouseY - height / 2) / height * 20);
                scene.rotateCameraY((float) (mouseX - width / 2) / width * -20);
                Point onScreen = getLocationOnScreen();
                robot.mouseMove(onScreen.x + width / 2, onScreen.y + height / 2);

            }
        });

        scene = new Scene();
        scene.addSphere(new Sphere(new Vec3d(1, 0, -2), 0.5f, new Vec3d(0, 0.7f, 0.3f)));
        scene.addSphere(new Sphere(new Vec3d(0.5f, 0, -3), 0.5f, new Vec3d(0.6f, 0.5f, 0.1f)));
    }

    Robot robot;

    @Override
    public void paint(Graphics graphics) {
        width = getWidth();
        height = getHeight();
        if (width != lastWidth || height != lastHeight) {
            buffer = new Image(width, height, Image.TYPE_INT_ARGB);
        }
        lastWidth = width;
        lastHeight = height;
        aspectRatio = (float) width / height;
        Graphics g = buffer.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int color = PixelShader(new Vec2d((float) i / width * aspectRatio, (float) j / height));
                buffer.setRGB(i, j, color);
            }
        }

        graphics.clearRect(0, 0, width, height);
        graphics.drawImage(buffer, 0, 0, null);
    }

    public float getClosestT(Vec3d origin, Vec3d direction, Sphere sphere) {
        float a = Vec3d.dot(direction, direction);
        float b = 2f * Vec3d.dot(origin, direction);
        float c = Vec3d.dot(origin, origin) - sphere.radius * sphere.radius;

        float discriminator = b*b - 4*a*c;

        if (discriminator < 0) {
            return -1;
        }

        float nominator = (float) (-b - Math.sqrt(discriminator));
        return nominator / (2 * a);
    }

    public Sphere getClosestSphere(Ray ray, Scene scene) {
        Sphere closestSphere = null;
        float closestT = Float.POSITIVE_INFINITY;

        for (Sphere sphere : scene.spheres) {
            Vec3d origin = Vec3d.copy(ray.origin);
            origin.subtract(sphere.origin);
            float t = getClosestT(origin, ray.direction, sphere);
            if (t < 0) continue;
            if (t < closestT) {
                closestT = t;
                closestSphere = sphere;
            }
        }

        return closestSphere;
    }

    public Vec3d getClosestHit(Ray ray, Sphere sphere) {
        Vec3d origin = Vec3d.copy(ray.origin);
        origin.subtract(sphere.origin);
        float t = getClosestT(origin, ray.direction, sphere);
        return Vec3d.add(origin, Vec3d.mult(ray.direction, t));
    }

    public void onKeyDown(int keyCode) {
        Vec3d forward = scene.forward;
        Vec3d right = scene.right;
        Vec3d up = new Vec3d(0, -0.0000003f, 0);
        if (keyCode == 'W') {
            scene.cameraOrigin.add(forward);
        }
        else if (keyCode == 'S') {
            scene.cameraOrigin.subtract(forward);
        }
        else if (keyCode == 'D') {
            scene.cameraOrigin.add(right);
        }
        else if (keyCode == 'A') {
            scene.cameraOrigin.subtract(right);
        }
        else if (keyCode == 'E') {
            scene.cameraOrigin.add(up);
        }
        else if (keyCode == 'Q') {
            scene.cameraOrigin.subtract(up);
        }
    }

    public int PixelShader(Vec2d coord) {
        Vec3d rayOrigin = new Vec3d(0, 0, 2);
        rayOrigin.add(scene.cameraOrigin);
        Vec3d rayDirection = new Vec3d(coord.x - 0.5f, coord.y - 0.5f, -1);

        rayDirection.set(rayDirection.x, scene.cameraRotationX.x * rayDirection.y + -scene.cameraRotationX.y * rayDirection.z, scene.cameraRotationX.y * rayDirection.y + scene.cameraRotationX.x * rayDirection.z);
        rayDirection.set(scene.cameraRotationY.x * rayDirection.x + scene.cameraRotationY.y * rayDirection.z, rayDirection.y, -scene.cameraRotationY.y * rayDirection.x + scene.cameraRotationY.x * rayDirection.z);

        Ray ray = new Ray(rayOrigin, Vec3d.copy(rayDirection));

        Sphere closestSphere = getClosestSphere(ray, scene);
        if (closestSphere == null) {
            return getRGBA(0, 0, 0, 1);
        }

        Vec3d closestHit = getClosestHit(ray, closestSphere);

        Vec3d lightOrigin = new Vec3d(-1, 1, -1);
        lightOrigin.normalize();
        lightOrigin.negate();

        Vec3d color = Vec3d.mult(closestSphere.color, Math.max(Vec3d.dot(Vec3d.normalized(closestHit), lightOrigin), 0.1f));

        return getRGBA(color, 1);
    }

    public int getRGBA(Vec3d color, float alpha) {
        return getRGBA(color.x, color.y, color.z, alpha);
    }

    public int getRGBA(float r, float g, float b, float a) {
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }

}
