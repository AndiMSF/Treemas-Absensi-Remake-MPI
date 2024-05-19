package com.treemaswebapi.treemaswebapi.entity.GeneralEntity;

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
@Table(name = "include_table_list", schema = "public")
public class IncludeTableListEntity {
    @Id
    @Column(name = "tablename")
    private String tableName;
}
