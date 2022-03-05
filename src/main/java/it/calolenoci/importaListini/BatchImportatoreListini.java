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
            File inputFolder = new File(appProperties.getInputDir());
            if(!inputFolder.exists()){
                log.debug("The inputFolder cannot be found");
                boolean mkdir = inputFolder.mkdir();
                if(mkdir){
                    log.info("cartella d'ingresso creata!");
                }
            }
            log.info("Inizio importazione");
            for (String fornitore : appProperties.getFornitoriMapper().keySet()) {
                File source = new File(inputFolder + "/" + fornitore);
                if(!source.exists()) {
                    log.debug("The source for the Excel file(s) cannot be found at " + source);
                    boolean mkdir = source.mkdir();
                    if(mkdir){
                        log.info("cartella per il fornitore " + fornitore + " creata!");
                    }
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
