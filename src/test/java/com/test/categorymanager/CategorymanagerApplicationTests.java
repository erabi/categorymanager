package com.test.categorymanager;

import com.test.categorymanager.config.dataloader.DataPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CategorymanagerApplicationTests {

    @MockBean
    protected DataPublisher dataPublisher;

    @Test
    void contextLoads() {
    }

}
