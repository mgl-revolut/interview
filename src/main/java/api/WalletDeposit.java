package api;

import core.BaseController;
import dto.WalletDepositDto;
import core.ServiceRegistry;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.Json;
import utils.ResponseModule;

import java.util.UUID;

public final class WalletDeposit extends BaseController {

    public WalletDeposit(ServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/wallet/:walletId/deposit";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.post;
    }

    @Override
    public Object handle(Request request, Response response) {

        UUID walletId = UUID.fromString(request.params(":walletId"));
        WalletDepositDto walletDepositDto = Json.deserialize(request.body(), WalletDepositDto.class);

        serviceRegistry.getAccountService().walletDeposit(walletId, walletDepositDto);

        return ResponseModule.ok("{}", response);
    }
}
