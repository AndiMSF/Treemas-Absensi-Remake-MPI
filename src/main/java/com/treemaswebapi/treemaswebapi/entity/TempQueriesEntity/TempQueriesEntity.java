package com.treemaswebapi.treemaswebapi.entity.TempQueriesEntity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tempqueries", schema = "public")
public class TempQueriesEntity implements Serializable {

    @Id
    @Column(name = "rownum")
    private Long rownum;

    @Column(name = "col1", length = 476)
    private String col1;

    @Column(name = "col2")
    private String col2;

    @Column(name = "col3", length = 359)
    private String col3;
}