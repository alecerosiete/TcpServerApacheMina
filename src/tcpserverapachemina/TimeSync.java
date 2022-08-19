package tcpserverapachemina;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alejandro
 */
public class TimeSync {

    private String uuid;
    int rec_type = 0;
    String time = "";
    String uploadInterval = "";
    String dataStartTime;
    String dataEndTime;

    public TimeSync(String uuid, int rec_type, String time, String uploadInterval, String dataStartTime, String dataEndTime) {
        this.setUuid(uuid);
        this.setRec_type(rec_type);
        this.setTime(time);
        this.setUploadInterval(uploadInterval);
        this.setDataStartTime(dataStartTime);
        this.setDataEndTime(dataEndTime);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setRec_type(int rec_type) {
        this.rec_type = rec_type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUploadInterval(String uploadInterval) {
        this.uploadInterval = uploadInterval;
    }

    public void setDataStartTime(String dataStartTime) {
        this.dataStartTime = dataStartTime;
    }

    public void setDataEndTime(String dataEndTime) {
        this.dataEndTime = dataEndTime;
    }

    public String getUuid() {
        return uuid;
    }

    public int getRec_type() {
        return rec_type;
    }

    public String getTime() {
        return time;
    }

    public String getUploadInterval() {
        return uploadInterval;
    }

    public String getDataStartTime() {
        return dataStartTime;
    }

    public String getDataEndTime() {
        return dataEndTime;
    }
}
