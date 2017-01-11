package kr.itanoss.androidexamplesocketio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            socket = IO.socket("http://YOUR_SOCKETIO_IP:ITS_PORT");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.e(TAG, "connected....");
                socket.emit("my event", "Android connected!");
            }

        }).on("my event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.e(TAG, "event triggered....");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.e(TAG, "disconnected....");
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(socket != null) {
            socket.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(socket != null) {
            socket.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(socket != null) {
            socket.off();
        }
    }
}
