/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 */
package radmin.client.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import radmin.client.MeteorClient;

public class StreamUtils {
    private StreamUtils() {
    }

    public static void copy(File from, File to) {
        try (FileInputStream in = new FileInputStream(from);
             FileOutputStream out = new FileOutputStream(to);){
            in.transferTo(out);
        }
        catch (IOException e) {
            MeteorClient.LOG.error("Error copying from file '{}' to file '{}'.", new Object[]{from.getName(), to.getName(), e});
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copy(InputStream in, File to) {
        try (FileOutputStream out = new FileOutputStream(to);){
            in.transferTo(out);
        }
        catch (IOException iOException) {
            MeteorClient.LOG.error("Error writing to file '{}'.", (Object)to.getName());
        }
        finally {
            IOUtils.closeQuietly((InputStream)in);
        }
    }
}

