package Utils;
public class GlobalConsts {
    
    static public final int wWidth = 1600;
    static public final int wHeight = 900;

    static public int wxCenter(int avWidth, int width){
        return (avWidth-width)/2;
    }
    static public int wyCenter(int avHeight, int height){
        return (avHeight-height)/2;
    }

    public enum userType{
        INDIVIDUAL,
        BUSINESS
    } 

}
