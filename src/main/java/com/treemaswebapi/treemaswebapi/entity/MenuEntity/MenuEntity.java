package com.treemaswebapi.treemaswebapi.entity.MenuEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_menu", schema = "public")
public class MenuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent")
    private String parent;

    @Column(name = "child")
    private String child;

    @Column(name = "jabatan")
    private String jabatan;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;

}
