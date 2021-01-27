package com.example.dept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>, JpaSpecificationExecutor<Department> {

    @Procedure("cdx_cims.add_departments")
    UUID addDepartment(@Param("p_department_uuid") UUID uuid,
                       @Param("p_dep_name") String depName,
                       @Param("p_organization_uuid") UUID organizationUuid,
                       @Param("p_parent_department_uuid") UUID parentDepartmentUuid,
                       @Param("p_chief_person_uuid") UUID chiefPersonUuid,
                       @Param("p_deleted") Integer deleted,
                       @Param("p_quality_check") Integer qualityCheck,
                       @Param("p_whse_check") Integer whseCheck,
                       @Param("p_is_work_center") Integer workCenter
    );

    @Procedure("cdx_cims.delete_departments")
    String deleteDepartment(@Param("p_department_uuid") UUID uuid);
}
