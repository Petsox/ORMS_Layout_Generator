package com.orms;

public class Replace {
    final static String TemplateSignal = "{y, x, \"q\", \"z\"},";
    static String Signal1 = "Config.Signal1 = {\n";
    static String Signal4 = "Config.Signal4 = {\n";
    static String Signal5 = "Config.Signal5 = {\n";
    static String SignalSh = "Config.SignalSh = {\n";
    static String SignalEx = "Config.SignalEx = {\n";
    static String Signal = "";

    public static void feedToTemplate(String[][] LayoutMap, int i, int j){

        Signal =  TemplateSignal.replace("x", Integer.toString(i));
        Signal = Signal.replace("y", Integer.toString(j));
        Signal = Signal.replace('z', LayoutMap[i][j].charAt(0));
        Signal = Signal.replace("q", LayoutMap[i][j].substring(4));
        Signal = Signal + '\n';
        sortSignal(LayoutMap[i][j]);
    }

    public static void sortSignal(String SignalI){
        if (SignalI.charAt(4) == '1') Signal1 += Signal;
        else if (SignalI.charAt(4) == '4') Signal4 += Signal;
        else if (SignalI.charAt(4) == '5') Signal5 += Signal;
        else if (SignalI.charAt(4) == 'P') SignalEx += Signal;
        else if (SignalI.charAt(4) == 'S') SignalSh += Signal;

    }

    public static void clearCache(){
        Signal1 = "Config.Signal1 = {\n";
        Signal4 = "Config.Signal4 = {\n";
        Signal5 = "Config.Signal5 = {\n";
        SignalSh = "Config.SignalSh = {\n";
        SignalEx = "Config.SignalEx = {\n";
        Signal = "";
    }

}
