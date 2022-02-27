package it.calolenoci.importaListini.writer;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.calolenoci.importaListini.model.AppProperties;
import it.calolenoci.importaListini.model.Matrice;
import it.calolenoci.importaListini.reader.ExcelReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelWriter implements IFileWriter{

    @Resource
    private AppProperties appProperties;

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    @Override
    public void write(Workbook wbCopyFrom, String filename, String fornitore) throws IOException, ParseException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
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

                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(0))){
                    matrice.setCodArticolo(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(1))){
                    matrice.setCodEan(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(2))){
                    matrice.setDescraarticolo(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(3))){
                    matrice.setUnitamisura(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(4))){
                    matrice.setUnitamisura2(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(5))){
                    matrice.setUnitamisuraSec(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(6))){
                    matrice.setCoefficiente(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(7))){
                    matrice.setCosto(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(8))){
                    matrice.setPrezzo(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(9))){
                    matrice.setCodiceIva(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(10))){
                    matrice.setClassea1(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(11))){
                    matrice.setClassea2(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(12))){
                    matrice.setClassea3(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(13))){
                    matrice.setClassea4(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(14))){
                    matrice.setClassea5(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(15))){
                    matrice.setQtaPerConf(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(16))){
                    matrice.setPeso(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(17))){
                    matrice.setQuantitauser01(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(18))){
                    matrice.setQuantitauser02(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(19))){
                    matrice.setQuantitauser03(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(20))){
                    matrice.setQuantitauser04(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(21))){
                    matrice.setQuantitauser05(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(22))){
                    matrice.setCampouser1(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(23))){
                    matrice.setCampouser2(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(24))){
                    matrice.setCampouser3(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(25))){
                    matrice.setCampouser4(cellCopyFrom.getStringCellValue());
                }
                if(columnFilteredMap.get(integer).equalsIgnoreCase(headerProps.get(26))){
                    matrice.setCampouser5(cellCopyFrom.getStringCellValue());
                }
            }
            matriceList.add(matrice);
        }

        FileWriter writer = new FileWriter(appProperties.getOutputDir()+"/"+filename+"_"+fornitore+".txt");
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
    }
}
