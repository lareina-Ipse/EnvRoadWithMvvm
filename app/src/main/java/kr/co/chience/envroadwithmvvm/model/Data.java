package kr.co.chience.envroadwithmvvm.model;

public class Data  {

    public String cdc;
    public String mic;
    public String voc;
    public String co2;
    public String temp;
    public String att;
    public String humInt;
    public String humDec;

    public Data(String cdc, String mic, String voc, String co2, String temp, String att, String humInt, String humDec) {
        this.cdc = cdc;
        this.mic = mic;
        this.voc = voc;
        this.co2 = co2;
        this.temp = temp;
        this.att = att;
        this.humInt = humInt;
        this.humDec = humDec;
    }
}
