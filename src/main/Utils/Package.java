package main.Utils;

/**
 * Created by Jakub on 2017-05-23.
 */
public class Package {
    private String command;
    private String additional;
    private int x;
    private int y;
    private int length;

    public Package(String command, String additional, int x, int y, int length) {
        this.command = command;
        this.additional = additional;
        this.x = x;
        this.y = y;
        this.length = length;
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
