package com.pemass.service.pemass.biz;

import com.pemass.persist.domain.jpa.biz.Instruction;
import com.pemass.persist.domain.jpa.sys.Organization;

/**
 * @Description: ${todo}
 * @author luoc
 * @date 2014/10/13
 */
public interface InstructionService {

    /**
     *新增招募书
     * @param organization
     */
    void addInstruction(Organization organization);

    /**
     * 查询招募书信息
     * @param id
     * @return
     */
    Instruction getInstructionInfo(Long id);

    /**
     * 更新招募书
     * @param instruction
     * @return
     */
    Instruction updateInstruction(Instruction instruction,Organization organization);
}
