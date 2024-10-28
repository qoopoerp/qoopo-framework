package net.qoopo.framework.img;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

public class QoopoIMG {

    private static ImageWriter writer = null;
    private static ImageWriteParam param = null;

    public static void iniciar() {
        ImageIO.setUseCache(false);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpeg");
        writer = writers.next();
        param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1);
    }

    public static void saveImageJPG(BufferedImage image, float quality, OutputStream out) throws IOException {
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        param.setCompressionQuality(quality);
        writer.write(null, new IIOImage(image, null, null), param);
        ios.close();
    }

    // public static void saveImageJPG(BufferedImage image, float quality,
    // ByteArrayOutputStream out) throws IOException {
    // try {
    // Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
    // ImageWriter writer = (ImageWriter) iter.next();
    // ImageWriteParam iwp = writer.getDefaultWriteParam();
    // iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    // iwp.setCompressionQuality(quality);
    // ImageOutputStream ios = ImageIO.createImageOutputStream(out);
    // writer.setOutput(ios);
    // IIOImage outimage = new IIOImage(image, null, null);
    // writer.write(null, outimage, iwp);
    // ios.close();
    // writer.dispose();
    // } catch (Exception e) {
    // }
    // }
    public static byte[] saveImageJPGByte(BufferedImage image) throws IOException {
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        byte[] salida = null;
        ImageIO.write(image, "jpeg", imageBytes);
        salida = imageBytes.toByteArray();
        return salida;
    }

    public static byte[] saveImageJPGByte(BufferedImage image, float compresion) throws IOException {
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        byte[] salida = null;
        try {
            saveImageJPG(image, compresion, imageBytes);
            salida = imageBytes.toByteArray();
            imageBytes.close();
        } catch (Exception ex) {
        }
        return salida;
    }

    public static byte[] getPantallaCompleta() {
        byte[] salida = null;
        try {
            Robot rt = new Robot();
            BufferedImage bi = rt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            bi.setAccelerationPriority(1.0F);
            bi = escalarSuave(bi, (int) (0.8 * bi.getWidth()), (int) (0.8 * bi.getHeight()));
            salida = saveImageJPGByte(bi, 0.75f);
        } catch (Exception ex) {
        }
        return salida;
    }

    public static BufferedImage clonar(BufferedImage img) {
        BufferedImage clon = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics g = clon.createGraphics();
        g.drawImage(img, 0, 0, null);
        return clon;
    }

    public static byte[] getPantallaMiniatura() {
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        byte[] salida = null;
        try {
            Robot rt = new Robot();
            BufferedImage bi = rt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            bi.setAccelerationPriority(1.0F);
            saveImageJPG(escalarSuave(bi, 320, 200), 0.75f, imageBytes);
            salida = imageBytes.toByteArray();
            imageBytes.close();
        } catch (OutOfMemoryError E) {

        } catch (Exception ex) {
        }
        return salida;
    }

    // public static BufferedImage escalarFuerte(BufferedImage bi, Double scallX,
    // Double scallY) {
    // AffineTransform tx = new AffineTransform();
    // tx.scale(scallX, scallY);
    // AffineTransformOp op = new AffineTransformOp(tx,
    // AffineTransformOp.TYPE_BILINEAR);
    // BufferedImage biNew = new BufferedImage((int) (bi.getWidth() * scallX), (int)
    // (bi.getHeight() * scallY), bi.getType());
    // return op.filter(bi, biNew);
    // }
    public static BufferedImage escalarFuerte(BufferedImage bi, int ancho, int alto) {
        AffineTransform tx = new AffineTransform();
        tx.scale((double) ancho / bi.getWidth(), (double) alto / bi.getHeight());// aqui fa el factor de escala
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage biNew = new BufferedImage(ancho, alto, bi.getType());
        return op.filter(bi, biNew);
    }

    public static BufferedImage escalarSuave(BufferedImage bi, int ancho, int alto) {
        ImageIcon icon = new ImageIcon(bi);
        Image mm = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        BufferedImage nuevo = new BufferedImage(ancho, alto, bi.getType());
        nuevo.createGraphics().drawImage(mm, 0, 0, null);
        return nuevo;
    }

    // mapa de colores para paleta de 16 entradas (4bits)
    private static byte[] rojo_bn;
    private static byte[] verde_bn;
    private static byte[] azul_bn;
    private static int colores_bn = 4;
    private static int bits_bn;// deberia ser 4
    private static IndexColorModel icm_bn;

    static {

        // System.out.println("Blanco y negro");
        rojo_bn = new byte[colores_bn];
        verde_bn = new byte[colores_bn];
        azul_bn = new byte[colores_bn];

        for (int i = 0; i < colores_bn; i++) {
            rojo_bn[i] = (byte) ((i * 255) / (colores_bn - 1));
            verde_bn[i] = (byte) ((i * 255) / (colores_bn - 1));
            azul_bn[i] = (byte) ((i * 255) / (colores_bn - 1));
        }

        bits_bn = (int) Math.ceil(Math.log(colores_bn) / Math.log(2));
        // System.out.println("los bits son =" + bits_bn);
        icm_bn = new IndexColorModel(bits_bn, colores_bn, rojo_bn, verde_bn, azul_bn);
    }

    public static BufferedImage filtrar(BufferedImage bi, int tipoColor, int tipoDatos) {
        try {

            // 1.- Blanco y negro
            // 2.- (gris),
            // 3.- 8 bits (256 colores),
            // 4.- 16 bits (65536 colores),
            // 5. 24 bits
            // 6.- hd tal como captura el robot de java)
            // 32 bits es tal como captura el robot
            BufferedImage biDestino = null;

            // tipo bytes
            if (tipoDatos == 1) {
                switch (tipoColor) {
                    case 1:
                        // blanco y negro
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_BYTE_BINARY);//de un bit
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_BINARY,
                                icm_bn);// de 2 bits
                        break;
                    case 2:
                        // escla de grises
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                        break;
                    case 3:
                        // 8 bits (256 colores)
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_BYTE_INDEXED, icm);
                        break;
                    // return convertRGBAToIndexed(bi);
                    case 5:
                        // 24 bits
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_INT_BGR);
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_INT_RGB);
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                        break;
                    case 6:
                        // 32 bits
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);// tipo
                                                                                                                    // byte
                        break;
                    default:// si no es algun tipo conocido filtramos a 24 bits
                        // 24 bits
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);// tipo
                                                                                                                   // byte
                        break;
                }
                // tipo int
            } else if (tipoDatos == 2) {
                switch (tipoColor) {
                    case 5:
                        // 24 bits
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_INT_BGR);
                        // biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(),
                        // BufferedImage.TYPE_INT_RGB);
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_BGR);
                        break;
                    case 6:
                        // 32 bits
                        biDestino = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);// tipo
                                                                                                                  // entero
                        break;
                    case 7:// sin proceso de imagen
                        return bi;// retorna la imagen sin procesar algun filtro
                    default:
                        // si no es algu tipo conocido no filtramos y la dejamos tal y como viene (32
                        // bits)
                        break;
                }
            }

            if (biDestino != null) {
                Graphics g = biDestino.createGraphics();
                g.drawImage(bi, 0, 0, null);
                bi = null;
                return biDestino;
            } else {
                return null;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getByteArray(BufferedImage bi) {
        byte[] datos = null;
        try {
            datos = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static int[] getIntArray(BufferedImage bi) {
        int[] datos = null;
        try {
            datos = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    // escalar la imagen toma tiempo, es mejor no escalarla en redes rapidas
    public static BufferedImage escalar(BufferedImage bi, int tipoEscala, int scala, boolean suavizado, int ancho,
            int alto) {
        BufferedImage nuevo;
        switch (tipoEscala) {
            case 0:
                // original, sin escalar
                return bi;
            case 1:
                // porcentual
                double escalaFactor = (double) scala / 100.0D;
                if (suavizado) {
                    nuevo = escalarSuave(bi, (int) (bi.getWidth() * escalaFactor),
                            (int) (bi.getHeight() * escalaFactor));
                } else {
                    // nuevo = escalarFuerte(bi, escalaFactor, escalaFactor);
                    nuevo = escalarFuerte(bi, (int) (bi.getWidth() * escalaFactor),
                            (int) (bi.getHeight() * escalaFactor));
                }
                break;
            case 2:
                // ajusta al tamanio de la pantalla
                if (suavizado) {
                    nuevo = escalarSuave(bi, ancho, alto);
                } else {
                    // nuevo = escalarFuerte(bi, (double) ancho / bi.getWidth(), (double) alto /
                    // bi.getHeight());
                    nuevo = escalarFuerte(bi, ancho, alto);
                }
                break;
            default:
                // ajuste perfecto
                float radio = ((float) bi.getWidth()) / bi.getHeight();
                int nuevoAncho = ancho;
                int nuevoAlto = alto;
                int nuevoAncho_1,
                        nuevoAlto_1,
                        nuevoAncho_2,
                        nuevoAlto_2;
                // caso 1: se ajusto al ancho (el alto es mayor)
                nuevoAncho_1 = ancho;
                nuevoAlto_1 = (int) (nuevoAncho_1 / radio);
                // caso 2: se ajusto al alto(el ancho es mayor)
                nuevoAlto_2 = alto;
                nuevoAncho_2 = (int) (nuevoAlto_2 * radio);
                if (nuevoAncho_1 <= ancho && nuevoAlto_1 <= alto) {
                    nuevoAncho = nuevoAncho_1;
                    nuevoAlto = nuevoAlto_1;
                }
                if (nuevoAncho_2 <= ancho && nuevoAlto_2 <= alto) {
                    nuevoAncho = nuevoAncho_2;
                    nuevoAlto = nuevoAlto_2;
                }
                ancho = nuevoAncho;
                alto = nuevoAlto;
                if (suavizado) {
                    nuevo = escalarSuave(bi, ancho, alto);
                } else {
                    // nuevo = escalarFuerte(bi, (double) ancho / bi.getWidth(), (double) alto /
                    // bi.getHeight());
                    nuevo = escalarFuerte(bi, ancho, alto);
                }
                break;
        }
        return nuevo;
    }

    public static BufferedImage leerImagen(byte[] datos) {
        try {
            ByteArrayInputStream inn = new ByteArrayInputStream(datos);
            BufferedImage imagenNueva = ImageIO.read(inn);
            inn.close();
            inn = null;
            datos = null;
            return imagenNueva;
        } catch (Exception e) {
            return null;
        }
    }

    // public static BufferedImage convertRGBAToIndexed(BufferedImage src) {
    // BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
    // BufferedImage.TYPE_BYTE_INDEXED);
    // Graphics g = dest.getGraphics();
    // g.setColor(new Color(231, 20, 189));
    // // fill with a hideous color and make it transparent
    // g.fillRect(0, 0, dest.getWidth(), dest.getHeight());
    // dest = makeTransparent(dest, 0, 0);
    // dest.createGraphics().drawImage(src, 0, 0, null);
    // return dest;
    // }
    //
    // public static BufferedImage makeTransparent(BufferedImage image, int x, int
    // y) {
    // ColorModel cm = image.getColorModel();
    // if (!(cm instanceof IndexColorModel)) {
    // return image; // sorry...
    // }
    // IndexColorModel icm = (IndexColorModel) cm;
    // WritableRaster raster = image.getRaster();
    // int pixel = raster.getSample(x, y, 0); // pixel is offset in ICM's palette
    // int size = icm.getMapSize();
    // byte[] reds = new byte[size];
    // byte[] greens = new byte[size];
    // byte[] blues = new byte[size];
    // icm.getReds(reds);
    // icm.getGreens(greens);
    // icm.getBlues(blues);
    // IndexColorModel icm2 = new IndexColorModel(8, size, reds, greens, blues,
    // pixel);
    // return new BufferedImage(icm2, raster, image.isAlphaPremultiplied(), null);
    // }
}
