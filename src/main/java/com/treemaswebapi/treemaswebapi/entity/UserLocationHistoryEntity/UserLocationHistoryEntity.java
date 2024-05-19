package com.treemaswebapi.treemaswebapi.entity.UserLocationHistoryEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_location_history", schema = "public")
public class UserLocationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_history_seqno")
    private Long locHistorySeqno;

    @Column(name = "userid")
    private String userId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "accuracy")
    private Long accuracy;

    @Column(name = "dtmcrt")
    private Timestamp dtmcrt;
}