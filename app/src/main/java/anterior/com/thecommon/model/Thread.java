package anterior.com.thecommon.model;


import android.support.annotation.NonNull;

public class Thread implements Comparable<Thread> {
    public String id;
    public String from_id;
    public String to_id;
    public String lastdisplayname;
    public String lasttext;
    public Double lasttimestamp;
    public String lastuserid;
    public String opponentid;
    public boolean isNew;

    public void setValues(String key, Object value){
        if(key.equalsIgnoreCase("last-display-name")){
            lastdisplayname = (String)value;
        }else if(key.equalsIgnoreCase("last-seen")){

        }else if(key.equalsIgnoreCase("last-text")){
            lasttext = (String)value;
        }else if(key.equalsIgnoreCase("last-timestamp")){
            lasttimestamp = (Double)value;
        }else if(key.equalsIgnoreCase("last-user-id")){
            lastuserid = (String)value;
        }

        isNew = false;


    }


    @Override
    public int compareTo(@NonNull Thread thread) {
        if(lasttimestamp > thread.lasttimestamp) {
            return -1;
        } else
            return 1;
    }
}
