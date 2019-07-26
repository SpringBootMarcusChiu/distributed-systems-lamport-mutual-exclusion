package com.marcuschiu.example.spring.boot.mastercodesnippet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyMessage {
    Integer timestamp;
    Integer fromID;
}
