package io.cheukyin699.wiicontroller;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class DataSender implements Observer {

    final private String serverIp;
    final private int serverPort;

    DataSender(final String serverIp, final int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof SensorController) {
            final SensorController controller = (SensorController) observable;
            final float[] vals = controller.getValues();
            StringBuilder builder = new StringBuilder();
            // There will be an extra comma at the end, but we can remove it on the server's side
            for (float f : vals) {
                builder.append(f);
                builder.append(",");
            }

            try {
                sendData(serverIp, serverPort, builder.toString());
            } catch (UnknownHostException e) {
                // TODO: Better error handling
                e.printStackTrace();
            }
        }
    }

    private void sendData(final String serverIp, final int serverPort, final String data) throws UnknownHostException {
        final SendTask client = new SendTask(serverIp, serverPort, data);
        client.execute();
    }

    private static class SendTask extends AsyncTask<Void, Void, Void> {

        final private InetAddress serverIp;
        final private int serverPort;
        final private String data;

        SendTask(final String serverIp, final int serverPort, final String data) throws UnknownHostException {
            this.serverIp = InetAddress.getByName(serverIp);
            this.serverPort = serverPort;
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DatagramSocket sock = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(
                        this.data.getBytes(),
                        this.data.getBytes().length,
                        this.serverIp,
                        this.serverPort);
                sock.setBroadcast(true);
                sock.send(packet);
            } catch (SocketException e) {
                // TODO: Better error handling
                e.printStackTrace();
            } catch (IOException e) {
                // TODO: Better error handling
                e.printStackTrace();
            }
            return null;
        }
    }
}
