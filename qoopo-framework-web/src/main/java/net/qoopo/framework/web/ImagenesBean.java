package net.qoopo.framework.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;

import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import net.qoopo.framework.web.util.FacesUtils;

/**
 * Este Bean permite la gestion de las imagenes para ser mostrados en el Xhtml
 *
 * @author ALBERTO
 */
@Named
@ViewScoped
public class ImagenesBean implements Serializable {

    public static final Logger log = Logger.getLogger("ImagenesBean");

    private static final String NO_DISPONIBLE = "resources/imagenes/nodisponible.png";
    private static final String NO_DISPONIBLE_FOTO = "resources/imagenes/persona.png";

    /**
     * Decode string to image
     *
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return image;
    }

    /**
     * Encode image to string
     *
     * @param image The image to encode
     * @param type  jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.UTF_8);
            bos.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return imageString;
    }

    private String dirImagesTmp;
    private byte[] imagen;// tmp de la imagen a subir

    public ImagenesBean() {
        dirImagesTmp = "imagestmp/";
    }

    /**
     * Devuelve el directorio actual en el sistema de archivos de ejecucion
     *
     * @return
     */
    private String directorioActual() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                .getContext();
        return (String) servletContext.getRealPath("");
    }

    public String getImage(byte[] datos) {
        return getImagePath(datos, NO_DISPONIBLE);
    }

    public String getPhoto(byte[] datos) {
        return getImagePath(datos, NO_DISPONIBLE_FOTO);
    }

    public String getImagePath(byte[] datos, String imgno) {
        String nombre = imgno;
        if (datos != null) {
            String directorioActual = directorioActual();
            File upload = new File(directorioActual, dirImagesTmp);
            try {
                if (!upload.exists()) {
                    if (!upload.mkdirs()) {
                        dirImagesTmp = "";
                    } else {
                        dirImagesTmp = "imagestmp/";
                    }
                } else {
                }
            } catch (Exception e) {
                dirImagesTmp = "";
            }

            nombre = dirImagesTmp + Arrays.hashCode(datos) + ".png";
            File file = new File(directorioActual, nombre);
            file.deleteOnExit();
            if (file.exists() && !file.delete()) {
                System.out.println("Img. preexistente no eliminado " + file.getAbsolutePath());
            }
            FileOutputStream fout = null;
            try {
                if (file.createNewFile()) {
                    fout = new FileOutputStream(file);
                    fout.write(datos);
                } else {
                    System.out.println("No se crea nuevo archivo para la imagen " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ImagenesBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return "/" + nombre;
    }

    public void archivoSubido(FileUploadEvent event) {
        try {
            setImagen(event.getFile().getContent());
            FacesUtils.addInfoMessage(event.getFile().getFileName() + "  fue subido.");
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            FacesUtils.addErrorMessage("Error al cargar imagen.");
        }
    }

    public String getDirImagesTmp() {
        return dirImagesTmp;
    }

    public void setDirImagesTmp(String dirImagesTmp) {
        this.dirImagesTmp = dirImagesTmp;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String base64Foto(byte[] datos) {
        try {
            BufferedImage img = ImageIO.read(new File(directorioActual(), getPhoto(datos)));
            String imgstr;
            imgstr = encodeToString(img, "png");
            return "data:image/png;base64," + imgstr;
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            return "data:image/png;base64,";
        }
    }

    public String base64Img(byte[] datos) {
        try {
            BufferedImage img = ImageIO.read(new File(directorioActual(), getImage(datos)));
            String imgstr;
            imgstr = encodeToString(img, "png");
            return "data:image/png;base64," + imgstr;
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            return "data:image/png;base64,";
        }
    }

}
