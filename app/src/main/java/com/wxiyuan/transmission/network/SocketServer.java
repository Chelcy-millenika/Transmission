package com.wxiyuan.transmission.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends SocketBase {

    private ServerSocket serverSocket;
    private Socket socket;
    private SocketParams socketParams;
    private boolean isActive = true;

    public SocketServer(@NonNull SocketParams params) {
        socketParams = params;
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (serverSocket == null) {
            return;
        }
        InputStream inputStream = null;
        while (isActive) {
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                StringBuilder sb = new StringBuilder();
                while (inputStream.read(buffer) != -1) {
                    sb.append(new String(buffer));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendMessage(String message) {
        if (socket == null) {
            return;
        }
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
