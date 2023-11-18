package banana.pekan.raytracing.math;

public class Ray {

    public Vec3d origin;
    public Vec3d direction;

    public Ray(Vec3d origin, Vec3d direction) {
        this.origin = origin;
        this.direction = direction;
    }

}
