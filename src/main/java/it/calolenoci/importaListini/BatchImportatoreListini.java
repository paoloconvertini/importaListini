package it.calolenoci.importaListini;

import it.calolenoci.importaListini.helper.ReaderHelper;
import it.calolenoci.importaListini.model.Configuration;
import it.calolenoci.importaListini.reader.FileReader;
import it.calolenoci.importaListini.reader.TxtReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe che si occupa di verificare le cartelle di alimentazione del processo
 * batch
 *
 * @author Paolo Convertini
 */
@Component
public class BatchImportatoreListini {

    private static final Logger log = LogManager.getLogger(BatchImportatoreListini.class);

    @Resource
    private Configuration configuration;

    @Resource
    ReaderHelper readerHelper;

    public void execute() {

        long startBatch = System.currentTimeMillis();

        try {
            log.debug("Inizio importazione");
            String inputDir = configuration.getInputDir();
            File folder = new File(inputDir);
            File[] files = folder.listFiles();
            if(files != null){
                readerHelper.read(files);
            }
            log.debug("inputdir: " + inputDir);
        } catch (Exception e) {
            log.error("Errore importazione ", e);
        } finally {
            long endBatch = System.currentTimeMillis();
            log.info("TERMINE ESECUZIONE IN " + ((endBatch - startBatch) / 1000) + " sec");
        }

    }

}
