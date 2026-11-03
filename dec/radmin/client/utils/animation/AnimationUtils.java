/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 */
package radmin.client.utils.animation;

import net.minecraft.util.Mth;
import radmin.client.utils.animation.Easing;
import radmin.client.utils.animation.Easings;

public class AnimationUtils {
    private float currentValue;
    private float targetValue;
    private float fromValue;
    private float speed;
    private Easing easing;
    private long startTime;
    private double duration;
    private boolean isRunning;

    public AnimationUtils(float initialValue, float speed, Easing easing) {
        this.currentValue = initialValue;
        this.targetValue = initialValue;
        this.fromValue = initialValue;
        this.speed = speed;
        this.easing = easing != null ? easing : Easings.LINEAR;
        this.startTime = 0L;
        this.duration = 0.0;
        this.isRunning = false;
    }

    public AnimationUtils(float initialValue, float speed) {
        this(initialValue, speed, Easings.LINEAR);
    }

    public void update(float target) {
        if (this.targetValue != target || !this.isRunning) {
            this.targetValue = target;
            this.fromValue = this.currentValue;
            this.startTime = System.nanoTime();
            this.duration = 1.0 / (double)this.speed * 2.0;
            this.isRunning = true;
        }
        if (this.isDone()) {
            this.currentValue = this.targetValue;
            this.isRunning = false;
            return;
        }
        double part = this.calculatePart();
        float easedPart = (float)this.easing.ease(part);
        this.currentValue = Mth.lerp((float)easedPart, (float)this.fromValue, (float)this.targetValue);
    }

    private double calculatePart() {
        if (!this.isRunning) {
            return 1.0;
        }
        long now = System.nanoTime();
        double elapsed = (double)(now - this.startTime) / 1.0E9;
        return Mth.clamp((double)(elapsed / this.duration), (double)0.0, (double)1.0);
    }

    public float getValue() {
        return this.currentValue;
    }

    public void setValue(float value) {
        this.currentValue = value;
        this.targetValue = value;
        this.fromValue = value;
        this.isRunning = false;
    }

    public float getTarget() {
        return this.targetValue;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        this.duration = 1.0 / (double)speed;
    }

    public void setEasing(Easing easing) {
        this.easing = easing != null ? easing : Easings.LINEAR;
    }

    public boolean isDone() {
        return this.calculatePart() >= 1.0;
    }

    public boolean isAlive() {
        return !this.isDone();
    }
}

