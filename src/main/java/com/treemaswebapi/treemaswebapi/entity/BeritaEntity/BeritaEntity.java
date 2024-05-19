package com.treemaswebapi.treemaswebapi.entity.BeritaEntity;

import java.time.Instant;

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
@Table(name = "tbl_berita", schema = "public")
public class BeritaEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "header")
    private String header;

    @Column(name = "detail")
    private String detail;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "datecrt")
    private Instant dateCrt;
}
