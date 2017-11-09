package shashank.robotplayer;

public class DebugLogger {
    private static final boolean WARNING_LEVEL = true;
    private static final boolean INFO_LEVEL = true;
    private static final boolean ERROR_LEVEL = true;

    public static void printInfo(String message){
        if(INFO_LEVEL){
            System.out.println(message);
        }
    }

    public static void printWarning(String message){
        if(WARNING_LEVEL){
            System.out.println(message);
        }
    }

    public static void printError(String message){
        if(ERROR_LEVEL){
            System.out.println(message);
        }
    }

    public static void printError(Exception e){
        if(ERROR_LEVEL){
            e.printStackTrace();
        }
    }
}
