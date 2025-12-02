package com.ciptadana.dashboard.database.backoffice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dummy")

@NamedStoredProcedureQuery(
        name="ClientAvgAsOf",
        procedureName = "DENPASAR.GET_CLIENTAVG_AS_OF_SP",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "MDATE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "MCLIENTCODE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "MSTOCKCODE", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "RES", type = String.class)
        }
)

public class NativeEntity {

    @Id
    private String id;

}