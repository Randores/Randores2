/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.randores.resource;

import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RandoresResourceManager {

    public static List<String> getLines(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static String getFileName(ResourceLocation location) {
        String[] pieces = location.getResourcePath().split(Pattern.quote("/"));
        return pieces[pieces.length - 1];
    }

    public static BufferedImage getImageResource(String resourceName) throws IOException {
        return ImageIO.read(RandoresResourceManager.class.getResourceAsStream(resourceName));
    }

    public static void exportResource(String resourceName, File path) throws IOException {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = RandoresResourceManager.class.getResourceAsStream(resourceName);
            if(stream == null) {
                throw new IOException("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(path);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }
    }

    public static List<String> getResourceLines(String resourceName) throws IOException {
        InputStream stream = RandoresResourceManager.class.getResourceAsStream(resourceName);
        if(stream == null) {
            throw new IOException("Cannot get resource \"" + resourceName + "\" from Jar file.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static String getResourceContent(String resourceName) throws IOException {
        InputStream stream = RandoresResourceManager.class.getResourceAsStream(resourceName);
        if(stream == null) {
            throw new IOException("Cannot get resource \"" + resourceName + "\" from Jar file.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public static boolean resourceExists(String resourceName) {
        return RandoresResourceManager.class.getResourceAsStream(resourceName) != null;
    }

}