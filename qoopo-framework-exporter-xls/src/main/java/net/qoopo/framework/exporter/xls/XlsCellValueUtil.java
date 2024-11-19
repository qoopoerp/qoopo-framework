package net.qoopo.framework.exporter.xls;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class XlsCellValueUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static final Logger log = Logger.getLogger("Qoopo-XlsCellValueUtil");

    public static String getValue(Cell cell) {

        try {
            if (cell.getCellType().equals(CellType.NUMERIC) || cell.getCellType().equals(CellType.FORMULA)) {
                try {
                    String numericValue = String.valueOf(cell.getNumericCellValue());
                    // si es de tipo fecha, lo parseamos,
                    if (
                    // HSSFDateUtil.isCellDateFormatted(cell)
                    DateUtil.isCellDateFormatted(cell)) {
                        Date dateValue = cell.getDateCellValue();
                        if (dateValue != null) {
                            String value = sdf.format(dateValue);
                            if (value != null && !value.isEmpty()) {
                                // log.info("fecha leida " + value + " original value=" + numericValue);
                                return value;
                            }
                        }
                    }
                    return numericValue;
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }

            } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
                return String.valueOf(cell.getBooleanCellValue());

            } else {
                return cell.getStringCellValue();
            }
        } catch (Exception e) {
            log.severe("Error al leer valor " + e.getLocalizedMessage());
            e.printStackTrace();
            try {
                return cell.getStringCellValue();
            } catch (Exception ee) {

            }
        }
        return "";
    }
}
