package cn.iocoder.yudao.module.promotion.api.bargain;

import cn.iocoder.yudao.module.promotion.api.bargain.dto.BargainRecordCreateReqDTO;
import org.springframework.stereotype.Service;

/**
 * 砍价活动 API 实现类 TODO @puhui999
 *
 * @author HUIHUI
 */
@Service
public class BargainRecordApiImpl implements BargainRecordApi {

    @Override
    public void createBargainRecord(BargainRecordCreateReqDTO reqDTO) {

    }

    @Override
    public boolean isBargainRecordSuccess(Long userId, Long orderId) {
        return false;
    }

}
