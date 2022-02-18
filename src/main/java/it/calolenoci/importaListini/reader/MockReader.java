package it.calolenoci.importaListini.reader;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Primary
public class MockReader implements IFileReader {


    @Override
    public void read(List<File> file) {
        // code here
    }
}
