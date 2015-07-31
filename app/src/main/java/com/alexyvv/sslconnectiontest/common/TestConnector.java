package com.alexyvv.sslconnectiontest.common;

import android.os.AsyncTask;
import android.util.Log;

import com.alexyvv.sslconnectiontest.fragment.ConnectionFragment;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alexy on 07.07.15.
 */
public class TestConnector extends AsyncTask<String, Void, String> {

    private static final String TAG = "HTTPSConnector";
    private static final String OK_CONNECTION_RESULT = "CONNECTION_OK";
    private static final int CONN_TIMEOUT = 60000;
    private String url;
    private String port;

    ConnectionFragment connectionFragment;

    public TestConnector(String aUrl, String aPort, ConnectionFragment aConnectionFragment) {

        this.url = aUrl;
        this.port = aPort;
        this.connectionFragment = aConnectionFragment;
    }

    @Override
    protected void onPreExecute() {

        this.connectionFragment.setConnectionProgressState();
    }

    @Override
    protected String doInBackground(String... params) {

        return testSocketConnect(this.url, this.port);
    }

    @Override
    protected void onPostExecute(String connectionResult) {

        if(connectionResult.equalsIgnoreCase(OK_CONNECTION_RESULT)) {
            // Соединение установленно
//            SnackBar snackbar = new SnackBar(this.connectionFragment.getActivity(), "Connection is established", "OK", null);
//            snackbar.show();
        } else {

            // Соединение не установленно
//            SnackBar snackbar = new SnackBar(this.connectionFragment.getActivity(), "Connection is NOT established", "OK", null);
//            snackbar.show();
        }
        this.connectionFragment.setCompleteState("");
    }

    /**
     * Завершить проверку соединения в "ручном" режиме.
     */
    public void stopTestConnection() {

        this.connectionFragment.setCompleteState("");
        cancel(false);
    }


    /**
     * Проверить соединение по сокету
     * @param aURL хост сервера
     * @param aPort порт сервера
     * @return <code>true</code> - соединение установленно, <code>false</code> - иначе.
     */
    private String testSocketConnect(String aURL, String aPort) {

        String result = OK_CONNECTION_RESULT;
        try {
            new Socket(aURL, Integer.parseInt(aPort));
        } catch (Exception e) {
            result = e.getMessage();
            Log.d(TAG, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Проверить соединение по HTTPS
     * @param aUrl хост сервера
     * @param aPort порт сервера
     * @return <code>true</code> - соединение установленно, <code>false</code> - иначе.
     */
    public boolean testHTTPSConnect(String aUrl, String aPort) {

        boolean result = false;
        HttpsURLConnection httpsURLConnection;
        try {
            URL url = new URL(aUrl + ":" + aPort);
            httpsURLConnection = createHttpsConnection(url);
            httpsURLConnection.connect();
            result = true;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return result;
    }


    /**
     * Создание HTTPS соединения.
     * @param url url = host:port
     * @return объект HttpsURLConnection
     * @throws IOException
     */
    private HttpsURLConnection createHttpsConnection(URL url) throws IOException {

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setConnectTimeout(CONN_TIMEOUT);
        conn.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }
}
