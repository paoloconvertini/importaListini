package it.calolenoci.importaListini.writer;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
public interface IFileWriter {
    void write(Workbook wbCopyFrom, String filename, String fornitore) throws IOException, ParseException;
}
