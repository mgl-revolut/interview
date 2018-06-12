package api;

import core.BaseController;
import dto.TransferMoneyBetweenAccountsDto;
import core.ServiceRegistry;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.Json;
import utils.ResponseModule;

public final class TransferMoneyBetweenAccounts extends BaseController {

    public TransferMoneyBetweenAccounts(ServiceRegistry serviceRegistry) {

        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/transferMoney";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.post;
    }

    @Override
    public Object handle(Request request, Response response) {

        TransferMoneyBetweenAccountsDto transferDto = Json.deserialize(request.body(),
            TransferMoneyBetweenAccountsDto.class);

        serviceRegistry.getAccountService().transferFundsBetweenAccounts(transferDto);

        return ResponseModule.ok("{}", response);
    }
}
