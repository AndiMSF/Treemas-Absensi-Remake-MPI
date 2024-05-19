package com.treemaswebapi.treemaswebapi.entity.KaryawanEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date; // Import java.util.Date untuk mendukung tanggal lahir

import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_karyawan", schema = "public")
public class KaryawanEntity {

    @Id
    @Column(name = "nik")
    private String nik;

    @Column(name = "nama")
    private String nama;

    @Column(name = "tempat_lahir")
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    @Temporal(TemporalType.DATE)
    private Date tanggalLahir;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "agama")
    private String agama;

    @Column(name = "kewarganegaraan")
    private String kewarganegaraan;

    @Column(name = "kode_pos")
    private String kodePos;

    @Column(name = "alamat_ktp")
    private String alamatKtp;

    @Column(name = "no_hp")
    private String noHp;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "no_rek")
    private String noRek;

    @Column(name = "no_npwp")
    private String noNpwp;

    @Column(name = "jenjang_pendidikan")
    private String jenjangPendidikan;

    @Column(name = "tanggal_bergabung")
    @Temporal(TemporalType.DATE)
    private Date tanggalBergabung;

    @Column(name = "alamat_sekarang")
    private String alamatSekarang;

    @Column(name = "status_perkawinan")
    private String statusPerkawinan;

    @Column(name = "golongan_darah")
    private String golonganDarah;

    @Column(name = "nomor_ktp")
    private String nomorKtp;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "status_emergency")
    private String statusEmergency;

    @Column(name = "alamat_emergency")
    private String alamatEmergency;

    @Column(name = "telp_emergency")
    private String telpEmergency;

    @Column(name = "divisi")
    private String divisi;

    @Column(name = "nik_leader")
    private String nikLeader;

    @Column(name = "is_leader")
    private String isLeader;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;

    @Column(name = "handset_imei")
    private String handsetImei;

    @Column(name = "hak_cuti")
    private BigDecimal  hakCuti;

    @Column(name = "is_karyawan")
    private String isKaryawan;

    // @OneToOne(mappedBy = "karyawan", cascade = CascadeType.ALL)
    // @PrimaryKeyJoinColumn
    // private SysUserEntity sysUserEntity;

    // @OneToOne(mappedBy = "karyawan2", cascade = CascadeType.ALL)
    // @PrimaryKeyJoinColumn
    // private KaryawanImageEntity karyawanImageEntity;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private ProjectEntity projectId;
}
