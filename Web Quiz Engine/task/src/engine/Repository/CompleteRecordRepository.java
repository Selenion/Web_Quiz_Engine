package engine.Repository;

import engine.Entities.CompleteRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompleteRecordRepository extends PagingAndSortingRepository<CompleteRecord, Integer> {

    @Query("select s from CompleteRecord s where s.email like %?1%")
    Page<CompleteRecord> findByEmail (String email, Pageable pageable, String sortBy);
}
