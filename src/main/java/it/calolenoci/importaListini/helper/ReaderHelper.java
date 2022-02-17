package it.calolenoci.importaListini.helper;

import it.calolenoci.importaListini.reader.CsvReader;
import it.calolenoci.importaListini.reader.FileReader;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReaderHelper {

    @Resource
    FileReader excelReader;

    public void read(File[]  files){
        List<File> txtFiles = Arrays.stream(files).filter(f -> "txt".equals(FilenameUtils.getExtension(f.getPath()))).collect(Collectors.toList());
        List<File> csvFiles = Arrays.stream(files).filter(f -> "csv".equals(FilenameUtils.getExtension(f.getPath()))).collect(Collectors.toList());
        List<File> excelFiles = Arrays.stream(files).filter(f -> "xlsx".equals(FilenameUtils.getExtension(f.getPath()))).collect(Collectors.toList());
        //txtReader = new TxtReader();
        //txtReader.read(txtFiles);
        FileReader csvReader = new CsvReader();
        csvReader.read(csvFiles);
        excelReader.read(excelFiles);
    }
}
