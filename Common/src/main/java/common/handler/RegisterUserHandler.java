package common.handler;

import common.dto.RegisterUserRequest;
import common.dto.RegisterUserResponse;


public class RegisterUserHandler implements RequestHandler<RegisterUserRequest, RegisterUserResponse> {

    @Override
    public RegisterUserResponse handle(RegisterUserRequest request) {
        //... логика регистрации
        return new RegisterUserResponse("OK");
    }
}
