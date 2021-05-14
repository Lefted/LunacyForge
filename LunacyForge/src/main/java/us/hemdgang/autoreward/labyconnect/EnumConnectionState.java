package us.hemdgang.autoreward.labyconnect;

public enum EnumConnectionState
{
    HELLO(-1, "d"), 
    LOGIN(0, "b"), 
    PLAY(1, "a"), 
    ALL(2, "f", "ALL"), 
    OFFLINE(3, "c");
    
    private int number;
    private String displayColor;
    private String buttonState;
    
    private EnumConnectionState(final int number, final String displayColor) {
        this.number = number;
        this.displayColor = displayColor;
        this.buttonState = "dont know";
    }
    
    private EnumConnectionState(final int number, final String displayColor, final String buttonState) {
        this.number = number;
        this.displayColor = displayColor;
        this.buttonState = buttonState;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public String getDisplayColor() {
        return this.displayColor;
    }
    
    public String getButtonState() {
        return this.buttonState;
    }
}
