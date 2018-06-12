package api;

import core.BaseController;
import model.Account;
import core.ServiceRegistry;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.ResponseModule;

import java.util.UUID;

public final class GetAccountDetails extends BaseController {

    public GetAccountDetails(ServiceRegistry serviceRegistry) {

        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/account/:accountId";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.get;
    }

    @Override
    public Object handle(Request request, Response response) {

        UUID accountId = UUID.fromString(request.params(":accountId"));

        Account account = serviceRegistry.getAccountService()
                              .getAccountById(accountId)
                              .orElseThrow(() -> new RuntimeException("Customer not found."));

        return ResponseModule.ok(account, response);
    }
}
