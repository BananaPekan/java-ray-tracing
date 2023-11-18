package banana.pekan.raytracing.math;

import java.util.ArrayList;

public class Scene {

    public ArrayList<Sphere> spheres;

    public Vec3d cameraOrigin;

    public Vec3d cameraRotation;
    public Vec2d cameraRotationX;
    public Vec2d cameraRotationY;

    public Vec3d forward;
    public Vec3d right;

    public Scene() {
        spheres = new ArrayList<>();
        cameraOrigin = new Vec3d(0, 0, 0);
        cameraRotation = new Vec3d(0, 0, 0);
        cameraRotationX = new Vec2d(0, 0);
        cameraRotationY = new Vec2d(0, 0);
        forward = new Vec3d(0, 0, -0.000001f);
        right = new Vec3d(0.0000005f, 0, 0);
    }

    public void rotateCameraX(float angleX) {
        cameraRotation.x += angleX;
        double angle = Math.toRadians(cameraRotation.x);
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        cameraRotationX.x = cos;
        cameraRotationX.y = sin;
    }

    public void rotateCameraY(float angleY) {
        cameraRotation.y += angleY;
        double angle = Math.toRadians(cameraRotation.y);
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        cameraRotationY.x = cos;
        cameraRotationY.y = sin;
        forward.set(-0.000001f * sin, 0, -0.000001f * cos);
        right.set(0.0000005f * cos, 0, 0.0000005f * -sin);
    }

    public void addSphere(Sphere sphere) {
        spheres.add(sphere);
    }

}
