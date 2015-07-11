package com.alexyvv.sslconnectiontest.common;

import android.content.Context;
import android.util.Log;

import com.alexyvv.sslconnectiontest.SSLConnectionTestApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Модель данных приложения.
 * Created by alexy on 08.07.15.
 */
public class ContextModel {

    public static final String TAG = "ContextModel";

    /** Имя файла для хранения кеша. */
    private static final String CACHE_FILE_NAME = "context_cache_file";

    /** Singleton. */
    private static volatile ContextModel instance;

    /** Карта подсказок хостов и ассоциированных с ними портов. */
    private Map<String, Set<String>> hintMap;

    /**
     * Получить Singleton модели контекста работы приложения.<br/>
     * (Checked Locking & volatile)
     * @return Singleton for ContextModel.
     */
    public static ContextModel getInstance() {

        ContextModel localInstance = instance;
        if(localInstance == null) {
            synchronized(ContextModel.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new ContextModel();
                }
            }
        }
        return localInstance;
    }

    private ContextModel() {

        this.hintMap = new HashMap<>();
        // Модели из кеша.
        initDataFromStorage();
    }

    /**
     * Инициировать модель из кеша.
     */
    private void initDataFromStorage() {

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = SSLConnectionTestApplication.getAppContext().openFileInput(CACHE_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            this.hintMap = (Map<String, Set<String>>) objectInputStream.readObject();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            try {
                if(objectInputStream != null) {
                    objectInputStream.close();
                }
                if(fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
    }

    /**
     * Сохранить данные модели в кеш.
     */
    private void saveDataToStorage() {

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = SSLConnectionTestApplication.getAppContext().openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.hintMap);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            try {
                if(objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
    }


    /**
     * Добавить host в набор подсказок.
     * @param host имя хоста.
     */
    public void addHostHint(String host) {

        // null - при добавлении host-а у нас еще нет ассоциированных с ним портов
        if(!this.hintMap.containsKey(host)) {
            this.hintMap.put(host, null);
            saveDataToStorage();
        }
    }

    /**
     * Добавить port в набор подсказок. Если ассоциированного хоста в подсказках нет,
     * то не добавляем указанный порт.
     * @param host имя хоста, к которому добавляется порт.
     * @param port имя порта.
     */
    public void addPortHint(String host, String port) {

        if(this.hintMap.containsKey(host)) {
            Set<String> portSet = this.hintMap.get(host);
            if(portSet == null) {
                portSet = new HashSet<>();
                this.hintMap.put(host, portSet);
            }
            portSet.add(port);
            saveDataToStorage();
        }
    }

    /**
     * Удаление подсказки для хоста.
     * @param hostName имя хоста.
     */
    public void removeHostHint(String hostName) {

        if(this.hintMap.containsKey(hostName)) {
            this.hintMap.remove(hostName);
            initDataFromStorage();
        }
    }

    /**
     * Удаление подсказки для порта.
     * @param hostName имя хоста.
     * @param portName имя порта.
     */
    public void removePortHoint(String hostName, String portName) {

        if(this.hintMap.containsKey(hostName)) {
            Set<String> portSet = this.hintMap.keySet();
            if(portSet.contains(portName)) {
                portSet.remove(portName);
                saveDataToStorage();
            }
        }
    }

    /**
     * Получить набор подсказок для хостов.
     * @return набор подсказок для хостов.
     */
    public Set<String> getHostHintSet() {

        return this.hintMap.keySet();
    }

    /**
     * Получить набор портов, ассоциированных с хостом.
     * @param hostName имя хоста.
     * @return набор ассоциированных с хостом портов.
     */
    public Set<String> getPortHintSet(String hostName) {

        return this.hintMap.get(hostName);
    }
}
