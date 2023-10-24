package com.orms;

public class CodeGenerator {

    // x, y, z - Contents, q - length / name-(Switch/Signal), r - to state (Switch)
    final static String TemplateTrack = "{y, x, \"z\"},";
    final static String TemplateSwitch = "{y, x, \"z\", \"r\", \"q\"},";
    static String Temp = "";
    static String Temp2 = "";
    static String Output;
    static int loop = 1;
    static String Config = "Config = {}\n";
    static String Tracks = "Config.Tracks = {";
    static String Switches = "Config.Switches = {";
    static String End = "}" + "\n";
    static String Return = "return Config";
    static String StorageSignals = "";
    static String StorageTracks = "\n";
    static String StorageSwitches = "\n";

    private static void clearCache() {
        StorageSignals = "";
        StorageTracks = "\n";
        StorageSwitches = "\n";

    }

    public static String generateTracks(String[][] LayoutMap) {

        for (int i = 0; i < LayoutMap.length; i++) {

            for (int j = 0; j < 160; j++) {

                if (loop <= 1) {

                    if (LayoutMap[i][j] != "") {

                        if (LayoutMap[i][j].length() > 1) {

                            if (LayoutMap[i][j].charAt(2) == 'V') {

                                Temp = TemplateSwitch.replace("x", Integer.toString(i));
                                Temp = Temp.replace("y", Integer.toString(j));
                                Temp = Temp.replace('z', LayoutMap[i][j].charAt(0));
                                Temp = Temp.replace('r', LayoutMap[i][j].charAt(4));
                                Temp = Temp.replace("q", LayoutMap[i][j].substring(6));
                                Temp = Temp + '\n';

                                StorageSwitches = StorageSwitches + Temp;


                            } else if (LayoutMap[i][j].charAt(2) == 'N') {

                                Replace.feedToTemplate(LayoutMap, i, j);

                            }
                        } else if (LayoutMap[i][j] != null) {

                            Temp = TemplateTrack.replace("x", Integer.toString(i));
                            Temp = Temp.replace("y", Integer.toString(j));
                            Temp2 = LayoutMap[i][j];

                            while (LayoutMap[i][j + loop].equals("═")) {
                                Temp2 = Temp2 + LayoutMap[i][j + loop];
                                loop++;
                                if (LayoutMap[i][j + loop].equals("╗")) {
                                    Temp2 = Temp2 + LayoutMap[i][j + loop];
                                    loop++;
                                }
                                if (LayoutMap[i][j + loop].equals("╝")) {
                                    Temp2 = Temp2 + LayoutMap[i][j + loop];
                                    loop++;
                                }
                                if (LayoutMap[i][j + loop].equals("╚")) {
                                    Temp2 = Temp2 + LayoutMap[i][j + loop];
                                    loop++;
                                }
                                if (LayoutMap[i][j + loop].equals("╗")) {
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
                } else loop--;
            }
        }

        StorageSignals = Replace.Signal1 + End + Replace.Signal3 + End + Replace.Signal4 + End + Replace.Signal5 + End + Replace.SignalSh + End + Replace.SignalEx + End;

        Output = Config + StorageSignals + Tracks + StorageTracks + End + Switches + StorageSwitches + End + Return;

        Replace.clearCache();
        clearCache();

        return Output;
    }

}
