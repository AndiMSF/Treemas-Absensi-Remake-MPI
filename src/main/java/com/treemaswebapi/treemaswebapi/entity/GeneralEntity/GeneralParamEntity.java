package com.treemaswebapi.treemaswebapi.entity.GeneralEntity;

import java.util.Date;

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
@Table(name = "generalparam", schema = "public")
public class GeneralParamEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "paramdesc")
    private String paramDesc;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "val")
    private String val;

    @Column(name = "isvisible")
    private String isVisible;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Date dtmUpd;

}
