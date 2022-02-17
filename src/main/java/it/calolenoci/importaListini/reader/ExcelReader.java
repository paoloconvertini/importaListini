package it.calolenoci.importaListini.reader;

import com.aspose.cells.*;
import it.calolenoci.importaListini.model.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
                org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook(inputStream);
                generate(workbook, f.getName());
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

    private void generate(org.apache.poi.ss.usermodel.Workbook wbCopyFrom, String filename) throws Exception{
        Sheet sheetCopyFrom = wbCopyFrom.getSheetAt(0);
        org.apache.poi.ss.usermodel.Row headerRow = sheetCopyFrom.getRow(0);
        int firstRow = sheetCopyFrom.getFirstRowNum();
        int lastRow = sheetCopyFrom.getLastRowNum();
        List<Integer> colIndexList = new ArrayList<>();
        Map<Integer, String> columnMap = new HashMap<>();
        //filtro la lista per recuperare solo i valori da riportare nel file definitivo
        int columnIndex;
        for (int cellHeaderIndex = headerRow.getFirstCellNum(); cellHeaderIndex < headerRow.getLastCellNum(); cellHeaderIndex++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.getCell(cellHeaderIndex, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            org.apache.poi.ss.usermodel.CellType cellType = cell.getCellType().equals(org.apache.poi.ss.usermodel.CellType.FORMULA)
                    ? cell.getCachedFormulaResultType() : cell.getCellType();
            if (cellType.equals(org.apache.poi.ss.usermodel.CellType.STRING)) {
                if (configuration.getCodice().equals(cell.getStringCellValue()) ||
                        configuration.getDescrizione().equals(cell.getStringCellValue()) ||
                        configuration.getPrezzi().equals(cell.getStringCellValue())) {
                    columnMap.put(cell.getColumnIndex(), cell.getStringCellValue());
                    columnIndex = cell.getColumnIndex();
                    log.debug("codice col index=" + columnIndex);
                    colIndexList.add(columnIndex);
                }
            }
        }
        XSSFWorkbook wbCopyTo = new XSSFWorkbook();
        FileOutputStream out = new FileOutputStream(configuration.getOutputDir() + "/" + filename);
        XSSFSheet sheetCopyTo = wbCopyTo.createSheet(sheetCopyFrom.getSheetName());
        for(int i = firstRow; i <= lastRow; i++){
            XSSFRow rowCopyTo = sheetCopyTo.createRow(i);
            org.apache.poi.ss.usermodel.Row rowCopyFrom = sheetCopyFrom.getRow(i);
            int counter = 0;
            for (Integer integer : columnMap.keySet()) {
                org.apache.poi.ss.usermodel.Cell cellCopyTo = rowCopyTo.createCell(counter);
                org.apache.poi.ss.usermodel.Cell cellCopyFrom = rowCopyFrom.getCell(integer, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                setCellValue(cellCopyFrom, cellCopyTo, columnMap.get(integer));
                counter++;
            }
        }
        wbCopyTo.write(out);
        out.close();
        wbCopyTo.close();
    }

   /* private void generateExcelToImport(Workbook wbCopyFrom, String filename) throws Exception {
        Workbook wbCopyTo = new Workbook();
        Worksheet sheet = wbCopyFrom.getWorksheets().get(0);
        Cells cells = sheet.getCells();
        Range range = cells.getMaxDisplayRange();
        int i = range.getColumnCount();
        while (i >= 0){
            int type = cells.get(0, i).getType();
            String value = cells.get(0, i).getStringValue();
            if (type == CellValueType.IS_STRING) {
                if (!configuration.getCodice().equals(value) &&
                        !configuration.getDescrizione().equals(value) &&
                        !configuration.getPrezzi().equals(value)) {
                    cells.deleteColumn(i);
                    log.debug("codice col index=" + i);
                }
            }
            i--;
        }
        wbCopyTo.getWorksheets().get(0).copy(wbCopyFrom.getWorksheets().get(0));
        wbCopyTo.save(configuration.getOutputDir() + "/" + filename, FileFormatType.XLSX);

    }

    private void polishExcel(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        XSSFRow row = sheetAt.getRow(0);
        XSSFCell cell = row.getCell(0);
        workbook.removeSheetAt(1);
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();
        workbook.close();
    }*/

    private void setCellValue(org.apache.poi.ss.usermodel.Cell cell, org.apache.poi.ss.usermodel.Cell cellCopyTo, String colName) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            cellCopyTo.setCellValue(cell.getStringCellValue());
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellCopyTo.setCellValue(cell.getDateCellValue());
            } else if (configuration.getPrezzi().equals(colName)){
                //TODO format currency
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
