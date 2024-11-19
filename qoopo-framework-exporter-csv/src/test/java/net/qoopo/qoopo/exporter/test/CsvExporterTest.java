package net.qoopo.qoopo.exporter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.qoopo.framework.exporter.csv.CsvExporter;
import net.qoopo.framework.exporter.csv.CsvImporter;


public class CsvExporterTest {

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void prueba() {
        try {

            File f = new File("test.csv");

            //PRUEBA DE EXPORTACION
            List<ItemTest> lstExportar = new ArrayList<>();
            lstExportar.add(new ItemTest("01", "Primero", 15, BigDecimal.valueOf(0.5f)));
            lstExportar.add(new ItemTest("02", "Segundo", 35, BigDecimal.valueOf(5.5f)));
            lstExportar.add(new ItemTest("03", "Contacto", 115, BigDecimal.valueOf(1.2f)));
            lstExportar.add(new ItemTest("04", "Cuarto", 3356, new BigDecimal("4.8")));

            CsvExporter exporter = new CsvExporter();
            FileOutputStream fos = new FileOutputStream(f);
            exporter.setOutputExporter(fos);
            for (ItemTest item : lstExportar) {
                exporter.startItem();
                item.exportar(exporter);
                exporter.endItem();
            }
            exporter.exportar();
            fos.close();
            System.out.println("Archivo final=" + f.getAbsolutePath());
            assertTrue(f.exists());

            // PRUEBA DE IMPORTACION
            List<ItemTest> lstImportar = new ArrayList<>();
            CsvImporter importer = new CsvImporter();
            FileInputStream fis = new FileInputStream(f);
            importer.setInputStream(fis);
            importer.importar();
            fis.close();
            while (importer.hasItems()) {
                importer.startItem();
                ItemTest item = new ItemTest();
                item.importar(importer);
                lstImportar.add(item);
                importer.endItem();
            }

            for (ItemTest item : lstImportar) {
                System.out.println("Item:" + item.toString());
            }
            f.delete();
            assertTrue(true);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

}
