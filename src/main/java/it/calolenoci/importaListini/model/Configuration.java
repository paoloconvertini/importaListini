package it.calolenoci.importaListini.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration {

    @Value("${input_dir}")
    private String inputDir;

    @Value("${output_dir}")
    private String outputDir;

    @Value("${codice}")
    private String codice;

    @Value("${descrizione}")
    private String descrizione;

    @Value("${prezzi}")
    private String prezzi;

    @Value("${csvSeparator}")
    private String csvSeparator;

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPrezzi() {
        return prezzi;
    }

    public void setPrezzi(String prezzi) {
        this.prezzi = prezzi;
    }

    public String getCsvSeparator() {
        return csvSeparator;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }
}
