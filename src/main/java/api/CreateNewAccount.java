package api;

import core.BaseController;
import dto.NewAccountDto;
import dto.ResourceIdentifierDto;
import core.ServiceRegistry;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.Json;
import utils.ResponseModule;

import java.util.UUID;

public final class CreateNewAccount extends BaseController {

    public CreateNewAccount(ServiceRegistry serviceRegistry) {

        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/account";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.post;
    }

    @Override
    public Object handle(Request request, Response response) {

        NewAccountDto newAccountDto = Json.deserialize(request.body(), NewAccountDto.class);

        UUID accountId = serviceRegistry.getAccountService().createNewAccount(newAccountDto);

        return ResponseModule.ok(new ResourceIdentifierDto(accountId), response);
    }
}
