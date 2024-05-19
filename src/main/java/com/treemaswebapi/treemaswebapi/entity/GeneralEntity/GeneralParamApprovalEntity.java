package com.treemaswebapi.treemaswebapi.entity.GeneralEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "generalparam_approval", schema = "public")
public class GeneralParamApprovalEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "newparamdesc")
    private String newParamDesc;

    @Column(name = "newdata_type")
    private String newDataType;

    @Column(name = "newval")
    private String newVal;

    @Column(name = "newisvisible")
    private String newIsVisible;

    @Column(name = "old_paramdesc")
    private String oldParamDesc;

    @Column(name = "old_data_type")
    private String oldDataType;

    @Column(name = "old_val")
    private String oldVal;

    @Column(name = "old_isvisible")
    private String oldIsVisible;

    @Column(name = "isapprove")
    private String isApprove;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private ZonedDateTime dtmUpd;

    @Column(name = "usrapp")
    private String usrApp;

    @Column(name = "dtmapp")
    private ZonedDateTime dtmApp;
}