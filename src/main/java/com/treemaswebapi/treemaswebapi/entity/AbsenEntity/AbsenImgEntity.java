package com.treemaswebapi.treemaswebapi.entity.AbsenEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
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

// nik FK ambil dari tbl_karyawan

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_absen_img", schema = "public")
public class AbsenImgEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "tgl_absen")
    @Temporal(TemporalType.DATE)
    private LocalDate tglAbsen;

    @Column(name = "image")
    private String image;

    @Column(name = "image_64")
    private String image64;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;
}
