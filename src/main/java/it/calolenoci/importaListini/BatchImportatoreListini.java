package it.calolenoci.importaListini;

import it.calolenoci.importaListini.model.Configuration;
import it.calolenoci.importaListini.reader.FileReader;
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
    private
    Configuration configuration;


    public void execute() {

        long startBatch = System.currentTimeMillis();

        try {
            log.debug("Inizio importazione");
            String inputDir = configuration.getInputDir();
            log.debug("inputdir: " + inputDir);
        } catch (Exception e) {
            log.error("Errore importazione ", e);
        } finally {
            long endBatch = System.currentTimeMillis();
            log.info("TERMINE ESECUZIONE IN " + ((endBatch - startBatch) / 1000) + " sec");
        }

    }

}
