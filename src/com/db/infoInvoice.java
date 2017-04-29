package com.db;

public class infoInvoice {
	 private static String Id;
    private static String Code;
    private static String Number;
    private static String Checkcode;
    private static String Date;

    infoInvoice(String Id, String Code, String Number, String Checkcode, String Date) {
        infoInvoice.Id = Id; //default
        infoInvoice.Code = Code;
        infoInvoice.Number = Number;
        infoInvoice.Checkcode = Checkcode;
        infoInvoice.Date = Date;
    }

    public static String getId() {
        return Id;
    }

    public void setId(String Id) {
        infoInvoice.Id = Id;
    }

    public static String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        infoInvoice.Code = Code;
    }

    public static String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        infoInvoice.Number = Number;
    }

    public static String getCheckcode() {
        return Checkcode;
    }

    public void setCheckcode(String Checkcode) {
        infoInvoice.Checkcode = Checkcode;
    }
    
    public static String getDate() {
   	 return Date;
    }
    
    public void setDate(String Date) {
   	 infoInvoice.Date = Date;
    }
}