package common.handler;

import common.dto.GetFilesListRequest;
import common.dto.GetFilesListResponse;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class GetFilesListHandler implements RequestHandler<GetFilesListRequest, GetFilesListResponse> {

    @Override
    public GetFilesListResponse handle(GetFilesListRequest request) {
        String getFilesListRequestPath = request.getPath();
        Path path = Paths.get(getFilesListRequestPath);
        String[] list = path.toFile().list();
        return new GetFilesListResponse("OK", list != null ? List.of(list) : Collections.emptyList());
    }

}
