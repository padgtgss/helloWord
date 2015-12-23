package com.pemass.service.pemass.biz.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.Instruction;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.InstructionTypeEnum;
import com.pemass.service.pemass.biz.InstructionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: InstructionServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-13 10:48
 */
@Service
public class InstructionServiceImpl implements InstructionService {

    @Resource
    private BaseDao jpaBaseDao;

    /**
     * 新增招募书
     * @param organization
     */
    @Override
    public void addInstruction(Organization organization) {
        Instruction newInstruction = new Instruction();

        newInstruction.setCompanyAdvantage("");
        newInstruction.setCompanyProfile("");
        newInstruction.setConditions("");
        newInstruction.setInstructionType(InstructionTypeEnum.RECRUITMENT);
        newInstruction.setOrganizationId(organization.getId());
        newInstruction.setPolicy("");

        jpaBaseDao.persist(newInstruction);
    }

    /**
     * 查询招募书信息
     * @param id
     * @return
     */
    @Override
    public Instruction getInstructionInfo(Long id) {

        return jpaBaseDao.getEntityByField(Instruction.class,"organizationId",id);
    }

    /**
     * 更新招募书
     * @param instruction
     * @return
     */
    @Override
    public Instruction updateInstruction(Instruction instruction,Organization organization) {
        Instruction newInstruction = getInstructionInfo(organization.getId());
        if(instruction == null){
            newInstruction.setPolicy("");
            newInstruction.setConditions("");
            newInstruction.setCompanyProfile("");
            newInstruction.setCompanyAdvantage("");
        }else{
            newInstruction.setCompanyAdvantage(instruction.getCompanyAdvantage());
            newInstruction.setCompanyProfile(instruction.getCompanyProfile());
            newInstruction.setConditions(instruction.getConditions());
            newInstruction.setPolicy(instruction.getPolicy());
        }
        newInstruction.setInstructionType(InstructionTypeEnum.RECRUITMENT);
        newInstruction.setOrganizationId(organization.getId());
        return jpaBaseDao.merge(newInstruction);
    }
}
