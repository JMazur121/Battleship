package Utils;

public class Package {
    private String command;
    private String additional=null;
    private int x = -1;
    private int y = -1;
    private int length = -1;

    public Package(String command, String additional, int x, int y, int length) {
        this.command = command;
        this.additional = additional;
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public Package(String command,int x,int y){
        this.command = command;
        this.x = x;
        this.y = y;
    }

    public Package(String command,String additional){
        this.command = command;
        this.additional = additional;
    }

    public String toString(){
        String message = "";
        message = message + this.command +"#";
        if(this.additional != null)
            message = message + this.additional + "#";
        if(this.x != -1)
            message = message + this.x + "#";
        if(this.y != -1)
            message = message + this.y + "#";
        if (this.length != -1)
            message = message + this.length + "#";
        return message;
    }
}
