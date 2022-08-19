package tcpserverapachemina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.json.*;

/**
 *
 * @author alejandro
 */
public class TcpServerApacheMina implements IoHandler {

    String messageSend = "Sever request...";
    int messageCounter = 0;
    String serverIp = "127.0.0.1";
    int serverPort = 8085;

    public TcpServerApacheMina() throws IOException {
        NioSocketAcceptor connector = new NioSocketAcceptor();
        connector.getFilterChain().addFirst("logger", new LoggingFilter());
        connector.setHandler(this);
        connector.bind(new InetSocketAddress(serverPort));

    }

    public static void main(String[] args) throws IOException {
        new TcpServerApacheMina();
    }

    @Override
    public void sessionCreated(IoSession is) throws Exception {
        System.err.println("Session created");
    }

    @Override
    public void sessionOpened(IoSession is) throws Exception {
        System.err.println("Session opened");
    }

    @Override
    public void sessionClosed(IoSession is) throws Exception {
        System.err.println("Session closed...");
    }

    @Override
    public void sessionIdle(IoSession is, IdleStatus is1) throws Exception {
        System.err.println("Session idle");
    }

    @Override
    public void exceptionCaught(IoSession is, Throwable thrwbl) throws Exception {
        System.out.println("JavaApacheMinaServer.exceptionCaught()");
    }

