package com.alexyvv.sslconnectiontest.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.animation.AnimationUtils;

import com.alexyvv.sslconnectiontest.R;
import com.alexyvv.sslconnectiontest.common.ContextModel;
import com.alexyvv.sslconnectiontest.common.TestConnector;
import com.github.clans.fab.FloatingActionButton;

import java.util.Arrays;
import java.util.Set;

/**
 * Фрагмент для отображения интерфейса проверки соединения.
 */
public class ConnectionFragment extends Fragment {

    private View mFragmentView;
    private AutoCompleteTextView mHostCompleteText;
    private AutoCompleteTextView mPortCompleteText;
    private FloatingActionButton fabConnect;

    /**
     * Получить экземпляр фрагмента.
     * @return экземпляр фрагмента.
     */
    public static ConnectionFragment newInstance(){

        return new ConnectionFragment();
    }

    public ConnectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_connection, container, false);
        mHostCompleteText = (AutoCompleteTextView) mFragmentView.findViewById(R.id.comp_text_url);
        mHostCompleteText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Задаем подсказки для выбора портов в зависимости от выбранного хоста.
                TextView itemTextView = (TextView) view.findViewById(android.R.id.text1);
                String selectedHostName = itemTextView.getText().toString();
                setHintAdapterForPorts(selectedHostName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mHostCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Задаем подсказки для выбора портов в зависимости от выбранного хоста.
                TextView itemTextView = (TextView) view.findViewById(android.R.id.text1);
                String selectedHostName = itemTextView.getText().toString();
                setHintAdapterForPorts(selectedHostName);
            }
        });

        setHintAdapterForHosts();
        mPortCompleteText = (AutoCompleteTextView) mFragmentView.findViewById(R.id.comp_text_port);

        //Floating Action Button:
        fabConnect = (FloatingActionButton) mFragmentView.findViewById(R.id.fab_connect);
        fabConnect.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up));
        fabConnect.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));
        fabConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String host = mHostCompleteText.getText().toString();
                String port = mPortCompleteText.getText().toString();
                // Добавляем host и port в модель подсказок:
                ContextModel.getInstance().addHostHint(host);
                ContextModel.getInstance().addPortHint(host, port);
                // Обновляем адаперты подсказок для хоста и порта:
                setHintAdapterForHosts();
                setHintAdapterForPorts(host);
                // Запускаем проверку возможности открытия соединения.
                TestConnector connector = new TestConnector(host, port, ConnectionFragment.this);
                connector.execute();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabConnect.show(true);
            }
        }, 400);

//        mProgressBar = (ProgressBar) mFragmentView.findViewById(R.id.progress_connection);
//        mProgressBar.setIndeterminate(true);
//        mTestConnectButton = (Button) mFragmentView.findViewById(R.id.button_test_connect);
//        mTestConnectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String host = mHostCompleteText.getText().toString();
//                String port = mPortCompleteText.getText().toString();
//                // Добавляем host и port в модель подсказок:
//                ContextModel.getInstance().addHostHint(host);
//                ContextModel.getInstance().addPortHint(host, port);
//                // Обновляем адаперты подсказок для хоста и порта:
//                setHintAdapterForHosts();
//                setHintAdapterForPorts(host);
//                // Запускаем проверку возможности открытия SSl соединения.
//                TestConnector connector = new TestConnector(host, port, ConnectionFragment.this);
//                connector.execute();
//            }
//        });

        return mFragmentView;
    }

    private void setHintAdapterForHosts() {

        Set<String> hostHintSet = ContextModel.getInstance().getHostHintSet();
        String[] hostHints = Arrays.copyOf(hostHintSet.toArray(), hostHintSet.toArray().length, String[].class);
        ArrayAdapter<String> urlCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, hostHints);
        mHostCompleteText.setAdapter(urlCompleteAdapter);
    }

    private void setHintAdapterForPorts(String hostName) {

        Set<String> portHintSet = ContextModel.getInstance().getPortHintSet(hostName);
        String[] portHints = Arrays.copyOf(portHintSet.toArray(), portHintSet.toArray().length, String[].class);
        ArrayAdapter<String> portCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, portHints);
        mPortCompleteText.setAdapter(portCompleteAdapter);
    }

    public void setConnectionProgressState(){

        mHostCompleteText.setEnabled(false);
        mPortCompleteText.setEnabled(false);
        fabConnect.setShowProgressBackground(true);
        fabConnect.setIndeterminate(true);
    }

    public void setCompleteState(){

        mHostCompleteText.setEnabled(true);
        mPortCompleteText.setEnabled(true);
        fabConnect.setShowProgressBackground(false);
        fabConnect.setIndeterminate(false);
    }
}
