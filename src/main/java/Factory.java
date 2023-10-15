import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
class StreamComparator implements Comparator<Stream>
{
    public int compare(Stream s1, Stream s2)
    {
        if(s1.nrOfStreams - s2.nrOfStreams > 0)
            return -1;
        else if(s1.nrOfStreams - s2.nrOfStreams < 0)
            return 1;
        else return 0;

    }
}
class StreamDateComparator implements Comparator<Stream>
{
    public int compare(Stream s1, Stream s2)
    {
        String date1 = s1.parseDateAdded(s1.dateAdded);
        String date2 = s2.parseDateAdded(s2.dateAdded);
        int y1 = Integer.parseInt(date1.substring(6,10));
        int y2 = Integer.parseInt(date2.substring(6,10));
        int m1 = Integer.parseInt(date1.substring(3,5));
        int m2 = Integer.parseInt(date2.substring(3,5));
        int d1 = Integer.parseInt(date1.substring(0,2));
        int d2 = Integer.parseInt(date2.substring(0,2));

        if(y1 < y2) return 1;
        else if(y1 > y2) return -1;

        if(m1 < m2) return 1;
        else if(m1 > m2) return -1;

        if(d1 < d2) return 1;
        else if(d1 > d2) return -1;

        if(s1.nrOfStreams - s2.nrOfStreams > 0)
            return -1;
        else if(s1.nrOfStreams - s2.nrOfStreams < 0)
            return 1;
        else return 0;

    }
}
abstract class Request{
    Request(){};
    public String text;
}

class AddStream extends Request implements Command{
    AddStream(String line){
        this.text = line;
    };
    public int streamerID;
    public int streamID;
    public int streamType;
    public int streamGenre;
    public long streamLength;
    public String streamName;
    //execute the command
    public void execute(ProiectPOO manager){
        Streamer sm = null;
        Stream st = null;
        boolean exists = false;
        //check is streamer is already on the list
        for(Streamer i : manager.Streamers){
            if(i.id.number == this.streamerID){
                exists = true;
                break;
            }
        }
        //if streamer is not on the list, add it
        if(!exists){
            sm = new Streamer();
            sm.id = new ID();
            sm.id.number = this.streamerID;
            sm.id.type = 0;
            sm.name = streamName;
            manager.Streamers.add(sm);
        }
        //add the stream to the Streams list
        st = new Stream();
        st.id = new ID();
        st.id.number = this.streamID;
        st.id.type = 3;
        st.streamerID = this.streamerID;
        st.streamType = this.streamType;
        st.streamGenre = this.streamGenre;
        st.nrOfStreams = 0;
        st.length = this.streamLength;
        st.name = this.streamName;
        st.dateAdded = System.currentTimeMillis()/1000;
        manager.Streams.add(st);
    }
}

class ListStreams extends Request implements Command{
    ListStreams(String line){
        this.text = line;
    };
    public int streamerID;
    public void execute(ProiectPOO manager){
        //get streamer name
        String streamerName = "";
        for(Streamer i : manager.Streamers){
            if(i.id.number == this.streamerID){
                streamerName = i.name;
            }
        }
        //get all the streams of the streamer
        //System.out.print("[");
        String allStreams = "[";
        for(Stream s : manager.Streams){
            if(s.streamerID == streamerID){
                allStreams += "{\"id\":\"" + s.id.number + "\",\"name\":\"" + s.name + "\",\"streamerName\":\""
                        + streamerName + "\",\"noOfListenings\":\"" + s.nrOfStreams + "\",\"length\":\"" +
                        s.parseTime(s.length) + "\",\"dateAdded\":\"" + s.parseDateAdded(s.dateAdded) +"\"},";
            }
        }
        allStreams = allStreams.substring(0, allStreams.length() - 1) + "]";
        System.out.println(allStreams);
    }
}

class DeleteStream extends Request implements Command{
    DeleteStream(String line){
        this.text = line;
    };
    public int streamerID;
    public int streamID;
    public void execute(ProiectPOO manager){
        for(Stream s : manager.Streams){
            if(s.id.number == streamID){
                manager.Streams.remove(s);
                break;
            }
        }
    }
}

