package it.calolenoci.importaListini.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "properties")
public class AppProperties {

    private Map<String, Map<String, String>> fornitoriMapper;
    private List<String> header;
    private String inputDir;
    private String outputDir;
    private Boolean csvQuoteSN;

    public Boolean getCsvQuoteSN() {
        return csvQuoteSN;
    }

    public void setCsvQuoteSN(Boolean csvQuoteSN) {
        this.csvQuoteSN = csvQuoteSN;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public Map<String, Map<String, String>> getFornitoriMapper() {
        return fornitoriMapper;
    }

    public void setFornitoriMapper(Map<String, Map<String, String>> fornitoriMapper) {
        this.fornitoriMapper = fornitoriMapper;
    }

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
}
