package it.calolenoci.importaListini.reader;

import it.calolenoci.importaListini.model.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;


public class TxtReader implements IFileReader {

    private static final Logger log = LogManager.getLogger(TxtReader.class);

    @Resource
    private Configuration configuration;

    @Override
    public void read(List<File> file) {
        try {
            for (File f : file) {
                FileInputStream inputStream = new FileInputStream(f);
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("Errore durante importazione dei file txt", e);
        }
    }
}
