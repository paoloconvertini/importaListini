package it.calolenoci.importaListini.reader;

import com.opencsv.*;
import it.calolenoci.importaListini.constant.ConstantString;
import it.calolenoci.importaListini.model.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Component
public class CsvReader implements IFileReader {

    private static final Logger log = LogManager.getLogger(CsvReader.class);

    @Resource
    Configuration configuration;

    @Override
    public void read(List<File> file) {
        try {
            for (File f : file) {
                CSVParserBuilder parserBuilder = new CSVParserBuilder();
                if(StringUtils.isNotEmpty(configuration.getCsvSeparator()) && StringUtils.equals(configuration.getCsvSeparator(), ";")){
                    parserBuilder.withSeparator(';');
                }
                CSVParser parser = parserBuilder.build();
                CSVReader csvReader = new CSVReaderBuilder(new FileReader(f))
                        .withCSVParser(parser)
                        .build();

                List<String[]> rowList = csvReader.readAll();
                if(rowList.isEmpty()){
                    log.error("il file non contiene dati");
                    break;
                }
                String[] headerArray = rowList.get(0);
                Map<Integer, String> columnMap = new HashMap<>();
                for (int i = 0; i < headerArray.length; i++) {
                    if (configuration.getCodice().equals(headerArray[i]) ||
                            configuration.getDescrizione().equals(headerArray[i]) ||
                            configuration.getPrezzi().equals(headerArray[i])) {
                        columnMap.put(i, headerArray[i]);
                    }
                }
                List<String[]> csvCopyTo = new ArrayList<>();
                //Recupero l'indice della colonna prezzi
                //per formattare tutti gli elementi di questa colonna
                Integer prezziIndex = 0;
                for (Integer integer : columnMap.keySet()) {
                    if(StringUtils.equals(columnMap.get(integer), configuration.getPrezzi())){
                        prezziIndex = integer;
                    }
                }
                for (String[] rowArray : rowList) {
                    List<String> stringToCopy = new ArrayList<>();
                    for (int i = 0; i < rowArray.length; i++) {
                        String s = rowArray[i];
                        //recupero solo gli elementi che mi servono da importare, cioè
                        //quelli presenti nella mappa creata prima
                        if(columnMap.containsKey(i)){
                            if(i == prezziIndex && !StringUtils.equals(configuration.getPrezzi(), s)){
                                String formatCurrency = formatCurrency(s);
                                stringToCopy.add(formatCurrency);
                            } else {
                                stringToCopy.add(s);
                            }
                        }
                    }
                    csvCopyTo.add(stringToCopy.toArray(new String[0]));
                }
                csvWriter(csvCopyTo, configuration.getOutputDir() + "/" + f.getName());
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file txt", e);
        }
    }

    private void csvWriter(List<String[]> stringArray, String path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        writer.writeAll(stringArray);
        writer.close();
    }

    private String formatCurrency(String amount) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = format.parse(amount);
        DecimalFormat formatter = new DecimalFormat(ConstantString.CSV_CURRENCY_FORMAT);
        return formatter.format(number.doubleValue());
    }
}
