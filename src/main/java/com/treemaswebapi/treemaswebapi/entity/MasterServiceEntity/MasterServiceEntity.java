package com.treemaswebapi.treemaswebapi.entity.MasterServiceEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_master_service ", schema = "public")
public class MasterServiceEntity {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "run")
    private String run;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private java.sql.Timestamp dtmUpd;
}
