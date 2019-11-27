package com.study.course.client.hystrix;


import com.study.course.client.CloudAdminClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zhongxin on 2017/9/17.
 */
@Component
public class CloudAdminHystrix implements CloudAdminClient {

    @Override
    public Map<Integer, String> getUserNames() {
        return null;
    }
}
