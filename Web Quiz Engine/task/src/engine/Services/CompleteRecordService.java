package engine.Services;

import engine.Repository.*;
import engine.Entities.CompleteRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CompleteRecordService {

    @Autowired
    CompleteRecordRepository completeRecordRepository;

    public Page<CompleteRecord> getAllCompleteRecords(Integer page,String email){
        Pageable paging = PageRequest.of(page, 10);
        Page<CompleteRecord> pageresult = completeRecordRepository.findByEmail(email,paging,"completedAt");
        return pageresult;
    }
}
