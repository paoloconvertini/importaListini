package it.calolenoci.importaListini.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Matrice {

    @CsvBindByName(column = "CodArticolo") @CsvBindByPosition(position = 0) private String codArticolo;

    @CsvBindByName(column = "CodEan") @CsvBindByPosition(position = 1) private String codEan;

    @CsvBindByName(column = "Descraarticolo") @CsvBindByPosition(position = 2) private String descraarticolo;

    @CsvBindByName(column = "unitamisura") @CsvBindByPosition(position = 3) private String unitamisura;

    @CsvBindByName(column = "unitamisura2") @CsvBindByPosition(position = 4) private String unitamisura2;

    @CsvBindByName(column = "unitamisuraSec") @CsvBindByPosition(position = 5) private String unitamisuraSec;

    @CsvBindByName(column = "Coefficiente") @CsvBindByPosition(position = 6) private String coefficiente;

    @CsvBindByName(column = "Costo") @CsvBindByPosition(position = 7) private String costo;

    @CsvBindByName(column = "Prezzo") @CsvBindByPosition(position = 8) private String prezzo;

    @CsvBindByName(column = "CodiceIva") @CsvBindByPosition(position = 9) private String codiceIva;

    @CsvBindByName(column = "Classea1") @CsvBindByPosition(position = 10) private String classea1;

    @CsvBindByName(column = "Classea2") @CsvBindByPosition(position = 11) private String classea2;

    @CsvBindByName(column = "Classea3") @CsvBindByPosition(position = 12) private String classea3;

    @CsvBindByName(column = "Classea4") @CsvBindByPosition(position = 13) private String classea4;

    @CsvBindByName(column = "Classea5") @CsvBindByPosition(position = 14) private String classea5;

    @CsvBindByName(column = "qtaPerConf") @CsvBindByPosition(position = 15) private String qtaPerConf;

    @CsvBindByName(column = "Peso") @CsvBindByPosition(position = 16) private String peso;

    @CsvBindByName(column = "quantitauser01") @CsvBindByPosition(position = 17) private String quantitauser01;

    @CsvBindByName(column = "quantitauser02") @CsvBindByPosition(position = 18) private String quantitauser02;

    @CsvBindByName(column = "quantitauser03") @CsvBindByPosition(position = 19) private String quantitauser03;

    @CsvBindByName(column = "quantitauser04") @CsvBindByPosition(position = 20) private String quantitauser04;

    @CsvBindByName(column = "quantitauser05") @CsvBindByPosition(position = 21) private String quantitauser05;

    @CsvBindByName(column = "campouser1") @CsvBindByPosition(position = 22) private String campouser1;

    @CsvBindByName(column = "campouser2") @CsvBindByPosition(position = 23) private String campouser2;

    @CsvBindByName(column = "campouser3") @CsvBindByPosition(position = 24) private String campouser3;

    @CsvBindByName(column = "campouser4") @CsvBindByPosition(position = 25) private String campouser4;

    @CsvBindByName(column = "campouser5") @CsvBindByPosition(position = 26) private String campouser5;

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

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
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
