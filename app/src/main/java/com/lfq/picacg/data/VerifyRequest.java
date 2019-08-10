package com.lfq.picacg.data;

import lombok.Data;

@Data
public class VerifyRequest {

    /**
     * problem : (2+3)*5=?
     * id : 1563237614053
     */

    public String problem;
    public long id;
}