class ListHistory extends Request implements Command{
    ListHistory(String line){
        this.text = line;
    }
    public int userID;
    public void execute(ProiectPOO manager){
        //execute the command
        User user = null;
        String allStreams = "[";
        for(User u : manager.Users){
            if(u.id.number == userID){
                user = u;
            }
        }
        if(user == null){
            System.out.println("User not found");
            return;
        }
        //for each stream ID in the user's history
        for(int i = 0; i < user.streams.size(); i++){
            int streamID = user.streams.get(i);
            //look it up in the Streams list
            for(Stream s : manager.Streams){
                if(s.id.number == streamID){
                    //when found, find streamer name and print
                    String streamerName = "";
                    for(Streamer st : manager.Streamers){
                        if(st.id.number == s.streamerID){
                            streamerName = st.name;
                        }
                    }
                    allStreams += "{\"id\":\"" + s.id.number + "\",\"name\":\"" + s.name + "\",\"streamerName\":\""
                            + streamerName + "\",\"noOfListenings\":\"" + s.nrOfStreams + "\",\"length\":\"" +
                            s.parseTime(s.length) + "\",\"dateAdded\":\"" + s.parseDateAdded(s.dateAdded) +"\"},";
                }
            }
        }
        allStreams = allStreams.substring(0, allStreams.length() - 1) + "]";
        System.out.println(allStreams);
    }
}

class ListenStream extends Request implements Command{
    ListenStream(String line){
        this.text = line;
    };
    public int userID;
    public int streamID;

    public void execute(ProiectPOO manager){
        //add stream to user's history
        for(User u : manager.Users){
            if(u.id.number == userID){
                u.streams.add(streamID);
            }
        }
        //increase noOfStreams
        for(Stream s : manager.Streams){
            if(s.id.number == streamID){
                s.nrOfStreams++;
            }
        }
    }
}

class Recommend5 extends Request implements Command{
    Recommend5(String line){
        this.text = line;
    };
    public int userID;
    public int recommendType;

    public void execute(ProiectPOO manager){
        //get user's history
        User user = null;
        TreeSet<Stream> recommended = new TreeSet<>(new StreamComparator());
        ArrayList<Integer> listenedStreamers = new ArrayList<>();
        for(User u : manager.Users){
            if(u.id.number == userID){
                user = u;
                for(int i = 0; i < u.streams.size(); i++){
                    int streamID = u.streams.get(i);
                    for(Stream s : manager.Streams){
                        if(s.id.number == streamID){
                            listenedStreamers.add(s.streamerID);
                        }
                    }
                }
            }
        }
        if(user == null){
            System.out.println("User not found");
            return;
        }


        for(Stream s : manager.Streams){
            if(s.streamType == recommendType && !(user.streams.contains(s.id.number)) && listenedStreamers.contains(s.streamerID)){
                recommended.add(s);
            }
        }
        //print the first 5
        String allStreams = "[";
        int i = 0;
        String streamerName = "";
        for(Stream s : recommended){
            if (i == 5) {
                break;
            }
            for(Streamer st : manager.Streamers){
                if(st.id.number == s.streamerID){
                    streamerName = st.name;
                }
            }
            allStreams += "{\"id\":\"" + s.id.number + "\",\"name\":\"" + s.name + "\",\"streamerName\":\""
                    + streamerName + "\",\"noOfListenings\":\"" + s.nrOfStreams + "\",\"length\":\"" +
                    s.parseTime(s.length) + "\",\"dateAdded\":\"" + s.parseDateAdded(s.dateAdded) +"\"},";
            i++;
        }
        allStreams = allStreams.substring(0, allStreams.length() - 1) + "]";
        System.out.println(allStreams);
    }
}

