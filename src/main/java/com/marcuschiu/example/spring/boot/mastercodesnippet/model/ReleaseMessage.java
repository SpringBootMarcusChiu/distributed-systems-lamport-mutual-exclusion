package com.marcuschiu.example.spring.boot.mastercodesnippet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReleaseMessage {
    Integer timestamp;
    Integer fromID;
}
