package it.calolenoci.importaListini.reader;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public interface FileReader {

    void read(List<File> file);
}
