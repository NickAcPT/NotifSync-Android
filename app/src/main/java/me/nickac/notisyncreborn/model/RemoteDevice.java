package me.nickac.notisyncreborn.model;

import java.util.UUID;

import me.nickac.notisyncreborn.networking.RestNotificationService;
import me.nickac.notisyncreborn.utils.PostConstruct;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDevice {
    private UUID id;
    private String name;
    private String ip;
    private RestNotificationService notificationService;

    protected RemoteDevice() {
    }

    public RemoteDevice(UUID id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }

    public UUID getId() {
        return id;
    }

    public RestNotificationService getNotificationService() {
        return notificationService;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    @PostConstruct
    private void createRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://%s:11785", ip))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        notificationService = retrofit.create(RestNotificationService.class);

    }
}
