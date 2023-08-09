package cn.iocoder.yudao.module.promotion.convert.decorate;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.promotion.controller.admin.decorate.vo.DecorateComponentSaveReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.decorate.vo.DecorateComponentRespVO;
import cn.iocoder.yudao.module.promotion.controller.app.decorate.vo.AppDecorateComponentRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.decorate.DecorateComponentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static cn.iocoder.yudao.module.promotion.controller.admin.decorate.vo.DecorateComponentRespVO.*;
import static cn.iocoder.yudao.module.promotion.controller.app.decorate.vo.AppDecorateComponentRespVO.*;

@Mapper
public interface DecorateComponentConvert {

    DecorateComponentConvert INSTANCE = Mappers.getMapper(DecorateComponentConvert.class);

    default List<DecorateComponentDO> convertList(Integer pageId, List<DecorateComponentSaveReqVO.ComponentReqVO> components) {
        return CollectionUtils.convertList(components, c -> convert(pageId, c));
    }

    default DecorateComponentRespVO convert2(Integer pageId, List<DecorateComponentDO> list) {
        List<ComponentRespVO> components = CollectionUtils.convertList(list, this::convert3);
        return new DecorateComponentRespVO().setPageId(pageId).setComponents(components);
    }

    DecorateComponentDO convert(Integer pageId, DecorateComponentSaveReqVO.ComponentReqVO reqVO);

    ComponentRespVO convert3(DecorateComponentDO componentDO);

    // ========== App convert ==========
    default AppDecorateComponentRespVO appConvert(Integer pageId, List<DecorateComponentDO> list) {
        List<AppComponentRespVO> components = CollectionUtils.convertList(list, this::appConvert2);
        return new AppDecorateComponentRespVO().setPageId(pageId).setComponents(components);
    }

    AppComponentRespVO appConvert2(DecorateComponentDO bean);

}
