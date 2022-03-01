package it.calolenoci.importaListini;

import it.calolenoci.importaListini.helper.ReaderHelper;
import it.calolenoci.importaListini.model.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

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
    private AppProperties appProperties;

    @Resource
    ReaderHelper readerHelper;

    public void execute() {

        long startBatch = System.currentTimeMillis();

        try {
            log.info("Inizio importazione");
            for (String fornitore : appProperties.getFornitoriMapper().keySet()) {
                String inputDir = appProperties.getInputDir() + "/" + fornitore;
                File source = new File(inputDir);
                if(!source.exists()) {
                    log.debug("The source for the Excel file(s) cannot be found at " + source);
                }
                if(source.isDirectory()){
                    File[] files = source.listFiles();
                    if(files != null && files.length > 0){
                        readerHelper.read(files, fornitore);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Errore importazione ", e);
        } finally {
            long endBatch = System.currentTimeMillis();
            log.info("TERMINE ESECUZIONE IN " + ((endBatch - startBatch) / 1000) + " sec");
        }

    }

}
