package it.calolenoci.importaListini.model;

import it.calolenoci.importaListini.constant.ConstantString;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Matrice {

    private String codArticolo;
    private String codEan;
    private String descraarticolo;
    private String unitamisura;
    private String unitamisura2;
    private String unitamisuraSec;
    private String coefficiente;
    private String costo;
    private String prezzo;
    private String codiceIva;
    private String classea1;
    private String classea2;
    private String classea3;
    private String classea4;
    private String classea5;
    private String qtaPerConf;
    private String peso;
    private String quantitauser01;
    private String quantitauser02;
    private String quantitauser03;
    private String quantitauser04;
    private String quantitauser05;
    private String campouser1;
    private String campouser2;
    private String campouser3;
    private String campouser4;
    private String campouser5;

    public Matrice() {
    }

    public Matrice(String codArticolo, String codEan, String descraarticolo, String unitamisura, String unitamisura2, String unitamisuraSec,
            String coefficiente, String costo, String prezzo, String codiceIva, String classea1, String classea2, String classea3,
            String classea4, String classea5, String qtaPerConf, String peso, String quantitauser01, String quantitauser02,
            String quantitauser03, String quantitauser04, String quantitauser05, String campouser1, String campouser2, String campouser3,
            String campouser4, String campouser5) {
        this.codArticolo = codArticolo;
        this.codEan = codEan;
        this.descraarticolo = descraarticolo;
        this.unitamisura = unitamisura;
        this.unitamisura2 = unitamisura2;
        this.unitamisuraSec = unitamisuraSec;
        this.coefficiente = coefficiente;
        this.costo = costo;
        this.prezzo = prezzo;
        this.codiceIva = codiceIva;
        this.classea1 = classea1;
        this.classea2 = classea2;
        this.classea3 = classea3;
        this.classea4 = classea4;
        this.classea5 = classea5;
        this.qtaPerConf = qtaPerConf;
        this.peso = peso;
        this.quantitauser01 = quantitauser01;
        this.quantitauser02 = quantitauser02;
        this.quantitauser03 = quantitauser03;
        this.quantitauser04 = quantitauser04;
        this.quantitauser05 = quantitauser05;
        this.campouser1 = campouser1;
        this.campouser2 = campouser2;
        this.campouser3 = campouser3;
        this.campouser4 = campouser4;
        this.campouser5 = campouser5;
    }

        private String formatCurrency(String amount) throws ParseException {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(amount);
            DecimalFormat formatter = new DecimalFormat(ConstantString.CSV_CURRENCY_FORMAT);
            return formatter.format(number.doubleValue());
        }

    public String getCodArticolo() {
        return codArticolo;
    }

    public void setCodArticolo(String codArticolo) {
        this.codArticolo = codArticolo;
    }

    public String getCodEan() {
        return codEan;
    }

    public void setCodEan(String codEan) {
        this.codEan = codEan;
    }

    public String getDescraarticolo() {
        return descraarticolo;
    }

    public void setDescraarticolo(String descraarticolo) {
        this.descraarticolo = descraarticolo;
    }

    public String getUnitamisura() {
        return unitamisura;
    }

    public void setUnitamisura(String unitamisura) {
        this.unitamisura = unitamisura;
    }

    public String getUnitamisura2() {
        return unitamisura2;
    }

    public void setUnitamisura2(String unitamisura2) {
        this.unitamisura2 = unitamisura2;
    }

    public String getUnitamisuraSec() {
        return unitamisuraSec;
    }

    public void setUnitamisuraSec(String unitamisuraSec) {
        this.unitamisuraSec = unitamisuraSec;
    }

    public String getCoefficiente() {
        return coefficiente;
    }

    public void setCoefficiente(String coefficiente) {
        this.coefficiente = coefficiente;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) throws ParseException {
        this.costo = formatCurrency(costo);
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo)  throws ParseException{
        this.prezzo = formatCurrency(prezzo);
    }

    public String getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(String codiceIva) {
        this.codiceIva = codiceIva;
    }

    public String getClassea1() {
        return classea1;
    }

    public void setClassea1(String classea1) {
        this.classea1 = classea1;
    }

    public String getClassea2() {
        return classea2;
    }

    public void setClassea2(String classea2) {
        this.classea2 = classea2;
    }

    public String getClassea3() {
        return classea3;
    }

    public void setClassea3(String classea3) {
        this.classea3 = classea3;
    }

    public String getClassea4() {
        return classea4;
    }

    public void setClassea4(String classea4) {
        this.classea4 = classea4;
    }

    public String getClassea5() {
        return classea5;
    }

    public void setClassea5(String classea5) {
        this.classea5 = classea5;
    }

    public String getQtaPerConf() {
        return qtaPerConf;
    }

    public void setQtaPerConf(String qtaPerConf) {
        this.qtaPerConf = qtaPerConf;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getQuantitauser01() {
        return quantitauser01;
    }

    public void setQuantitauser01(String quantitauser01) {
        this.quantitauser01 = quantitauser01;
    }

    public String getQuantitauser02() {
        return quantitauser02;
    }

    public void setQuantitauser02(String quantitauser02) {
        this.quantitauser02 = quantitauser02;
    }

    public String getQuantitauser03() {
        return quantitauser03;
    }

    public void setQuantitauser03(String quantitauser03) {
        this.quantitauser03 = quantitauser03;
    }

    public String getQuantitauser04() {
        return quantitauser04;
    }

    public void setQuantitauser04(String quantitauser04) {
        this.quantitauser04 = quantitauser04;
    }

    public String getQuantitauser05() {
        return quantitauser05;
    }

    public void setQuantitauser05(String quantitauser05) {
        this.quantitauser05 = quantitauser05;
    }

    public String getCampouser1() {
        return campouser1;
    }

    public void setCampouser1(String campouser1) {
        this.campouser1 = campouser1;
    }

    public String getCampouser2() {
        return campouser2;
    }

    public void setCampouser2(String campouser2) {
        this.campouser2 = campouser2;
    }

    public String getCampouser3() {
        return campouser3;
    }

    public void setCampouser3(String campouser3) {
        this.campouser3 = campouser3;
    }

    public String getCampouser4() {
        return campouser4;
    }

    public void setCampouser4(String campouser4) {
        this.campouser4 = campouser4;
    }

    public String getCampouser5() {
        return campouser5;
    }

    public void setCampouser5(String campouser5) {
        this.campouser5 = campouser5;
    }

}
