package api;

import core.BaseController;
import core.ServiceRegistry;
import dto.NewCustomerDto;
import dto.ResourceIdentifierDto;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.Json;
import utils.ResponseModule;

import java.util.UUID;

public class CreateNewCustomer extends BaseController {

    public CreateNewCustomer(ServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/customer";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.post;
    }

    @Override
    public Object handle(Request request, Response response) {

        NewCustomerDto newCustomerDto = Json.deserialize(request.body(), NewCustomerDto.class);

        UUID accountId = serviceRegistry.getCustomerService().createNewCustomer(newCustomerDto);

        return ResponseModule.ok(new ResourceIdentifierDto(accountId), response);
    }
}
