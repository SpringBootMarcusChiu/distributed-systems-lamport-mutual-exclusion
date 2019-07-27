package com.marcuschiu.example.spring.boot.mastercodesnippet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReplyMessage {
    Integer timestamp;
    Integer fromID;
}
