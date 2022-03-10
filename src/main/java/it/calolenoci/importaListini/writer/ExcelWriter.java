package it.calolenoci.importaListini.writer;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.calolenoci.importaListini.mapping.ExcelMappingStrategy;
import it.calolenoci.importaListini.model.AppProperties;
import it.calolenoci.importaListini.model.Matrice;
import it.calolenoci.importaListini.reader.ExcelReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component public class ExcelWriter implements IFileWriter {

    @Resource private AppProperties appProperties;

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    private DataFormatter formatter;

    private FormulaEvaluator evaluator;

    private Map<Integer, String> columnFilteredMap = new HashMap<>();

    private List<Matrice> matriceList;

    private String fornitore;

    @Override public void write(Workbook workbook, String filename, String fornitore) throws IOException, ParseException {
        this.fornitore = fornitore;
        this.formatter = new DataFormatter();
        this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        int index = 1;
        for (Sheet sheet : workbook) {
            int firstRow = getFirstNotEmptyRows(sheet);
            Row headerRow = sheet.getRow(firstRow);
            int lastRow = sheet.getLastRowNum();
            //filtro la lista per recuperare solo i valori da riportare nel file definitivo usando una map. Così mi salvo anche il nome della colonna
            //da poter usare per la formattazione eventuale delle celle
            this.columnFilteredMap = setColumnFilteredMap(fornitore, headerRow, firstRow);
            this.convertToCSV(sheet, firstRow, lastRow);
            File destination = new File(appProperties.getOutputDir());
            if(!destination.exists()){
                log.debug("The destination directory cannot be found");
                boolean mkdir = destination.mkdir();
                if(mkdir){
                    log.info("cartella d'uscita creata!");
                }
            }
            if (!destination.isDirectory()) {
                throw new IllegalArgumentException("The destination " + destination + " for the CSV " + "file(s) is not a directory/folder.");
            }
            this.writeCsvFile(filename, destination, index);
            index++;
        }
        this.moveExcelFile(filename);
    }

    private void moveExcelFile(String filename){
        try{
            File sourceFile = new File(appProperties.getInputDir()+"/"+fornitore+"/"+filename);
            File importedFolder = new File(appProperties.getImportedDir());
            if(!importedFolder.exists()){
                boolean mkdir = importedFolder.mkdir();
                if(mkdir){
                    log.debug("cartella importati creata");
                }
            }
            File destFile = new File(importedFolder+"/"+filename);
            FileUtils.moveFile(sourceFile, destFile);
        } catch (IOException e){
            log.error("Error moving file: ", e);
        }
    }

    private int getFirstNotEmptyRows(Sheet sheet){
        int totalRow = sheet.getLastRowNum();
        int currentRow = sheet.getFirstRowNum();
        int firstNotEmptyRow = 0;
        boolean breakRow = false;
        Row row;

        while (currentRow <= totalRow) {
            row = sheet.getRow(currentRow);
            if (row.getPhysicalNumberOfCells() > 0){
                for (Cell cell : row) {
                    if(!cell.getCellType().equals(CellType.STRING)){
                        continue;
                    }
                    if(appProperties.getFornitoriMapper().get(fornitore).containsKey(StringUtils.deleteWhitespace(cell.getStringCellValue()))){
                        firstNotEmptyRow = currentRow;
                        breakRow = true;
                        break;
                    }
                }
            }
            if(breakRow){
                break;
            }
            currentRow++;
        }
        return firstNotEmptyRow;
    }

    private void writeCsvFile(String filename, File destination, int i) throws IOException {
        String extension = FilenameUtils.getExtension(filename);
        String filenameWithoutExt = StringUtils.remove(filename, ("." + extension));
        File destFolder = new File(destination + "/" + fornitore);
        if(!destFolder.exists()){
            boolean mkdir = destFolder.mkdir();
            if(mkdir){
                log.debug("cartella output per il fornitore " + fornitore + " creata");
            }
        }
        FileWriter writer = new FileWriter(destFolder + "/" + filenameWithoutExt + "_" + i + ".txt");
        try {
            final ExcelMappingStrategy<Matrice> mappingStrategy = new ExcelMappingStrategy<>();
            mappingStrategy.setColumnMapping();
            mappingStrategy.setType(Matrice.class);

            final StatefulBeanToCsv<Matrice> beanToCsv = new StatefulBeanToCsvBuilder<Matrice>(writer).withMappingStrategy(mappingStrategy)
                    .withApplyQuotesToAll(appProperties.getCsvQuoteSN()).withSeparator('\t').build();
            beanToCsv.write(matriceList);
        } catch (CsvRequiredFieldEmptyException e) {
            log.error("CsvRequiredFieldEmptyException: ", e);
        } catch (CsvDataTypeMismatchException ex) {
            log.error("CsvDataTypeMismatchException: ", ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ee) {
                log.error("Error closing csv writer: ", ee);
            }
        }
    }

    /**
     * Called to convert the contents of the currently opened workbook into
     * a CSV file.
     */
    private void convertToCSV(Sheet sheetCopyFrom, int firstRow, int lastRow) throws ParseException {
        log.debug("Converting files contents to CSV format.");
        this.matriceList = new ArrayList<>();
        for (int i = (firstRow + 1); i <= lastRow; i++) {
            Row row = sheetCopyFrom.getRow(i);
            Matrice matrice = this.rowToCSV(row);
            this.matriceList.add(matrice);
        }
    }

    /**
     * Called to convert a row of cells into a line of data that can later be
     * output to the CSV file.
     *
     * @param row               An instance of either the HSSFRow or XSSFRow classes that
     *                          encapsulates information about a row of cells recovered from
     *                          an Excel workbook.
     *
     * @return Matrice
     */
    private Matrice rowToCSV(Row row) throws ParseException {
        log.debug("Processing row..." + row.getRowNum());
        Matrice matrice = new Matrice();
        List<String> headerProps = appProperties.getHeader();
        for (Integer integer : this.columnFilteredMap.keySet()) {
            Cell cellCopyFrom = row.getCell(integer, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //devo capire dove settare il valore filtrato
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(0))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCodArticolo(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCodArticolo(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(1))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCodEan(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCodEan(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(2))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setDescraarticolo(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setDescraarticolo(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(3))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setUnitamisura(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setUnitamisura(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(4))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setUnitamisura2(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setUnitamisura2(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(5))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setUnitamisuraSec(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setUnitamisuraSec(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(6))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCoefficiente(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCoefficiente(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(7))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCosto(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCosto(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(8))) {
                double prezzo;
                prezzo = cellCopyFrom.getNumericCellValue();
                if(appProperties.getFornitoriMapper().get(fornitore).containsKey("aumento_perc") &&
                        appProperties.getFornitoriMapper().get(fornitore).get("aumento_perc") != null){
                    log.debug("applicare l'aumento della percentuale");
                    double aumento = Integer.parseInt(appProperties.getFornitoriMapper().get(fornitore).get("aumento_perc"));
                    double tot = prezzo + (prezzo * aumento / 100);
                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    log.debug("il nuovo prezzo è di: " + prezzo);
                    matrice.setPrezzo(formatter.format(tot));
                    continue;
                }
                NumberFormat formatter = NumberFormat.getNumberInstance();
                matrice.setPrezzo(formatter.format(prezzo));
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(9))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCodiceIva(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCodiceIva(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(10))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setClassea1(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setClassea1(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(11))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setClassea2(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setClassea2(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(12))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setClassea3(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setClassea3(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(13))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setClassea4(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setClassea4(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(14))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setClassea5(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setClassea5(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(15))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQtaPerConf(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQtaPerConf(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(16))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setPeso(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setPeso(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(17))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQuantitauser01(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQuantitauser01(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(18))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQuantitauser02(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQuantitauser02(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(19))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQuantitauser03(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQuantitauser03(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(20))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQuantitauser04(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQuantitauser04(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(21))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setQuantitauser05(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setQuantitauser05(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(22))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCampouser1(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCampouser1(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(23))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCampouser2(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCampouser2(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(24))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCampouser3(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCampouser3(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(25))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCampouser4(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCampouser4(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
                continue;
            }
            if (columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(26))) {
                if (cellCopyFrom.getCellType() != CellType.FORMULA) {
                    matrice.setCampouser5(formatter.formatCellValue(cellCopyFrom));
                } else {
                    matrice.setCampouser5(formatter.formatCellValue(cellCopyFrom, evaluator));
                }
            }
        }
        return matrice;
    }

    private Map<Integer, String> setColumnFilteredMap(String fornitore, Row row, int firstRow) {
        for (int i = firstRow; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();
            // ciclo sulle configurazione fornitore.
            if (!cellType.equals(CellType.STRING)) {
                continue;
            }
            Map<String, String> fornitoreMap = appProperties.getFornitoriMapper().get(fornitore);
            String excelHeaderColumn = cell.getStringCellValue();
            //es. atlas chiave->codice
            //          valore->codArticolo
            if (fornitoreMap.containsKey(StringUtils.deleteWhitespace(excelHeaderColumn))) {
                this.columnFilteredMap.put(cell.getColumnIndex(), fornitoreMap.get(StringUtils.deleteWhitespace(excelHeaderColumn)));
            }
        }
        return this.columnFilteredMap;
    }
}