package cmc.feelim.domain.laboratory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface LaboratoryRepository extends JpaRepository<ProcessingLaboratory, Long> {

    List<ProcessingLaboratory> findByNameContaining(@Param("keyword") String keyword);
}
