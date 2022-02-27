package it.calolenoci.importaListini.writer;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
public interface IFileWriter {
    void write(Workbook wbCopyFrom, String filename, String fornitore) throws IOException, ParseException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
