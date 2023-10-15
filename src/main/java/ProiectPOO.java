import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProiectPOO {
        //Singleton design pattern
    private static ProiectPOO instance;
    private ProiectPOO() {}
    public static ProiectPOO Instance(){
        if(instance == null){
            instance = new ProiectPOO();
        }
        return instance;
    }
    public static void renewInstance(){
        instance = new ProiectPOO();
    }
    //Declare class variables
    public String Folder;
    File StreamersFile, UsersFile, StreamsFile, CommandsFile;
    public List<Streamer> Streamers = new ArrayList<>();
    public List<User> Users = new ArrayList<>();
    public List<Stream> Streams = new ArrayList<>();
    public Queue<Request> Requests = new LinkedList<>();
    public List<Integer> uid_list = new ArrayList<>();

    public void BuildManager(String args[]){
        this.Folder = "src/main/resources/";
        StreamersFile = new File(Folder + args[0]);
        UsersFile = new File(Folder + args[2]);
        StreamsFile = new File(Folder + args[1]);
        CommandsFile = new File(Folder + args[3]);
    }
    public void getUsers(File file) throws FileNotFoundException{
        //get users from file
        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(file));
            if(line.equals("")){line = br.readLine();}
            while((line = br.readLine()) != null){
                String[] user = line.split(",");
                User u = new User();
                u.id = new ID();
                u.id.number = Integer.parseInt(user[0].substring(1, user[0].length()-1));
                u.id.type = 1;
                u.name = user[1].substring(1, user[1].length()-1);
                String[] streams = (user[2].substring(1,user[2].length()-1)).split(" ");
                for(int i = 0; i < streams.length; i++){
                    u.streams.add(Integer.parseInt(streams[i]));
                }
                this.uid_list.add(u.id.number);
                this.Users.add(u);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getStreamers(File file) throws FileNotFoundException{
        //get streamers from file
        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(file));
            if(line.equals("")){line = br.readLine();}
            while((line = br.readLine()) != null){
                String[] user = line.split(",");
                Streamer s = new Streamer();
                s.id = new ID();
                s.id.number = Integer.parseInt(user[1].substring(1, user[1].length()-1));
                s.id.type = 0;
                s.name = user[2].substring(1, user[2].length()-1);
                s.streamerType = Integer.parseInt(user[0].substring(1, user[0].length()-1));
                this.Streamers.add(s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void getStreams(File file) throws FileNotFoundException{
        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(file));
            if(line.equals("")){line = br.readLine();}
            while((line = br.readLine()) != null){
                String[] stream = line.split(",");
                Stream s = new Stream();
                s.id = new ID();
                s.streamType = Integer.parseInt(stream[0].substring(1, stream[0].length()-1));
                s.id.number = Integer.parseInt(stream[1].substring(1, stream[1].length()-1));
                s.id.type = 3;
                s.streamGenre = Integer.parseInt(stream[2].substring(1, stream[2].length()-1));
                s.nrOfStreams = Integer.parseInt(stream[3].substring(1, stream[3].length()-1));
                s.streamerID = Integer.parseInt(stream[4].substring(1, stream[4].length()-1));
                s.length = Integer.parseInt(stream[5].substring(1, stream[5].length()-1));
                s.dateAdded = Long.parseLong(stream[6].substring(1, stream[6].length()-1));
                String name = stream[7];
                for(int i = 8; i < stream.length; i++){
                    name += "," + stream[i];
                }
                s.name = name.substring(1, name.length()-1);
                this.Streams.add(s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getRequests(File file) throws FileNotFoundException{
        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(file));
            Factory factory = new Factory();
            //generate requests
            while((line = br.readLine()) != null){
                Request r = factory.createItem(line, this.uid_list);
                this.Requests.add(r);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //facade
    public void getData() throws FileNotFoundException{
        this.getStreamers(StreamersFile);
        this.getStreams(StreamsFile);
        this.getUsers(UsersFile);
        this.getRequests(CommandsFile);
    }

    public void runRequests(){
        for(Request j : this.Requests){
            if(j != null) {
                ((Command) j).execute(this);
            }
        }
    }

    public static void main(String[] args){
        ProiectPOO Manager = Instance();
        if(args == null) {
            System.out.println("Nothing to read here");
            return;
        }
        //create manager instance
        Manager.BuildManager(args);
        //get data from files
        try {
            Manager.getData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //run requests
        Manager.runRequests();
        //renew singleton when done
        renewInstance();
    }
}
