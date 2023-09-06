package com.orms;

public class CodeGenerator {

    protected static String generateTracks(String[][] LayoutMap){
        // x, y, z - Contents, q - length / name-(Switch/Signal), r - to state (Switch)
        final String TemplateTrack = "{x, y, \"z\"},";
        final String TemplateSwitch = "{x, y, \"z\", \"r\", \"q\"},";
        final String TemplateSignal = "{x, y, \"q\", \"z\"},";
        String Temp = "";
        String Temp2 = "";
        String Output;
        int loop = 1;
        String Config = "Config = {}\n";
        String Tracks = "Config.Tracks = {";
        String Signals = "Config.Signals = {";
        String Switches = "Config.Switches = {";
        String End = "}" + "\n";
        String Return = "return Config";

        String StorageSignals = "\n";
        String StorageTracks = "\n";
        String StorageSwitches = "\n";

        for (int i = 0; i < LayoutMap.length; i++) {

            for (int j = 0; j < 160; j++) {

                if (loop <= 1){

                    if (LayoutMap[i][j] != ""){

                        if (LayoutMap[i][j].length() > 1){

                            if (LayoutMap[i][j].charAt(2) == 'V'){

                                Temp =  TemplateSwitch.replace("x", Integer.toString(i));
                                Temp = Temp.replace("y", Integer.toString(j));
                                Temp = Temp.replace('z', LayoutMap[i][j].charAt(0));
                                Temp = Temp.replace('r', LayoutMap[i][j].charAt(4));
                                Temp = Temp.replace("q", LayoutMap[i][j].substring(6));
                                Temp = Temp + '\n';

                                StorageSwitches = StorageSwitches + Temp;


                            } else if (LayoutMap[i][j].charAt(2) == 'N') {

                                Temp =  TemplateSignal.replace("x", Integer.toString(i));
                                Temp = Temp.replace("y", Integer.toString(j));
                                Temp = Temp.replace('z', LayoutMap[i][j].charAt(0));
                                Temp = Temp.replace("q", LayoutMap[i][j].substring(4));
                                Temp = Temp + '\n';

                                StorageSignals = StorageSignals + Temp;

                            }
                        }else if (LayoutMap[i][j].equals("═") || LayoutMap[i][j].equals("╔") || LayoutMap[i][j].equals("║")){

                            Temp = TemplateTrack.replace("x", Integer.toString(i));
                            Temp = Temp.replace("y", Integer.toString(j));
                            Temp2 = LayoutMap[i][j];

                            while (LayoutMap[i][j + loop].equals("═")){
                                Temp2 = Temp2 + LayoutMap[i][j + loop];
                                loop++;
                                if (LayoutMap[i][j + loop].equals("╗")){
                                    Temp2 = Temp2 + LayoutMap[i][j + loop];
                                    loop++;
                                }
                                if (LayoutMap[i][j + loop].equals("╝")){
                                    Temp2 = Temp2 + LayoutMap[i][j + loop];
                                    loop++;
                                }
                            }



                            Temp = Temp.replace("z", Temp2);
                            Temp = Temp.replace("q", Integer.toString(loop));
                            Temp = Temp + '\n';

                            StorageTracks = StorageTracks + Temp;
                        }
                    }
                }else loop--;
            }
        }
        Output = Config + Signals + StorageSignals + End + Tracks + StorageTracks + End + Switches + StorageSwitches + End + Return;

        return Output;
    }
}
