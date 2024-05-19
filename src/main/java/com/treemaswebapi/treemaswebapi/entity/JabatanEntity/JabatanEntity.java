package com.treemaswebapi.treemaswebapi.entity.JabatanEntity;

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
@Table(name = "tbl_jabatan", schema = "public")
public class JabatanEntity {
    @Id
    @Column(name = "jabatan_id")
    private String jabatanId;

    @Column(name = "nama_jabatan")
    private String namaJabatan;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private java.sql.Timestamp dtmUpd;

}
