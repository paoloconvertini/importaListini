package it.calolenoci.importaListini.reader;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import it.calolenoci.importaListini.constant.ConstantString;
import it.calolenoci.importaListini.model.AppProperties;
import it.calolenoci.importaListini.model.Matrice;
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
    private AppProperties appProperties;

    private Workbook wbCopyTo;

    @Override
    public void read(List<File> file, String fornitore) {
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
                generate(workbook, f.getName(), fornitore);
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file excel", e);
        }
    }

    private void generate(Workbook wbCopyFrom, String filename, String fornitore) throws Exception {
        // FIXME da verificare la presenza dei dati sul primo sheet
        Sheet sheetCopyFrom = wbCopyFrom.getSheetAt(0);
        //FIXME da verificare la presenza dell'header nella prima row
        Row headerRow = sheetCopyFrom.getRow(0);
        int firstRow = sheetCopyFrom.getFirstRowNum();
        int lastRow = sheetCopyFrom.getLastRowNum();
        //filtro la lista per recuperare solo i valori da riportare nel file definitivo usando una map. Così mi salvo anche il nome della colonna
        //da poter usare per la formattazione eventuale delle celle
        Map<Integer, String> columnFilteredMap = new HashMap<>();
        for (int cellHeaderIndex = headerRow.getFirstCellNum(); cellHeaderIndex < headerRow.getLastCellNum(); cellHeaderIndex++) {
            Cell cell = headerRow.getCell(cellHeaderIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();
            // ciclo sulle configurazione fornitore.
            // se 
            if (!cellType.equals(CellType.STRING)) {
                break;
            }
            Map<String, String> fornitoreMap = appProperties.getFornitoriMapper().get(fornitore);
            String excelHeaderColumn = cell.getStringCellValue();
            //es. atlas chiave->codice
            //          valore->codArticolo
            if (fornitoreMap.containsKey(excelHeaderColumn)) {
                columnFilteredMap.put(cell.getColumnIndex(), fornitoreMap.get(excelHeaderColumn));
            }
        }

        List<Matrice> matriceList = new ArrayList<>();
        List<String> headerProps = appProperties.getHeader();

        for (int i = firstRow; i <= lastRow; i++) {
            Row rowCopyFrom = sheetCopyFrom.getRow(i);
            Matrice matrice = new Matrice();

            for (Integer integer : columnFilteredMap.keySet()) {
                Cell cellCopyFrom = rowCopyFrom.getCell(integer, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //devo capire dove settare il valore filtrato

                if(columnFilteredMap.get(integer).equals(headerProps.get(0))){
                    matrice.setCodArticolo(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equals(headerProps.get(1))){
                    matrice.setCodEan(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equals(headerProps.get(2))){
                    matrice.setDescraarticolo(cellCopyFrom.getStringCellValue());
                }
            }
            matriceList.add(matrice);
        }

        FileWriter writer = new FileWriter(appProperties.getOutputDir()+"/listino_"+fornitore+".txt");
        ColumnPositionMappingStrategy mappingStrategy= new ColumnPositionMappingStrategy();
        mappingStrategy.setType(Matrice.class);

        // Arrange column name as provided in below array.
        mappingStrategy.setColumnMapping(headerProps.toArray(new String[0]));

        // Creating StatefulBeanToCsv object
        StatefulBeanToCsvBuilder<Matrice> builder= new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();

        // Write list to StatefulBeanToCsv object
        beanWriter.write(matriceList);

        // closing the writer object
        writer.close();





/*        FileOutputStream out = new FileOutputStream(appProperties.getOutputDir() + "/" + filename);
        Sheet sheetCopyTo = wbCopyTo.createSheet(sheetCopyFrom.getSheetName());
        for (int i = firstRow; i <= lastRow; i++) {
            Row rowCopyTo = sheetCopyTo.createRow(i);
            Row rowCopyFrom = sheetCopyFrom.getRow(i);
            int counter = 0;
            for (Integer integer : columnFilteredMap.keySet()) {
                //FIXME da capire se è importante rispettare un ordine per le colonne
                Cell cellCopyTo = rowCopyTo.createCell(counter);
                Cell cellCopyFrom = rowCopyFrom.getCell(integer, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                setCellValue(wbCopyTo, cellCopyFrom, cellCopyTo, columnFilteredMap.get(integer));
                counter++;
            }
        }
        wbCopyTo.write(out);
        out.close();
        wbCopyTo.close();*/
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
            } else if (1==0) {
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
