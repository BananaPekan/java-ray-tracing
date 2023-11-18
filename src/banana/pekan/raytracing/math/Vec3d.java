package banana.pekan.raytracing.math;

public class Vec3d {

    public float x, y, z;

    public Vec3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void mult(float by) {
        this.x *= by;
        this.y *= by;
        this.z *= by;
    }

    public void mult(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vec3d vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public float magnitudeSqr() {
        return x*x + y*y + z*z;
    }

    public float magnitude() {
        return (float) Math.sqrt(magnitudeSqr());
    }

    public void normalize() {
        float mag = magnitude();
        this.x /= mag;
        this.y /= mag;
        this.z /= mag;
    }

    public void subtract(Vec3d vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public void negate() {
        mult(-1);
    }

    public static float dot(Vec3d a, Vec3d b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vec3d mult(Vec3d vec, float by) {
        Vec3d v = copy(vec);
        v.mult(by);
        return v;
    }

    public static Vec3d copy(Vec3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z);
    }

    public static Vec3d add(Vec3d a, Vec3d b) {
        Vec3d vec = copy(a);
        vec.add(b);
        return vec;
    }

    public static Vec3d normalized(Vec3d vec) {
        Vec3d v = copy(vec);
        v.normalize();
        return v;
    }

    public static Vec3d negated(Vec3d vec) {
        Vec3d v = copy(vec);
        v.negate();
        return v;
    }

}
