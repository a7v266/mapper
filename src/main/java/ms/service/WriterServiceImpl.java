package ms.service;

import ms.model.Writer;
import ms.service.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WriterServiceImpl implements WriterService {

    @Autowired
    private WriterRepository writerRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Writer> getWriters() {
        return writerRepository.findWriters();
    }

}
