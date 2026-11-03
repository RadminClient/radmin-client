/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.misc;

public interface ICopyable<T extends ICopyable<T>> {
    public T set(T var1);

    public T copy();
}