class Recommend3 extends Request implements Command{
    public int userID;
    public int recommendType;
    Recommend3(String line){
        this.text = line;
    };
    public void execute(ProiectPOO manager){
        TreeSet<Stream> surprise = new TreeSet<>(new StreamDateComparator());
        ArrayList<Integer> listenedStreamers = new ArrayList<>();
        User user = null;
        for(User u : manager.Users){
            if(u.id.number == userID){
                user = u;
                for(int i = 0; i < u.streams.size(); i++){
                    int streamID = u.streams.get(i);
                    for(Stream s : manager.Streams){
                        if(s.id.number == streamID){
                            listenedStreamers.add(s.streamerID);
                        }
                    }
                }
            }
        }
        if(user == null){
            System.out.println("User not found");
            return;
        }
        for(Stream s : manager.Streams){
            if(s.streamType == recommendType && !(listenedStreamers.contains(s.streamerID))){
                surprise.add(s);
            }
        }
        String allStreams = "[";
        int i = 0;
        String streamerName = "";
        for(Stream s : surprise){
            if (i == 3) {
                break;
            }
            for(Streamer st : manager.Streamers){
                if(st.id.number == s.streamerID){
                    streamerName = st.name;
                }
            }
            allStreams += "{\"id\":\"" + s.id.number + "\",\"name\":\"" + s.name + "\",\"streamerName\":\""
                    + streamerName + "\",\"noOfListenings\":\"" + s.nrOfStreams + "\",\"length\":\"" +
                    s.parseTime(s.length) + "\",\"dateAdded\":\"" + s.parseDateAdded(s.dateAdded) +"\"},";
            i++;
        }
        allStreams = allStreams.substring(0, allStreams.length() - 1) + "]";
        System.out.println(allStreams);
    }
}
public class Factory {
    public Request createItem(String text, List<Integer> uid_list){
        String[] args = text.split(" ");

        switch (args[1]) {
            case "ADD" :
                AddStream newAddStream = new AddStream(text);
                newAddStream.streamerID = Integer.parseInt(args[0]);
                newAddStream.streamType = Integer.parseInt(args[2]);
                newAddStream.streamID = Integer.parseInt(args[3]);
                newAddStream.streamGenre = Integer.parseInt(args[4]);
                newAddStream.streamLength = Long.parseLong(args[5]);
                String name = "";
                for(int i = 6; i < args.length; i++){
                    name += " " + args[i];
                }
                newAddStream.streamName = name;
                return newAddStream;

            case "LIST" :
                int parsedID = Integer.parseInt(args[0]);
                if(!(uid_list.contains(parsedID))){
                    ListStreams newListStreams = new ListStreams(text);
                    newListStreams.streamerID = parsedID;
                    return newListStreams;
                }
                else{
                    ListHistory newListHistory = new ListHistory(text);
                    newListHistory.userID = parsedID;
                    return newListHistory;
                }

            case "DELETE" :
                DeleteStream newDeleteStream = new DeleteStream(text);
                newDeleteStream.streamerID = Integer.parseInt(args[0]);
                newDeleteStream.streamID = Integer.parseInt(args[2]);
                return newDeleteStream;

            case "LISTEN" :
                ListenStream newListenStream = new ListenStream(text);
                newListenStream.userID = Integer.parseInt(args[0]);
                newListenStream.streamID = Integer.parseInt(args[2]);
                return newListenStream;

            case "RECOMMEND" :
                Recommend5 newRecommend5 = new Recommend5(text);
                newRecommend5.userID = Integer.parseInt(args[0]);
                if(args[2].equals("SONG")){
                    newRecommend5.recommendType = 1;
                }
                if(args[2].equals("PODCAST")){
                    newRecommend5.recommendType = 2;
                }
                if(args[2].equals("AUDIOBOOK")){
                    newRecommend5.recommendType = 3;
                }
                return newRecommend5;
            case "SURPRISE" :
                Recommend3 newRecommend3 = new Recommend3(text);
                newRecommend3.userID = Integer.parseInt(args[0]);
                if(args[2].equals("SONG")){
                    newRecommend3.recommendType = 1;
                }
                if(args[2].equals("PODCAST")){
                    newRecommend3.recommendType = 2;
                }
                if(args[2].equals("AUDIOBOOK")){
                    newRecommend3.recommendType = 3;
                }
                return newRecommend3;
        }
        return null;
    }
}
