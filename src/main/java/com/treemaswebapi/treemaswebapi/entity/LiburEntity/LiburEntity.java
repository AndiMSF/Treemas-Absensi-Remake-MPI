package com.treemaswebapi.treemaswebapi.entity.LiburEntity;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_libur", schema = "public")
public class LiburEntity {
    @Id
    @Column(name = "libur_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long liburId;

    @Column(name = "tgl_libur")
    @Temporal(TemporalType.DATE)
    private Date tglLibur;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "is_cutibersama")
    private String isCutiBersama;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;
}
