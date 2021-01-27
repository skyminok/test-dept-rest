package com.example.org;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Immutable
@Entity
@Table(schema = "cdx_cims", name = "organizations_user_v")
@Getter
@Setter
@EqualsAndHashCode(of = "organizationUuid")
public class Organization implements Serializable {

    private static final long serialVersionUID = 2906492747592228730L;

    @Id
    @Column(name = "organization_uuid")
    private String organizationUuid;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "org_form")
    private String orgForm;

    @Column(name = "parent_organization_uuid")
    private UUID parentOrganizationUuid;

    @Column(name = "parent_organization_name")
    private String parentOrganizationName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "inn")
    private String inn;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_main")
    private boolean main;

    @Column(name = "is_main_str")
    private String mainStr;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "deleted_str")
    private String deletedStr;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "user_created")
    private String userCreated;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "org_guid")
    private String orgGuid;

}
