package vista.theme;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public final class SvgIconLoader {

    private static final String ICON_BASE_PATH = "assets/icons/";

    private SvgIconLoader() {
    }

    public static Icon load(String fileName, int width, int height) {
        String path = ICON_BASE_PATH + fileName;
        ClassLoader appCl = SvgIconLoader.class.getClassLoader();

        URL inClasspath = appCl.getResource(path);
        if (inClasspath != null) {
            return new FlatSVGIcon(path, width, height);
        }

        File srcRoot = new File(System.getProperty("user.dir") + File.separator + "src");
        File fsFile = new File(srcRoot, path.replace("/", File.separator));
        if (fsFile.exists()) {
            try {
                URLClassLoader fileCl = new URLClassLoader(new URL[] { srcRoot.toURI().toURL() }, appCl);
                float scale = Math.max(1f, Math.min(width, height) / 16f);
                return new FlatSVGIcon(path, scale, fileCl);
            } catch (Exception ignored) {
                // Sigue a fallback por icono estandar.
            }
        }

        return UIManager.getIcon("FileView.fileIcon");
    }
}

