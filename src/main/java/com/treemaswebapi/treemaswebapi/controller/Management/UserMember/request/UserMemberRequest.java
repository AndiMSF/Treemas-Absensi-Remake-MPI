package com.treemaswebapi.treemaswebapi.controller.Management.UserMember.request;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMemberRequest {
    private String nikLeader;
    private String nikUser;
    private String usrUpd;
    private Timestamp dtmUpd;
}
