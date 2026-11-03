/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.utils.render;

import net.minecraft.world.phys.Vec3;
import radmin.client.MeteorClient;
import radmin.client.renderer.MeshBuilder;
import radmin.client.utils.render.color.Color;

public class WorldTexturedRenderer {
    public static void addBillboard(MeshBuilder mesh, Vec3 pos, double width, double height, double rotation, Color color) {
        Vec3 up;
        Vec3 cam = MeteorClient.mc.gameRenderer.getMainCamera().position();
        Vec3 dir = pos.subtract(cam);
        double dist = dir.length();
        if (dist < 1.0E-4) {
            return;
        }
        Vec3 n = dir.scale(1.0 / dist);
        Vec3 right = n.cross(up = new Vec3(0.0, 1.0, 0.0));
        right = right.length() < 0.01 ? new Vec3(1.0, 0.0, 0.0) : right.normalize();
        Vec3 upV = right.cross(n).normalize();
        double rad = Math.toRadians(rotation);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        Vec3 r = right.scale(cos).add(upV.scale(sin));
        Vec3 u = right.scale(-sin).add(upV.scale(cos));
        double hw = width / 2.0;
        double hh = height / 2.0;
        mesh.ensureQuadCapacity();
        Vec3 c1 = pos.add(r.scale(-hw)).add(u.scale(-hh));
        Vec3 c2 = pos.add(r.scale(-hw)).add(u.scale(hh));
        Vec3 c3 = pos.add(r.scale(hw)).add(u.scale(hh));
        Vec3 c4 = pos.add(r.scale(hw)).add(u.scale(-hh));
        int i1 = mesh.vec3(c1.x, c1.y, c1.z).vec2(0.0, 0.0).color(color).next();
        int i2 = mesh.vec3(c2.x, c2.y, c2.z).vec2(0.0, 1.0).color(color).next();
        int i3 = mesh.vec3(c3.x, c3.y, c3.z).vec2(1.0, 1.0).color(color).next();
        int i4 = mesh.vec3(c4.x, c4.y, c4.z).vec2(1.0, 0.0).color(color).next();
        mesh.quad(i1, i2, i3, i4);
    }
}

