package com.example.dept;

import com.example.AppZonedDateTimeDeserializer;
import com.example.AppZonedDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Immutable
@Entity
@Table(schema = "cdx_cims", name = "departments_user_v")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "departmentUuid")
public class Department implements Serializable {

    private static final long serialVersionUID = -224616476977536624L;

    @Id
    @Column(name = "department_uuid")
    private UUID departmentUuid;

    @Column(name = "dep_id_pers")
    private String depIdPers;

    @Column(name = "dep_id_struct")
    private String depIdStruct;

    @NotNull
    @Column(name = "dep_name")
    private String depName;

    @Column(name = "organization_uuid")
    private UUID organizationUuid;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "parent_department_uuid")
    private UUID parentDepartmentUuid;

    @Column(name = "parent_department_name")
    private String parentDepartmentName;

    @Column(name = "chief_person_uuid")
    private UUID chiefPersonUuid;

    @Column(name = "chief_person_name")
    private String chiefPersonName;

    @Column(name = "is_work_center")
    private Integer isWorkCenter;

    @Column(name = "is_work_center_str")
    private String isWorkCenterStr;

    @Column(name = "quality_check")
    private Integer qualityCheck;

    @Column(name = "quality_check_str")
    private String qualityCheckStr;

    @Column(name = "whse_check")
    private Integer whseCheck;

    @Column(name = "whse_check_str")
    private String whseCheckStr;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "deleted_str")
    private String deletedStr;

    @JsonDeserialize(using = AppZonedDateTimeDeserializer.class)
    @JsonSerialize(using = AppZonedDateTimeSerializer.class)
    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "user_created")
    private String userCreated;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "dep_guid_pers")
    private UUID depGuidPers;

    @Column(name = "dep_guid_struct")
    private UUID depGuidStruct;

    @Column(name = "dep_type_id")
    private Integer depTypeId;

    @Column(name = "dep_type_name")
    private String depTypeName;
}
