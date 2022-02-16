package it.calolenoci.importaListini.reader;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public interface FileReader {

    void read(File file);
}
