package it.calolenoci.importaListini.reader;

import it.calolenoci.importaListini.model.Configuration;

import javax.annotation.Resource;
import java.io.File;

public class CsvReader implements FileReader {

    @Resource
    Configuration configuration;

    @Override
    public void read(File file) {

    }
}