    @Override
    public void messageReceived(IoSession is, Object o) throws Exception {
        try {
            System.out.println("*******************************************");
            System.out.println("-------------------------------------------");
            System.out.println("Message Received!");
            System.out.println("-------------------------------------------");

            ByteBuffer received = ((IoBuffer) o).buf();
            byte[] byt = received.array();
            String rcvd = new String(byt);

            rcvd = rcvd.trim();
            String xmlString = rcvd.substring(8, rcvd.length() - 3);
            JSONObject json = XML.toJSONObject(xmlString);

            System.out.println("\n-------------------------------------------");
            System.out.println("Received json data: " + json.toString());
            System.out.println("-------------------------------------------");
            if (json.toString() == "{}") {
                return;
            }

            String uuid;
            int rec_type;
            String time;
            String uploadInterval;
            String startTime;
            String endTime;
            String ret;
            String response = "";

            if (json.has("TIME_SYSNC_REQ")) {
                /**
                 * TIME SYNC RECEIVED
                 * <TIME_SYSNC_REQ>
                 * <uuid>2410640A0B0C</uuid>
                 * </TIME_SYSNC_REQ>
                 */
                uuid = json.getJSONObject("TIME_SYSNC_REQ").getNumber("uuid").toString();
                LocalDateTime ldt = LocalDateTime.now();
                time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.ENGLISH).format(ldt);
                uploadInterval = "0001"; //cada 1 minuto
                startTime = "0800";
                endTime = "2300";
                ret = "0"; //status OK

                System.out.println("\n-------------------------------------------");
                System.out.println("XML received received TIME_SYSNC_REQ:  " + xmlString);
                System.out.println("Device UUID: " + uuid);
                System.out.println("-------------------------------------------");

                TimeSync ts = getSensorTimeSync(uuid);
                
                
                System.out.println("Datos config de la DB");
                System.err.println("UUID: "+ts.getUuid());
                System.err.println("Rec type: "+ts.getRec_type());
                System.err.println("Time: "+ts.getTime());
                System.err.println("Upload Interval: "+ts.getUploadInterval());
                System.err.println("Start Time: "+ts.getDataStartTime());
                System.err.println("End Time: "+ts.getDataEndTime());
                
                
                
                
                
                /**
                 * esto es la configuracion del sensor lo que exista en la tabla
                 * config se le debe enviar al sensor con esta respuesta:
                 *
                 * TimeSync es la clase que contiene las configuraciones del
                 * sensor y se obtiene para enviar al sensor y sincronizar
                 * TimeSync timesync = new TimeSync(uuid, rec_type, time,
                 * uploadInterval, startTime, endTime);
                 *
                 * TIME SYNC RESPONSE
                 * <TIME_SYSNC_RES>
                 * <uuid>2410640A0B0C</uuid>
                 * <ret>0/1</ret>
                 * <time>YYYYMMDDHHMMSS</time>
                 * < uploadInterval >HHMM</ uploadInterval >
                 * < dataStartTime >HHMM</ dataStartTime >
                 * < dataEndTime >HHMM</ dataEndTime >
                 * </TIME_SYSNC_RES>
                 */
                response = "<TIME_SYSNC_RES>";
                response += "<uuid>" + uuid + "</uuid>";
                response += "<ret>" + ret + "</ret>";
                response += "<time>" + time + "</time>";
                response += "<uploadInterval>" + uploadInterval + "</uploadInterval>";
                response += "<dataStartTime>" + startTime + "</dataStartTime>";
                response += "<dataEndTime>" + endTime + "</dataEndTime>";
                response += "</TIME_SYSNC_RES>";
            } else if (json.has("UP_SENSOR_DATA_REQ")) {
                /**
                 * REPORT DATA RECEIVED
                 * <UP_SENSOR_DATA_REQ>
                 * <uuid>2410640A0B0C</uuid>
                 * <rec_type>0/1</rec_type>
                 * <in>52</in>
                 * <out>53</out>
                 * <time>YYYYMMDDHHMMSS</time>
                 * < battery_level >80</ battery_level >
                 * <warn_status>0</warn_status>
                 * < batterytx_level >70</ batterytx_level >
                 * < signal_status >0</ signal_status >
                 * </UP_SENSOR_DATA_REQ>
                 */
                uuid = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("uuid").toString();
                rec_type = json.getJSONObject("UP_SENSOR_DATA_REQ").getInt("rec_type");
                String in = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("in").toString();
                String out = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("out").toString();
                String report_time = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("time").toString();
                String battery_level = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("battery_level").toString();
                String warn_status = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("warn_status").toString();
                String batterytx_level = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("batterytx_level").toString();
                String signal_status = json.getJSONObject("UP_SENSOR_DATA_REQ").getNumber("signal_status").toString();

                System.out.println("\n-------------------------------------------");
                System.out.println("XML received received UP_SENSOR_DATA_REQ:  " + xmlString);
                System.out.println("Device UUID: " + uuid);
                System.out.println("Device Record type: " + rec_type);
                System.out.println("Device People In: " + in);
                System.out.println("Device People Out: " + out);
                System.out.println("Device Report Time: " + report_time);
                System.out.println("Device Battery Level: " + battery_level);
                System.out.println("Device Warn Status: " + warn_status);
                System.out.println("Device Battery Tx Level: " + batterytx_level);
                System.out.println("Device Signal Status: " + signal_status);
                System.out.println("-------------------------------------------");

                /**
                 * inserta en la tabla records UpSensorData es la clase para los
                 * registros enviados por el sensor y se utiliza para enviar a
                 * guardar en la base de datos UpSensorData timesync = new
                 * TimeSync(uuid, rec_type, time, uploadInterval, startTime,
                 * endTime); InsertToDatabase(timesync);
                 */
                UpSensorData sensorData = new UpSensorData(uuid, rec_type, in, out, report_time, battery_level, warn_status, batterytx_level, signal_status);
                InsertUpSensorData(sensorData);

                /**
                 * REPORT DATA RESPONSE
                 * <UP_SENSOR_DATA_RES>
                 * <uuid>2410640A0B0C</uuid>
                 * <ret>0/1</ret>
                 * </UP_SENSOR_DATA_RES>
                 */
                response = "<UP_SENSOR_DATA_RES>";
                response += "<uuid>" + uuid + "</uuid>";
                response += "<ret>0</ret>";
                response += "</UP_SENSOR_DATA_RES>";

            }

            //send
            IoBuffer buffer = IoBuffer.allocate(response.length());
            buffer.put(response.getBytes());
            buffer.flip();
            messageCounter++;
            is.write(buffer);

        } catch (JSONException je) {
            System.out.println(je.toString());
        }
    }

    @Override
    public void messageSent(IoSession is, Object o) throws Exception {
        System.out.println("\n\n------------------------------------------");
        System.out.println("Message Sent!");
        System.out.println("-------------------------------------------");

        ByteBuffer sent = ((IoBuffer) o).buf();
        byte[] byt = sent.array();
        String rcvd = new String(byt);

        rcvd = rcvd.substring(0, sent.limit());
        System.out.println("\n------------------------------------------");
        System.out.println("Server message sent:  " + rcvd);
        System.out.println("------------------------------------------");
        System.out.println("*******************************************\n\n");

    }

    @Override
    public void inputClosed(IoSession is) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void event(IoSession is, FilterEvent fe) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void InsertUpSensorData(UpSensorData sensorData) {
        Connection conn;
        System.out.print("\n[Performing INSERT] ... ");
        try {
            String url = "jdbc:mysql://127.0.0.1/people_flow";
            conn = DriverManager.getConnection(url, "people_flow", "people_flow");

            String query = "INSERT INTO records (uuid, rec_type, people_in, people_out, time, battery_level, warn_status, batterytx_level, signal_status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sensorData.getUuid());
            preparedStmt.setInt(2, sensorData.getRec_type());
            preparedStmt.setString(3, sensorData.getIn());
            preparedStmt.setString(4, sensorData.getOut());
            preparedStmt.setString(5, sensorData.getReport_time());
            preparedStmt.setString(6, sensorData.getBattery_level());
            preparedStmt.setString(7, sensorData.getWarn_status());
            preparedStmt.setString(8, sensorData.getBatterytx_level());
            preparedStmt.setString(9, sensorData.getSignal_status());

            preparedStmt.execute();
            System.out.println("Insert success!");
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error en la conexion con la base de datos");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("error de conexión");
        }

    }

    public TimeSync getSensorTimeSync(String uuid) {

        System.out.println("[OUTPUT FROM SELECT]");
        /**
         * get config from database
         */

        Connection conn;

        TimeSync ts;
        ts = new TimeSync(uuid, 0, null, null, null, null);
        String query = "SELECT * FROM config where uuid = ?";
        try {
            String url = "jdbc:mysql://127.0.0.1/people_flow";
            conn = DriverManager.getConnection(url, "people_flow", "people_flow");

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, uuid);

            ResultSet rs = preparedStmt.executeQuery(query);
            if (rs.next()) {
                int id = rs.getInt("id");
                String rec_type = rs.getString("rec_tipe");
                String time = rs.getString("time");
                String uploadInterval = rs.getString("uploadInterval");
                String dataStartTime = rs.getString("dataStartTime");
                String dataEndTime = rs.getString("dataEndTime");
                String updated = rs.getString("updated");
                String registered = rs.getString("registered");

                System.out.println("ID: " + id);
                System.out.println("UUID: " + uuid);
                System.out.println("Time: " + time);
                System.out.println("Upload Interval: " + uploadInterval);
                System.out.println("Data start time: " + dataStartTime);
                System.out.println("Data end time: " + dataEndTime);
                System.out.println("Data updated: " + updated);
                System.out.println("Data registered: " + registered);

                ts = new TimeSync(uuid, serverPort, uuid, uuid, messageSend, serverIp);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return ts;
    }

}
