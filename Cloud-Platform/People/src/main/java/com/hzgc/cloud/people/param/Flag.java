package com.hzgc.cloud.people.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class Flag implements Serializable {
    private Integer id;

    private String flag;
}
