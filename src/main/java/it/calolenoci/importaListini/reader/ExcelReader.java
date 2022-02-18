package it.calolenoci.importaListini.reader;

import it.calolenoci.importaListini.constant.ConstantString;
import it.calolenoci.importaListini.model.Configuration;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelReader implements IFileReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    @Resource
    private Configuration configuration;

    private Workbook wbCopyTo;

    @Override
    public void read(List<File> file) {
        try {
            for (File f : file) {
                FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook;
                if (StringUtils.equals(ConstantString.XLS, FilenameUtils.getExtension(f.getPath()))) {
                    workbook = new HSSFWorkbook(inputStream);
                    wbCopyTo = new HSSFWorkbook();
                    log.debug("il file " + f.getName() + " da processare è .xls");
                } else {
                    workbook = new XSSFWorkbook(inputStream);
                    wbCopyTo = new XSSFWorkbook();
                    log.debug("il file " + f.getName() + " da processare è .xlsx");
                }
                generate(workbook, f.getName());
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

    private void generate(Workbook wbCopyFrom, String filename) throws Exception {
        // FIXME da verificare la presenza dei dati sul primo sheet
        Sheet sheetCopyFrom = wbCopyFrom.getSheetAt(0);
        //FIXME da verificare la presenza dell'header nella prima row
        Row headerRow = sheetCopyFrom.getRow(0);
        int firstRow = sheetCopyFrom.getFirstRowNum();
        int lastRow = sheetCopyFrom.getLastRowNum();
        //filtro la lista per recuperare solo i valori da riportare nel file definitivo usando una map. Così mi salvo anche il nome della colonna
        //da poter usare per la formattazione eventuale delle celle
        Map<Integer, String> columnMap = new HashMap<>();
        for (int cellHeaderIndex = headerRow.getFirstCellNum(); cellHeaderIndex < headerRow.getLastCellNum(); cellHeaderIndex++) {
            Cell cell = headerRow.getCell(cellHeaderIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();
            if (!cellType.equals(CellType.STRING)) {
                break;
            }
            if (configuration.getCodice().equals(cell.getStringCellValue()) ||
                    configuration.getDescrizione().equals(cell.getStringCellValue()) ||
                    configuration.getPrezzi().equals(cell.getStringCellValue())) {
                columnMap.put(cell.getColumnIndex(), cell.getStringCellValue());
            }
        }
        FileOutputStream out = new FileOutputStream(configuration.getOutputDir() + "/" + filename);
        Sheet sheetCopyTo = wbCopyTo.createSheet(sheetCopyFrom.getSheetName());
        for (int i = firstRow; i <= lastRow; i++) {
            Row rowCopyTo = sheetCopyTo.createRow(i);
            Row rowCopyFrom = sheetCopyFrom.getRow(i);
            int counter = 0;
            for (Integer integer : columnMap.keySet()) {
                //FIXME da capire se è importante rispettare un ordine per le colonne
                Cell cellCopyTo = rowCopyTo.createCell(counter);
                Cell cellCopyFrom = rowCopyFrom.getCell(integer, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                setCellValue(wbCopyTo, cellCopyFrom, cellCopyTo, columnMap.get(integer));
                counter++;
            }
        }
        wbCopyTo.write(out);
        out.close();
        wbCopyTo.close();
    }


    private void setCellValue(Workbook wbCopyTo, Cell cell, Cell cellCopyTo, String colName) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            cellCopyTo.setCellValue(cell.getStringCellValue());
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellCopyTo.setCellValue(cell.getDateCellValue());
            } else if (configuration.getPrezzi().equals(colName)) {
                CellStyle styleCurrencyFormat = wbCopyTo.createCellStyle();
                styleCurrencyFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat(ConstantString.CELL_CURRENCY_FORMAT));
                cellCopyTo.setCellValue(cell.getNumericCellValue());
                cellCopyTo.setCellStyle(styleCurrencyFormat);
            } else {
                cellCopyTo.setCellValue(cell.getNumericCellValue());
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            cellCopyTo.setCellValue(cell.getBooleanCellValue());
        }
    }
}
