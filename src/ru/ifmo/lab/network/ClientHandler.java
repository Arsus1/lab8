package ru.ifmo.lab.network;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.network.packets.CommandExecutionPacket;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.utils.AppConstant;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

@Slf4j
public class ClientHandler extends Thread {
    private class ResponseReceiver extends Thread {

        Object receivedObject = null;


        @Override
        public void run() {
            while (true) {
                try {
                    receiveData();
                } catch (ClosedChannelException ignored) {
                } catch (EOFException ex) {
                    System.err.println("Reached limit of data to receive");
                    log.error("Reached Limit", ex);
                } catch (IOException | ClassNotFoundException e) {
                    log.error("I/O Problems", e);
                }
            }
        }

        /**
         * Функция для получения данных
         */
        public void receiveData() throws IOException, ClassNotFoundException {
            final ByteBuffer buf = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
            final SocketAddress addressFromServer = channel.receiveDatagram(buf);
            buf.flip();

            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);

            if (bytes.length < 1)
                return;

            synchronized (ClientHandler.class) {
                channel.setRequestSent(false);
                if (bytes.length < AppConstant.MESSAGE_BUFFER)
                    receivedObject = processResponse(bytes);
                else
                    throw new EOFException();
            }
        }

        /**
         * Функция для десериализации полученных данных
         * @param petitionBytes - данные
         * @return obj - объект десериализованных данных
         */
        private Object processResponse(byte[] petitionBytes) throws IOException, ClassNotFoundException {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                final Object obj = stream.readObject();
                log.info("received object: " + obj);
                if (obj == null)
                    throw new ClassNotFoundException();
                return obj;
            }
        }
    }

    private final ResponseReceiver receiverThread;
    private final ClientUdpChannel channel;
    private final CurrentUser currentUser;
    private volatile long startRequestTime = 0L;
    private volatile boolean flagReceived = false;

    public ClientHandler(ClientUdpChannel channel, CurrentUser user) {
        this.channel = channel;
        this.currentUser = user;
        receiverThread = new ResponseReceiver();
        receiverThread.setName("ClientReceiverThread");
        receiverThread.start();
    }

    public void checkForResponse() throws ClassNotFoundException, InterruptedException {

        Object received = receiverThread.receivedObject;

        if (received instanceof String && received.equals("connect")) {
            channel.setConnected(true);
            log.info("Successfully connected to the server");
            System.out.println("Successfully connected to the server");
        }

        synchronized (this) {
            if (received != null) {
                printResponse(received);
                receiverThread.receivedObject = null;
            }


        }

        //System.out.println("wassent: " + channel.requestWasSent() + "  connection: " + channel.connected + "   addr: " + channel.addressServer);
    }

    /**
     * Функция для вывода объектов коллекции
     * @param obj- коллекция с объектами
     */
    public void printResponse(Object obj) throws ClassNotFoundException {
        if (obj instanceof String) {
            System.out.println(obj);
        }
        else if(obj instanceof CommandExecutionPacket){
            Object resObj = ((CommandExecutionPacket) obj).getMessage();
            String msgType = ((CommandExecutionPacket) obj).getMessageType();
            /*if(resObj instanceof String) {
                System.out.println(resObj);
            }*/

            if ("LoginCommand".equals(msgType)) {
                if(((CommandExecutionPacket) obj).getSuccessful()) {
                    currentUser.setCredentials((Credentials) resObj);
                    NetworkManager.getInstance().loginEvent.onLoginSuccessful(currentUser);
                }else {
                    NetworkManager.getInstance().loginEvent.onLoginError((String) resObj);
                }
            }else if ("RegisterCommand".equals(msgType)) {
                if(((CommandExecutionPacket) obj).getSuccessful()) {
                    currentUser.setCredentials((Credentials) resObj);
                    NetworkManager.getInstance().registerEvent.onLoginSuccessful(currentUser);
                }else {
                    NetworkManager.getInstance().registerEvent.onLoginError((String) resObj);
                }
            }else if("ShowCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().showEvent.onResponse(resObj);
                else NetworkManager.getInstance().showEvent.onError(resObj);
            }else if("AddCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().addEvent.onResponse(resObj);
                else NetworkManager.getInstance().addEvent.onError(resObj);
            }else if("InfoCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().infoEvent.onResponse(resObj);
                else NetworkManager.getInstance().infoEvent.onError(resObj);
            }else if("ClearCommad".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().clearEvent.onResponse(resObj);
                else NetworkManager.getInstance().clearEvent.onError(resObj);
            }else if("RemoveIdCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().removeEvent.onResponse(resObj);
                else NetworkManager.getInstance().removeEvent.onError(resObj);
            }else if("UpdateIdCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().updateEvent.onResponse(resObj);
                else NetworkManager.getInstance().updateEvent.onError(resObj);
            }else if("PrintFieldAscendingTimezoneCommand".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().ascendingTimezoneEvent.onResponse(resObj);
                else NetworkManager.getInstance().ascendingTimezoneEvent.onError(resObj);
            }else if("CountLessThanGovernor".equals(msgType)){
                if(((CommandExecutionPacket) obj).getSuccessful())
                    NetworkManager.getInstance().countLessThanGovernorEvent.onResponse(resObj);
                else NetworkManager.getInstance().countLessThanGovernorEvent.onError(resObj);
            }
        }
        else
            throw new ClassNotFoundException();
    }

    public void finishReceiver() {
        receiverThread.interrupt();
    }

    public ResponseReceiver getReceiver() {
        return receiverThread;
    }

}
