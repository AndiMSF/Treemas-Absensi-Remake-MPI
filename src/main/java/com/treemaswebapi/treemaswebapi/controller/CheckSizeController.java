package com.treemaswebapi.treemaswebapi.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckSizeController {

    @PostMapping("/checkBase64Size")
    public String checkBase64Size(@RequestBody String base64Data) {
        // Dekode data base64 menjadi array byte
        byte[] decodedBytes = Base64.decodeBase64(base64Data);

        // Hitung ukuran array byte
        int dataSize = decodedBytes.length;

        return "Ukuran data base64 adalah " + dataSize + " byte";
    }
}