package common.handler;

import common.dto.BasicRequest;
import common.dto.BasicResponse;


public interface RequestHandler<REQUEST extends BasicRequest, RESPONSE extends BasicResponse> {

    RESPONSE handle(REQUEST request);
}
