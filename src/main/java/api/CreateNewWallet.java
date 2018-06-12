package api;

import core.BaseController;
import dto.NewWalletDto;
import dto.ResourceIdentifierDto;
import core.ServiceRegistry;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import utils.Json;
import utils.ResponseModule;

import java.util.UUID;

public final class CreateNewWallet extends BaseController {

    public CreateNewWallet(ServiceRegistry serviceRegistry) {

        super(serviceRegistry);
    }

    @Override
    public String getControllerUri() {

        return "/account/:accountId/wallet";
    }

    @Override
    public HttpMethod getHttpMethod() {

        return HttpMethod.post;
    }

    @Override
    public Object handle(Request request, Response response) {

        UUID accountId = UUID.fromString(request.params(":accountId"));
        NewWalletDto newWalletDto = Json.deserialize(request.body(), NewWalletDto.class);

        UUID walletId = serviceRegistry.getAccountService().createNewWallet(accountId, newWalletDto.walletCurrency);

        return ResponseModule.ok(new ResourceIdentifierDto(walletId), response);
    }
}
