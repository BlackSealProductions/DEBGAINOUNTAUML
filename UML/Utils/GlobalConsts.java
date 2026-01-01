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

    static public userType getUserTypeByString(String type){
        if (type.equalsIgnoreCase("Company")){return userType.BUSINESS;}
        else if (type.equalsIgnoreCase("Individual")){return userType.INDIVIDUAL;}
        else return null;
    }

}
