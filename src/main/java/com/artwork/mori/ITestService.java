package com.artwork.mori;

import com.artwork.mori.entity.Resource;

public interface ITestService {

    void update1(Long id, long sleep);

    void update2(Long id, long sleep);

    Resource getOne(Long id);
}
