package it.calolenoci.importaListini.helper;

import it.calolenoci.importaListini.constant.ConstantString;
import it.calolenoci.importaListini.reader.IFileReader;
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
    IFileReader excelReader;

    //@Resource
    //IFileReader csvReader;

    public void read(File[]  files, String fornitore){
        List<File> csvFiles = Arrays.stream(files).filter(f -> ConstantString.CSV.equals(FilenameUtils.getExtension(f.getPath()))).collect(Collectors.toList());
        List<File> excelFiles = Arrays.stream(files).filter(f -> FilenameUtils.getExtension(f.getPath()).startsWith(ConstantString.XLS)).collect(Collectors.toList());
        //csvReader.read(csvFiles, fornitore);
        excelReader.read(excelFiles, fornitore);
    }
}
