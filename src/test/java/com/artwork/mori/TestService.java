package com.artwork.mori;

import com.artwork.mori.entity.Resource;
import com.artwork.mori.entity.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestService implements ITestService {

    Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private ResourceRepository resourceRepository;

    @Transactional
    @Override
    public void update1(Long id, long sleep) {
        logger.info("T1 starts");
        Resource resource = resourceRepository.getByIdAndLock(id);
        logger.info("T1 sleep");
        sleep(sleep);
        resource.setName("t1");
        resourceRepository.save(resource);
        logger.info("T1 ends");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.info(e.toString());
        }
    }

    @Transactional
    @Override
    public void update2(Long id, long sleep) {
        sleep(1000);
        logger.info("T2 starts");
        Resource resource = resourceRepository.getByIdAndLock(id);
        resource.setName("t2");
        resourceRepository.save(resource);
        logger.info("T2 ends");
    }

    @Transactional
    @Override
    public Resource getOne(Long id) {
        Resource one = resourceRepository.getByIdWithPessimisticRead (id);
        logger.info(one.getName());
        return one;
    }
}
