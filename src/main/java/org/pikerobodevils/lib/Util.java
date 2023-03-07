/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Util {

  public static Attributes getManifestAttributesForClass(Object object) {
    String className = object.getClass().getSimpleName() + ".class";
    String classPath = object.getClass().getResource(className).toString();
    if (!classPath.startsWith("jar")) {
      return new Attributes();
    }

    URL url = null;
    try {
      url = new URL(classPath);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return new Attributes();
    }
    JarURLConnection jarConnection = null;
    Manifest manifest = null;
    try {
      jarConnection = (JarURLConnection) url.openConnection();
      manifest = jarConnection.getManifest();
    } catch (IOException e) {
      e.printStackTrace();
      return new Attributes();
    }
    return manifest.getMainAttributes();
  }
}
