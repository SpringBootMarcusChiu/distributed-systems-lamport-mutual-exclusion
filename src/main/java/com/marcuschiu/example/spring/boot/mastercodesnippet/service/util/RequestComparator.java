package com.marcuschiu.example.spring.boot.mastercodesnippet.service.util;

import com.marcuschiu.example.spring.boot.mastercodesnippet.model.RequestMessage;

import java.util.Comparator;

public class RequestComparator implements Comparator<RequestMessage> {

    @Override
    public int compare(RequestMessage o1, RequestMessage o2) {
        Integer tsDifference = o1.getTimestamp() - o2.getTimestamp();
        if (!tsDifference.equals(0)) {
            return tsDifference;
        }

        Integer idDifference = o1.getFromID() - o2.getFromID();
        if (!idDifference.equals(0)) {
            return idDifference;
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
