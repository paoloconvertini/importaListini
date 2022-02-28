package it.calolenoci.importaListini.reader;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import it.calolenoci.importaListini.constant.ConstantString;
import it.calolenoci.importaListini.model.AppProperties;
import it.calolenoci.importaListini.model.Matrice;
import it.calolenoci.importaListini.writer.IFileWriter;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Component public class ExcelReader implements IFileReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    @Resource
    IFileWriter excelWriter;

    private Workbook         workbook;

    @Override
    public void read(List<File> file, String fornitore) {
        try {
            for (File f : file) {
                // Open the workbook
                this.openWorkbook(f);
                excelWriter.write(workbook, f.getName(), fornitore);
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

    /**
     * Open an Excel workbook ready for conversion.
     *
     * @param file An instance of the File class that encapsulates a handle
     *             to a valid Excel workbook. Note that the workbook can be in
     *             either binary (.xls) or SpreadsheetML (.xlsx) format.
     * @throws java.io.FileNotFoundException Thrown if the file cannot be located.
     * @throws java.io.IOException           Thrown if a problem occurs in the file system.
     */
    private void openWorkbook(File file) throws FileNotFoundException, IOException {
        log.debug("Opening workbook [" + file.getName() + "]");
        try (FileInputStream fis = new FileInputStream(file)) {
            this.workbook = WorkbookFactory.create(fis);
        }
    }

}
