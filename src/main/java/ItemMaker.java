import java.text.SimpleDateFormat;
import java.util.*;

class ID{
    ID(){}; //constructor
    public int number;
    public int type;
}
abstract class Item {
    public ID id;
}
class Streamer extends Item{
    public String name;
    public int streamerType;
}
class User extends Item{
    public String name;
    public List<Integer> streams = new ArrayList<>();
}
class Stream extends Item{
    String name;
    public int streamType;
    public int streamGenre;
    public long nrOfStreams;
    public int streamerID;
    public long length;
    public long dateAdded;
    public String parseTime(long time){
        long seconds = time % 60;
        long minutes = (time / 60) % 60;
        long hours = time / (60 * 60);
        String timeString = "";
        if(hours > 0){
            if(hours < 10){
                timeString += "0";
            }
            timeString += hours + ":";
        }
        if(minutes > 0){
            if(minutes < 10){
                timeString += "0";
            }
            timeString += minutes + ":";
        }else if(hours > 0){
                timeString += "00:";
        }
        if(seconds > 0){
            if(seconds < 10){
                timeString += "0";
            }
            timeString += seconds;
        }else if(hours > 0 || minutes > 0){
                timeString += "00";
        }
        return timeString;
    }
    public String parseDateAdded(long date){
        String dateAdded = "";
        Date d = new Date(date * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateAdded = simpleDateFormat.format(d);
        return dateAdded;
    }

}
public class ItemMaker {
    public Item createItem(String type){
        if(type.equals("Streamer")){
            return new Streamer();
        }
        else if(type.equals("User")){
            return new User();
        }
        else if(type.equals("Stream")){
            return new Stream();
        }
        return null;
    }
}
