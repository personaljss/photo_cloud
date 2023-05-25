package gui.home;

import javax.swing.JFileChooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileChooser extends JFileChooser {
    private static final long serialVersionUID = -844763092384722761L;

    public FileChooser() {
        // Here you can add any custom configuration for the JFileChooser
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(createImageFileFilter());
    }

    private FileFilter createImageFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                String extension = getFileExtension(file);
                if (extension != null) {
                    extension = extension.toLowerCase();
                    return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")
                            || isSupportedImageExtension(extension);
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png)";
            }

            private boolean isSupportedImageExtension(String extension) {
                // Add any additional image extensions here
                return extension.equals("gif") || extension.equals("bmp");
            }

            private String getFileExtension(File file) {
                String name = file.getName();
                int lastDotIndex = name.lastIndexOf('.');
                if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
                    return name.substring(lastDotIndex + 1);
                }
                return null;
            }
        };
    }
}
