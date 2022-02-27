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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

@Component
public class ExcelReader implements IFileReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    @Resource
    IFileWriter excelWriter;

    @Override
    public void read(List<File> file, String fornitore) {
        try {
            for (File f : file) {
                FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook;
                if (StringUtils.equals(ConstantString.XLS, FilenameUtils.getExtension(f.getPath()))) {
                    workbook = new HSSFWorkbook(inputStream);
                    log.debug("il file " + f.getName() + " da processare è .xls");
                } else {
                    workbook = new XSSFWorkbook(inputStream);
                    log.debug("il file " + f.getName() + " da processare è .xlsx");
                }
                excelWriter.write(workbook, f.getName(), fornitore);
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

}
