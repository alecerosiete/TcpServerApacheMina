package tcpserverapachemina;

/**
 *
 * @author alejandro
 */
public class UpSensorData {

    private String uuid;
    private int rec_type = 0;
    private String in = "";
    private String out = "";
    private String report_time;
    private String battery_level;
    private String warn_status;
    private String batterytx_level;
    private String signal_status;

    public UpSensorData(String uuid, int rec_type, String in, String out, String report_time, String battery_level, String warn_status, String batterytx_level, String signal_status) {
       this.setUuid(uuid);
       this.setRec_type(rec_type);
       this.setIn(in);
       this.setOut(out);
       this.setReport_time(report_time);
       this.setBattery_level(battery_level);
       this.setWarn_status(warn_status);
       this.setBatterytx_level(batterytx_level);
       this.setSignal_status(signal_status);
    }

    public String getUuid() {
        return uuid;
    }

    public int getRec_type() {
        return rec_type;
    }

    public String getIn() {
        return in;
    }

    public String getOut() {
        return out;
    }

    public String getReport_time() {
        return report_time;
    }

    public String getBattery_level() {
        return battery_level;
    }

    public String getWarn_status() {
        return warn_status;
    }

    public String getBatterytx_level() {
        return batterytx_level;
    }

    public String getSignal_status() {
        return signal_status;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setRec_type(int rec_type) {
        this.rec_type = rec_type;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public void setBattery_level(String battery_level) {
        this.battery_level = battery_level;
    }

    public void setWarn_status(String warn_status) {
        this.warn_status = warn_status;
    }

    public void setBatterytx_level(String batterytx_level) {
        this.batterytx_level = batterytx_level;
    }

    public void setSignal_status(String signal_status) {
        this.signal_status = signal_status;
    }
    
    

}
