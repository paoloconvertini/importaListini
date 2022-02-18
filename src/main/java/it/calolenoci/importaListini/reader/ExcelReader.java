package it.calolenoci.importaListini.reader;

import it.calolenoci.importaListini.model.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
public class ExcelReader implements FileReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    @Resource
    private Configuration configuration;





    @Override
    public void read(List<File> file) {
        try {
            for (File f : file) {
                FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                generate(workbook, f.getName());
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

    private void generate(Workbook wbCopyFrom, String filename) throws Exception{
        Sheet sheetCopyFrom = wbCopyFrom.getSheetAt(0);
        Row headerRow = sheetCopyFrom.getRow(0);
        int firstRow = sheetCopyFrom.getFirstRowNum();
        int lastRow = sheetCopyFrom.getLastRowNum();
        Map<Integer, String> columnMap = new HashMap<>();
        //filtro la lista per recuperare solo i valori da riportare nel file definitivo
        int columnIndex;
        for (int cellHeaderIndex = headerRow.getFirstCellNum(); cellHeaderIndex < headerRow.getLastCellNum(); cellHeaderIndex++) {
            Cell cell = headerRow.getCell(cellHeaderIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                    ? cell.getCachedFormulaResultType() : cell.getCellType();
            if (cellType.equals(CellType.STRING)) {
                if (configuration.getCodice().equals(cell.getStringCellValue()) ||
                        configuration.getDescrizione().equals(cell.getStringCellValue()) ||
                        configuration.getPrezzi().equals(cell.getStringCellValue())) {
                    columnIndex = cell.getColumnIndex();
                    columnMap.put(columnIndex, cell.getStringCellValue());
                    log.debug("codice col index=" + columnIndex);
                }
            }
        }
        XSSFWorkbook wbCopyTo = new XSSFWorkbook();
        FileOutputStream out = new FileOutputStream(configuration.getOutputDir() + "/" + filename);
        XSSFSheet sheetCopyTo = wbCopyTo.createSheet(sheetCopyFrom.getSheetName());
        for(int i = firstRow; i <= lastRow; i++){
            XSSFRow rowCopyTo = sheetCopyTo.createRow(i);
            Row rowCopyFrom = sheetCopyFrom.getRow(i);
            int counter = 0;
            for (Integer integer : columnMap.keySet()) {
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


    private void setCellValue(XSSFWorkbook wbCopyTo, Cell cell, Cell cellCopyTo, String colName) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            cellCopyTo.setCellValue(cell.getStringCellValue());
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellCopyTo.setCellValue(cell.getDateCellValue());
            } else if (configuration.getPrezzi().equals(colName)){
                XSSFCellStyle styleCurrencyFormat = wbCopyTo.createCellStyle();
                styleCurrencyFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                cellCopyTo.setCellValue(cell.getNumericCellValue());
                cellCopyTo.setCellStyle(styleCurrencyFormat);
            }
            else {
                cellCopyTo.setCellValue(cell.getNumericCellValue());
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            cellCopyTo.setCellValue(cell.getBooleanCellValue());
        }
    }

}
