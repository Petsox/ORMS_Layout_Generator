package com.orms;

public class CodeGenerator {

    protected static String GenerateTracks(String[][] LayoutMap){
        // x, y, z - Contents, q - length / name-(Switch/Signal), r - to state (Switch)
        final String TemplateTrack = "gui.newLabel(mainGui, y, x, \"z\", black, white, q),";
        final String TemplateSwitch = "gui.newSwitch(mainGui, y, x, \"z\", \"r\", \"q\", Switch),";
        final String TemplateSignal = "gui.newSignal(mainGui, y, x, \"q\", red, \"z\", Signal),";
        String Temp = "";
        String Temp2 = "";
        String Output;
        int loop = 1;
        String Tracks = "Tracks = {";
        String Signals = "Signals = {";
        String Switches = "Switches = {";
        String End = "}";

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
                        }else {

                            Temp = TemplateTrack.replace("x", Integer.toString(i));
                            Temp = Temp.replace("y", Integer.toString(j));
                            Temp2 = LayoutMap[i][j];

                            while (LayoutMap[i][j + loop] == "═"){
                                Temp2 = Temp2 + LayoutMap[i][j + loop];
                                loop++;
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
        Output = Signals + StorageSignals + End + Tracks + StorageTracks + End + Switches + StorageSwitches + End;

        return Output;
    }
}
