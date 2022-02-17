package it.calolenoci.importaListini.reader;

import it.calolenoci.importaListini.model.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Component
public class CsvReader implements FileReader {

    @Resource
    Configuration configuration;

    @Override
    public void read(List<File> file) {

    }
}
