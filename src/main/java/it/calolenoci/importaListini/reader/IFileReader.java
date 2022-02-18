package it.calolenoci.importaListini.reader;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public interface IFileReader {

    void read(List<File> file);
}
