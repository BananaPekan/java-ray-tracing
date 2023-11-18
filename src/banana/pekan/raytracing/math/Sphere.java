package banana.pekan.raytracing.math;

public class Sphere {

    public Vec3d origin;
    public float radius;

    public Vec3d color;

    public Sphere(Vec3d origin, float radius, Vec3d color) {
        this.origin = origin;
        this.radius = radius;
        this.color = color;
    }

}
